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

public class ImageBuffer {
	public static final int TYPE_RGB565 = 1;
    public static final int TYPE_RGB666 = 2;
    public static final int TYPE_RGB888 = 3;
    public static final int TYPE_ARGB8888 = 4;

    private int bytes_per_pixel;

	private int width;
	private int height;
    private int bufferType;

	private byte imageData[];

    private void createImageBuffer(int w, int h, int type, byte[] imageData) throws IllegalArgumentException {
		if ((w < 0) || (h < 0)) {
			throw new IllegalArgumentException("Wrong width or height of ImageBuffer");
		}

        bytes_per_pixel = 0;
        bufferType = type;

		switch (type) {
        case TYPE_RGB565:
            bytes_per_pixel = 2;
            break;
        case TYPE_RGB888:
        case TYPE_RGB666:
            bytes_per_pixel = 3;
            break;
        case TYPE_ARGB8888:
            bytes_per_pixel = 4;
            break;
        default:
			throw new IllegalArgumentException("Unsupported type: "+type);
		}

		width = w;
		height = h;

		if (imageData != null) {
			setImageData(imageData);
		} else {
			setImageData(new byte[w*h*bytes_per_pixel]);
		}
	}

    private void createDeviceCompatibleImageBuffer(int w, int h, int colorMode)
                                                                    throws IllegalArgumentException {
        int type;
        
        switch (colorMode) {
        case Display.COLOR_MODE_RGB565:
            type = TYPE_RGB565;
            break;
        case Display.COLOR_MODE_RGB666:
            type = TYPE_RGB666;
            break;
        case Display.COLOR_MODE_RGB888:
            type = TYPE_RGB888;
            break;
        case Display.COLOR_MODE_ARGB8888:
            type = TYPE_ARGB8888;
            break;
        default:
            throw new IllegalArgumentException("Unsupported Display color mode: "+colorMode);
        }
        createImageBuffer(w, h, type, null);
    }

    public ImageBuffer(int w, int h, Display compatibleDisplay) throws IllegalArgumentException {        
		createDeviceCompatibleImageBuffer(w, h, compatibleDisplay.getColorMode());
	}

	public ImageBuffer(int w, int h, int type) throws IllegalArgumentException {        
		createImageBuffer(w, h, type, null);
	}

	public ImageBuffer(int w, int h, int type, byte[] imageData) throws IllegalArgumentException {
		createImageBuffer(w, h, type, imageData);
	}

	public void setImageData(byte[] imageData)  throws IllegalArgumentException {
		if (width*height*bytes_per_pixel != imageData.length) {
			throw new IllegalArgumentException("imageData length is mismatched with width and height");
		}

		this.imageData = imageData;
	}

	public void setRGB(int rgb) throws IllegalArgumentException {
		setRGB(0, width - 1, 0, height - 1, rgb);
	}

	public void setRGB(int col, int row, int rgb) throws IllegalArgumentException {
		setRGB(col, col, row, row, rgb);
	}

	public void setRGB(int begincol, int endcol,
			int beginrow, int endrow, int rgb) throws IllegalArgumentException {
		if ((begincol < 0 || begincol >= width) ||
			(endcol < 0 || endcol >= width) ||
			(beginrow < 0 || beginrow >= height) ||
			(endrow < 0 || endrow >= height)) {
			throw new IllegalArgumentException("col/row is out of range:("
				+ begincol + "," + beginrow + "->" + endcol + "," + endrow + ")");
		}

        int b = (rgb & 0xff);
		int g = (rgb & 0xff00) >> 8;
		int r = (rgb & 0xff0000) >> 16;

        if (bufferType == TYPE_RGB888 || bufferType == TYPE_ARGB8888) {
            for (int col =  begincol; col <= endcol; col++) {
		    	for (int row = beginrow; row <= endrow; row++) {
			    	imageData[(row*width+col)*bytes_per_pixel] = (byte)(b&0xff);
				    imageData[(row*width+col)*bytes_per_pixel+1] = (byte)(g&0xff);
                    imageData[(row*width+col)*bytes_per_pixel+2] = (byte)(r&0xff);
                    if (bufferType == TYPE_ARGB8888) {
                        imageData[(row*width+col)*bytes_per_pixel+3] = (byte)0xff;
                    }
			    }
		    }            
        } else if (bufferType == TYPE_RGB565) {
    		byte colorL = (byte)(((b >> 3) | ((g & 0xfc) << 3)) & 0xff);
    		byte colorH = (byte)((g >> 5) | (r & 0xf8) & 0xff);
    		for (int col =  begincol; col <= endcol; col++) {
    			for (int row = beginrow; row <= endrow; row++) {
    				imageData[(row*width+col)*bytes_per_pixel] = colorL;
    				imageData[(row*width+col)*bytes_per_pixel+1] = colorH;
    			}
    		}
        }
	}

	public byte[] getImageData() {
		return imageData;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
    
    public int getBytesPerPixel() {
        return bytes_per_pixel;
    }
    
    public int getType() {
        return bufferType;
    }
}
