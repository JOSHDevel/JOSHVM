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
package com.sun.midp.ssl;

import java.io.*;
import javax.microedition.io.*;

class SSLNativeStreamConnection implements StreamConnection {
    SocketConnection socket;
    int handle;
    int sslHandle;
    
    SSLNativeStreamConnection(SocketConnection sc, int socketHandle) throws IOException {
        socket = sc;
        handle = socketHandle;
        sslHandle = doHandshake(socketHandle, sc.getAddress());
        if (doVerify(sslHandle, sc.getAddress()) != 0) {
            throw new IOException("SSL certificate verification error");
        }
    }
    
    private static native int doHandshake(int socketHandle, String host) throws IOException;

    /**
     * doVerify() returns 0 if success, otherwise error
     */
    private static native int doVerify(int sslHandle, String host) throws IOException;
    protected static native int readBuf(int sslHandle, byte b[], int off,
                                             int len);
    protected static native int readByte(int sslHandle);
    protected static native int writeBuf(int sslHandle, byte b[], int off,
                                              int len);
    protected static native int writeByte(int sslHandle, int b);
    protected static native int available0(int sslHandle);
    protected static native void close0(int sslHandle);
    
    /**
     * Returns the InputStream associated with this SSLStreamConnection.
     *
     * @return InputStream object from which SSL protected bytes can
     * be read
     * @exception IOException if the connection is not open or the stream was 
     * already open
     */ 
    synchronized public InputStream openInputStream() throws IOException {
        return new PrivateInputStream(this);
    }
    
    /**
     * Returns the OutputStream associated with this SSLStreamConnection.
     * 
     * @return OutputStream object such that bytes written to this stream
     * are sent over an SSL secured channel
     * @exception IOException if the connection is not open or the stream was 
     * already open
     */
    synchronized public OutputStream openOutputStream() throws IOException {
        return new PrivateOutputStream(this);
    }
    
    /**
     * Returns the DataInputStream associated with this SSLStreamConnection.
     * @exception IOException if the connection is not open or the stream was 
     * already open
     * @return a DataInputStream object
     */ 
    public DataInputStream openDataInputStream() throws IOException {
	    return new DataInputStream(openInputStream());
    }
     
    /** 
     * Returns the DataOutputStream associated with this SSLStreamConnection.
     * @exception IOException if the connection is not open or the stream was 
     * already open
     * @return a DataOutputStream object
     */
    public DataOutputStream openDataOutputStream() throws IOException {
	    return new DataOutputStream(openOutputStream());
    }

    public void close() throws IOException {
        close0(sslHandle);
        socket.close();
    }
}

/**
 * Input stream for the connection
 */
class PrivateInputStream extends InputStream {

    /**
     * Pointer to the connection
     */
    private SSLNativeStreamConnection parent;

    /**
     * End of file flag
     */
    boolean eof = false;

    /**
     * Constructor
     * @param pointer to the connection object
     *
     * @exception  IOException  if an I/O error occurs.
     */
    /* public */ PrivateInputStream(SSLNativeStreamConnection parent) throws IOException {
        this.parent = parent;
    }

    /**
     * Check the stream is open
     *
     * @exception  IOException  if it is not.
     */
    void ensureOpen() throws IOException {
        if (parent == null) {
            throw new IOException(
/* #ifdef VERBOSE_EXCEPTIONS */
/// skipped                       "Stream closed"
/* #endif */
            );
        }
    }

    /**
     * Reads the next byte of data from the input stream.
     * <p>
     * Polling the native code is done here to allow for simple
     * asynchronous native code to be written. Not all implementations
     * work this way (they block in the native code) but the same
     * Java code works for both.
     *
     * @return     the next byte of data, or <code>-1</code> if the end of the
     *             stream is reached.
     * @exception  IOException  if an I/O error occurs.
     */
    synchronized public int read() throws IOException {
        int res;
        ensureOpen();
        if (eof) {
            return -1;
        }
        res = parent.readByte(parent.sslHandle);
        if (res == -1) {
            eof = true;
        }
        if (parent == null) {
            throw new InterruptedIOException();
        }
        return res;
    }

