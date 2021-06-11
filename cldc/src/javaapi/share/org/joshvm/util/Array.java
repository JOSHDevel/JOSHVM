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
package org.joshvm.util;

public class Array
{
    
    /**
     * Get length from the specified source array
     *
     * @param      src  the source array.
     * @return     the length of array.
     * @exception  IllegalArgumentException  if <code>src</code> is not array
     * @exception  NullPointerException if <code>src</code> is <code>null</code>.
     */
    public static native int getLength(Object src);

    /**
     * Returns the value of the indexed component in the specified
     * array object.  The value is automatically wrapped in an object
     * if it has a primitive type.
     *
     * @param array the array
     * @param index the index
     * @return the (possibly wrapped) value of the indexed component in
     * the specified array
     * @exception NullPointerException If the specified object is null
     * @exception IllegalArgumentException If the specified object is not
     * an array
     * @exception ArrayIndexOutOfBoundsException If the specified {@code index}
     * argument is negative, or if it is greater than or equal to the
     * length of the specified array
     */
    public static Object get(Object array, int index)
        throws NullPointerException, IllegalArgumentException, ArrayIndexOutOfBoundsException {
        if (array == null) {
            throw new NullPointerException();
        }
        
        if (isObjArray(array)) {
            return getObjectArray(array, index);          
        }
        
        if (isLongArray(array)) {
            return new Long(getLongArray(array, index));
        }

        if (isIntArray(array)) {
            return new Integer(getIntArray(array, index));
        }

        if (isByteArray(array)) {
           return new Byte(getByteArray(array, index));
        }

        if (isFloatArray(array)) {
            return new Float(getFloatArray(array, index));
        }

        if (isDoubleArray(array)) {
            return new Double(getDoubleArray(array, index));
        }        

        if (isBoolArray(array)) {
            return new Boolean(getBoolArray(array, index));
        }

        if (isCharArray(array)) {
            return new Character(getCharArray(array, index));
        } 

        if (isShortArray(array)) {
            return new Short(getShortArray(array, index));
        }


        throw new IllegalArgumentException("The Object is not an array.");
    }

    private static native boolean isObjArray(Object array);
    private static native boolean isByteArray(Object array);
    private static native boolean isIntArray(Object array);
    private static native boolean isBoolArray(Object array);
    private static native boolean isCharArray(Object array);
    private static native boolean isShortArray(Object array);
    private static native boolean isLongArray(Object array);
    private static native boolean isFloatArray(Object array);
    private static native boolean isDoubleArray(Object array);
  
    private static native Object getObjectArray(Object array, int index) throws ArrayIndexOutOfBoundsException;
    private static native byte getIntArray(Object array, int index) throws ArrayIndexOutOfBoundsException;
    private static native boolean getBoolArray(Object array, int index) throws ArrayIndexOutOfBoundsException;
    private static native char getCharArray(Object array, int index) throws ArrayIndexOutOfBoundsException;
    private static native byte getByteArray(Object array, int index) throws ArrayIndexOutOfBoundsException;
    private static native short getShortArray(Object array, int index) throws ArrayIndexOutOfBoundsException;
    private static native long getLongArray(Object array, int index) throws ArrayIndexOutOfBoundsException;
    private static native float getFloatArray(Object array, int index) throws ArrayIndexOutOfBoundsException;
    private static native double getDoubleArray(Object array, int index) throws ArrayIndexOutOfBoundsException;

}
