#
# Autogenerated by Thrift
#
# DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
#  @generated
#

import folly.iobuf as _fbthrift_iobuf
import thrift.py3.types
import thrift.py3.exceptions
from thrift.py3.types import __NotSet, NOTSET
import typing as _typing
from typing_extensions import Final

import sys
import itertools
import transitive.types as _transitive_types


__property__ = property


class Included(thrift.py3.types.Struct, _typing.Hashable, _typing.Iterable[_typing.Tuple[str, _typing.Any]]):
    class __fbthrift_IsSet:
        MyIntField: bool
        MyTransitiveField: bool
        pass

    MyIntField: Final[int] = ...

    MyTransitiveField: Final[_transitive_types.Foo] = ...

    def __init__(
        self, *,
        MyIntField: _typing.Optional[int]=None,
        MyTransitiveField: _typing.Optional[_transitive_types.Foo]=None
    ) -> None: ...

    def __call__(
        self, *,
        MyIntField: _typing.Union[int, __NotSet, None]=NOTSET,
        MyTransitiveField: _typing.Union[_transitive_types.Foo, __NotSet, None]=NOTSET
    ) -> Included: ...

    def __reduce__(self) -> _typing.Tuple[_typing.Callable, _typing.Tuple[_typing.Type['Included'], bytes]]: ...
    def __iter__(self) -> _typing.Iterator[_typing.Tuple[str, _typing.Any]]: ...
    def __hash__(self) -> int: ...
    def __lt__(self, other: 'Included') -> bool: ...
    def __gt__(self, other: 'Included') -> bool: ...
    def __le__(self, other: 'Included') -> bool: ...
    def __ge__(self, other: 'Included') -> bool: ...


ExampleIncluded: Included = ...
IncludedConstant: int = ...
IncludedInt64 = int
TransitiveFoo = _transitive_types.Foo
