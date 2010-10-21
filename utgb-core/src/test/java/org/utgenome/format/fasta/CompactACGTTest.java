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
// CompactACGTTest.java
// Since: 2010/10/05
//
//--------------------------------------
package org.utgenome.format.fasta;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class CompactACGTTest {

	@Test
	public void constructor() throws Exception {
		final String s = "ACGTAAT";
		final CompactACGT seq = CompactACGT.createFromString(s);
		assertEquals(7, seq.length());
		for (int i = 0; i < s.length(); ++i) {
			assertEquals(s.charAt(i), seq.charAt(i));
		}

	}

	@Test
	public void iterator() throws Exception {
		final String s = "ACGTAAT";
		final int K = 3;
		final CompactACGT seq = CompactACGT.createFromString(s);

		OverlappingKmerIterator it = seq.getKmerIterator(K);
		int kmerCount = 0;
		for (int i = 0; i < s.length() - K + 1; ++i, ++kmerCount) {
			CompactACGT kmer = it.nextKmer();
			assertNotNull(kmer);
			String kmerStr = kmer.toString();
			assertEquals(3, kmer.length());
			assertEquals(s.substring(i, i + K), kmerStr);
		}

		assertEquals(5, kmerCount);

	}

	@Test
	public void subsequence() throws Exception {

		final String s = "ACGTAATACGATAT";
		final CompactACGT seq = CompactACGT.createFromString(s);

		for (int K = 1; K < s.length(); ++K) {
			for (int i = 0; i < s.length() - K + 1; ++i) {
				CompactACGT subseq = seq.subSequence(i, K);
				assertEquals(s.substring(i, i + K), subseq.toString());
			}
		}
	}

	@Test
	public void reverseComplement() throws Exception {
		final String s = "ACGTAATACGATAT";
		final CompactACGT seq = CompactACGT.createFromString(s);
		final CompactACGT rc = seq.reverseComplement();
		assertEquals(seq.length(), rc.length());
		assertEquals(s.length(), rc.length());

		assertEquals("ATATCGTATTACGT", rc.toString());

	}

}
