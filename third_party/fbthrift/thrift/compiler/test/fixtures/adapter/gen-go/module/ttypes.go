// Autogenerated by Thrift Compiler (facebook)
// DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
// @generated

package module

import (
	"bytes"
	"context"
	"sync"
	"fmt"
	thrift "github.com/facebook/fbthrift/thrift/lib/go/thrift"
)

// (needed to ensure safety because of naive import list construction.)
var _ = thrift.ZERO
var _ = fmt.Printf
var _ = sync.Mutex{}
var _ = bytes.Equal
var _ = context.Background

var GoUnusedProtection__ int;

type SetWithAdapter = []string

func SetWithAdapterPtr(v SetWithAdapter) *SetWithAdapter { return &v }

type ListWithElemAdapter = []string

func ListWithElemAdapterPtr(v ListWithElemAdapter) *ListWithElemAdapter { return &v }

type StructWithAdapter = *Bar

func StructWithAdapterPtr(v StructWithAdapter) *StructWithAdapter { return &v }

func NewStructWithAdapter() StructWithAdapter { return NewBar() }

// Attributes:
//  - IntField
//  - OptionalIntField
//  - IntFieldWithDefault
//  - SetField
//  - OptionalSetField
//  - MapField
//  - OptionalMapField
type Foo struct {
  IntField int32 `thrift:"intField,1" db:"intField" json:"intField"`
  OptionalIntField *int32 `thrift:"optionalIntField,2" db:"optionalIntField" json:"optionalIntField,omitempty"`
  IntFieldWithDefault int32 `thrift:"intFieldWithDefault,3" db:"intFieldWithDefault" json:"intFieldWithDefault"`
  SetField SetWithAdapter `thrift:"setField,4" db:"setField" json:"setField"`
  OptionalSetField SetWithAdapter `thrift:"optionalSetField,5" db:"optionalSetField" json:"optionalSetField,omitempty"`
  MapField map[string]ListWithElemAdapter `thrift:"mapField,6" db:"mapField" json:"mapField"`
  OptionalMapField map[string]ListWithElemAdapter `thrift:"optionalMapField,7" db:"optionalMapField" json:"optionalMapField,omitempty"`
}

func NewFoo() *Foo {
  return &Foo{
    IntFieldWithDefault: 13,
  }
}


func (p *Foo) GetIntField() int32 {
  return p.IntField
}
var Foo_OptionalIntField_DEFAULT int32
func (p *Foo) GetOptionalIntField() int32 {
  if !p.IsSetOptionalIntField() {
    return Foo_OptionalIntField_DEFAULT
  }
return *p.OptionalIntField
}

func (p *Foo) GetIntFieldWithDefault() int32 {
  return p.IntFieldWithDefault
}

func (p *Foo) GetSetField() SetWithAdapter {
  return p.SetField
}
var Foo_OptionalSetField_DEFAULT SetWithAdapter

func (p *Foo) GetOptionalSetField() SetWithAdapter {
  return p.OptionalSetField
}

func (p *Foo) GetMapField() map[string]ListWithElemAdapter {
  return p.MapField
}
var Foo_OptionalMapField_DEFAULT map[string]ListWithElemAdapter

func (p *Foo) GetOptionalMapField() map[string]ListWithElemAdapter {
  return p.OptionalMapField
}
func (p *Foo) IsSetOptionalIntField() bool {
  return p != nil && p.OptionalIntField != nil
}

func (p *Foo) IsSetOptionalSetField() bool {
  return p != nil && p.OptionalSetField != nil
}

func (p *Foo) IsSetOptionalMapField() bool {
  return p != nil && p.OptionalMapField != nil
}

type FooBuilder struct {
  obj *Foo
}

func NewFooBuilder() *FooBuilder{
  return &FooBuilder{
    obj: NewFoo(),
  }
}

func (p FooBuilder) Emit() *Foo{
  return &Foo{
    IntField: p.obj.IntField,
    OptionalIntField: p.obj.OptionalIntField,
    IntFieldWithDefault: p.obj.IntFieldWithDefault,
    SetField: p.obj.SetField,
    OptionalSetField: p.obj.OptionalSetField,
    MapField: p.obj.MapField,
    OptionalMapField: p.obj.OptionalMapField,
  }
}