    /**
     * Reads up to <code>len</code> bytes of data from the input stream into
     * an array of bytes.
     * <p>
     * Polling the native code is done here to allow for simple
     * asynchronous native code to be written. Not all implementations
     * work this way (they block in the native code) but the same
     * Java code works for both.
     *
     * @param      b     the buffer into which the data is read.
     * @param      off   the start offset in array <code>b</code>
     *                   at which the data is written.
     * @param      len   the maximum number of bytes to read.
     * @return     the total number of bytes read into the buffer, or
     *             <code>-1</code> if there is no more data because the end of
     *             the stream has been reached.
     * @exception  IOException  if an I/O error occurs.
     */
    synchronized public int read(byte b[], int off, int len)
            throws IOException {
        ensureOpen();
        if (eof) {
            return -1;
        }
        if (len == 0) {
            return 0;
        }
        // Check for array index out of bounds, and NullPointerException,
        // so that the native code doesn't need to do it
        int test = b[off] + b[off + len - 1];


        int count = parent.readBuf(parent.sslHandle, b, off, len);
        if (count == -1) {
            eof = true;
        }

        if (parent == null) {
            throw new InterruptedIOException();
        }
        return count;
    }

    /**
     * Returns the number of bytes that can be read (or skipped over) from
     * this input stream without blocking by the next caller of a method for
     * this input stream.
     *
     * @return     the number of bytes that can be read from this input stream.
     * @exception  IOException  if an I/O error occurs.
     */
    synchronized public int available() throws IOException {
        ensureOpen();
        return parent.available0(parent.sslHandle);
    }

    /**
     * Close the stream.
     *
     * @exception  IOException  if an I/O error occurs
     */
    public void close() throws IOException {
        if (parent != null) {
            ensureOpen();
            parent = null;
        }
    }
}

/**
 * Output stream for the connection
 */
class PrivateOutputStream extends OutputStream {

    /**
     * Pointer to the connection
     */
    private SSLNativeStreamConnection parent;

    /**
     * Constructor
     * @param pointer to the connection object
     *
     * @exception  IOException  if an I/O error occurs.
     */
    /* public */ PrivateOutputStream(SSLNativeStreamConnection parent) throws IOException {
        this.parent = parent;
    }

    /**
     * Check the stream is open
     *
     * @exception  IOException  if it is not.
     */
    void ensureOpen() throws IOException {
        if (parent == null) {
            throw new IOException(
/* #ifdef VERBOSE_EXCEPTIONS */
/// skipped                       "Stream closed"
/* #endif */
            );
        }
    }

    /**
     * Writes the specified byte to this output stream.
     * <p>
     * Polling the native code is done here to allow for simple
     * asynchronous native code to be written. Not all implementations
     * work this way (they block in the native code) but the same
     * Java code works for both.
     *
     * @param      b   the <code>byte</code>.
     * @exception  IOException  if an I/O error occurs. In particular,
     *             an <code>IOException</code> may be thrown if the
     *             output stream has been closed.
     */
    synchronized public void write(int b) throws IOException {
        ensureOpen();
        while (true) {
            int res = parent.writeByte(parent.sslHandle, b);
            if (res != 0) {
                // IMPL_NOTE: should EOFException be thrown if write fails?
                return;
            }
        }
    }

    /**
     * Writes <code>len</code> bytes from the specified byte array
     * starting at offset <code>off</code> to this output stream.
     * <p>
     * Polling the native code is done here to allow for simple
     * asynchronous native code to be written. Not all implementations
     * work this way (they block in the native code) but the same
     * Java code works for both.
     *
     * @param      b     the data.
     * @param      off   the start offset in the data.
     * @param      len   the number of bytes to write.
     * @exception  IOException  if an I/O error occurs. In particular,
     *             an <code>IOException</code> is thrown if the output
     *             stream is closed.
     */
    synchronized public void write(byte b[], int off, int len)
            throws IOException {
        ensureOpen();
        if (len == 0) {
            return;
        }

        // Check for array index out of bounds, and NullPointerException,
        // so that the native code doesn't need to do it
        int test = b[off] + b[off + len - 1];

        int n = 0;
        while (true) {
            n += parent.writeBuf(parent.sslHandle, b, off + n, len - n);
            if (n == len) {
                break;
            }
        }
    }

    /**
     * Close the stream.
     *
     * @exception  IOException  if an I/O error occurs
     */
    public void close() throws IOException {
        if (parent != null) {
            ensureOpen();
            parent = null;
        }
    }
}
