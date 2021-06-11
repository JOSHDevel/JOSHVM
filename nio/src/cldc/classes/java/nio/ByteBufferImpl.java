/*
 * Copyright  1990-2009 Sun Microsystems, Inc. All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License version
 * 2 only, as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License version 2 for more details (a copy is
 * included at /legal/license.txt).
 * 
 * You should have received a copy of the GNU General Public License
 * version 2 along with this work; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA
 * 
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa
 * Clara, CA 95054 or visit www.sun.com if you need additional
 * information or have any questions.
 */

package java.nio;

/**
 * A <code>Buffer</code> storing <code>byte</code> data.
 */
class ByteBufferImpl extends ByteBuffer {
    
    static native int _allocNative(int capacity);

    static native void _copyBytes(int srcAddress,
                                  int dstAddress,
                                  int bytes);
    
    static native byte _getByte(int address);
    static native void _getBytes(int address,
                                 byte[] dst, int offset, int length);
    static native void _putByte(int address, byte value);
    static native void _putBytes(int address,
                                 byte[] dst, int offset, int length);

    static native short _getShort(int address);
    static native void _getShorts(int address,
                                  short[] dst, int offset, int length);
    static native void _putShort(int address, short value);
    static native void _putShorts(int address,
                                  short[] dst, int offset, int length);

    static native int _getInt(int address);
    static native void _getInts(int address,
                                int[] dst, int offset, int length);
    static native void _putInt(int address, int value);
    static native void _putInts(int address,
                                int[] src, int offset, int length);
    
    static native float _getFloat(int address);
    static native void _getFloats(int address,
                                  float[] dst, int offset, int length);
    static native void _putFloat(int address, float value);
    static native void _putFloats(int address,
                                  float[] src, int offset, int length);

    static native void _putLong(int address, long value);
    static native void _putLongs(int address,
                                  long[] dst, int offset, int length);
    static native long _getLong(int address);
    static native void _getLongs(int address,
                                  long[] dst, int offset, int length);

    native private void finalize();

    private boolean bigEndian = true;
    private boolean nativeByteOrder = (ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN);

    ByteBufferImpl(int capacity, byte[] array, int arrayOffset,
                   ByteBuffer directParent) {
        this.array = array;
        this.arrayOffset = arrayOffset;

        this.capacity = capacity;
        this.limit = capacity;
        this.position = 0;

        this.isDirect = array == null;
        this.directParent = directParent;
    }

    public FloatBuffer asFloatBuffer() {
        int pos = position + (isDirect ? arrayOffset : 0);
        return new FloatBufferImpl(this, remaining() >> 2,
                                   null, pos, isDirect);
    }

    public IntBuffer asIntBuffer() {
        int pos = position + (isDirect ? arrayOffset : 0);
        return new IntBufferImpl(this, remaining() >> 2,
                                 null, pos, isDirect);
    }

    public ShortBuffer asShortBuffer() {
        int pos = position + (isDirect ? arrayOffset : 0);
        return new ShortBufferImpl(this, remaining() >> 1,
                                   null, pos, isDirect);
    }

    public LongBuffer asLongBuffer() {
        int pos = position + (isDirect ? arrayOffset : 0);
        return new LongBufferImpl(this, remaining() >> 3,
                                   null, pos, isDirect);
    }
 
    public byte get() {
        if (position >= limit) {
            throw new BufferUnderflowException();
        }
	return get(position++);
    }

    public byte get(int index) {
        if (index < 0 || index >= limit) {
            throw new IndexOutOfBoundsException();
        }
	if (isDirect) {
	    return _getByte(arrayOffset + index);
	} else {
	   return array[arrayOffset + index]; 
	}
    }

    public ByteBuffer put(byte b) {
        if (position >= limit) {
            throw new BufferOverflowException();
        }
	return put(position++, b);
    }

    public ByteBuffer put(int index, byte b) {
        if (index < 0 || index >= limit) {
            throw new IndexOutOfBoundsException();
        }
        if (isDirect) {
	    _putByte(arrayOffset + index, b);
	} else {
	    array[arrayOffset + index] = b;
	}
	return this;
    }
    
    public float getFloat() {
        return Float.intBitsToFloat(getInt());
    }
    
    public float getFloat(int index) {
        return Float.intBitsToFloat(getInt(index));
    }

    public long getLong() {
        if (position >= limit - 7) {
            throw new BufferUnderflowException();
        }
        long x = getLong(position);
        position += 8;
        return x;
    }

