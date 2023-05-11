/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

/**
 * Thrift scanner.
 *
 * Tokenizes a thrift definition file.
 */

%option noyywrap
%option reentrant
%option yylineno
%option nounistd
%option never-interactive
%option prefix="fbthrift_compiler_parse_"

%{

#include <errno.h>
#include <stdlib.h>

#include "thrift/compiler/parse/parsing_driver.h"

using parsing_driver = apache::thrift::compiler::parsing_driver;

/**
 * Note macro expansion because this is different between OSS and internal
 * build, sigh.
 */
#include THRIFTY_HH

YY_DECL;

static void integer_overflow(parsing_driver& driver, char* text) {
  driver.failure("This integer is too big: \"%s\"\n", text);
}

static void unexpected_token(parsing_driver& driver, char* text) {
  driver.failure("Unexpected token in input: \"%s\"\n", text);
}

%}

/**
 * Helper definitions, comments, constants, and whatnot
 */

intconstant   ([+-]?[1-9][0-9]*|"0")
octconstant   ("0"[0-7]+)
hexconstant   ("0x"[0-9A-Fa-f]+)
dubconstant   ([+-]?[0-9]*(\.[0-9]+)?([eE][+-]?[0-9]+)?)
identifier    ([a-zA-Z_][\.a-zA-Z_0-9]*)
whitespace    ([ \t\r\n]*)
sillycomm     ("/*""*"*"*/")
multicomm     ("/*"[^*]"/"*([^*/]|[^*]"/"|"*"[^/])*"*"*"*/")
doctext       ("/**"([^*/]|[^*]"/"|"*"[^/])*"*"*"*/")
comment       ("//"[^\n]*)
unixcomment   ("#"[^\n]*)
symbol        ([:;\,\{\}\(\)\=<>\[\]@])
dliteral      ("\""[^"]*"\"")
sliteral      ("'"[^']*"'")
st_identifier ([a-zA-Z-][\.a-zA-Z_0-9-]*)

%%

{whitespace}         { /* do nothing */                 }
{sillycomm}          { /* do nothing */                 }
{multicomm}          { /* do nothing */                 }
{comment}            { /* do nothing */                 }
{unixcomment}        { /* do nothing */                 }

"{"                  {
  return apache::thrift::compiler::yy::parser::make_tok_char_bracket_curly_l();
}
"}"                  {
  return apache::thrift::compiler::yy::parser::make_tok_char_bracket_curly_r();
}

{symbol}             {
  switch (yytext[0]) {
  case ',':
    return apache::thrift::compiler::yy::parser::make_tok_char_comma();
  case ';':
    return apache::thrift::compiler::yy::parser::make_tok_char_semicolon();
  case '{':
    return apache::thrift::compiler::yy::parser::make_tok_char_bracket_curly_l();
  case '}':
    return apache::thrift::compiler::yy::parser::make_tok_char_bracket_curly_r();
  case '=':
    return apache::thrift::compiler::yy::parser::make_tok_char_equal();
  case '[':
    return apache::thrift::compiler::yy::parser::make_tok_char_bracket_square_l();
  case ']':
    return apache::thrift::compiler::yy::parser::make_tok_char_bracket_square_r();
  case ':':
    return apache::thrift::compiler::yy::parser::make_tok_char_colon();
  case '(':
    return apache::thrift::compiler::yy::parser::make_tok_char_bracket_round_l();
  case ')':
    return apache::thrift::compiler::yy::parser::make_tok_char_bracket_round_r();
  case '<':
    return apache::thrift::compiler::yy::parser::make_tok_char_bracket_angle_l();
  case '>':
    return apache::thrift::compiler::yy::parser::make_tok_char_bracket_angle_r();
  case '@':
    return apache::thrift::compiler::yy::parser::make_tok_char_at_sign();
  }

  driver.failure("Invalid symbol encountered.");
}

"false"              { return apache::thrift::compiler::yy::parser::make_tok_bool_constant(0); }
"true"               { return apache::thrift::compiler::yy::parser::make_tok_bool_constant(1); }

"namespace"          { return apache::thrift::compiler::yy::parser::make_tok_namespace();            }
"cpp_include"        { return apache::thrift::compiler::yy::parser::make_tok_cpp_include();          }
"hs_include"         { return apache::thrift::compiler::yy::parser::make_tok_hs_include();           }
"include"            { return apache::thrift::compiler::yy::parser::make_tok_include();              }
"void"               { return apache::thrift::compiler::yy::parser::make_tok_void();                 }
"bool"               { return apache::thrift::compiler::yy::parser::make_tok_bool();                 }
"byte"               { return apache::thrift::compiler::yy::parser::make_tok_byte();                 }
"i16"                { return apache::thrift::compiler::yy::parser::make_tok_i16();                  }
"i32"                { return apache::thrift::compiler::yy::parser::make_tok_i32();                  }
"i64"                { return apache::thrift::compiler::yy::parser::make_tok_i64();                  }
"double"             { return apache::thrift::compiler::yy::parser::make_tok_double();               }
"float"              { return apache::thrift::compiler::yy::parser::make_tok_float();                }
"string"             { return apache::thrift::compiler::yy::parser::make_tok_string();               }
"binary"             { return apache::thrift::compiler::yy::parser::make_tok_binary();               }
"map"                { return apache::thrift::compiler::yy::parser::make_tok_map();                  }
"list"               { return apache::thrift::compiler::yy::parser::make_tok_list();                 }
"set"                { return apache::thrift::compiler::yy::parser::make_tok_set();                  }
"sink"               { return apache::thrift::compiler::yy::parser::make_tok_sink();                 }
"stream"             { return apache::thrift::compiler::yy::parser::make_tok_stream();               }
"interaction"        { return apache::thrift::compiler::yy::parser::make_tok_interaction();          }
"performs"           { return apache::thrift::compiler::yy::parser::make_tok_performs();             }
"oneway"             { return apache::thrift::compiler::yy::parser::make_tok_oneway();               }
"idempotent"         { return apache::thrift::compiler::yy::parser::make_tok_idempotent();           }
"readonly"           { return apache::thrift::compiler::yy::parser::make_tok_readonly();             }
"safe"               { return apache::thrift::compiler::yy::parser::make_tok_safe();                 }
"transient"          { return apache::thrift::compiler::yy::parser::make_tok_transient();            }
"stateful"           { return apache::thrift::compiler::yy::parser::make_tok_stateful();             }
"permanent"          { return apache::thrift::compiler::yy::parser::make_tok_permanent();            }
"server"             { return apache::thrift::compiler::yy::parser::make_tok_server();               }
"client"             { return apache::thrift::compiler::yy::parser::make_tok_client();               }
"typedef"            { return apache::thrift::compiler::yy::parser::make_tok_typedef();              }
"struct"             { return apache::thrift::compiler::yy::parser::make_tok_struct();               }
"union"              { return apache::thrift::compiler::yy::parser::make_tok_union();                }
"exception"          { return apache::thrift::compiler::yy::parser::make_tok_exception();            }
"extends"            { return apache::thrift::compiler::yy::parser::make_tok_extends();              }
"throws"             { return apache::thrift::compiler::yy::parser::make_tok_throws();               }
"service"            { return apache::thrift::compiler::yy::parser::make_tok_service();              }
"enum"               { return apache::thrift::compiler::yy::parser::make_tok_enum();                 }
"const"              { return apache::thrift::compiler::yy::parser::make_tok_const();                }
"required"           { return apache::thrift::compiler::yy::parser::make_tok_required();             }
"optional"           { return apache::thrift::compiler::yy::parser::make_tok_optional();             }

{octconstant} {
  errno = 0;
  int64_t val = strtoll(yytext+1, NULL, 8);
  if (errno == ERANGE) {
    integer_overflow(driver, yytext);
  }
  return apache::thrift::compiler::yy::parser::make_tok_int_constant(val);
}

{intconstant} {
  errno = 0;
  int64_t val = strtoll(yytext, NULL, 10);
  if (errno == ERANGE) {
    integer_overflow(driver, yytext);
  }
  return apache::thrift::compiler::yy::parser::make_tok_int_constant(val);
}

{hexconstant} {
  errno = 0;
  int64_t val = strtoll(yytext+2, NULL, 16);
  if (errno == ERANGE) {
    integer_overflow(driver, yytext);
  }
  return apache::thrift::compiler::yy::parser::make_tok_int_constant(val);
}

{dubconstant} {
  double val = atof(yytext);
  return apache::thrift::compiler::yy::parser::make_tok_dub_constant(val);
}

{identifier} {
  return apache::thrift::compiler::yy::parser::make_tok_identifier(std::string{yytext});
}

{st_identifier} {
  return apache::thrift::compiler::yy::parser::make_tok_st_identifier(std::string{yytext});
}

{dliteral} {
  std::string val{yytext + 1};
  val = val.substr(0, val.length() - 1);
  return apache::thrift::compiler::yy::parser::make_tok_literal(std::move(val));
}

{sliteral} {
  std::string val{yytext + 1};
  val = val.substr(0, val.length() - 1);
  return apache::thrift::compiler::yy::parser::make_tok_literal(std::move(val));
}

{doctext} {
 /* This does not show up in the parse tree. */
 /* Rather, the parser will grab it out of the global. */
  if (driver.mode == apache::thrift::compiler::parsing_mode::PROGRAM) {
    std::string doctext{yytext + 3};
    doctext = doctext.substr(0, doctext.length() - 2);

    driver.clear_doctext();
    driver.doctext = driver.clean_up_doctext(doctext);
    driver.doctext_lineno = yylineno;
  }
}

. {
  unexpected_token(driver, yytext);
}

<<EOF>> {
  return apache::thrift::compiler::yy::parser::make_tok_eof();
}

%%

/* vim: filetype=lex
*/
