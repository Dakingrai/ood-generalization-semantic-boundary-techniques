/**
 * Autogenerated by Thrift
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */

#pragma once

#include <functional>
#include <folly/Range.h>

#include <thrift/lib/py3/enums.h>
#include "gen-cpp2/hsmodule_data.h"
#include "gen-cpp2/hsmodule_types.h"
#include "gen-cpp2/hsmodule_metadata.h"
namespace thrift {
namespace py3 {



template<>
void reset_field<::cpp2::HsFoo>(
    ::cpp2::HsFoo& obj, uint16_t index) {
  switch (index) {
    case 0:
      obj.MyInt_ref().copy_from(default_inst<::cpp2::HsFoo>().MyInt_ref());
      return;
  }
}

template<>
const std::unordered_map<std::string_view, std::string_view>& PyStructTraits<
    ::cpp2::HsFoo>::namesmap() {
  static const folly::Indestructible<NamesMap> map {
    {
    }
  };
  return *map;
}
} // namespace py3
} // namespace thrift
