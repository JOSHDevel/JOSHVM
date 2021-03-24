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

public class DecodableImage {
    private int imageType;
    private byte imageData[];
    private ImageDecoder decoder;
    
    public DecodableImage(Image image, ImageDecoder decoder) {
       this(image.getImageType(), decoder);
       imageData = image.getImageData();
    }

    public DecodableImage(int type, ImageDecoder decoder) {
       if (type == Image.IMAGE_TYPE_PNG ||
            type == Image.IMAGE_TYPE_JPG ||
            type == Image.IMAGE_TYPE_BMP ||
            type == Image.IMAGE_TYPE_CUSTOME) {
            imageType = type;
            if (decoder == null) {
                decoder = new PlatformImageDecoder();
            }
            this.decoder = decoder;
            return;
        }
        throw new IllegalArgumentException();
    }

    public ImageBuffer createImageBuffer() throws java.io.IOException {
        byte[] data = getImageData();
        if (data == null) {
            throw new NullPointerException("Image data not set");
        }
        return decoder.decode(data, getImageType());
    }

    public Image getImage() {
        return new Image(this);
    }
    
    int getImageType() {
        return imageType;
    }

    void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    byte[] getImageData() {
        return imageData;
    }

    int getWidth() {
        return decoder.getWidth(imageData, imageType);
    }

    int getHeight() {
        return decoder.getHeight(imageData, imageType);
    }
    
}

