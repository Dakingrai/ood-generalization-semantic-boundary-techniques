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

#include <thrift/compiler/generate/t_mstch_generator.h>

namespace apache {
namespace thrift {
namespace compiler {

class t_mstch_html_generator : public t_mstch_generator {
 public:
  t_mstch_html_generator(
      t_program* program,
      t_generation_context context,
      const std::map<std::string, std::string>& parsed_options,
      const std::string& option_string);

  void generate_program() override;
};

t_mstch_html_generator::t_mstch_html_generator(
    t_program* program,
    t_generation_context context,
    const std::map<std::string, std::string>& parsed_options,
    const std::string& /* option_string */)
    : t_mstch_generator(program, std::move(context), "html", parsed_options) {
  this->out_dir_base_ = "gen-mstch_html";
}

void t_mstch_html_generator::generate_program() {
  // Generate index.html
  render_to_file(*this->get_program(), "index.html", "index.html");
}

THRIFT_REGISTER_GENERATOR(mstch_html, "HTML", "");

} // namespace compiler
} // namespace thrift
} // namespace apache
