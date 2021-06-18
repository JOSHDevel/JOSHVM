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

/*
 *   
 *
 * Copyright  1990-2009 Sun Microsystems, Inc. All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License version
 * 2 only, as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License version 2 for more details (a copy is
 * included at /legal/license.txt).
 * 
 * You should have received a copy of the GNU General Public License
 * version 2 along with this work; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA
 * 
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa
 * Clara, CA 95054 or visit www.sun.com if you need additional
 * information or have any questions.
 */

package com.sun.midp.ssl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.SecurityInfo;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.SocketConnection;


import javax.microedition.pki.Certificate;

import com.sun.midp.pki.*;
import com.sun.midp.publickeystore.WebPublicKeyStore;

/**
 * The SSLStreamConnection class implements the StreamConnection
 * interface. Data exchanged through a SSLStreamConnection is 
 * automatically protected by SSL. Currently, only SSL version 3.0
 * is supported and the list of cipher suites proposed 
 * by the client is hardcoded to {SSL_RSA_WITH_RC4_128_MD5,
 * SSL_RSA_EXPORT_WITH_RC4_40_MD5}. This version of the implementation 
 * does not support client authentication at the SSL layer -- a feature
 * that is rarely used.
 * 
 * Typical usage of this class by an application would be along the
 * following lines: <BR />
 * 
 * <PRE>
 *      // create a TCP connection
 *      StreamConnection t = Connector.open("socket://www.server.com:443");
 *
 *      // Create an SSL connection
 *      SSLStreamConnection s = new SSLStreamConnection("www.server.com", 443,
 *                 t.openInputStream(), t.openOutputStream());
 *      t.close();
 * 
 *      // obtain the associated input/output streams
 *      OutputStream sout = s.openOutputStream();
 *      InputStream sin = s.openInputStream();
 *      ...
 *      // send SSL-protected data by writing to sout and
 *      // receive SSL-protected by reading from sin
 *      ...
 *      sin.close();
 *      sout.close();
 *      s.close();   // close the SSL connection when done
 *
 * </PRE>
 * 
 */ 

public class SSLStreamConnection implements StreamConnection {
    /** Underlying SSL connection. */
    private SSLBouncyCastleStreamConnection sslConnection;

    /**
     * Establish and SSL session over a reliable stream.
     * This connection will forward the input and output stream close methods
     * to the given connection. If the caller wants to have the given
     * connection closed with this connection, the caller can close given
     * connection after constructing this connection, but leaving the closing
     * of the streams to this connection.
     * 
     * @param host hostname of the SSL server
     * @param port port number of the SSL server
     * @param in   InputStream associated with the StreamConnection
     * @param out  OutputStream associated with the StreamConnection
     * @param cs trusted certificate store to be used for this connection
     *
     * @exception IOException if there is a problem initializing the SSL
     * data structures or the SSL handshake fails
     */ 
    public SSLStreamConnection(SocketConnection socket)
            throws IOException {

        if (socket == null) {
            throw new IllegalArgumentException(
                "SSLStreamConnection: socket missing");
        }

        this.sslConnection = new SSLBouncyCastleStreamConnection(socket.getAddress(), socket.getPort(),
                                    socket.openInputStream(), socket.openOutputStream(), WebPublicKeyStore.getTrustedKeyStore());

    }
    
    /**
     * Returns the InputStream associated with this SSLStreamConnection.
     *
     * @return InputStream object from which SSL protected bytes can
     * be read
     * @exception IOException if the connection is not open or the stream was 
     * already open
     */ 
    synchronized public InputStream openInputStream() throws IOException {
        return sslConnection.openInputStream();
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
        return sslConnection.openOutputStream();
    }
    
    /**
     * Returns the DataInputStream associated with this SSLStreamConnection.
     * @exception IOException if the connection is not open or the stream was 
     * already open
     * @return a DataInputStream object
     */ 
    public DataInputStream openDataInputStream() throws IOException {
	    return sslConnection.openDataInputStream();
    }
     
    /** 
     * Returns the DataOutputStream associated with this SSLStreamConnection.
     * @exception IOException if the connection is not open or the stream was 
     * already open
     * @return a DataOutputStream object
     */
    public DataOutputStream openDataOutputStream() throws IOException {
	    return sslConnection.openDataOutputStream();
    }
            
    /**
     * Closes the SSL connection. The underlying TCP socket, over which
     * SSL is layered, is also closed unless the latter was opened by
     * an external application and its input/output streams were passed 
     * as argument to the SSLStreamConnection constructor.
     *
     * @exception IOException if the SSL connection could not be
     *                        terminated cleanly
     */ 
    synchronized public void close() throws IOException {
        sslConnection.close();
    }

    /**
     * Returns the security information associated with this connection.
     *
     * @return the security information associated with this open connection
     *
     * @exception IOException if the connection is closed
     */
    public SecurityInfo getSecurityInfo() throws IOException {
        return sslConnection.getSecurityInfo();
    }

    /**
     * Returns the server certificate associated with this connection.
     *
     * @return the server certificate associated with this connection
     */
    public X509Certificate getServerCertificate() {
        return sslConnection.getServerCertificate();
    }

    /**
     * Returns the cipher suite in use for the connection.
     * The value returned is one of the CipherSuite definitions
     * in Appendix C of RFC 2246.
     * The cipher suite string should be used to represent the
     * actual parameters used to establish the connection regardless
     * of whether the secure connection uses SSL V3 or TLS 1.0 or WTLS.
     *
     * @return a String containing the cipher suite in use
     */
    String getCipherSuite() {
        return sslConnection.getCipherSuite();
    }   
}

