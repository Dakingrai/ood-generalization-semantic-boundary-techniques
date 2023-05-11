/*
 * Copyright (c) Facebook, Inc. and its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#include <wangle/acceptor/Acceptor.h>

#include <fizz/server/TicketTypes.h>
#include <folly/io/async/AsyncTransport.h>
#include <folly/io/async/AsyncSSLSocket.h>
#include <folly/io/async/AsyncSocket.h>
#include <folly/io/async/EventBase.h>
#include <folly/portability/GFlags.h>
#include <folly/portability/Sockets.h>
#include <folly/portability/Unistd.h>
#include <wangle/acceptor/AcceptorHandshakeManager.h>
#include <wangle/acceptor/FizzConfigUtil.h>
#include <wangle/acceptor/ManagedConnection.h>
#include <wangle/acceptor/AcceptObserver.h>
#include <wangle/acceptor/SecurityProtocolContextManager.h>
#include <wangle/ssl/SSLContextManager.h>

#include <fstream>

using folly::AsyncServerSocket;
using folly::AsyncSocket;
using folly::AsyncSSLSocket;
using folly::AsyncTransport;
using folly::EventBase;
using folly::SocketAddress;
using std::string;
using std::chrono::milliseconds;

namespace wangle {

static const std::string empty_string;
std::atomic<uint64_t> Acceptor::totalNumPendingSSLConns_{0};

Acceptor::Acceptor(const ServerSocketConfig& accConfig)
    : accConfig_(accConfig),
      socketOptions_(accConfig.getSocketOptions()),
      observerList_(this) {}

void Acceptor::init(
    AsyncServerSocket* serverSocket,
    EventBase* eventBase,
    SSLStats* stats,
    std::shared_ptr<const fizz::server::FizzServerContext> fizzContext) {
  if (accConfig_.isSSL()) {
    if (accConfig_.allowInsecureConnectionsOnSecureServer) {
      securityProtocolCtxManager_.addPeeker(&tlsPlaintextPeekingCallback_);
    }

    if (accConfig_.fizzConfig.enableFizz) {
      ticketSecrets_ = {accConfig_.initialTicketSeeds.oldSeeds,
                        accConfig_.initialTicketSeeds.currentSeeds,
                        accConfig_.initialTicketSeeds.newSeeds};

      if (!fizzCertManager_) {
        fizzCertManager_ = createFizzCertManager();
      }

      auto context = fizzContext ? fizzContext : recreateFizzContext();

      auto* peeker = getFizzPeeker();
      peeker->setContext(std::move(context));
      securityProtocolCtxManager_.addPeeker(peeker);
    } else {
      securityProtocolCtxManager_.addPeeker(&defaultPeekingCallback_);
    }

    if (!sslCtxManager_) {
      sslCtxManager_ = std::make_unique<SSLContextManager>(
          "vip_" + getName(), accConfig_.strictSSL, stats);
    }
    try {
      // If the default ctx is nullptr, we can assume it hasn't been configured
      // yet.
      if (sslCtxManager_->getDefaultSSLCtx() == nullptr) {
        for (const auto& sslCtxConfig : accConfig_.sslContextConfigs) {
          sslCtxManager_->addSSLContextConfig(
              sslCtxConfig,
              accConfig_.sslCacheOptions,
              &accConfig_.initialTicketSeeds,
              accConfig_.bindAddress,
              cacheProvider_);
        }
      }
      CHECK(sslCtxManager_->getDefaultSSLCtx());
    } catch (const std::runtime_error& ex) {
      if (accConfig_.strictSSL) {
        throw;
      } else {
        sslCtxManager_->clear();
        // This is not a Not a fatal error, but useful to know.
        LOG(INFO) << "Failed to configure TLS. This is not a fatal error. "
                  << ex.what();
      }
    }
  }

  initDownstreamConnectionManager(eventBase);
  if (serverSocket) {
    serverSocket->addAcceptCallback(this, eventBase);

    for (auto& fd : serverSocket->getNetworkSockets()) {
      if (fd == folly::NetworkSocket()) {
        continue;
      }
      for (const auto& opt : socketOptions_) {
        opt.first.apply(fd, opt.second);
      }
    }
  }
}

void Acceptor::initDownstreamConnectionManager(EventBase* eventBase) {
  CHECK(nullptr == this->base_ || eventBase == this->base_);
  base_ = eventBase;
  state_ = State::kRunning;
  downstreamConnectionManager_ = ConnectionManager::makeUnique(
      eventBase, accConfig_.connectionIdleTimeout, this);
}

std::shared_ptr<fizz::server::FizzServerContext> Acceptor::createFizzContext() {
  return FizzConfigUtil::createFizzContext(accConfig_);
}

std::shared_ptr<const fizz::server::FizzServerContext>
Acceptor::recreateFizzContext() {
  if (fizzCertManager_ == nullptr) {
    return nullptr;
  }
  auto ctx = createFizzContext();
  if (ctx) {
    ctx->setCertManager(fizzCertManager_);
    ctx->setTicketCipher(createFizzTicketCipher(
        ticketSecrets_,
        ctx->getFactoryPtr(),
        fizzCertManager_,
        getPskContext()));
  }
  return ctx;
}

std::shared_ptr<fizz::server::TicketCipher> Acceptor::createFizzTicketCipher(
    const TLSTicketKeySeeds& seeds,
    std::shared_ptr<fizz::Factory> factory,
    std::shared_ptr<fizz::server::CertManager> certManager,
    folly::Optional<std::string> pskContext) {
  return FizzConfigUtil::createFizzTicketCipher(
      seeds,
      accConfig_.sslCacheOptions.sslCacheTimeout,
      accConfig_.sslCacheOptions.handshakeValidity,
      std::move(factory),
      std::move(certManager),
      std::move(pskContext));
}

std::unique_ptr<fizz::server::CertManager> Acceptor::createFizzCertManager() {
  return FizzConfigUtil::createCertManager(accConfig_, nullptr);
}

std::string Acceptor::getPskContext() {
  std::string pskContext;
  if (!accConfig_.sslContextConfigs.empty()) {
    pskContext =
        accConfig_.sslContextConfigs.front().sessionContext.value_or("");
  }
  return pskContext;
}

void Acceptor::resetSSLContextConfigs(
    std::shared_ptr<fizz::server::CertManager> certManager,
    std::shared_ptr<SSLContextManager> ctxManager,
    std::shared_ptr<const fizz::server::FizzServerContext> fizzContext) {
  try {
    if (accConfig_.fizzConfig.enableFizz) {
      auto manager = certManager ? certManager : createFizzCertManager();
      if (manager) {
        fizzCertManager_ = std::move(manager);
        auto context = fizzContext ? fizzContext : recreateFizzContext();
        getFizzPeeker()->setContext(std::move(context));
      }
    }
    if (ctxManager) {
      sslCtxManager_ = ctxManager;
    } else if (sslCtxManager_) {
      sslCtxManager_->resetSSLContextConfigs(
          accConfig_.sslContextConfigs,
          accConfig_.sslCacheOptions,
          nullptr,
          accConfig_.bindAddress,
          cacheProvider_);
    }
  } catch (const std::runtime_error& ex) {
    LOG(ERROR) << "Failed to re-configure TLS: " << ex.what()
               << "will keep old config";
  }
}

Acceptor::~Acceptor(void) {}

void Acceptor::setTLSTicketSecrets(
    const std::vector<std::string>& oldSecrets,
    const std::vector<std::string>& currentSecrets,
    const std::vector<std::string>& newSecrets) {
  if (accConfig_.fizzConfig.enableFizz) {
    ticketSecrets_ = {oldSecrets, currentSecrets, newSecrets};
    getFizzPeeker()->setContext(recreateFizzContext());
  }

  if (sslCtxManager_) {
    sslCtxManager_->reloadTLSTicketKeys(oldSecrets, currentSecrets, newSecrets);
  }
}

void Acceptor::drainAllConnections() {
  if (downstreamConnectionManager_) {
    downstreamConnectionManager_->initiateGracefulShutdown(
        gracefulShutdownTimeout_);
  }
}

bool Acceptor::canAccept(const SocketAddress& /*address*/) {
  return true;
}

