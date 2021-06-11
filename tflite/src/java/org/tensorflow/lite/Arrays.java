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
package org.tensorflow.lite;

class Arrays {
    public static boolean equals(int[] a1, int[] a2) {
        return org.bouncycastle.util.Arrays.areEqual(a1, a2);
    }

    public static String toString(int[] a) {
        if (a == null) {
            return "null";
        }

        StringBuffer str = new StringBuffer();
        str.append("[");
        for (int i = 0; i < a.length; i++) {
            if (i > 0) {
                str.append(", ");
            }
            str.append(String.valueOf(a[i]));
        }
        str.append("]");
        return str.toString();
    }

    public static String toString(String[] a) {
        if (a == null) {
            return "null";
        }

        StringBuffer str = new StringBuffer();
        str.append("[");
        for (int i = 0; i < a.length; i++) {
            if (i > 0) {
                str.append(", ");
            }
            str.append(a[i]);
        }
        str.append("]");
        return str.toString();
    }

}
