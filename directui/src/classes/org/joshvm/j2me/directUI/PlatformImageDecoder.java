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

class PlatformImageDecoder implements ImageDecoder {

    public ImageBuffer decode(byte[] imageData, int imageType) throws java.io.IOException {
        int x, y, i;
        int w = getWidth(imageData, imageType);
        int h = getHeight(imageData, imageType);
        ImageBuffer imageBuffer = new ImageBuffer(w, h, imageType);
        byte[] rawImageData = new byte[w*h*3]; //24bit raw
        decode0(imageData, 0, imageData.length, rawImageData, rawImageData.length, imageType);
        i = 0;
        for (y = 0; y < h; y++) {
            for (x = 0; x < w; x++) {
                int R = rawImageData[i++]&0xff;
                int G = rawImageData[i++]&0xff;
                int B = rawImageData[i++]&0xff;
                imageBuffer.setRGB(x, y, (R<<16)|(G<<8)|B);
            }
        }
        return imageBuffer;
    }

    public int getWidth(byte[] imageData, int imageType) {
        if (imageData == null || imageData.length == 0) {
            return 0;
        }
        return getWidth0(imageData, 0, imageData.length, imageType);
    }

    public int getHeight(byte[] imageData, int imageType) {
        if (imageData == null || imageData.length == 0) {
            return 0;
        }
        return getHeight0(imageData, 0, imageData.length, imageType);
    }

    public native boolean isSupportedType(int imageType);

    private native int getWidth0(byte[] data, int offset, int len, int type);
    private native int getHeight0(byte[] data, int offset, int len, int type);
    private native void decode0(byte[] imageData, int offset, int imageLen, byte[] decodedImageData, int decodedLen, int imageType);

}

