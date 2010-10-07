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
package org.utgenome.gwt.utgb;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;
import org.utgenome.gwt.utgb.client.bio.ACGT;
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
			AminoAcid aminoAcid2 = CodonTable.toAminoAcid(ACGT.toString(i, 3));
			assertEquals(aminoAcid, aminoAcid2);
		}

	}
}
