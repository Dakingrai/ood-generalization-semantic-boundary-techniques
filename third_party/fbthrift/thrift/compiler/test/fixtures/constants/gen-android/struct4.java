/**
 * Autogenerated by Thrift
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;
import java.util.BitSet;
import java.util.Arrays;
import com.facebook.thrift.*;
import com.facebook.thrift.annotations.*;
import com.facebook.thrift.async.*;
import com.facebook.thrift.meta_data.*;
import com.facebook.thrift.server.*;
import com.facebook.thrift.transport.*;
import com.facebook.thrift.protocol.*;

@SuppressWarnings({ "unused", "serial" })
public class struct4 implements TBase, java.io.Serializable, Cloneable {
  private static final TStruct STRUCT_DESC = new TStruct("struct4");
  private static final TField A_FIELD_DESC = new TField("a", TType.I32, (short)1);
  private static final TField B_FIELD_DESC = new TField("b", TType.DOUBLE, (short)2);
  private static final TField C_FIELD_DESC = new TField("c", TType.BYTE, (short)3);

  public final Integer a;
  public final Double b;
  public final Byte c;
  public static final int A = 1;
  public static final int B = 2;
  public static final int C = 3;

  public struct4(
      Integer a,
      Double b,
      Byte c) {
    this.a = a;
    this.b = b;
    this.c = c;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public struct4(struct4 other) {
    if (other.isSetA()) {
      this.a = TBaseHelper.deepCopy(other.a);
    } else {
      this.a = null;
    }
    if (other.isSetB()) {
      this.b = TBaseHelper.deepCopy(other.b);
    } else {
      this.b = null;
    }
    if (other.isSetC()) {
      this.c = TBaseHelper.deepCopy(other.c);
    } else {
      this.c = null;
    }
  }

  public struct4 deepCopy() {
    return new struct4(this);
  }

  public Integer getA() {
    return this.a;
  }

  // Returns true if field a is set (has been assigned a value) and false otherwise
  public boolean isSetA() {
    return this.a != null;
  }

  public Double getB() {
    return this.b;
  }

  // Returns true if field b is set (has been assigned a value) and false otherwise
  public boolean isSetB() {
    return this.b != null;
  }

  public Byte getC() {
    return this.c;
  }

  // Returns true if field c is set (has been assigned a value) and false otherwise
  public boolean isSetC() {
    return this.c != null;
  }

  @Override
  public boolean equals(Object _that) {
    if (_that == null)
      return false;
    if (this == _that)
      return true;
    if (!(_that instanceof struct4))
      return false;
    struct4 that = (struct4)_that;

    if (!TBaseHelper.equalsNobinary(this.isSetA(), that.isSetA(), this.a, that.a)) { return false; }

    if (!TBaseHelper.equalsNobinary(this.isSetB(), that.isSetB(), this.b, that.b)) { return false; }

    if (!TBaseHelper.equalsNobinary(this.isSetC(), that.isSetC(), this.c, that.c)) { return false; }

    return true;
  }

  @Override
  public int hashCode() {
    return Arrays.deepHashCode(new Object[] {a, b, c});
  }

  // This is required to satisfy the TBase interface, but can't be implemented on immutable struture.
  public void read(TProtocol iprot) throws TException {
    throw new TException("unimplemented in android immutable structure");
  }

  public static struct4 deserialize(TProtocol iprot) throws TException {
    Integer tmp_a = null;
    Double tmp_b = null;
    Byte tmp_c = null;
    TField __field;
    iprot.readStructBegin();
    while (true)
    {
      __field = iprot.readFieldBegin();
      if (__field.type == TType.STOP) { 
        break;
      }
      switch (__field.id)
      {
        case A:
          if (__field.type == TType.I32) {
            tmp_a = iprot.readI32();
          } else { 
            TProtocolUtil.skip(iprot, __field.type);
          }
          break;
        case B:
          if (__field.type == TType.DOUBLE) {
            tmp_b = iprot.readDouble();
          } else { 
            TProtocolUtil.skip(iprot, __field.type);
          }
          break;
        case C:
          if (__field.type == TType.BYTE) {
            tmp_c = iprot.readByte();
          } else { 
            TProtocolUtil.skip(iprot, __field.type);
          }
          break;
        default:
          TProtocolUtil.skip(iprot, __field.type);
          break;
      }
      iprot.readFieldEnd();
    }
    iprot.readStructEnd();

    struct4 _that;
    _that = new struct4(
      tmp_a
      ,tmp_b
      ,tmp_c
    );
    _that.validate();
    return _that;
  }

  public void write(TProtocol oprot) throws TException {
    validate();

    oprot.writeStructBegin(STRUCT_DESC);
    if (this.a != null) {
      oprot.writeFieldBegin(A_FIELD_DESC);
      oprot.writeI32(this.a);
      oprot.writeFieldEnd();
    }
    if (this.b != null) {
      if (isSetB()) {
        oprot.writeFieldBegin(B_FIELD_DESC);
        oprot.writeDouble(this.b);
        oprot.writeFieldEnd();
      }
    }
    if (this.c != null) {
      if (isSetC()) {
        oprot.writeFieldBegin(C_FIELD_DESC);
        oprot.writeByte(this.c);
        oprot.writeFieldEnd();
      }
    }
    oprot.writeFieldStop();
    oprot.writeStructEnd();
  }

  @Override
  public String toString() {
    return toString(1, true);
  }

  @Override
  public String toString(int indent, boolean prettyPrint) {
    return TBaseHelper.toStringHelper(this, indent, prettyPrint);
  }

  public void validate() throws TException {
    // check for required fields
  }

}