func (f *FooBuilder) IntField(intField int32) *FooBuilder {
  f.obj.IntField = intField
  return f
}

func (f *FooBuilder) OptionalIntField(optionalIntField *int32) *FooBuilder {
  f.obj.OptionalIntField = optionalIntField
  return f
}

func (f *FooBuilder) IntFieldWithDefault(intFieldWithDefault int32) *FooBuilder {
  f.obj.IntFieldWithDefault = intFieldWithDefault
  return f
}

func (f *FooBuilder) SetField(setField SetWithAdapter) *FooBuilder {
  f.obj.SetField = setField
  return f
}

func (f *FooBuilder) OptionalSetField(optionalSetField SetWithAdapter) *FooBuilder {
  f.obj.OptionalSetField = optionalSetField
  return f
}

func (f *FooBuilder) MapField(mapField map[string]ListWithElemAdapter) *FooBuilder {
  f.obj.MapField = mapField
  return f
}

func (f *FooBuilder) OptionalMapField(optionalMapField map[string]ListWithElemAdapter) *FooBuilder {
  f.obj.OptionalMapField = optionalMapField
  return f
}

func (f *Foo) SetIntField(intField int32) *Foo {
  f.IntField = intField
  return f
}

func (f *Foo) SetOptionalIntField(optionalIntField *int32) *Foo {
  f.OptionalIntField = optionalIntField
  return f
}

func (f *Foo) SetIntFieldWithDefault(intFieldWithDefault int32) *Foo {
  f.IntFieldWithDefault = intFieldWithDefault
  return f
}

func (f *Foo) SetSetField(setField SetWithAdapter) *Foo {
  f.SetField = setField
  return f
}

func (f *Foo) SetOptionalSetField(optionalSetField SetWithAdapter) *Foo {
  f.OptionalSetField = optionalSetField
  return f
}

func (f *Foo) SetMapField(mapField map[string]ListWithElemAdapter) *Foo {
  f.MapField = mapField
  return f
}

func (f *Foo) SetOptionalMapField(optionalMapField map[string]ListWithElemAdapter) *Foo {
  f.OptionalMapField = optionalMapField
  return f
}

func (p *Foo) Read(iprot thrift.Protocol) error {
  if _, err := iprot.ReadStructBegin(); err != nil {
    return thrift.PrependError(fmt.Sprintf("%T read error: ", p), err)
  }


  for {
    _, fieldTypeId, fieldId, err := iprot.ReadFieldBegin()
    if err != nil {
      return thrift.PrependError(fmt.Sprintf("%T field %d read error: ", p, fieldId), err)
    }
    if fieldTypeId == thrift.STOP { break; }
    switch fieldId {
    case 1:
      if err := p.ReadField1(iprot); err != nil {
        return err
      }
    case 2:
      if err := p.ReadField2(iprot); err != nil {
        return err
      }
    case 3:
      if err := p.ReadField3(iprot); err != nil {
        return err
      }
    case 4:
      if err := p.ReadField4(iprot); err != nil {
        return err
      }
    case 5:
      if err := p.ReadField5(iprot); err != nil {
        return err
      }
    case 6:
      if err := p.ReadField6(iprot); err != nil {
        return err
      }
    case 7:
      if err := p.ReadField7(iprot); err != nil {
        return err
      }
    default:
      if err := iprot.Skip(fieldTypeId); err != nil {
        return err
      }
    }
    if err := iprot.ReadFieldEnd(); err != nil {
      return err
    }
  }
  if err := iprot.ReadStructEnd(); err != nil {
    return thrift.PrependError(fmt.Sprintf("%T read struct end error: ", p), err)
  }
  return nil
}

func (p *Foo)  ReadField1(iprot thrift.Protocol) error {
  if v, err := iprot.ReadI32(); err != nil {
    return thrift.PrependError("error reading field 1: ", err)
  } else {
    p.IntField = v
  }
  return nil
}

func (p *Foo)  ReadField2(iprot thrift.Protocol) error {
  if v, err := iprot.ReadI32(); err != nil {
    return thrift.PrependError("error reading field 2: ", err)
  } else {
    p.OptionalIntField = &v
  }
  return nil
}

func (p *Foo)  ReadField3(iprot thrift.Protocol) error {
  if v, err := iprot.ReadI32(); err != nil {
    return thrift.PrependError("error reading field 3: ", err)
  } else {
    p.IntFieldWithDefault = v
  }
  return nil
}

