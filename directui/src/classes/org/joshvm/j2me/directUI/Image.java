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
package org.joshvm.j2me.directUI;

public class Image {
    public static final int IMAGE_TYPE_CUSTOME = -1;
    public static final int IMAGE_TYPE_PNG = 0;
    public static final int IMAGE_TYPE_JPG = 1;
    public static final int IMAGE_TYPE_BMP = 2;

    private int imageType;
    private byte imageData[];
    private PlatformImageDecoder decoder;

    public Image(int type) {
        if (type == IMAGE_TYPE_PNG ||
            type == IMAGE_TYPE_JPG ||
            type == IMAGE_TYPE_BMP) {
            imageType = type;
            decoder = new PlatformImageDecoder();
            return;
        }
        throw new IllegalArgumentException();
    }

    public int getImageType() {
        return imageType;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public int getWidth() {
        return decoder.getWidth(imageData, imageType);
    }

    public int getHeight() {
        return decoder.getHeight(imageData, imageType);
    }

}
