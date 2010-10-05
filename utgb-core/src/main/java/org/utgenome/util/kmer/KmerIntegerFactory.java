/*--------------------------------------------------------------------------
 *  Copyright 2010 utgenome.org
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
// KmerInterger.java
// Since: 2010/10/05
//
//--------------------------------------
package org.utgenome.util.kmer;

import org.utgenome.UTGBErrorCode;
import org.utgenome.UTGBException;
import org.utgenome.format.fasta.CompactACGTWriter;

/**
 * Generator of the bit-integer representation of a k-mer sequence
 * 
 * @author leo
 * 
 */
public class KmerIntegerFactory {

	private final int K;

	public KmerIntegerFactory(int K) {
		if (K >= 16)
			throw new IllegalArgumentException("K must be less than 16: K=" + K);

		this.K = K;
	}

	/**
	 * Return the XOR value of the double strand reads of a given k-mer sequence. Since XOR is commutative, the result
	 * value is the same for both strand reads.
	 * 
	 * @param K
	 * @return
	 */
	public int doubleStrandXOR(int kmer) {
		int rc = reverseComplement(kmer);
		return kmer ^ rc;
	}

	public int reverseComplement(int kmerInt) {
		int complement = ~kmerInt;

		int reverseComplement = 0;
		for (int i = 0; i < K; i++) {
			int next = (complement >>> 2 * i) & 0x03;
			reverseComplement <<= 2;
			reverseComplement |= next;
		}

		return reverseComplement;
	}

	private char[] ACGT = { 'A', 'C', 'G', 'T' };

	/**
	 * @param acgt
	 * @return
	 * @throws UTGBException
	 *             when the input contains a non-ACGT character;
	 */
	public int parseString(String acgt) throws UTGBException {

		int kmer = 0;

		for (int i = 0; i < acgt.length(); i++) {
			byte b = CompactACGTWriter.to2bitCode(acgt.charAt(i));
			if (b >= 4)
				throw new UTGBException(UTGBErrorCode.NOT_AN_ACGT);

			kmer <<= 2;
			kmer |= b;
		}

		return kmer;
	}

	public String toString(int kmerInt) {
		StringBuilder seq = new StringBuilder();
		for (int i = 0; i < K; i++) {
			int index = (kmerInt >>> (2 * (K - i - 1))) & 0x03;
			seq.append(ACGT[index]);
		}
		return seq.toString();
	}

}