void Acceptor::connectionAccepted(
    folly::NetworkSocket fdNetworkSocket,
    const SocketAddress& clientAddr) noexcept {
  int fd = fdNetworkSocket.toFd();

  namespace fsp = folly::portability::sockets;
  if (!canAccept(clientAddr)) {
    // Send a RST to free kernel memory faster
    struct linger optLinger = {1, 0};
    fsp::setsockopt(fd, SOL_SOCKET, SO_LINGER, &optLinger, sizeof(optLinger));
    close(fd);
    return;
  }
  auto acceptTime = std::chrono::steady_clock::now();
  for (const auto& opt : socketOptions_) {
    opt.first.apply(folly::NetworkSocket::fromFd(fd), opt.second);
  }

  onDoneAcceptingConnection(fd, clientAddr, acceptTime);
}

void Acceptor::onDoneAcceptingConnection(
    int fd,
    const SocketAddress& clientAddr,
    std::chrono::steady_clock::time_point acceptTime) noexcept {
  TransportInfo tinfo;
  processEstablishedConnection(fd, clientAddr, acceptTime, tinfo);
}

void Acceptor::processEstablishedConnection(
    int fd,
    const SocketAddress& clientAddr,
    std::chrono::steady_clock::time_point acceptTime,
    TransportInfo& tinfo) noexcept {
  bool shouldDoSSL = false;
  if (accConfig_.isSSL()) {
    CHECK(sslCtxManager_);
    shouldDoSSL = sslCtxManager_->getDefaultSSLCtx() != nullptr;
  }
  if (shouldDoSSL) {
    AsyncSSLSocket::UniquePtr sslSock(
        makeNewAsyncSSLSocket(sslCtxManager_->getDefaultSSLCtx(), base_, fd));
    ++numPendingSSLConns_;
    ++totalNumPendingSSLConns_;
    if (numPendingSSLConns_ > accConfig_.maxConcurrentSSLHandshakes) {
      VLOG(2) << "dropped SSL handshake on " << accConfig_.name
              << " too many handshakes in progress";
      auto error = SSLErrorEnum::DROPPED;
      auto latency = std::chrono::milliseconds(0);
      auto ex = folly::make_exception_wrapper<SSLException>(
          error, latency, sslSock->getRawBytesReceived());
      updateSSLStats(sslSock.get(), latency, error, ex);
      sslConnectionError(ex);
      return;
    }

    tinfo.tfoSucceded = sslSock->getTFOSucceded();
    for (const auto& cb : observerList_.getAll()) {
      cb->accept(sslSock.get());
    }
    startHandshakeManager(
        std::move(sslSock), this, clientAddr, acceptTime, tinfo);
  } else {
    tinfo.secure = false;
    tinfo.acceptTime = acceptTime;
    AsyncSocket::UniquePtr sock(makeNewAsyncSocket(base_, fd));
    tinfo.tfoSucceded = sock->getTFOSucceded();
    for (const auto& cb : observerList_.getAll()) {
      cb->accept(sock.get());
    }
    plaintextConnectionReady(
        std::move(sock),
        clientAddr,
        tinfo);
  }
}

