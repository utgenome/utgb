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
// Kmer.java
// Since: 2010/11/10
//
//--------------------------------------
package org.utgenome.format.fasta;

import org.utgenome.gwt.utgb.client.bio.ACGTEncoder;

/**
 * K-mer integer representation of ACGT.
 * 
 * The difference with {@link CompactACGT} is this K-mer representation is modifiable, to facilitate genome sequence
 * editing.
 * 
 * @author leo
 * 
 */
public class Kmer implements GenomeSequence {

	private byte[] sequence2bit;
	private int size;

	private Kmer(byte[] sequence2bit, int size) {
		this.sequence2bit = sequence2bit;
		this.size = size;
	}

	public Kmer(CompactACGT acgt) {
		this.size = acgt.length();
		sequence2bit = new byte[acgt.sequence.length];
		for (int i = 0; i < sequence2bit.length; ++i) {
			sequence2bit[i] = 0;
		}

		for (int i = 0; i < acgt.length(); ++i) {
			// TODO byte copy
			set(i, acgt.charAt(i));
		}
	}

	public Kmer(String acgt) {
		this.size = acgt.length();
		sequence2bit = new byte[this.size / 4 + (this.size % 4 == 0 ? 0 : 1)];
		for (int i = 0; i < sequence2bit.length; ++i) {
			sequence2bit[i] = 0;
		}
		for (int i = 0; i < acgt.length(); ++i) {
			set(i, acgt.charAt(i));
		}
	}

	public int length() {
		return size;
	}

	public char charAt(int index) {
		final int offset = index % 4;
		byte code = (byte) (sequence2bit[index / 4] >>> (6 - offset * 2) & 0x03);
		return ACGTEncoder.toBase(code);
	}

	public void set(int index, char acgt) {
		final int offset = index % 4;
		sequence2bit[index / 4] |= (byte) ((ACGTEncoder.to2bitCode(acgt) & 0x03) << (6 - offset * 2));
	}

	public void set(int index, String acgt) {
		final int offset = index % 4;
		sequence2bit[index / 4] |= (byte) ((ACGTEncoder.to2bitCode(acgt.charAt(0)) & 0x03) << (6 - offset * 2));
	}

	public Kmer reverseComplement() {
		byte[] rc = new byte[sequence2bit.length];

		int rIndex = 0;
		for (int i = size - 1; i >= 0; --i, rIndex++) {
			final int offset = i % 4;
			int code = (sequence2bit[i / 4] >>> (6 - offset * 2)) & 0x03;
			rc[rIndex / 4] |= (byte) ((~code & 0x03) << (6 - (rIndex % 4) * 2));
		}

		return new Kmer(rc, size);
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < size; i++) {
			s.append(this.charAt(i));
		}
		return s.toString();

	}

}
