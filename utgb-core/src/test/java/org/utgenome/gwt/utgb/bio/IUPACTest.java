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
// IUPACTest.java
// Since: 2010/10/08
//
//--------------------------------------
package org.utgenome.gwt.utgb.bio;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.utgenome.gwt.utgb.client.bio.IUPAC;

public class IUPACTest {

	@Test
	public void encode() throws Exception {
		assertEquals(IUPAC.A, IUPAC.toIUPAC("A"));
		assertEquals(IUPAC.C, IUPAC.toIUPAC("C"));
		assertEquals(IUPAC.G, IUPAC.toIUPAC("G"));
		assertEquals(IUPAC.T, IUPAC.toIUPAC("T"));

		assertEquals(IUPAC.M, IUPAC.toIUPAC("AC"));
		assertEquals(IUPAC.M, IUPAC.toIUPAC("CA"));
		assertEquals(IUPAC.M, IUPAC.toIUPAC("A/C"));

		assertEquals(IUPAC.R, IUPAC.toIUPAC("AG"));
		assertEquals(IUPAC.R, IUPAC.toIUPAC("GA"));
		assertEquals(IUPAC.R, IUPAC.toIUPAC("A/G"));

		assertEquals(IUPAC.W, IUPAC.toIUPAC("AT"));
		assertEquals(IUPAC.W, IUPAC.toIUPAC("TA"));
		assertEquals(IUPAC.W, IUPAC.toIUPAC("T/A"));

		assertEquals(IUPAC.S, IUPAC.toIUPAC("CG"));
		assertEquals(IUPAC.Y, IUPAC.toIUPAC("CT"));
		assertEquals(IUPAC.K, IUPAC.toIUPAC("GT"));

		assertEquals(IUPAC.V, IUPAC.toIUPAC("ACG"));
		assertEquals(IUPAC.V, IUPAC.toIUPAC("AGC"));
		assertEquals(IUPAC.V, IUPAC.toIUPAC("CGA"));
		assertEquals(IUPAC.V, IUPAC.toIUPAC("CAG"));
		assertEquals(IUPAC.V, IUPAC.toIUPAC("GAC"));

		assertEquals(IUPAC.H, IUPAC.toIUPAC("ACT"));
		assertEquals(IUPAC.D, IUPAC.toIUPAC("AGT"));
		assertEquals(IUPAC.B, IUPAC.toIUPAC("CGT"));
		assertEquals(IUPAC.N, IUPAC.toIUPAC("ACGT"));

		assertEquals(IUPAC.None, IUPAC.toIUPAC("*"));
	}

}