func (p *Foo)  ReadField4(iprot thrift.Protocol) error {
  _, size, err := iprot.ReadSetBegin()
  if err != nil {
    return thrift.PrependError("error reading set begin: ", err)
  }
  tSet := make(SetWithAdapter, 0, size)
  p.SetField =  tSet
  for i := 0; i < size; i ++ {
    var _elem0 string
    if v, err := iprot.ReadString(); err != nil {
      return thrift.PrependError("error reading field 0: ", err)
    } else {
      _elem0 = v
    }
    p.SetField = append(p.SetField, _elem0)
  }
  if err := iprot.ReadSetEnd(); err != nil {
    return thrift.PrependError("error reading set end: ", err)
  }
  return nil
}

func (p *Foo)  ReadField5(iprot thrift.Protocol) error {
  _, size, err := iprot.ReadSetBegin()
  if err != nil {
    return thrift.PrependError("error reading set begin: ", err)
  }
  tSet := make(SetWithAdapter, 0, size)
  p.OptionalSetField =  tSet
  for i := 0; i < size; i ++ {
    var _elem1 string
    if v, err := iprot.ReadString(); err != nil {
      return thrift.PrependError("error reading field 0: ", err)
    } else {
      _elem1 = v
    }
    p.OptionalSetField = append(p.OptionalSetField, _elem1)
  }
  if err := iprot.ReadSetEnd(); err != nil {
    return thrift.PrependError("error reading set end: ", err)
  }
  return nil
}

func (p *Foo)  ReadField6(iprot thrift.Protocol) error {
  _, _, size, err := iprot.ReadMapBegin()
  if err != nil {
    return thrift.PrependError("error reading map begin: ", err)
  }
  tMap := make(map[string]ListWithElemAdapter, size)
  p.MapField =  tMap
  for i := 0; i < size; i ++ {
    var _key2 string
    if v, err := iprot.ReadString(); err != nil {
      return thrift.PrependError("error reading field 0: ", err)
    } else {
      _key2 = v
    }
    _, size, err := iprot.ReadListBegin()
    if err != nil {
      return thrift.PrependError("error reading list begin: ", err)
    }
    tSlice := make(ListWithElemAdapter, 0, size)
    _val3 :=  tSlice
    for i := 0; i < size; i ++ {
      var _elem4 string
      if v, err := iprot.ReadString(); err != nil {
        return thrift.PrependError("error reading field 0: ", err)
      } else {
        _elem4 = v
      }
      _val3 = append(_val3, _elem4)
    }
    if err := iprot.ReadListEnd(); err != nil {
      return thrift.PrependError("error reading list end: ", err)
    }
    p.MapField[_key2] = _val3
  }
  if err := iprot.ReadMapEnd(); err != nil {
    return thrift.PrependError("error reading map end: ", err)
  }
  return nil
}

func (p *Foo)  ReadField7(iprot thrift.Protocol) error {
  _, _, size, err := iprot.ReadMapBegin()
  if err != nil {
    return thrift.PrependError("error reading map begin: ", err)
  }
  tMap := make(map[string]ListWithElemAdapter, size)
  p.OptionalMapField =  tMap
  for i := 0; i < size; i ++ {
    var _key5 string
    if v, err := iprot.ReadString(); err != nil {
      return thrift.PrependError("error reading field 0: ", err)
    } else {
      _key5 = v
    }
    _, size, err := iprot.ReadListBegin()
    if err != nil {
      return thrift.PrependError("error reading list begin: ", err)
    }
    tSlice := make(ListWithElemAdapter, 0, size)
    _val6 :=  tSlice
    for i := 0; i < size; i ++ {
      var _elem7 string
      if v, err := iprot.ReadString(); err != nil {
        return thrift.PrependError("error reading field 0: ", err)
      } else {
        _elem7 = v
      }
      _val6 = append(_val6, _elem7)
    }
    if err := iprot.ReadListEnd(); err != nil {
      return thrift.PrependError("error reading list end: ", err)
    }
    p.OptionalMapField[_key5] = _val6
  }
  if err := iprot.ReadMapEnd(); err != nil {
    return thrift.PrependError("error reading map end: ", err)
  }
  return nil
}

