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
// CodonTableTest.java
// Since: 2010/10/07
//
//--------------------------------------
package org.utgenome.gwt.utgb.bio;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;
import org.utgenome.gwt.utgb.client.bio.ACGTEncoder;
import org.utgenome.gwt.utgb.client.bio.AminoAcid;
import org.utgenome.gwt.utgb.client.bio.CodonTable;
import org.utgenome.util.kmer.KmerIntegerFactory;

public class CodonTableTest {

	@Test
	public void codon() throws Exception {
		KmerIntegerFactory f = new KmerIntegerFactory(3);

		ArrayList<AminoAcid> list = new ArrayList<AminoAcid>();
		for (int i = 0; i < 64; i++) {
			String codon = f.toString(i);
			AminoAcid aminoAcid = CodonTable.toAminoAcid(i);
			AminoAcid aminoAcid2 = CodonTable.toAminoAcid(ACGTEncoder.toString(i, 3));
			assertEquals(aminoAcid, aminoAcid2);
		}

	}

	@Test
	public void toAA() throws Exception {

		// AA
		assertEquals(CodonTable.toAminoAcid("AAA"), AminoAcid.Lys);
		assertEquals(CodonTable.toAminoAcid("AAC"), AminoAcid.Asn);
		assertEquals(CodonTable.toAminoAcid("AAG"), AminoAcid.Lys);
		assertEquals(CodonTable.toAminoAcid("AAT"), AminoAcid.Asn);

		// AC
		assertEquals(CodonTable.toAminoAcid("ACA"), AminoAcid.Thr);
		assertEquals(CodonTable.toAminoAcid("ACC"), AminoAcid.Thr);
		assertEquals(CodonTable.toAminoAcid("ACG"), AminoAcid.Thr);
		assertEquals(CodonTable.toAminoAcid("ACT"), AminoAcid.Thr);

		// AG
		assertEquals(CodonTable.toAminoAcid("AGA"), AminoAcid.Arg);
		assertEquals(CodonTable.toAminoAcid("AGC"), AminoAcid.Ser);
		assertEquals(CodonTable.toAminoAcid("AGG"), AminoAcid.Arg);
		assertEquals(CodonTable.toAminoAcid("AGT"), AminoAcid.Ser);

		// AT
		assertEquals(CodonTable.toAminoAcid("ATA"), AminoAcid.Ile);
		assertEquals(CodonTable.toAminoAcid("ATC"), AminoAcid.Ile);
		assertEquals(CodonTable.toAminoAcid("ATG"), AminoAcid.Met);
		assertEquals(CodonTable.toAminoAcid("ATT"), AminoAcid.Ile);

		// CA
		assertEquals(CodonTable.toAminoAcid("CAA"), AminoAcid.Gln);
		assertEquals(CodonTable.toAminoAcid("CAC"), AminoAcid.His);
		assertEquals(CodonTable.toAminoAcid("CAG"), AminoAcid.Glu);
		assertEquals(CodonTable.toAminoAcid("CAT"), AminoAcid.His);

		// CC
		assertEquals(CodonTable.toAminoAcid("CCA"), AminoAcid.Pro);
		assertEquals(CodonTable.toAminoAcid("CCC"), AminoAcid.Pro);
		assertEquals(CodonTable.toAminoAcid("CCG"), AminoAcid.Pro);
		assertEquals(CodonTable.toAminoAcid("CCT"), AminoAcid.Pro);

		// CG
		assertEquals(CodonTable.toAminoAcid("CGA"), AminoAcid.Arg);
		assertEquals(CodonTable.toAminoAcid("CGC"), AminoAcid.Arg);
		assertEquals(CodonTable.toAminoAcid("CGG"), AminoAcid.Arg);
		assertEquals(CodonTable.toAminoAcid("CGT"), AminoAcid.Arg);

		// CT
		assertEquals(CodonTable.toAminoAcid("CTA"), AminoAcid.Leu);
		assertEquals(CodonTable.toAminoAcid("CTC"), AminoAcid.Leu);
		assertEquals(CodonTable.toAminoAcid("CTG"), AminoAcid.Leu);
		assertEquals(CodonTable.toAminoAcid("CTT"), AminoAcid.Leu);

		// GA
		assertEquals(CodonTable.toAminoAcid("GAA"), AminoAcid.Glu);
		assertEquals(CodonTable.toAminoAcid("GAC"), AminoAcid.Asp);
		assertEquals(CodonTable.toAminoAcid("GAG"), AminoAcid.Glu);
		assertEquals(CodonTable.toAminoAcid("GAT"), AminoAcid.Asp);

		// GC
		assertEquals(CodonTable.toAminoAcid("GCA"), AminoAcid.Ala);
		assertEquals(CodonTable.toAminoAcid("GCC"), AminoAcid.Ala);
		assertEquals(CodonTable.toAminoAcid("GCG"), AminoAcid.Ala);
		assertEquals(CodonTable.toAminoAcid("GCT"), AminoAcid.Ala);

		// GG
		assertEquals(CodonTable.toAminoAcid("GGA"), AminoAcid.Gly);
		assertEquals(CodonTable.toAminoAcid("GGC"), AminoAcid.Gly);
		assertEquals(CodonTable.toAminoAcid("GGG"), AminoAcid.Gly);
		assertEquals(CodonTable.toAminoAcid("GGT"), AminoAcid.Gly);

		// GT
		assertEquals(CodonTable.toAminoAcid("GTA"), AminoAcid.Val);
		assertEquals(CodonTable.toAminoAcid("GTC"), AminoAcid.Val);
		assertEquals(CodonTable.toAminoAcid("GTG"), AminoAcid.Val);
		assertEquals(CodonTable.toAminoAcid("GTT"), AminoAcid.Val);

		// TA
		assertEquals(CodonTable.toAminoAcid("TAA"), AminoAcid.Ochre);
		assertEquals(CodonTable.toAminoAcid("TAC"), AminoAcid.Tyr);
		assertEquals(CodonTable.toAminoAcid("TAG"), AminoAcid.Amber);
		assertEquals(CodonTable.toAminoAcid("TAT"), AminoAcid.Tyr);

		// TC
		assertEquals(CodonTable.toAminoAcid("TCA"), AminoAcid.Ser);
		assertEquals(CodonTable.toAminoAcid("TCC"), AminoAcid.Ser);
		assertEquals(CodonTable.toAminoAcid("TCG"), AminoAcid.Ser);
		assertEquals(CodonTable.toAminoAcid("TCT"), AminoAcid.Ser);

		// TG
		assertEquals(CodonTable.toAminoAcid("TGA"), AminoAcid.Opal);
		assertEquals(CodonTable.toAminoAcid("TGC"), AminoAcid.Cys);
		assertEquals(CodonTable.toAminoAcid("TGG"), AminoAcid.Trp);
		assertEquals(CodonTable.toAminoAcid("TGT"), AminoAcid.Cys);

		// TT
		assertEquals(CodonTable.toAminoAcid("TTA"), AminoAcid.Leu);
		assertEquals(CodonTable.toAminoAcid("TTC"), AminoAcid.Phe);
		assertEquals(CodonTable.toAminoAcid("TTG"), AminoAcid.Leu);
		assertEquals(CodonTable.toAminoAcid("TTT"), AminoAcid.Phe);

	}
}
