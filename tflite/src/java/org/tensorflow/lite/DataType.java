/* Copyright 2017 The TensorFlow Authors. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
==============================================================================*/

package org.tensorflow.lite;

/** Represents the type of elements in a TensorFlow Lite {@link Tensor}. */
public class DataType {
  /** 32-bit single precision floating point. */
  private final static int idFLOAT32 = 1;

  /** 32-bit signed integer. */
  private final static int idINT32 = 2;

  /** 8-bit unsigned integer. */
  private final static int idUINT8 = 3;

  /** 64-bit signed integer. */
  private final static int idINT64 = 4;

  /** Strings. */
  private final static int idSTRING = 5;

  /** Bool. */
  private final static int idBOOL = 6;

  /** 16-bit signed integer. */
  private final static int idINT16 = 7;

  /** 8-bit signed integer. */
  private final static int idINT8 = 9;

  private final int value;
  
  public final static DataType FLOAT32 = new DataType(idFLOAT32);
  public final static DataType INT32 = new DataType(idINT32);
  public final static DataType UINT8 = new DataType(idUINT8);
  public final static DataType INT64 = new DataType(idINT64);
  public final static DataType STRING = new DataType(idSTRING);
  public final static DataType BOOL = new DataType(idBOOL);
  public final static DataType INT16 = new DataType(idINT16);
  public final static DataType INT8 = new DataType(idINT8);

  DataType(int value) {
    this.value = value;
  }

  /** Returns the size of an element of this type, in bytes, or -1 if element size is variable. */
  public int byteSize() {
    switch (this.value) {
      case idFLOAT32:
      case idINT32:
        return 4;
      case idINT16:
        return 2;
      case idINT8:
      case idUINT8:
        return 1;
      case idINT64:
        return 8;
      case idBOOL:
        // Boolean size is JVM-dependent.
        return -1;
      case idSTRING:
        return -1;
    }
    throw new IllegalArgumentException(
        "DataType error: DataType " + this + " is not supported yet");
  }

  /** Corresponding value of the TfLiteType enum in the TensorFlow Lite C API. */
  int c() {
    return value;
  }

  /** Converts a C TfLiteType enum value to the corresponding type. */
  static DataType fromC(int c) {
    for (int i = 0 ; i < values.length; i++) {
      if (values[i].value == c) {
        return values[i];
      }
    }
    throw new IllegalArgumentException(
        "DataType error: DataType "
            + c
            + " is not recognized in Java (version "
            + TensorFlowLite.runtimeVersion()
            + ")");
  }

  /** Gets string names of the data type. */
  String toStringName() {
    switch (this.value) {
      case idFLOAT32:
        return "float";
      case idINT32:
        return "int";
      case idINT16:
        return "short";
      case idINT8:
      case idUINT8:
        return "byte";
      case idINT64:
        return "long";
      case idBOOL:
        return "bool";
      case idSTRING:
        return "string";
    }
    throw new IllegalArgumentException(
        "DataType error: DataType " + this + " is not supported yet");
  }

  // Cached to avoid copying it
  private static final DataType[] values = {
		  FLOAT32,
		  INT32,
		  UINT8,
		  INT64,
		  STRING,
		  BOOL,
		  INT16,
		  INT8
  };
}