func (p *Foo) Write(oprot thrift.Protocol) error {
  if err := oprot.WriteStructBegin("Foo"); err != nil {
    return thrift.PrependError(fmt.Sprintf("%T write struct begin error: ", p), err) }
  if err := p.writeField1(oprot); err != nil { return err }
  if err := p.writeField2(oprot); err != nil { return err }
  if err := p.writeField3(oprot); err != nil { return err }
  if err := p.writeField4(oprot); err != nil { return err }
  if err := p.writeField5(oprot); err != nil { return err }
  if err := p.writeField6(oprot); err != nil { return err }
  if err := p.writeField7(oprot); err != nil { return err }
  if err := oprot.WriteFieldStop(); err != nil {
    return thrift.PrependError("write field stop error: ", err) }
  if err := oprot.WriteStructEnd(); err != nil {
    return thrift.PrependError("write struct stop error: ", err) }
  return nil
}

func (p *Foo) writeField1(oprot thrift.Protocol) (err error) {
  if err := oprot.WriteFieldBegin("intField", thrift.I32, 1); err != nil {
    return thrift.PrependError(fmt.Sprintf("%T write field begin error 1:intField: ", p), err) }
  if err := oprot.WriteI32(int32(p.IntField)); err != nil {
  return thrift.PrependError(fmt.Sprintf("%T.intField (1) field write error: ", p), err) }
  if err := oprot.WriteFieldEnd(); err != nil {
    return thrift.PrependError(fmt.Sprintf("%T write field end error 1:intField: ", p), err) }
  return err
}

func (p *Foo) writeField2(oprot thrift.Protocol) (err error) {
  if p.IsSetOptionalIntField() {
    if err := oprot.WriteFieldBegin("optionalIntField", thrift.I32, 2); err != nil {
      return thrift.PrependError(fmt.Sprintf("%T write field begin error 2:optionalIntField: ", p), err) }
    if err := oprot.WriteI32(int32(*p.OptionalIntField)); err != nil {
    return thrift.PrependError(fmt.Sprintf("%T.optionalIntField (2) field write error: ", p), err) }
    if err := oprot.WriteFieldEnd(); err != nil {
      return thrift.PrependError(fmt.Sprintf("%T write field end error 2:optionalIntField: ", p), err) }
  }
  return err
}

func (p *Foo) writeField3(oprot thrift.Protocol) (err error) {
  if err := oprot.WriteFieldBegin("intFieldWithDefault", thrift.I32, 3); err != nil {
    return thrift.PrependError(fmt.Sprintf("%T write field begin error 3:intFieldWithDefault: ", p), err) }
  if err := oprot.WriteI32(int32(p.IntFieldWithDefault)); err != nil {
  return thrift.PrependError(fmt.Sprintf("%T.intFieldWithDefault (3) field write error: ", p), err) }
  if err := oprot.WriteFieldEnd(); err != nil {
    return thrift.PrependError(fmt.Sprintf("%T write field end error 3:intFieldWithDefault: ", p), err) }
  return err
}

func (p *Foo) writeField4(oprot thrift.Protocol) (err error) {
  if err := oprot.WriteFieldBegin("setField", thrift.SET, 4); err != nil {
    return thrift.PrependError(fmt.Sprintf("%T write field begin error 4:setField: ", p), err) }
  if err := oprot.WriteSetBegin(thrift.STRING, len(p.SetField)); err != nil {
    return thrift.PrependError("error writing set begin: ", err)
  }
  set := make(map[string]bool, len(p.SetField))
  for _, v := range p.SetField {
    if ok := set[v]; ok {
      return thrift.PrependError("", fmt.Errorf("%T error writing set field: slice is not unique", v))
    }
    set[v] = true
  }
  for _, v := range p.SetField {
    if err := oprot.WriteString(string(v)); err != nil {
    return thrift.PrependError(fmt.Sprintf("%T. (0) field write error: ", p), err) }
  }
  if err := oprot.WriteSetEnd(); err != nil {
    return thrift.PrependError("error writing set end: ", err)
  }
  if err := oprot.WriteFieldEnd(); err != nil {
    return thrift.PrependError(fmt.Sprintf("%T write field end error 4:setField: ", p), err) }
  return err
}