void Acceptor::startHandshakeManager(
    AsyncSSLSocket::UniquePtr sslSock,
    Acceptor*,
    const SocketAddress& clientAddr,
    std::chrono::steady_clock::time_point acceptTime,
    TransportInfo& tinfo) noexcept {
  auto manager = securityProtocolCtxManager_.getHandshakeManager(
      this, clientAddr, acceptTime, tinfo);
  manager->start(std::move(sslSock));
}

void Acceptor::connectionReady(
    AsyncTransport::UniquePtr sock,
    const SocketAddress& clientAddr,
    const string& nextProtocolName,
    SecureTransportType secureTransportType,
    TransportInfo& tinfo) {
  // Limit the number of reads from the socket per poll loop iteration,
  // both to keep memory usage under control and to prevent one fast-
  // writing client from starving other connections.
  auto asyncSocket = sock->getUnderlyingTransport<AsyncSocket>();
  asyncSocket->setMaxReadsPerEvent(accConfig_.socketMaxReadsPerEvent);
  tinfo.initWithSocket(asyncSocket);
  tinfo.appProtocol = std::make_shared<std::string>(nextProtocolName);
  if (state_ < State::kDraining) {
    for (const auto& cb : observerList_.getAll()) {
      cb->ready(sock.get());
    }
    onNewConnection(
        std::move(sock),
        &clientAddr,
        nextProtocolName,
        secureTransportType,
        tinfo);
  }
}

void Acceptor::plaintextConnectionReady(
    AsyncSocket::UniquePtr sock,
    const SocketAddress& clientAddr,
    TransportInfo& tinfo) {
  connectionReady(
      std::move(sock),
      clientAddr,
      {},
      SecureTransportType::NONE,
      tinfo);
}

void Acceptor::sslConnectionReady(
    AsyncTransport::UniquePtr sock,
    const SocketAddress& clientAddr,
    const string& nextProtocol,
    SecureTransportType secureTransportType,
    TransportInfo& tinfo) {
  CHECK(numPendingSSLConns_ > 0);
  --numPendingSSLConns_;
  --totalNumPendingSSLConns_;
  connectionReady(
      std::move(sock), clientAddr, nextProtocol, secureTransportType, tinfo);
  if (state_ == State::kDraining) {
    checkDrained();
  }
}

