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

package com.joshvm.java.util;

import java.util.Vector;

public class ArrayList extends AbstractList
    implements List
    {
        Vector m_Vector=null;

        public ArrayList()
            {
                m_Vector=new Vector();
            }

        public ArrayList(Collection c)
            {
                m_Vector=new Vector((int)(c.size()*1.1));
                addAll(c);
            }

        public ArrayList(int initialCapacity)
          {
                m_Vector=new Vector(initialCapacity);
            }

        public void trimToSize()
            {
                m_Vector.trimToSize();
            }

        public void ensureCapacity(int minCapacity)
            {
                m_Vector.ensureCapacity(minCapacity);
            }

        public int size()
            {
                return m_Vector.size();
            }

        public boolean contains(Object elem)
            {
                return m_Vector.contains(elem);
            }

        public int indexOf(Object elem)
            {
                return m_Vector.indexOf(elem);
            }

        public int lastIndexOf(Object elem)
            {
                return m_Vector.lastIndexOf(elem);
            }

        public Object clone()
            {
                ArrayList al=new ArrayList(this);

              return al;
            }

        public Object[] toArray()
            {
                Object[] o=new Object[m_Vector.size()];
                m_Vector.copyInto(o);
                return o;
            }

        public Object get(int index)
            {
                return m_Vector.elementAt(index);
            }

        public Object set(int index,Object elem)
            {
                Object o=m_Vector.elementAt(index);
                m_Vector.setElementAt(elem,index);
                return o;
            }

        public boolean add(Object o)
            {
                m_Vector.addElement(o);
                return true;
            }

        public void add(int index,Object elem)
            {
                m_Vector.insertElementAt(elem,index);
            }

        public Object remove(int index)
            {
                Object o=m_Vector.elementAt(index);
                m_Vector.removeElementAt(index);
                return o;
            }

        public void clear()
            {
                m_Vector.removeAllElements();
            }





    }