func (p *Foo) writeField5(oprot thrift.Protocol) (err error) {
  if p.IsSetOptionalSetField() {
    if err := oprot.WriteFieldBegin("optionalSetField", thrift.SET, 5); err != nil {
      return thrift.PrependError(fmt.Sprintf("%T write field begin error 5:optionalSetField: ", p), err) }
    if err := oprot.WriteSetBegin(thrift.STRING, len(p.OptionalSetField)); err != nil {
      return thrift.PrependError("error writing set begin: ", err)
    }
    set := make(map[string]bool, len(p.OptionalSetField))
    for _, v := range p.OptionalSetField {
      if ok := set[v]; ok {
        return thrift.PrependError("", fmt.Errorf("%T error writing set field: slice is not unique", v))
      }
      set[v] = true
    }
    for _, v := range p.OptionalSetField {
      if err := oprot.WriteString(string(v)); err != nil {
      return thrift.PrependError(fmt.Sprintf("%T. (0) field write error: ", p), err) }
    }
    if err := oprot.WriteSetEnd(); err != nil {
      return thrift.PrependError("error writing set end: ", err)
    }
    if err := oprot.WriteFieldEnd(); err != nil {
      return thrift.PrependError(fmt.Sprintf("%T write field end error 5:optionalSetField: ", p), err) }
  }
  return err
}

func (p *Foo) writeField6(oprot thrift.Protocol) (err error) {
  if err := oprot.WriteFieldBegin("mapField", thrift.MAP, 6); err != nil {
    return thrift.PrependError(fmt.Sprintf("%T write field begin error 6:mapField: ", p), err) }
  if err := oprot.WriteMapBegin(thrift.STRING, thrift.LIST, len(p.MapField)); err != nil {
    return thrift.PrependError("error writing map begin: ", err)
  }
  for k, v := range p.MapField {
    if err := oprot.WriteString(string(k)); err != nil {
    return thrift.PrependError(fmt.Sprintf("%T. (0) field write error: ", p), err) }
    if err := oprot.WriteListBegin(thrift.STRING, len(v)); err != nil {
      return thrift.PrependError("error writing list begin: ", err)
    }
    for _, v := range v {
      if err := oprot.WriteString(string(v)); err != nil {
      return thrift.PrependError(fmt.Sprintf("%T. (0) field write error: ", p), err) }
    }
    if err := oprot.WriteListEnd(); err != nil {
      return thrift.PrependError("error writing list end: ", err)
    }
  }
  if err := oprot.WriteMapEnd(); err != nil {
    return thrift.PrependError("error writing map end: ", err)
  }
  if err := oprot.WriteFieldEnd(); err != nil {
    return thrift.PrependError(fmt.Sprintf("%T write field end error 6:mapField: ", p), err) }
  return err
}

func (p *Foo) writeField7(oprot thrift.Protocol) (err error) {
  if p.IsSetOptionalMapField() {
    if err := oprot.WriteFieldBegin("optionalMapField", thrift.MAP, 7); err != nil {
      return thrift.PrependError(fmt.Sprintf("%T write field begin error 7:optionalMapField: ", p), err) }
    if err := oprot.WriteMapBegin(thrift.STRING, thrift.LIST, len(p.OptionalMapField)); err != nil {
      return thrift.PrependError("error writing map begin: ", err)
    }
    for k, v := range p.OptionalMapField {
      if err := oprot.WriteString(string(k)); err != nil {
      return thrift.PrependError(fmt.Sprintf("%T. (0) field write error: ", p), err) }
      if err := oprot.WriteListBegin(thrift.STRING, len(v)); err != nil {
        return thrift.PrependError("error writing list begin: ", err)
      }
      for _, v := range v {
        if err := oprot.WriteString(string(v)); err != nil {
        return thrift.PrependError(fmt.Sprintf("%T. (0) field write error: ", p), err) }
      }
      if err := oprot.WriteListEnd(); err != nil {
        return thrift.PrependError("error writing list end: ", err)
      }
    }
    if err := oprot.WriteMapEnd(); err != nil {
      return thrift.PrependError("error writing map end: ", err)
    }
    if err := oprot.WriteFieldEnd(); err != nil {
      return thrift.PrependError(fmt.Sprintf("%T write field end error 7:optionalMapField: ", p), err) }
  }
  return err
}

