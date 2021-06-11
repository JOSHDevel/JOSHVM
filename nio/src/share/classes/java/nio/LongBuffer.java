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
 * A long buffer.
 *
 * <p> This class is a subset of the
 * <code>java.nio.LongBuffer</code> class in Java(TM) Standard Edition
 * version 1.4.2.  Differences are noted in <b><i>bold italic</i></b>.
 * The class documentation may make reference to classes that are not
 * present in the building block.
 *
 * <p><b><i> I/O channels, marking and resetting, and read-only buffers
 * are not supported.  Allocation of non-direct long buffers,
 * compaction, and duplication are not supported.  
 * The following methods are omitted:
 *
 * <ul>
 * <li><code>LongBuffer allocate(int capacity)</code></li>
 * <li><code>LongBuffer compact()</code></li>
 * <li><code>LongBuffer duplicate()</code></li>
 * <li><code>Buffer mark()</code></li>
 * <li><code>Buffer reset()</code></li>
 * <li><code>boolean isReadOnly()</code></li>
 * <li><code>LongBuffer asReadOnlyBuffer()</code></li>
 * </ul>
 * </i></b>
 *
 * <p> This class defines four categories of operations upon
 * long buffers:
 *
 * <ul>
 *
 *   <li><p> Absolute and relative {@link #get() </code><i>get</i><code>} and
 *   {@link #put(long) </code><i>put</i><code>} methods that read and write
 *   single long; </p></li>
 *
 *   <li><p> Relative {@link #get(long[]) </code><i>bulk get</i><code>}
 *   methods that transfer contiguous sequences of longs from this buffer
 *   into an array;</li>
 *
 *   <li><p> Relative {@link #put(long[]) </code><i>bulk put</i><code>}
 *   methods that transfer contiguous sequences of longs from a
 *   long array or some other long
 *   buffer into this buffer; &#32;and </p></li>
 * 
 *   <li><p> Methods for compacting, duplicating, and {@link #slice
 *   slicing} a long buffer.  <b><i>This implementation does
 *   not support compacting and duplicating buffers.</i></b> </p></li>
*
 * </ul>
 *
 * <p> Long buffers can be created either by <i>allocation</i>, which
 * allocates space for the buffer's content, by {@link #wrap(long[])
 * </code><i>wrapping</i><code>} an existing long array into a
 * buffer, or by creating a <a
 * href="ByteBuffer.html#view"><i>view</i></a> of an existing byte
 * buffer. <b><i>This implementation supports allocation of
 * <code>ByteBuffer</code>s only.</i></b>
 *
 * <p> Like a byte buffer, a long buffer is either <a
 * href="ByteBuffer.html#direct"><i>direct</i> or <i>non-direct</i></a>.  A
 * long buffer created via the <tt>wrap</tt> methods of this class will
 * be non-direct.  A long buffer created as a view of a byte buffer will
 * be direct if, and only if, the byte buffer itself is direct.  Whether or not
 * a long buffer is direct may be determined by invoking the {@link
 * #isDirect isDirect} method.  </p>
 *
 * <p> Methods in this class that do not otherwise have a value to return are
 * specified to return the buffer upon which they are invoked.  This allows
 * method invocations to be chained.
 */
public abstract class LongBuffer extends Buffer implements Comparable {

    ByteBufferImpl parent = null;

    long[] array;
    int arrayOffset;

    boolean isDirect;

    boolean disposed = false;

    /**
     * Constructs a new <code>LongBuffer</code>.
     */ 
    LongBuffer() {}

    /**
     * Wraps a long array into a buffer.
     *
     * <p> The new buffer will be backed by the given long array;
     * that is, modifications to the buffer will cause the array to be modified
     * and vice versa.  The new buffer's capacity will be
     * <tt>array.length</tt>, its position will be <tt>offset</tt>, its limit
     * will be <tt>offset + length</tt>, and its mark will be undefined.  Its
     * {@link #array </code>backing array<code>} will be the given array, and
     * its {@link #arrayOffset </code>array offset<code>} will be zero.  </p>
     *
     * @param  array
     *         The array that will back the new buffer
     *
     * @param  offset
     *         The offset of the subarray to be used; must be non-negative and
     *         no larger than <tt>array.length</tt>.  The new buffer's position
     *         will be set to this value.
     *
     * @param  length
     *         The length of the subarray to be used;
     *         must be non-negative and no larger than
     *         <tt>array.length - offset</tt>.
     *         The new buffer's limit will be set to <tt>offset + length</tt>.
     *
     * @return  The new long buffer
     *
     * @throws  IndexOutOfBoundsException
     *          If the preconditions on the <tt>offset</tt> and <tt>length</tt>
     *          parameters do not hold
     */
    public static LongBuffer wrap(long[] array, int offset, int length) {
        if (offset < 0 || offset > array.length ||
            length < 0 || length > array.length - offset) {
            throw new IndexOutOfBoundsException();
        }

        LongBufferImpl sbi =
            new LongBufferImpl(null, array.length, array, 0, false);
        sbi.position(offset);
        sbi.limit(offset + length);
        return sbi;
    }

    /**
     * Wraps a long array into a buffer.
     *
     * <p> The new buffer will be backed by the given long array;
     * that is, modifications to the buffer will cause the array to be modified
     * and vice versa.  The new buffer's capacity and limit will be
     * <tt>array.length</tt>, its position will be zero, and its mark will be
     * undefined.  Its {@link #array </code>backing array<code>} will be the
     * given array, and its {@link #arrayOffset </code>array offset<code>} will
     * be zero.  </p> 
     *
     * @param  array
     *         The array that will back this buffer
     *
     * @return  The new long buffer
     */
    public static LongBuffer wrap(long[] array) {
    	return wrap(array, 0, array.length);
    }

    /**
     * Creates a new long buffer whose content is a shared
     * subsequence of this buffer's content.
     *
     * <p> The content of the new buffer will start at this buffer's
     * current position.  Changes to this buffer's content will be
     * visible in the new buffer, and vice versa; the two buffers'
     * position, limit, and mark values will be independent. <b><i>This 
     * implementatin does not support the mark.</i></b>
     *
     * <p> The new buffer's position will be zero, its capacity and
     * its limit will be the number of longs remaining in this
     * buffer, and its mark will be undefined.  The new buffer will be
     * direct if, and only if, this buffer is direct, and it will be
     * read-only if, and only if, this buffer is read-only. <b><i>This
     * implementation does not support the mark or read-only buffers.</i></b>
     * </p>
     *
     * @return The new long buffer.
     */
    public abstract LongBuffer slice();

    /**
     * Relative <i>get</i> method.  Reads the long at this
     * buffer's current position, and then increments the
     * position. </p>
     *
     * @return The long at the buffer's current position.
     *
     * @throws BufferUnderflowException If the buffer's current
     * position is not smaller than its limit.
     */
    public abstract long get();

    /**
     * Relative <i>put</i> method&nbsp;&nbsp;<i>(optional
     * operation)</i>.
     * 
     * <p> Writes the given long into this buffer at the current
     * position, and then increments the position. </p>
     *
     * @param s The long to be written.
     *
     * @return This buffer.
     *
     * @throws BufferOverflowException If this buffer's current
     * position is not smaller than its limit.
     *
     * @throws ReadOnlyBufferException If this buffer is
     * read-only. <b><i>This implementation does not support read-only buffer or
     * the <code>ReadOnlyBufferException</code> class.</i></b>
     */
    public abstract LongBuffer put(long s);

    /**
     * Absolute <i>get</i> method.  Reads the long at the given
     * index. </p>
     *
     * @param  index The index from which the long will be read.
     *
     * @return  The long at the given index.
     *
     * @throws IndexOutOfBoundsException If <tt>index</tt> is negative
     * or not smaller than the buffer's limit.
     */
    public abstract long get(int index);

    /**
     * Absolute <i>put</i> method&nbsp;&nbsp;<i>(optional operation)</i>.
     * 
     * <p> Writes the given long into this buffer at the given
     * index. </p>
     *
     * @param index The index at which the long will be written.
     *
     * @param s The long value to be written.
     *
     * @return This buffer.
     *
     * @throws IndexOutOfBoundsException If <tt>index</tt> is negative
     * or not smaller than the buffer's limit.
     *
     * @throws ReadOnlyBufferException If this buffer is
     * read-only. <b><i>This implementation does not support read-only buffer or
     * the <code>ReadOnlyBufferException</code> class.</i></b>
     */
    public abstract LongBuffer put(int index, long s);
    
    /**
     * Relative bulk <i>get</i> method.
     *
     * <p> This method transfers longs from this buffer into the
     * given destination array.  If there are fewer longs
     * remaining in the buffer than are required to satisfy the
     * request, that is, if
     * <tt>length</tt>&nbsp;<tt>&gt;</tt>&nbsp;<tt>remaining()</tt>,
     * then no longs are transferred and a {@link
     * BufferUnderflowException} is thrown.
     *
     * <p> Otherwise, this method copies <tt>length</tt> longs
     * from this buffer into the given array, starting at the current
     * position of this buffer and at the given offset in the array.
     * The position of this buffer is then incremented by
     * <tt>length</tt>.
     *
     * <p> In other words, an invocation of this method of the form
     * <tt>src.get(dst,&nbsp;off,&nbsp;len)</tt> has exactly the same
     * effect as the loop
     *
     * <pre>
     *     for (int i = off; i < off + len; i++)
     *         dst[i] = src.get(); </pre>
     *
     * except that it first checks that there are sufficient
     * longs in this buffer and it is potentially much more
     * efficient. </p>
     *
     * @param dst The array into which longs are to be written.
     *
     * @param offset The offset within the array of the first
     * long to be written; must be non-negative and no larger
     * than <tt>dst.length</tt>.
     *
     * @param length The maximum number of longs to be written
     * to the given array; must be non-negative and no larger than
     * <tt>dst.length - offset</tt>.
     *
     * @return This buffer.
     *
     * @throws BufferUnderflowException If there are fewer than
     * <tt>length</tt> longs remaining in this buffer.
     *
     * @throws IndexOutOfBoundsException If the preconditions on the
     * <tt>offset</tt> and <tt>length</tt> parameters do not hold.
     */
    public LongBuffer get(long[] dst, int offset, int length) {
        if (offset < 0 || offset > dst.length ||
            length < 0 || length > dst.length - offset) {
            throw new IndexOutOfBoundsException();
        }
        if (limit - position < length) {
            throw new BufferUnderflowException();
        }

        int bytePtr = arrayOffset + (position << 3);
    	if (isDirect) {
            if (order() == ByteOrder.nativeOrder()) {
    	        ByteBufferImpl._getLongs(bytePtr, dst, offset, length);
            } else {
                bytePtr -= arrayOffset;
                for (int i = 0; i < length; i++) {
                    dst[offset++] = parent.getLong(bytePtr);
                    bytePtr += 8;
                }
            }
    	} else if (array != null) {
    	    System.arraycopy(array, arrayOffset + position,
    			     dst, offset, length);
    	} else {
                for (int i = 0; i < length; i++) {
                    dst[offset++] = parent.getLong(bytePtr);
                    bytePtr += 8;
                }
    	}
    	position += length;
    	return this;
    }

    /**
     * Relative bulk <i>get</i> method.
     *
     * <p> This method transfers longs from this buffer into the
     * given destination array.  An invocation of this method of the
     * form <tt>src.get(a)</tt> behaves in exactly the same way as the
     * invocation
     *
     * <pre>
     *     src.get(a, 0, a.length) </pre>
     *
     * @return This buffer.
     *
     * @throws BufferUnderflowException If there are fewer than
     * <tt>dst.length</tt> longs remaining in this buffer.
     */
    public LongBuffer get(long[] dst) {
    	return get(dst, 0, dst.length);
    }

    /**
     * Relative bulk <i>put</i> method&nbsp;&nbsp;<i>(optional
     * operation)</i>.
     *
     * <p> This method transfers the longs remaining in the
     * given source buffer into this buffer.  If there are more
     * longs remaining in the source buffer than in this buffer,
     * that is, if
     * <tt>src.remaining()</tt>&nbsp;<tt>&gt;</tt>&nbsp;<tt>remaining()</tt>,
     * then no longs are transferred and a {@link
     * BufferOverflowException} is thrown.
     *
     * <p> Otherwise, this method copies
     * <i>n</i>&nbsp;=&nbsp;<tt>src.remaining()</tt> longs from
     * the given buffer into this buffer, starting at each buffer's
     * current position.  The positions of both buffers are then
     * incremented by <i>n</i>.
     *
     * <p> In other words, an invocation of this method of the form
     * <tt>dst.put(src)</tt> has exactly the same effect as the loop
     *
     * <pre>
     *     while (src.hasRemaining())
     *         dst.put(src.get()); </pre>
     *
     * except that it first checks that there is sufficient space in
     * this buffer and it is potentially much more efficient. </p>
     *
     * @param src The source buffer from which longs are to be
     * read; must not be this buffer.
     *
     * @return This buffer.
     *
     * @throws BufferOverflowException If there is insufficient space
     * in this buffer for the remaining longs in the source
     * buffer.
     *
     * @throws IllegalArgumentException If the source buffer is this buffer.
     *
     * @throws ReadOnlyBufferException If this buffer is
     * read-only. <b><i>This implementation does not support read-only buffer or
     * the <code>ReadOnlyBufferException</code> class.</i></b>
     */
    public LongBuffer put(LongBuffer src) {
        if (src == this) {
            throw new IllegalArgumentException();
        }
	
	    LongBufferImpl srci = (LongBufferImpl)src;

    	int length = srci.limit - srci.position;
        if (length > this.limit - this.position) {
            throw new BufferOverflowException();
        }
        if ((order() != srci.order()) || 
            ( isDirect && order() != ByteOrder.nativeOrder()) ||
            ( srci.isDirect && srci.order() != ByteOrder.nativeOrder())) {
            //No optimization if order is not same to each other
            while (srci.hasRemaining()) {
                put(srci.get());
            }
            return this;
        } else if (isDirect && srci.isDirect) {
	        ByteBufferImpl._copyBytes(srci.arrayOffset + (srci.position << 3),
		       this.arrayOffset + (this.position << 3),
		       (length << 3));
	    } else if (isDirect && !srci.isDirect) {
            if (srci.array != null) {
                ByteBufferImpl._putLongs(this.arrayOffset +
                                          (this.position << 3),
                                          srci.array,
                                          srci.arrayOffset + srci.position,
                                          length);
            } else {
                byte[] srcArray = srci.parent.array;
                int srciArrayOffset = srci.parent.arrayOffset +
                    srci.arrayOffset +
                    (srci.position << 3);
                
                ByteBufferImpl._putBytes(this.arrayOffset +
                                         (this.position << 3),
                                         srcArray,
                                         srciArrayOffset,
                                         8*length);
            }
    	} else if (!isDirect && srci.isDirect) {
            if (array != null) {
                ByteBufferImpl._getLongs(srci.arrayOffset +
                                          (srci.position << 3),
                                          this.array,
                                          this.arrayOffset + this.position,
                                          length);
            } else {
                byte[] dstArray = parent.array;
                int dstArrayOffset = parent.arrayOffset +
                    arrayOffset +
                    (position << 3);
                
                ByteBufferImpl._getBytes(srci.arrayOffset +
                                         (srci.position << 3),
                                         dstArray,
                                         dstArrayOffset,
                                         8*length);
            }
	    } else if (!isDirect && !srci.isDirect) {
            if (array != null && srci.array != null) {
                System.arraycopy(srci.array, srci.arrayOffset + srci.position,
                                 this.array, this.arrayOffset + this.position,
                                 length);
            } else {
                while (srci.hasRemaining()) {
                    put(srci.get());
                }
                return this;
            }
	    }
	
	    srci.position += length;
	    this.position += length;
	    return this;
    }
    
    /**
     * Relative bulk <i>put</i> method&nbsp;&nbsp;<i>(optional
     * operation)</i>.
     *
     * <p> This method transfers longs into this buffer from the
     * given source array.  If there are more longs to be copied
     * from the array than remain in this buffer, that is, if
     * <tt>length</tt>&nbsp;<tt>&gt;</tt>&nbsp;<tt>remaining()</tt>,
     * then no longs are transferred and a {@link
     * BufferOverflowException} is thrown.
     *
     * <p> Otherwise, this method copies <tt>length</tt> longs
     * from the given array into this buffer, starting at the given
     * offset in the array and at the current position of this buffer.
     * The position of this buffer is then incremented by
     * <tt>length</tt>.
     *
     * <p> In other words, an invocation of this method of the form
     * <tt>dst.put(src,&nbsp;off,&nbsp;len)</tt> has exactly the same
     * effect as the loop
     *
     * <pre>
     *     for (int i = off; i < off + len; i++)
     *         dst.put(a[i]); </pre>
     *
     * except that it first checks that there is sufficient space in
     * this buffer and it is potentially much more efficient. </p>
     *
     * @param src The array from which longs are to be read.
     *
     * @param offset The offset within the array of the first
     * long to be read; must be non-negative and no larger than
     * <tt>array.length</tt>.
     *
     * @param length The number of longs to be read from the
     * given array; must be non-negative and no larger than
     * <tt>array.length - offset</tt>.
     *
     * @return This buffer.
     *
     * @throws BufferOverflowException If there is insufficient space
     * in this buffer.
     *
     * @throws IndexOutOfBoundsException If the preconditions on the
     * <tt>offset</tt> and <tt>length</tt> parameters do not hold.
     *
     * @throws ReadOnlyBufferException If this buffer is
     * read-only. <b><i>This implementation does not support read-only buffer or
     * the <code>ReadOnlyBufferException</code> class.</i></b>
     */
    public LongBuffer put(long[] src, int offset, int length) {
        if (offset < 0 || offset > src.length ||
            length < 0 || length > src.length - offset) {
            throw new IndexOutOfBoundsException();
        }
        if (length > limit - position) {
            throw new BufferOverflowException();
        }

        int bytePtr = arrayOffset + (position << 3);
	    if (isDirect) {
            if (order() == ByteOrder.nativeOrder()) {
	            ByteBufferImpl._putLongs(bytePtr, src, offset, length);
            } else {
                bytePtr -= arrayOffset;
                for (int i = 0; i < length; i++) {
                    parent.putLong(bytePtr, src[offset++]);
                    bytePtr += 8;
                }
            }
    	} else if (array != null) {
    	    System.arraycopy(src, offset,
    			     array, arrayOffset + position, length);
    	} else {
                for (int i = 0; i < length; i++) {
                    parent.putLong(bytePtr, src[offset++]);
                    bytePtr += 8;
                }
    	}
    	position += length;
    	return this;
    }

    /**
     * Relative bulk <i>put</i> method&nbsp;&nbsp;<i>(optional operation)</i>.
     *
     * <p> This method transfers the entire content of the given
     * source long array into this buffer.  An invocation of
     * this method of the form <tt>dst.put(a)</tt> behaves in exactly
     * the same way as the invocation
     *
     * <pre>
     *     dst.put(a, 0, a.length) </pre>
     *
     * @return This buffer.
     *
     * @throws BufferOverflowException If there is insufficient space
     * in this buffer.
     *
     * @throws ReadOnlyBufferException If this buffer is
     * read-only. <b><i>This implementation does not support read-only buffer or
     * the <code>ReadOnlyBufferException</code> class.</i></b>
     */
    public final LongBuffer put(long[] src) {
	    return put(src, 0, src.length);
    }

    /**
     * Tells whether or not this buffer is backed by an accessible
     * long array.
     *
     * <p> If this method returns <tt>true</tt> then the {@link
     * #array() array} and {@link #arrayOffset() arrayOffset} methods
     * may safely be invoked.  </p>
     *
     * @return <tt>true</tt> if, and only if, this buffer is backed by
     * an array and is not read-only. <b><i>This implementation does not support
     * read-only buffers.</i></b>
     */
    public final boolean hasArray() {
    	return array != null;
    }

    /**
     * Returns the long array that backs this
     * buffer&nbsp;&nbsp;<i>(optional operation)</i>.
     *
     * <p> Modifications to this buffer's content will cause the returned
     * array's content to be modified, and vice versa.
     *
     * <p> Invoke the {@link #hasArray hasArray} method before
     * invoking this method in order to ensure that this buffer has an
     * accessible backing array.  </p>
     *
     * @return The array that backs this buffer.
     *
     * @throws ReadOnlyBufferException If this buffer is
     * read-only. <b><i>This implementation does not support read-only buffer or
     * the <code>ReadOnlyBufferException</code> class.</i></b>
     *
     * @throws UnsupportedOperationException If this buffer is not
     * backed by an accessible array.
     */
    public final long[] array() {
    	if (array == null) {
    	    throw new UnsupportedOperationException();
    	}
    	return array;
    }

    /**
     * Returns the offset within this buffer's backing array of the
     * first element of the buffer&nbsp;&nbsp;<i>(optional
     * operation)</i>.
     *
     * <p> If this buffer is backed by an array then buffer position
     * <i>p</i> corresponds to array index
     * <i>p</i>&nbsp;+&nbsp;<tt>arrayOffset()</tt>.
     *
     * <p> Invoke the {@link #hasArray hasArray} method before
     * invoking this method in order to ensure that this buffer has an
     * accessible backing array.  </p>
     *
     * @return The offset within this buffer's array of the first
     * element of the buffer.
     *
     * @throws ReadOnlyBufferException If this buffer is
     * read-only. <b><i>This implementation does not support read-only buffer or
     * the <code>ReadOnlyBufferException</code> class.</i></b>
     *
     * @throws UnsupportedOperationException If this buffer is not
     * backed by an accessible array.
     */
    public final int arrayOffset() {
    	if (array == null) {
    	    throw new UnsupportedOperationException();
    	}
    	return arrayOffset;
    }

    /**
     * Tells whether or not this long buffer is direct. </p>
     *
     * @return  <tt>true</tt> if, and only if, this buffer is direct.
     */
    public abstract boolean isDirect();

    /**
     * Returns a string summarizing the state of this buffer.
     *
     * @return A summary string
     */
    public String toString() {
        return "java.nio.LongBuffer[" +
            "pos=" + position() +
            "lim=" + limit() +
            "cap=" + capacity() +
            "]";
    }

    /**
     * Returns the current hash code of this buffer.
     *
     * <p> The hash code of a long buffer depends only upon its remaining
     * elements; that is, upon the elements from <tt>position()</tt> up to, and
     * including, the element at <tt>limit()</tt>&nbsp;-&nbsp;<tt>1</tt>.
     *
     * <p> Because buffer hash codes are content-dependent, it is inadvisable
     * to use buffers as keys in hash maps or similar data structures unless it
     * is known that their contents will not change.  </p>
     *
     * @return  The current hash code of this buffer
     */
    public int hashCode() {
    	int h = 1;
    	int p = position();
    	for (int i = limit() - 1; i >= p; i--)
    	    h = 31 * h + (int)get(i);
    	return h;
    }

    /**
     * Tells whether or not this buffer is equal to another object.
     *
     * <p> Two long buffers are equal if, and only if,
     *
     * <p><ol>
     *
     *   <li><p> They have the same element type,  </p></li>
     *
     *   <li><p> They have the same number of remaining elements, and
     *   </p></li>
     *
     *   <li><p> The two sequences of remaining elements, considered
     *   independently of their starting positions, are pointwise equal.
     *   </p></li>
     *
     * </ol>
     *
     * <p> A long buffer is not equal to any other type of object.  </p>
     *
     * @param  ob  The object to which this buffer is to be compared.
     *
     * @return  <tt>true</tt> if, and only if, this buffer is equal to the
     *           given object.
     */
    public boolean equals(Object ob) {
    	if (!(ob instanceof LongBuffer))
    	    return false;
    	LongBuffer that = (LongBuffer)ob;
    	if (this.remaining() != that.remaining())
    	    return false;
    	int p = this.position();
    	for (int i = this.limit() - 1, j = that.limit() - 1; i >= p; i--, j--) {
    	    long v1 = this.get(i);
    	    long v2 = that.get(j);
    	    if (v1 != v2) {
    		if ((v1 != v1) && (v2 != v2))	// For float and double
    		    continue;
    		return false;
    	    }
    	}
    	return true;
    }

    /**
     * Compares this buffer to another.
     *
     * <p> Two long buffers are compared by comparing their sequences of
     * remaining elements lexicographically, without regard to the starting
     * position of each sequence within its corresponding buffer.
     *
     * <p> A long buffer is not comparable to any other type of object.
     *
     * @return  A negative integer, zero, or a positive integer as this buffer
     *		is less than, equal to, or greater than the given buffer.
     * @throws  ClassCastException If the argument is not a long buffer.
     */
    public int compareTo(Object ob) {
    	LongBuffer that = (LongBuffer)ob;
    	int n = this.position() + Math.min(this.remaining(), that.remaining());
    	for (int i = this.position(), j = that.position(); i < n; i++, j++) {
    	    long v1 = this.get(i);
    	    long v2 = that.get(j);
    	    if (v1 == v2)
    		continue;
    	    if ((v1 != v1) && (v2 != v2)) 	// For float and double
    		continue;
    	    if (v1 < v2)
    		return -1;
    	    return +1;
    	}
    	return this.remaining() - that.remaining();
    }

    /**
     * Retrieves this buffer's byte order.
     *
     * <p> The byte order of a long buffer created by allocation or by
     * wrapping an existing <tt>long</tt> array is the {@link
     * ByteOrder#nativeOrder </code>native order<code>} of the underlying
     * hardware.  The byte order of a long buffer created as a <a
     * href="ByteBuffer.html#views">view</a> of a byte buffer is that of the
     * byte buffer at the moment that the view is created.  </p>
     *
     * @return  This buffer's byte order
     */
    public abstract ByteOrder order();
}

