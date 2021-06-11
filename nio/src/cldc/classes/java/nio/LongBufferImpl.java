/*
 * Copyright (C) Max Mu
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * version 2, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License version 2 for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * Please visit www.joshvm.org if you need additional information or
 * have any questions.
 */

package java.nio;

/**
 * A <code>Buffer</code> storing <code>long</code> data.
 */
class LongBufferImpl extends LongBuffer {
    private final ByteOrder byteOrder;

    LongBufferImpl(ByteBufferImpl parent, int capacity,
                    long[] array, int arrayOffset,
                    boolean isDirect) {
        this.parent = parent;
	this.isDirect = isDirect;
	this.array = array;
	this.arrayOffset = arrayOffset;
	this.capacity = this.limit = capacity;
	this.position = 0;
    
        if (parent != null) {
            this.byteOrder = parent.order();
        } else {
            this.byteOrder = ByteOrder.nativeOrder();
        }
    }

    public long get() {
        if (position >= limit) {
            throw new BufferUnderflowException();
        }
	return get(position++);
    }

    public long get(int index) {
        if (index < 0 || index >= limit) {
            throw new IndexOutOfBoundsException();
        }

        int bytePtr = arrayOffset + (index << 3);
	if (isDirect) {
        if (order() == ByteOrder.nativeOrder()) {
    	    return ByteBufferImpl._getLong(bytePtr);
        } else {
            return parent.getLong(index<<3);
        }
	} else if (array != null) {
	   return array[arrayOffset + index];
	} else {
            return parent.getLong(bytePtr);
	}
    }

    public LongBuffer put(long s) {
        if (position >= limit) {
            throw new BufferOverflowException();
        }
	return put(position++, s);
    }

    public LongBuffer put(int index, long s) {
        if (index < 0 || index >= limit) {
            throw new IndexOutOfBoundsException();
        }

        int bytePtr = arrayOffset + (index << 3);
	if (isDirect) {
        if (order() == ByteOrder.nativeOrder()) {
	        ByteBufferImpl._putLong(bytePtr, s);
        } else {
            parent.putLong(index<<3, s);
        }	    
	} else if (array != null) {
	    array[arrayOffset + index] = s;
	} else {
            parent.putLong(bytePtr, s);
	}
	return this;
    }

    public LongBuffer slice() {
        int pos = position;
        if (isDirect) {
            pos <<= 1;
        }
        return new LongBufferImpl(parent, limit - position, array,
                                   arrayOffset + pos,
                                   isDirect);
    }

    public boolean isDirect() {
	return isDirect;
    }

    public int nativeAddress() {
	return arrayOffset;
    }

    public void dispose() {
        // Need revisit
        this.disposed = true;
    }

    public ByteOrder order() {
        return byteOrder;
    }

}