func (p *Foo) String() string {
  if p == nil {
    return "<nil>"
  }

  intFieldVal := fmt.Sprintf("%v", p.IntField)
  var optionalIntFieldVal string
  if p.OptionalIntField == nil {
    optionalIntFieldVal = "<nil>"
  } else {
    optionalIntFieldVal = fmt.Sprintf("%v", *p.OptionalIntField)
  }
  intFieldWithDefaultVal := fmt.Sprintf("%v", p.IntFieldWithDefault)
  setFieldVal := fmt.Sprintf("%v", p.SetField)
  optionalSetFieldVal := fmt.Sprintf("%v", p.OptionalSetField)
  mapFieldVal := fmt.Sprintf("%v", p.MapField)
  optionalMapFieldVal := fmt.Sprintf("%v", p.OptionalMapField)
  return fmt.Sprintf("Foo({IntField:%s OptionalIntField:%s IntFieldWithDefault:%s SetField:%s OptionalSetField:%s MapField:%s OptionalMapField:%s})", intFieldVal, optionalIntFieldVal, intFieldWithDefaultVal, setFieldVal, optionalSetFieldVal, mapFieldVal, optionalMapFieldVal)
}

// Attributes:
//  - StructField
//  - OptionalStructField
//  - StructListField
//  - OptionalStructListField
type Bar struct {
  StructField *Foo `thrift:"structField,1" db:"structField" json:"structField"`
  OptionalStructField *Foo `thrift:"optionalStructField,2" db:"optionalStructField" json:"optionalStructField,omitempty"`
  StructListField []*Foo `thrift:"structListField,3" db:"structListField" json:"structListField"`
  OptionalStructListField []*Foo `thrift:"optionalStructListField,4" db:"optionalStructListField" json:"optionalStructListField,omitempty"`
}

func NewBar() *Bar {
  return &Bar{
    StructField: NewFoo(),
  }
}

var Bar_StructField_DEFAULT *Foo
func (p *Bar) GetStructField() *Foo {
  if !p.IsSetStructField() {
    return Bar_StructField_DEFAULT
  }
return p.StructField
}
var Bar_OptionalStructField_DEFAULT *Foo
func (p *Bar) GetOptionalStructField() *Foo {
  if !p.IsSetOptionalStructField() {
    return Bar_OptionalStructField_DEFAULT
  }
return p.OptionalStructField
}

func (p *Bar) GetStructListField() []*Foo {
  return p.StructListField
}
var Bar_OptionalStructListField_DEFAULT []*Foo

func (p *Bar) GetOptionalStructListField() []*Foo {
  return p.OptionalStructListField
}
func (p *Bar) IsSetStructField() bool {
  return p != nil && p.StructField != nil
}

func (p *Bar) IsSetOptionalStructField() bool {
  return p != nil && p.OptionalStructField != nil
}

func (p *Bar) IsSetOptionalStructListField() bool {
  return p != nil && p.OptionalStructListField != nil
}

type BarBuilder struct {
  obj *Bar
}

func NewBarBuilder() *BarBuilder{
  return &BarBuilder{
    obj: NewBar(),
  }
}

func (p BarBuilder) Emit() *Bar{
  return &Bar{
    StructField: p.obj.StructField,
    OptionalStructField: p.obj.OptionalStructField,
    StructListField: p.obj.StructListField,
    OptionalStructListField: p.obj.OptionalStructListField,
  }
}

func (b *BarBuilder) StructField(structField *Foo) *BarBuilder {
  b.obj.StructField = structField
  return b
}

func (b *BarBuilder) OptionalStructField(optionalStructField *Foo) *BarBuilder {
  b.obj.OptionalStructField = optionalStructField
  return b
}

func (b *BarBuilder) StructListField(structListField []*Foo) *BarBuilder {
  b.obj.StructListField = structListField
  return b
}

func (b *BarBuilder) OptionalStructListField(optionalStructListField []*Foo) *BarBuilder {
  b.obj.OptionalStructListField = optionalStructListField
  return b
}

func (b *Bar) SetStructField(structField *Foo) *Bar {
  b.StructField = structField
  return b
}

func (b *Bar) SetOptionalStructField(optionalStructField *Foo) *Bar {
  b.OptionalStructField = optionalStructField
  return b
}

func (b *Bar) SetStructListField(structListField []*Foo) *Bar {
  b.StructListField = structListField
  return b
}