    public long getLong(int index) {
        if (index < 0 || index >= limit - 7) {
            throw new IndexOutOfBoundsException();
        }
        long x0, x1, x2, x3, x4, x5, x6, x7;
	if (isDirect) {
            index += arrayOffset;
	    x0 = _getByte(index++);
	    x1 = _getByte(index++);
	    x2 = _getByte(index++);
	    x3 = _getByte(index++);
        x4 = _getByte(index++);
	    x5 = _getByte(index++);
	    x6 = _getByte(index++);
	    x7 = _getByte(index);
	} else {
            index += arrayOffset;
            x0 =  array[index++]; 
            x1 =  array[index++]; 
            x2 =  array[index++]; 
            x3 =  array[index++]; 
            x4 =  array[index++]; 
            x5 =  array[index++]; 
            x6 =  array[index++]; 
            x7 =  array[index++]; 
	}

        if (isBigEndian()) {
            return (((x0 & 0xff) << 56) |
                    ((x1 & 0xff) << 48) |
                    ((x2 & 0xff) << 40) |
                    ((x3 & 0xff) << 32) |
                    ((x4 & 0xff) << 24) |
                    ((x5 & 0xff) << 16) |
                    ((x6 & 0xff) <<  8) |
                     (x7 & 0xff)
                    );
        } else {
            return (((x7 & 0xff) << 56) |
                    ((x6 & 0xff) << 48) |
                    ((x5 & 0xff) << 40) |
                    ((x4 & 0xff) << 32) |
                    ((x3 & 0xff) << 24) |
                    ((x2 & 0xff) << 16) |
                    ((x1 & 0xff) <<  8) |
                     (x0 & 0xff)
                    );
        }
    }

    public int getInt() {
        if (position >= limit - 3) {
            throw new BufferUnderflowException();
        }
        int x = getInt(position);
        position += 4;
        return x;
    }

    public int getInt(int index) {
        if (index < 0 || index >= limit - 3) {
            throw new IndexOutOfBoundsException();
        }
        int x0, x1, x2, x3;
	if (isDirect) {
            index += arrayOffset;
	    x0 = _getByte(index++);
	    x1 = _getByte(index++);
	    x2 = _getByte(index++);
	    x3 = _getByte(index);
	} else {
            index += arrayOffset;
            x0 =  array[index++]; 
            x1 =  array[index++]; 
            x2 =  array[index++]; 
            x3 =  array[index++]; 
	}

        if (isBigEndian()) {
            return (( x0         << 24) |
                    ((x1 & 0xff) << 16) |
                    ((x2 & 0xff) <<  8) |
                     (x3 & 0xff));
        } else {
            return (( x3         << 24) |
                    ((x2 & 0xff) << 16) |
                    ((x1 & 0xff) <<  8) |
                     (x0 & 0xff));
        }
    }

    public short getShort() {
        if (position >= limit - 1) {
            throw new BufferUnderflowException();
        }
        short s = getShort(position);
        position += 2;
        return s;
    }

    public short getShort(int index) {
        if (index < 0 || index >= limit - 1) {
            throw new IndexOutOfBoundsException();
        }
        int x0, x1;
	if (isDirect) {
            index += arrayOffset;
	    x0 = _getByte(index++);
	    x1 = _getByte(index++);
	} else {
            index += arrayOffset;
            x0 =  array[index++]; 
            x1 =  array[index++]; 
	}

        if (isBigEndian()) {
            return (short)(((x0 & 0xff) << 8) | (x1 & 0xff));
        } else {
            return (short)(((x1 & 0xff) << 8) | (x0 & 0xff));
        }
    }

    public ByteBuffer putFloat(float value) {
        return putInt(Float.floatToIntBits(value));
    }

    public ByteBuffer putFloat(int index, float value) {
        return putInt(index, Float.floatToIntBits(value));
    }

    
    public ByteBuffer putLong(long value) {
        if (position >= limit - 7) {
            throw new BufferOverflowException();
        }
        putLong(position, value);
        position += 8;
        return this;
    }
    
