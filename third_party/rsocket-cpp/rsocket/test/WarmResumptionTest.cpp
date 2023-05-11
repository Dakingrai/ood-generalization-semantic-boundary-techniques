// Copyright (c) Facebook, Inc. and its affiliates.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

#include <folly/io/async/ScopedEventBaseThread.h>
#include <gtest/gtest.h>
#include <thread>

#include "RSocketTests.h"

#include "rsocket/test/handlers/HelloServiceHandler.h"
#include "rsocket/test/handlers/HelloStreamRequestHandler.h"

#include "yarpl/flowable/TestSubscriber.h"

using namespace rsocket;
using namespace rsocket::tests;
using namespace rsocket::tests::client_server;
using namespace yarpl::flowable;

TEST(WarmResumptionTest, SuccessfulResumption) {
  folly::ScopedEventBaseThread worker;
  auto server = makeResumableServer(std::make_shared<HelloServiceHandler>());
  auto client =
      makeWarmResumableClient(worker.getEventBase(), *server->listeningPort());
  auto ts = TestSubscriber<std::string>::create(7 /* initialRequestN */);
  client->getRequester()
      ->requestStream(Payload("Bob"))
      ->map([](auto p) { return p.moveDataToString(); })
      ->subscribe(ts);
  // Wait for a few frames before disconnecting.
  while (ts->getValueCount() < 3) {
    std::this_thread::yield();
  }
  auto result =
      client->disconnect(std::runtime_error("Test triggered disconnect"))
          .thenValue([&](auto&&) { return client->resume(); });
  EXPECT_NO_THROW(std::move(result).get());
  ts->request(3);
  ts->awaitTerminalEvent();
  ts->assertSuccess();
  ts->assertValueCount(10);
}

// Verify after resumption the client is able to consume stream
// from within onError() context
TEST(WarmResumptionTest, FailedResumption1) {
  folly::ScopedEventBaseThread worker;
  auto server =
      makeServer(std::make_shared<rsocket::tests::HelloStreamRequestHandler>());
  auto listeningPort = *server->listeningPort();
  auto client = makeWarmResumableClient(worker.getEventBase(), listeningPort);
  auto ts = TestSubscriber<std::string>::create(7 /* initialRequestN */);
  client->getRequester()
      ->requestStream(Payload("Bob"))
      ->map([](auto p) { return p.moveDataToString(); })
      ->subscribe(ts);
  // Wait for a few frames before disconnecting.
  while (ts->getValueCount() < 3) {
    std::this_thread::yield();
  }

  client->disconnect(std::runtime_error("Test triggered disconnect"))
      .thenValue([&](auto&&) { return client->resume(); })
      .thenValue(
          [](auto&&) { FAIL() << "Resumption succeeded when it should not"; })
      .thenError([listeningPort, &worker](folly::exception_wrapper) {
        folly::ScopedEventBaseThread worker2;
        auto newClient =
            makeWarmResumableClient(worker2.getEventBase(), listeningPort);
        auto newTs =
            TestSubscriber<std::string>::create(6 /* initialRequestN */);
        newClient->getRequester()
            ->requestStream(Payload("Alice"))
            ->map([](auto p) { return p.moveDataToString(); })
            ->subscribe(newTs);
        while (newTs->getValueCount() < 3) {
          std::this_thread::yield();
        }
        newTs->request(2);
        newTs->request(2);
        newTs->awaitTerminalEvent();
        newTs->assertSuccess();
        newTs->assertValueCount(10);
      })
      .wait();
}

// Verify after resumption, the client is able to consume stream
// from within and outside of onError() context
TEST(WarmResumptionTest, FailedResumption2) {
  folly::ScopedEventBaseThread worker;
  folly::ScopedEventBaseThread worker2;
  auto server =
      makeServer(std::make_shared<rsocket::tests::HelloStreamRequestHandler>());
  auto listeningPort = *server->listeningPort();
  auto client = makeWarmResumableClient(worker.getEventBase(), listeningPort);
  auto ts = TestSubscriber<std::string>::create(7 /* initialRequestN */);
  client->getRequester()
      ->requestStream(Payload("Bob"))
      ->map([](auto p) { return p.moveDataToString(); })
      ->subscribe(ts);
  // Wait for a few frames before disconnecting.
  while (ts->getValueCount() < 3) {
    std::this_thread::yield();
  }

  auto newTs = TestSubscriber<std::string>::create(6 /* initialRequestN */);
  std::shared_ptr<RSocketClient> newClient;

  client->disconnect(std::runtime_error("Test triggered disconnect"))
      .thenValue([&](auto&&) { return client->resume(); })
      .thenValue(
          [](auto&&) { FAIL() << "Resumption succeeded when it should not"; })
      .thenError([listeningPort, newTs, &newClient, &worker2](
                     folly::exception_wrapper) {
        newClient =
            makeWarmResumableClient(worker2.getEventBase(), listeningPort);
        newClient->getRequester()
            ->requestStream(Payload("Alice"))
            ->map([](auto p) { return p.moveDataToString(); })
            ->subscribe(newTs);
        while (newTs->getValueCount() < 3) {
          std::this_thread::yield();
        }
        newTs->request(2);
      })
      .wait();
  newTs->request(2);
  newTs->awaitTerminalEvent();
  newTs->assertSuccess();
  newTs->assertValueCount(10);
}

// Verify resumption when the stateMachine and Transport run on different
// EventBase
TEST(WarmResumptionTest, DifferentEvb) {
  folly::ScopedEventBaseThread transportWorker;
  folly::ScopedEventBaseThread SMWorker;
  auto server = makeResumableServer(std::make_shared<HelloServiceHandler>());
  auto client = makeWarmResumableClient(
      transportWorker.getEventBase(),
      *server->listeningPort(),
      nullptr, // connectionEvents
      SMWorker.getEventBase());
  auto ts = TestSubscriber<std::string>::create(7 /* initialRequestN */);
  client->getRequester()
      ->requestStream(Payload("Bob"))
      ->map([](auto p) { return p.moveDataToString(); })
      ->subscribe(ts);
  // Wait for a few frames before disconnecting.
  while (ts->getValueCount() < 3) {
    std::this_thread::yield();
  }
  auto result =
      client->disconnect(std::runtime_error("Test triggered disconnect"))
          .thenValue([&](auto&&) { return client->resume(); });
  EXPECT_NO_THROW(std::move(result).get());
  ts->request(3);
  ts->awaitTerminalEvent();
  ts->assertSuccess();
  ts->assertValueCount(10);
}
