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
// KmerTest.java
// Since: 2010/11/10
//
//--------------------------------------
package org.utgenome.format.fasta;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.utgenome.gwt.utgb.client.bio.ACGTEncoder;

public class KmerTest {

	@Test
	public void constructor() throws Exception {

		final String s = "AAACCTGT";
		Kmer kmer = new Kmer(s);
		assertEquals(s, kmer.toString());
		for (int i = 0; i < s.length(); ++i) {
			assertEquals(s.charAt(i), kmer.charAt(i));
		}

		assertEquals(s.length(), kmer.length());
	}

	@Test
	public void createFromCompactACGT() throws Exception {
		final String s = "AAACCTGT";
		Kmer kmer = new Kmer(CompactACGT.createFromString(s));
		assertEquals(s, kmer.toString());
		for (int i = 0; i < s.length(); ++i) {
			assertEquals(s.charAt(i), kmer.charAt(i));
		}

		assertEquals(s.length(), kmer.length());

	}

	@Test
	public void setACGT() throws Exception {
		final String s = "AAACCTGT";
		Kmer kmer = new Kmer(s);
		kmer.set(0, "G");
		assertEquals("GAACCTGT", kmer.toString());
		kmer.set(1, "C");
		assertEquals("GCACCTGT", kmer.toString());
		kmer.set(2, "T");
		assertEquals("GCTCCTGT", kmer.toString());
		kmer.set(3, "T");
		assertEquals("GCTTCTGT", kmer.toString());

	}

	@Test
	public void reverse() throws Exception {
		final String s = "ACGTAATACGATAT";
		final Kmer seq = new Kmer(s);
		final Kmer rc = seq.reverseComplement();
		assertEquals(seq.length(), rc.length());
		assertEquals(s.length(), rc.length());

		assertEquals("ATATCGTATTACGT", rc.toString());

	}

	@Test
	public void kmerInt() throws Exception {
		final String s = "AAACCTGT";
		Kmer kmer = new Kmer(s);

		assertEquals(ACGTEncoder.toKmerInt(s.length(), s), kmer.toInt());
		kmer.set(4, "T");
		final String alt = "AAACTTGT";
		assertEquals(ACGTEncoder.toKmerInt(alt.length(), alt), kmer.toInt());

	}

}