    public ByteBuffer putLong(int index, long value) {
        if (index < 0 || index >= limit - 7) {
            throw new IndexOutOfBoundsException();
        }

        byte x0, x1, x2, x3, x4, x5, x6, x7;
        if (isBigEndian()) {
            x0 = (byte)(value >> 56);
            x1 = (byte)(value >> 48);
            x2 = (byte)(value >> 40);
            x3 = (byte)(value >> 32);
            x4 = (byte)(value >> 24);
            x5 = (byte)(value >> 16);
            x6 = (byte)(value >> 8);
            x7 = (byte)(value);
        } else {
            x7 = (byte)(value >> 56);
            x6 = (byte)(value >> 48);
            x5 = (byte)(value >> 40);
            x4 = (byte)(value >> 32);
            x3 = (byte)(value >> 24);
            x2 = (byte)(value >> 16);
            x1 = (byte)(value >> 8);
            x0 = (byte)(value);
        }

	if (isDirect) {
            index += arrayOffset;
	    _putByte(index++, x0);
	    _putByte(index++, x1);
	    _putByte(index++, x2);
	    _putByte(index++, x3);
        _putByte(index++, x4);
	    _putByte(index++, x5);
	    _putByte(index++, x6);
	    _putByte(index,   x7);
	} else {
            index += arrayOffset;
            array[index++] = x0;
            array[index++] = x1;
            array[index++] = x2;
            array[index++] = x3;
            array[index++] = x4;
            array[index++] = x5;
            array[index++] = x6;
            array[index  ] = x7;
	}
        
        return this;
    }

    public ByteBuffer putInt(int value) {
        if (position >= limit - 3) {
            throw new BufferOverflowException();
        }
        putInt(position, value);
        position += 4;
        return this;
    }
    
    public ByteBuffer putInt(int index, int value) {
        if (index < 0 || index >= limit - 3) {
            throw new IndexOutOfBoundsException();
        }

        byte x0, x1, x2, x3;
        if (isBigEndian()) {
            x0 = (byte)(value >> 24);
            x1 = (byte)(value >> 16);
            x2 = (byte)(value >> 8);
            x3 = (byte)(value);
        } else {
            x3 = (byte)(value >> 24);
            x2 = (byte)(value >> 16);
            x1 = (byte)(value >> 8);
            x0 = (byte)(value);
        }

	if (isDirect) {
            index += arrayOffset;
	    _putByte(index++, x0);
	    _putByte(index++, x1);
	    _putByte(index++, x2);
	    _putByte(index,   x3);
	} else {
            index += arrayOffset;
            array[index++] = x0;
            array[index++] = x1;
            array[index++] = x2;
            array[index  ] = x3;
	}
        
        return this;
    }
    
    public ByteBuffer putShort(short value) {
        if (position >= limit - 1) {
            throw new BufferOverflowException();
        }
        putShort(position, value);
        position += 2;
        return this;
    }
    
    public ByteBuffer putShort(int index, short value) {
        if (index < 0 || index >= limit - 1) {
            throw new IndexOutOfBoundsException();
        }

        byte x0, x1;
        if (isBigEndian()) {
            x0 = (byte)(value >> 8);
            x1 = (byte)(value);
        } else {
            x1 = (byte)(value >> 8);
            x0 = (byte)(value);
        }

	if (isDirect) {
            index += arrayOffset;
	    _putByte(index++, x0);
	    _putByte(index,   x1);
	} else {
            index += arrayOffset;
            array[index++] = x0;
            array[index  ] = x1;
	}
        
        return this;
    }
    
    public ByteBuffer slice() {
        return new ByteBufferImpl(limit - position, array,
                arrayOffset + position, this);
    }

    public boolean isDirect() {
	return isDirect;
    }

    public int nativeAddress() {
	return arrayOffset;
    }

    public boolean isBigEndian() {
        return bigEndian;
    }

//     public String toString() {
// 	return "ByteBufferImpl[" +
//  	    "array=" + array +
// 	    ",arrayOffset=" + arrayOffset +
//  	    ",capacity=" + capacity +
//   	    ",limit=" + limit +
//   	    ",position=" + position +
//   	    ",isBigEndian=" + GLConfiguration.IS_BIG_ENDIAN +
//   	    ",isDirect=" + isDirect +
//   	    ",disposed=" + disposed + "]";
//     }

    public void dispose() {
        // Need revisit
        this.disposed = true;
    }


    public final ByteOrder order() {
        return bigEndian ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN;
    }

    public final ByteBuffer order(ByteOrder bo) {
        bigEndian = (bo == ByteOrder.BIG_ENDIAN);
        //nativeByteOrder =
        //    (bigEndian == (ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN));
        return this;
    }

}
