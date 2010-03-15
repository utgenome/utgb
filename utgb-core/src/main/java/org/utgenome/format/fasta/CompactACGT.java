/*--------------------------------------------------------------------------
 *  Copyright 2009 utgenome.org
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
// utgb-core Project
//
// CompactACGT.java
// Since: Feb 22, 2010
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.format.fasta;

import org.utgenome.UTGBErrorCode;
import org.utgenome.UTGBException;

/**
 * Compact array for ACGT (and N) sequences
 * 
 * @author leo
 * 
 */
public class CompactACGT implements GenomeSequence {

	private byte[] sequence; // 2 bit for each char
	private byte[] sequenceMask; // 1 bit for each char: 0 for ACGT, 1 for otherwise including N
	private int length;
	private int offset;

	private final static int CODE_SIZE = 2;
	private final static int BYTE = 8;
	private final static char[] ACGT = { 'A', 'C', 'G', 'T' };

	/**
	 * @param sequence
	 *            packed sequence
	 * @param sequenceMask
	 *            packed sequence for N
	 * @param bitOffset
	 *            offset char length in the packed array
	 * @throws UTGBException
	 */
	public CompactACGT(byte[] sequence, byte[] sequenceMask, int length, int offset) throws UTGBException {
		this.sequence = sequence;
		this.sequenceMask = sequenceMask;

		this.length = length;
		this.offset = offset;

		// assertion
		if ((this.sequence.length * BYTE / CODE_SIZE - offset) <= length) {
			throw new UTGBException(UTGBErrorCode.INVALID_INPUT, String.format("packed array is shorter than the specified length: %d < %d", sequence.length,
					length));
		}
		if ((this.sequenceMask.length * BYTE - offset) <= length) {
			throw new UTGBException(UTGBErrorCode.INVALID_INPUT, String.format("invalid mask binary"));
		}
	}

	public int length() {
		return length;
	}

	public char charAt(int index) {

		int x = index + offset;

		int maskPos = x / 8;
		int maskOffset = x % 8;
		if ((sequenceMask[maskPos] >>> (7 - maskOffset) & 0x01) != 0)
			return 'N';

		int bPos = x / 4;
		int bOffset = x % 4;

		int c = (sequence[bPos] >>> (6 - bOffset * 2)) & 0x03;
		return ACGT[c];
	}

	public String toString() {
		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < length; i++) {
			buf.append(charAt(i));
		}
		return buf.toString();
	}

}