func (b *Bar) SetOptionalStructListField(optionalStructListField []*Foo) *Bar {
  b.OptionalStructListField = optionalStructListField
  return b
}

func (p *Bar) Read(iprot thrift.Protocol) error {
  if _, err := iprot.ReadStructBegin(); err != nil {
    return thrift.PrependError(fmt.Sprintf("%T read error: ", p), err)
  }


  for {
    _, fieldTypeId, fieldId, err := iprot.ReadFieldBegin()
    if err != nil {
      return thrift.PrependError(fmt.Sprintf("%T field %d read error: ", p, fieldId), err)
    }
    if fieldTypeId == thrift.STOP { break; }
    switch fieldId {
    case 1:
      if err := p.ReadField1(iprot); err != nil {
        return err
      }
    case 2:
      if err := p.ReadField2(iprot); err != nil {
        return err
      }
    case 3:
      if err := p.ReadField3(iprot); err != nil {
        return err
      }
    case 4:
      if err := p.ReadField4(iprot); err != nil {
        return err
      }
    default:
      if err := iprot.Skip(fieldTypeId); err != nil {
        return err
      }
    }
    if err := iprot.ReadFieldEnd(); err != nil {
      return err
    }
  }
  if err := iprot.ReadStructEnd(); err != nil {
    return thrift.PrependError(fmt.Sprintf("%T read struct end error: ", p), err)
  }
  return nil
}

func (p *Bar)  ReadField1(iprot thrift.Protocol) error {
  p.StructField = NewFoo()
  if err := p.StructField.Read(iprot); err != nil {
    return thrift.PrependError(fmt.Sprintf("%T error reading struct: ", p.StructField), err)
  }
  return nil
}

func (p *Bar)  ReadField2(iprot thrift.Protocol) error {
  p.OptionalStructField = NewFoo()
  if err := p.OptionalStructField.Read(iprot); err != nil {
    return thrift.PrependError(fmt.Sprintf("%T error reading struct: ", p.OptionalStructField), err)
  }
  return nil
}

func (p *Bar)  ReadField3(iprot thrift.Protocol) error {
  _, size, err := iprot.ReadListBegin()
  if err != nil {
    return thrift.PrependError("error reading list begin: ", err)
  }
  tSlice := make([]*Foo, 0, size)
  p.StructListField =  tSlice
  for i := 0; i < size; i ++ {
    _elem8 := NewFoo()
    if err := _elem8.Read(iprot); err != nil {
      return thrift.PrependError(fmt.Sprintf("%T error reading struct: ", _elem8), err)
    }
    p.StructListField = append(p.StructListField, _elem8)
  }
  if err := iprot.ReadListEnd(); err != nil {
    return thrift.PrependError("error reading list end: ", err)
  }
  return nil
}

func (p *Bar)  ReadField4(iprot thrift.Protocol) error {
  _, size, err := iprot.ReadListBegin()
  if err != nil {
    return thrift.PrependError("error reading list begin: ", err)
  }
  tSlice := make([]*Foo, 0, size)
  p.OptionalStructListField =  tSlice
  for i := 0; i < size; i ++ {
    _elem9 := NewFoo()
    if err := _elem9.Read(iprot); err != nil {
      return thrift.PrependError(fmt.Sprintf("%T error reading struct: ", _elem9), err)
    }
    p.OptionalStructListField = append(p.OptionalStructListField, _elem9)
  }
  if err := iprot.ReadListEnd(); err != nil {
    return thrift.PrependError("error reading list end: ", err)
  }
  return nil
}

func (p *Bar) Write(oprot thrift.Protocol) error {
  if err := oprot.WriteStructBegin("Bar"); err != nil {
    return thrift.PrependError(fmt.Sprintf("%T write struct begin error: ", p), err) }
  if err := p.writeField1(oprot); err != nil { return err }
  if err := p.writeField2(oprot); err != nil { return err }
  if err := p.writeField3(oprot); err != nil { return err }
  if err := p.writeField4(oprot); err != nil { return err }
  if err := oprot.WriteFieldStop(); err != nil {
    return thrift.PrependError("write field stop error: ", err) }
  if err := oprot.WriteStructEnd(); err != nil {
    return thrift.PrependError("write struct stop error: ", err) }
  return nil
}

