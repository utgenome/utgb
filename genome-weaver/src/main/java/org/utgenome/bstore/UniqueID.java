/*--------------------------------------------------------------------------
 *  Copyright 2008 utgenome.org
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *--------------------------------------------------------------------------*/
//--------------------------------------
// genome-weaver Project
//
// UniqueID.java
// Since: 2010/04/17
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.bstore;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 160-bit unique identifier
 * 
 * @author leo
 * 
 */
public class UniqueID implements Comparable<UniqueID>
{
    public final static int     ID_LENGTH        = 20;
    public final static int     ID_PREFIX_LENGTH = 6;
    private final static char[] HEX              = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
            'B', 'C', 'D', 'E', 'F'             };

    private byte[]              id               = new byte[ID_LENGTH];

    /**
     * Computes the SHA-1 value of the input stream
     * 
     * @param input
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    static byte[] sha1(InputStream input) throws IOException {
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("SHA-1");
            DigestInputStream digestInputStream = new DigestInputStream(input, digest);
            for (; digestInputStream.read() >= 0;) {

            }
            ByteArrayOutputStream sha1out = new ByteArrayOutputStream(ID_PREFIX_LENGTH);
            sha1out.write(digest.digest());
            return sha1out.toByteArray();
        }
        catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-1 algorithm is not available: " + e);
        }
    }

    private UniqueID(byte[] id) {
        this.id = id.clone();
    }

    public static UniqueID createID(InputStream input) throws IOException {
        return new UniqueID(sha1(input));
    }

    public static UniqueID createID(String input) throws IOException {
        return new UniqueID(sha1(new ByteArrayInputStream(input.getBytes())));
    }

    /**
     * Get the first 6-bytes of the ID as String
     * 
     * @return
     */
    public String getPrefix() {
        StringBuilder s = new StringBuilder(ID_LENGTH);
        for (int i = 0; i < ID_PREFIX_LENGTH; ++i) {
            byte v = id[i];
            s.append(HEX[(v >>> 4) & 0x0F]);
            s.append(HEX[v & 0x0F]);
        }
        return s.toString();
    }

    /**
     * Get the full ID string
     * 
     * @return
     */
    public String getFullID() {
        StringBuilder s = new StringBuilder(ID_LENGTH);
        for (int i = 0; i < ID_LENGTH; ++i) {
            byte v = id[i];
            s.append(HEX[(v >>> 4) & 0x0F]);
            s.append(HEX[v & 0x0F]);
        }
        return s.toString();
    }

    @Override
    public String toString() {
        return getFullID();
    }

    public int compareTo(UniqueID other) {
        int i = 0;
        while (i < id.length && i < other.id.length) {
            int diff = id[i] - other.id[i];
            if (diff != 0)
                return diff;
            ++i;
        }
        return 0;
    }

}