void Acceptor::sslConnectionError(const folly::exception_wrapper&) {
  CHECK(numPendingSSLConns_ > 0);
  --numPendingSSLConns_;
  --totalNumPendingSSLConns_;
  if (state_ == State::kDraining) {
    checkDrained();
  }
}

void Acceptor::acceptError(const std::exception& ex) noexcept {
  // An error occurred.
  // The most likely error is out of FDs.  AsyncServerSocket will back off
  // briefly if we are out of FDs, then continue accepting later.
  // Just log a message here.
  LOG(ERROR) << "error accepting on acceptor socket: " << ex.what();
}

void Acceptor::acceptStopped() noexcept {
  VLOG(3) << "Acceptor " << this << " acceptStopped()";
  // Drain the open client connections
  drainAllConnections();

  // If we haven't yet finished draining, begin doing so by marking ourselves
  // as in the draining state. We must be sure to hit checkDrained() here, as
  // if we're completely idle, we can should consider ourself drained
  // immediately (as there is no outstanding work to complete to cause us to
  // re-evaluate this).
  if (state_ != State::kDone) {
    state_ = State::kDraining;
    checkDrained();
  }
}

void Acceptor::onEmpty(const ConnectionManager&) {
  VLOG(3) << "Acceptor=" << this << " onEmpty()";
  if (state_ == State::kDraining) {
    checkDrained();
  }
}

void Acceptor::checkDrained() {
  CHECK(state_ == State::kDraining);
  if (forceShutdownInProgress_ ||
      (downstreamConnectionManager_->getNumConnections() != 0) ||
      (numPendingSSLConns_ != 0)) {
    return;
  }

  VLOG(2) << "All connections drained from Acceptor=" << this << " in thread "
          << base_;

  downstreamConnectionManager_.reset();

  state_ = State::kDone;

  onConnectionsDrained();
}

void Acceptor::drainConnections(double pctToDrain) {
  if (downstreamConnectionManager_) {
    LOG(INFO) << "Draining " << pctToDrain * 100 << "% of "
              << getNumConnections() << " connections from Acceptor=" << this
              << " in thread " << base_;
    assert(base_->isInEventBaseThread());
    downstreamConnectionManager_->drainConnections(
        pctToDrain, gracefulShutdownTimeout_);
  }
}

milliseconds Acceptor::getConnTimeout() const {
  return accConfig_.connectionIdleTimeout;
}

void Acceptor::addConnection(ManagedConnection* conn) {
  // Add the socket to the timeout manager so that it can be cleaned
  // up after being left idle for a long time.
  downstreamConnectionManager_->addConnection(conn, true);
}

void Acceptor::forceStop() {
  base_->runInEventBaseThread([&] { dropAllConnections(); });
}

void Acceptor::dropAllConnections() {
  if (downstreamConnectionManager_) {
    LOG(INFO) << "Dropping all connections from Acceptor=" << this
              << " in thread " << base_;
    assert(base_->isInEventBaseThread());
    forceShutdownInProgress_ = true;
    downstreamConnectionManager_->dropAllConnections();
    CHECK(downstreamConnectionManager_->getNumConnections() == 0);
    downstreamConnectionManager_.reset();
  }
  CHECK(numPendingSSLConns_ == 0);

  state_ = State::kDone;
  onConnectionsDrained();
}

void Acceptor::dropConnections(double pctToDrop) {
  base_->runInEventBaseThread([&, pctToDrop] {
    if (downstreamConnectionManager_) {
      LOG(INFO) << "Dropping " << pctToDrop * 100 << "% of "
                << getNumConnections() << " connections from Acceptor=" << this
                << " in thread " << base_;
      assert(base_->isInEventBaseThread());
      forceShutdownInProgress_ = true;
      downstreamConnectionManager_->dropConnections(pctToDrop);
    }
  });
}

Acceptor::AcceptObserverList::AcceptObserverList(Acceptor* acceptor)
    : acceptor_(acceptor) {}

Acceptor::AcceptObserverList::~AcceptObserverList() {
  for (const auto& cb : observers_) {
  cb->acceptorDestroy(acceptor_);
  }
}

void Acceptor::AcceptObserverList::add(AcceptObserver* observer) {
  observers_.emplace_back(observer);
  observer->observerAttach(acceptor_);
}

bool Acceptor::AcceptObserverList::remove(AcceptObserver* observer) {
  const auto eraseIt =
      std::remove(observers_.begin(), observers_.end(), observer);
  if (eraseIt == observers_.end()) {
    return false;
  }

  for (auto it = eraseIt; it != observers_.end(); it++) {
    (*it)->observerDetach(acceptor_);
  }
  observers_.erase(eraseIt, observers_.end());
  return true;
}

} // namespace wangle