func (p *Bar) writeField1(oprot thrift.Protocol) (err error) {
  if err := oprot.WriteFieldBegin("structField", thrift.STRUCT, 1); err != nil {
    return thrift.PrependError(fmt.Sprintf("%T write field begin error 1:structField: ", p), err) }
  if err := p.StructField.Write(oprot); err != nil {
    return thrift.PrependError(fmt.Sprintf("%T error writing struct: ", p.StructField), err)
  }
  if err := oprot.WriteFieldEnd(); err != nil {
    return thrift.PrependError(fmt.Sprintf("%T write field end error 1:structField: ", p), err) }
  return err
}

func (p *Bar) writeField2(oprot thrift.Protocol) (err error) {
  if p.IsSetOptionalStructField() {
    if err := oprot.WriteFieldBegin("optionalStructField", thrift.STRUCT, 2); err != nil {
      return thrift.PrependError(fmt.Sprintf("%T write field begin error 2:optionalStructField: ", p), err) }
    if err := p.OptionalStructField.Write(oprot); err != nil {
      return thrift.PrependError(fmt.Sprintf("%T error writing struct: ", p.OptionalStructField), err)
    }
    if err := oprot.WriteFieldEnd(); err != nil {
      return thrift.PrependError(fmt.Sprintf("%T write field end error 2:optionalStructField: ", p), err) }
  }
  return err
}

func (p *Bar) writeField3(oprot thrift.Protocol) (err error) {
  if err := oprot.WriteFieldBegin("structListField", thrift.LIST, 3); err != nil {
    return thrift.PrependError(fmt.Sprintf("%T write field begin error 3:structListField: ", p), err) }
  if err := oprot.WriteListBegin(thrift.STRUCT, len(p.StructListField)); err != nil {
    return thrift.PrependError("error writing list begin: ", err)
  }
  for _, v := range p.StructListField {
    if err := v.Write(oprot); err != nil {
      return thrift.PrependError(fmt.Sprintf("%T error writing struct: ", v), err)
    }
  }
  if err := oprot.WriteListEnd(); err != nil {
    return thrift.PrependError("error writing list end: ", err)
  }
  if err := oprot.WriteFieldEnd(); err != nil {
    return thrift.PrependError(fmt.Sprintf("%T write field end error 3:structListField: ", p), err) }
  return err
}

func (p *Bar) writeField4(oprot thrift.Protocol) (err error) {
  if p.IsSetOptionalStructListField() {
    if err := oprot.WriteFieldBegin("optionalStructListField", thrift.LIST, 4); err != nil {
      return thrift.PrependError(fmt.Sprintf("%T write field begin error 4:optionalStructListField: ", p), err) }
    if err := oprot.WriteListBegin(thrift.STRUCT, len(p.OptionalStructListField)); err != nil {
      return thrift.PrependError("error writing list begin: ", err)
    }
    for _, v := range p.OptionalStructListField {
      if err := v.Write(oprot); err != nil {
        return thrift.PrependError(fmt.Sprintf("%T error writing struct: ", v), err)
      }
    }
    if err := oprot.WriteListEnd(); err != nil {
      return thrift.PrependError("error writing list end: ", err)
    }
    if err := oprot.WriteFieldEnd(); err != nil {
      return thrift.PrependError(fmt.Sprintf("%T write field end error 4:optionalStructListField: ", p), err) }
  }
  return err
}

func (p *Bar) String() string {
  if p == nil {
    return "<nil>"
  }

  var structFieldVal string
  if p.StructField == nil {
    structFieldVal = "<nil>"
  } else {
    structFieldVal = fmt.Sprintf("%v", p.StructField)
  }
  var optionalStructFieldVal string
  if p.OptionalStructField == nil {
    optionalStructFieldVal = "<nil>"
  } else {
    optionalStructFieldVal = fmt.Sprintf("%v", p.OptionalStructField)
  }
  structListFieldVal := fmt.Sprintf("%v", p.StructListField)
  optionalStructListFieldVal := fmt.Sprintf("%v", p.OptionalStructListField)
  return fmt.Sprintf("Bar({StructField:%s OptionalStructField:%s StructListField:%s OptionalStructListField:%s})", structFieldVal, optionalStructFieldVal, structListFieldVal, optionalStructListFieldVal)
}

