#
# Autogenerated by Thrift
#
# DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
#  @generated
#
from cpython cimport bool as pbool, int as pint, float as pfloat

cimport folly.iobuf as _fbthrift_iobuf

cimport thrift.py3.builder


cimport module.types as _module_types

cdef class Foo_Builder(thrift.py3.builder.StructBuilder):
    cdef public pint intField
    cdef public pint optionalIntField
    cdef public pint intFieldWithDefault
    cdef public set setField
    cdef public set optionalSetField
    cdef public dict mapField
    cdef public dict optionalMapField


cdef class Bar_Builder(thrift.py3.builder.StructBuilder):
    cdef public object structField
    cdef public object optionalStructField
    cdef public list structListField
    cdef public list optionalStructListField


