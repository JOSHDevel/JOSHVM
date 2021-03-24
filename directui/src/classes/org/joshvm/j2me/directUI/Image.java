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

    private DecodableImage decodableImage;

    public Image(int type) {
        decodableImage = new DecodableImage(type, null);
    }

    Image(DecodableImage decodableImage) {
        this.decodableImage = decodableImage;
    }

    public int getImageType() {
        return decodableImage.getImageType();
    }

    public void setImageData(byte[] imageData) {
        decodableImage.setImageData(imageData);
    }

    public byte[] getImageData() {
        return decodableImage.getImageData();
    }

    public int getWidth() {
        return decodableImage.getWidth();
    }

    public int getHeight() {
        return decodableImage.getHeight();
    }

}
