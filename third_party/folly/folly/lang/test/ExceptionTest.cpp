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

#include <folly/lang/Exception.h>

#include <algorithm>
#include <cstring>
#include <string>

#include <folly/Portability.h>
#include <folly/lang/Keep.h>
#include <folly/lang/Pretty.h>
#include <folly/portability/GTest.h>

extern "C" FOLLY_KEEP void check_cond_std_terminate(bool c) {
  if (c) {
    std::terminate();
  }
  folly::detail::keep_sink();
}
extern "C" FOLLY_KEEP void check_cond_folly_terminate_with(bool c) {
  if (c) {
    folly::terminate_with<std::runtime_error>("bad error");
  }
  folly::detail::keep_sink();
}

template <typename Ex>
static std::string message_for_terminate_with(std::string const& what) {
  auto const name = folly::pretty_name<Ex>();
  std::string const p0 = "terminate called after throwing an instance of";
  std::string const p1 = "terminating with uncaught exception of type";
  // clang-format off
  return
      folly::kIsGlibcxx ? p0 + " '" + name + "'\\s+what\\(\\):\\s+" + what :
      folly::kIsLibcpp ? p1 + " " + name + ": " + what :
      "" /* empty regex matches anything */;
  // clang-format on
}

static std::string message_for_terminate() {
  // clang-format off
  return
      folly::kIsGlibcxx ? "terminate called without an active exception" :
      folly::kIsLibcpp ? "terminating" :
      "" /* empty regex matches anything */;
  // clang-format on
}

class MyException : public std::exception {
 private:
  char const* what_;

 public:
  explicit MyException(char const* const what) : MyException(what, 0) {}
  MyException(char const* const what, std::size_t const strip)
      : what_(what + strip) {}

  char const* what() const noexcept override { return what_; }
};

class ExceptionTest : public testing::Test {};

TEST_F(ExceptionTest, throw_exception_direct) {
  try {
    folly::throw_exception<MyException>("hello world");
    ADD_FAILURE();
  } catch (MyException const& ex) {
    EXPECT_STREQ("hello world", ex.what());
  }
}

TEST_F(ExceptionTest, throw_exception_variadic) {
  try {
    folly::throw_exception<MyException>("hello world", 6);
    ADD_FAILURE();
  } catch (MyException const& ex) {
    EXPECT_STREQ("world", ex.what());
  }
}

TEST_F(ExceptionTest, terminate_with_direct) {
  EXPECT_DEATH(
      folly::terminate_with<MyException>("hello world"),
      message_for_terminate_with<MyException>("hello world"));
}

TEST_F(ExceptionTest, terminate_with_variadic) {
  EXPECT_DEATH(
      folly::terminate_with<MyException>("hello world", 6),
      message_for_terminate_with<MyException>("world"));
}

TEST_F(ExceptionTest, invoke_cold) {
  EXPECT_THROW(
      folly::invoke_cold([] { throw std::runtime_error("bad"); }),
      std::runtime_error);
  EXPECT_EQ(7, folly::invoke_cold([] { return 7; }));
}

TEST_F(ExceptionTest, invoke_noreturn_cold) {
  EXPECT_THROW(
      folly::invoke_noreturn_cold([] { throw std::runtime_error("bad"); }),
      std::runtime_error);
  EXPECT_DEATH(folly::invoke_noreturn_cold([] {}), message_for_terminate());
}

TEST_F(ExceptionTest, catch_exception) {
  auto identity = [](int i) { return i; };
  auto returner = [](int i) { return [=] { return i; }; };
  auto thrower = [](int i) { return [=]() -> int { throw i; }; };
  EXPECT_EQ(3, folly::catch_exception(returner(3), returner(4)));
  EXPECT_EQ(3, folly::catch_exception<int>(returner(3), identity));
  EXPECT_EQ(4, folly::catch_exception(thrower(3), returner(4)));
  EXPECT_EQ(3, folly::catch_exception<int>(thrower(3), identity));
}

TEST_F(ExceptionTest, rethrow_current_exception) {
  EXPECT_THROW(
      folly::invoke_noreturn_cold([] {
        try {
          throw std::runtime_error("bad");
        } catch (...) {
          folly::rethrow_current_exception();
        }
      }),
      std::runtime_error);
}
