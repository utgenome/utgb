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
package org.utgenome.util.sequence;

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

	private final static int BYTE = 8;
	private final static char[] ACGT = { 'A', 'C', 'G', 'T' };

	public int length() {
		return length;
	}

	public char charAt(int index) {

		int maskPos = index / 8;
		int maskOffset = index % 8;
		if ((sequenceMask[maskPos] & (0x01 << (7 - maskOffset))) != 0)
			return 'N';

		int pos = index / 4;
		int offset = index % 4;

		int c = (sequence[pos] >> ((3 - offset) * 2)) & 0x03;
		return ACGT[c];
	}

	public static class Builder {

	}

}
