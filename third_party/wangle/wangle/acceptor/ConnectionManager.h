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

#pragma once

#include <wangle/acceptor/ManagedConnection.h>

#include <chrono>
#include <iterator>
#include <utility>
#include <folly/Memory.h>
#include <folly/io/async/AsyncTimeout.h>
#include <folly/io/async/HHWheelTimer.h>
#include <folly/io/async/DelayedDestruction.h>
#include <folly/io/async/EventBase.h>

namespace wangle {

/**
 * A ConnectionManager keeps track of ManagedConnections.
 */
class ConnectionManager: public folly::DelayedDestruction,
                         private ManagedConnection::Callback {
 public:

  /**
   * Interface for an optional observer that's notified about
   * various events in a ConnectionManager
   */
  class Callback {
  public:
    virtual ~Callback() = default;

    /**
     * Invoked when the number of connections managed by the
     * ConnectionManager changes from nonzero to zero.
     */
    virtual void onEmpty(const ConnectionManager& cm) = 0;

    /**
     * Invoked when a connection is added to the ConnectionManager.
     */
    virtual void onConnectionAdded(const ManagedConnection* conn) = 0;

    /**
     * Invoked when a connection is removed from the ConnectionManager.
     */
    virtual void onConnectionRemoved(const ManagedConnection* conn) = 0;
  };

  typedef std::unique_ptr<ConnectionManager, Destructor> UniquePtr;

  typedef folly::CountedIntrusiveList<
    ManagedConnection, &ManagedConnection::listHook_>::iterator ConnectionIterator;
  /**
   * Returns a new instance of ConnectionManager wrapped in a unique_ptr
   */
  template<typename... Args>
  static UniquePtr makeUnique(Args&&... args) {
    return UniquePtr(new ConnectionManager(std::forward<Args>(args)...));
  }

  /**
   * Constructor not to be used by itself.
   */
  ConnectionManager(folly::EventBase* eventBase,
                    std::chrono::milliseconds timeout,
                    Callback* callback = nullptr);

  /**
   * Add a connection to the set of connections managed by this
   * ConnectionManager.
   *
   * @param connection     The connection to add.
   * @param timeout        Whether to immediately register this connection
   *                         for an idle timeout callback.
   */
  void addConnection(ManagedConnection* connection,
      bool timeout = false);

  /**
   * Schedule a timeout callback for a connection.
   */
  void scheduleTimeout(ManagedConnection* const connection,
                       std::chrono::milliseconds timeout);

  /*
   * Schedule a callback on the wheel timer
   */
  void scheduleTimeout(folly::HHWheelTimer::Callback* callback,
                       std::chrono::milliseconds timeout);

  /**
   * Remove a connection from this ConnectionManager and, if
   * applicable, cancel the pending timeout callback that the
   * ConnectionManager has scheduled for the connection.
   *
   * @note This method does NOT destroy the connection.
   */
  void removeConnection(ManagedConnection* connection);

  /* Begin gracefully shutting down connections in this ConnectionManager.
   * Notify all connections of pending shutdown, and after idleGrace,
   * begin closing idle connections.
   */
  void initiateGracefulShutdown(std::chrono::milliseconds idleGrace);

  /**
   * Gracefully shutdown certain percentage of persistent client connections
   * and leave the rest intact.
   */
  void drainConnections(double pct, std::chrono::milliseconds idleGrace);

  /**
   * Destroy all connections Managed by this ConnectionManager, even
   * the ones that are busy.
   */
  void dropAllConnections();

  /**
   * Force-stop "pct" (0.0 to 1.0) of remaining client connections,
   * regardless of whether they are busy or idle.
   */
  void dropConnections(double pct);

  size_t getNumConnections() const { return conns_.size(); }

  template <typename F>
  void iterateConns(F func) {
    auto it = conns_.begin();
    while ( it != conns_.end()) {
      func(&(*it));
      it++;
    }
  }

  std::chrono::milliseconds getDefaultTimeout() const {
    return timeout_;
  }

  std::chrono::milliseconds getIdleConnEarlyDropThreshold() const {
    return idleConnEarlyDropThreshold_;
  }

  void setLoweredIdleTimeout(std::chrono::milliseconds timeout) {
    CHECK(timeout >= std::chrono::milliseconds(0));
    CHECK(timeout <= timeout_);
    idleConnEarlyDropThreshold_ = timeout;
  }

  /**
   * try to drop num idle connections to release system resources.  Return the
   * actual number of dropped idle connections
   */
  size_t dropIdleConnections(size_t num);

  /**
   * ManagedConnection::Callbacks
   */
  void onActivated(ManagedConnection& conn) override;

  void onDeactivated(ManagedConnection& conn) override;

 private:

   enum class ShutdownState : uint8_t {
     NONE = 0,
     // All ManagedConnections receive notifyPendingShutdown
     NOTIFY_PENDING_SHUTDOWN = 1,
     // All ManagedConnections have received notifyPendingShutdown
     NOTIFY_PENDING_SHUTDOWN_COMPLETE = 2,
     // All ManagedConnections receive closeWhenIdle
     CLOSE_WHEN_IDLE = 3,
     // All ManagedConnections have received closeWhenIdle
     CLOSE_WHEN_IDLE_COMPLETE = 4,
   };

  class DrainHelper :
      public folly::EventBase::LoopCallback,
      public folly::AsyncTimeout {
   public:
    explicit DrainHelper(ConnectionManager& manager)
        : folly::AsyncTimeout(manager.eventBase_),
          manager_(manager) {}

    ShutdownState getShutdownState() {
      // only cares about full shutdown state
      if (!all_) {
        return ShutdownState::NONE;
      }
      return shutdownState_;
    }

    void setShutdownState(ShutdownState state) {
      shutdownState_ = state;
    }

    void startDrainPartial(double pct, std::chrono::milliseconds idleGrace);
    void startDrainAll(std::chrono::milliseconds idleGrace);

    void runLoopCallback() noexcept override {
      VLOG(3) << "Draining more conns from loop callback";
      drainConnections();
    }

    void timeoutExpired() noexcept override {
      VLOG(3) << "Idle grace expired";
      idleGracefulTimeoutExpired();
    }

    void drainConnections();

    void idleGracefulTimeoutExpired();

    void startDrain(std::chrono::milliseconds idleGrace);

    ConnectionIterator drainStartIterator() const {
      if (all_) {
        return manager_.conns_.begin();
      }
      auto it = manager_.conns_.begin();
      const auto conns_size = manager_.conns_.size();
      const auto numToDrain =
        std::max<size_t>(0, std::min<size_t>(conns_size, conns_size * pct_));
      std::advance(it, conns_size - numToDrain);
      return it;
    }

   private:
    bool all_{true};
    double pct_{1.0};
    ConnectionManager& manager_;
    ShutdownState shutdownState_{ShutdownState::NONE};
  };

  ~ConnectionManager() override = default;

  ConnectionManager(const ConnectionManager&) = delete;
  ConnectionManager& operator=(ConnectionManager&) = delete;

  /**
   * Destroy all connections managed by this ConnectionManager that
   * are currently idle, as determined by a call to each ManagedConnection's
   * isBusy() method.
   */
  void drainAllConnections();

  /**
   * Signal the drain helper that we are about to start dropping connections.
   */
  void stopDrainingForShutdown();

  void idleGracefulTimeoutExpired();

  /**
   * All the managed connections. idleIterator_ seperates them into two parts:
   * idle and busy ones.  [conns_.begin(), idleIterator_) are the busy ones,
   * while [idleIterator_, conns_.end()) are the idle one. Moreover, the idle
   * ones are organized in the decreasing idle time order. */
  folly::CountedIntrusiveList<
    ManagedConnection,&ManagedConnection::listHook_> conns_;

  /** Optional callback to notify of state changes */
  Callback* callback_;

  /** Event base in which we run */
  folly::EventBase* eventBase_;

  /** Iterator to the next connection to shed; used by drainAllConnections() */
  ConnectionIterator drainIterator_;
  ConnectionIterator idleIterator_;
  DrainHelper drainHelper_;
  bool notifyPendingShutdown_{true};

  /**
   * the default idle timeout for downstream sessions when no system resource
   * limit is reached
   */
  std::chrono::milliseconds timeout_;

  /**
   * The idle connections can be closed earlier that their idle timeout when any
   * system resource limit is reached.  This feature can be considerred as a pre
   * load shedding stage for the system, and can be easily disabled by setting
   * idleConnEarlyDropThreshold_ to defaultIdleTimeout_. Also,
   * idleConnEarlyDropThreshold_ can be used to bottom the idle timeout. That
   * is, connection manager will not early drop the idle connections whose idle
   * time is less than idleConnEarlyDropThreshold_.
   */
  std::chrono::milliseconds idleConnEarlyDropThreshold_;
};

} // wangle
