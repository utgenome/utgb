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

import java.io.StringWriter;
import java.util.ArrayList;

import org.junit.Test;
import org.utgenome.gwt.utgb.client.bio.IUPAC;
import org.xerial.util.StringUtil;
import org.xerial.util.log.Logger;

public class IUPACTest {

	private static Logger _logger = Logger.getLogger(IUPACTest.class);

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

	@Test
	public void toGenoType() throws Exception {
		assertEquals("A", IUPAC.A.toGenoType());
		assertEquals("C", IUPAC.C.toGenoType());
		assertEquals("G", IUPAC.G.toGenoType());
		assertEquals("T", IUPAC.T.toGenoType());
		assertEquals("", IUPAC.None.toGenoType());
		assertEquals("ACT", IUPAC.H.toGenoType());
	}

	@Test
	public void dot() throws Exception {
		assertEquals(IUPAC.N, IUPAC.encode('.'));
	}

	@Test
	public void complement() throws Exception {

		ArrayList<String> complementList = new ArrayList<String>();

		for (byte i = 0; i < 16; i++) {
			IUPAC orig = IUPAC.decode(i);
			String genoType = orig.toGenoType();
			StringWriter complement = new StringWriter(genoType.length());
			for (int x = 0; x < genoType.length(); ++x) {
				char base = genoType.charAt(x);
				switch (base) {
				case 'A':
					complement.append('T');
					break;
				case 'C':
					complement.append('G');
					break;
				case 'G':
					complement.append('C');
					break;
				case 'T':
					complement.append('A');
					break;
				default:
					complement.append(base);
				}
			}

			String genoTypeComplement = complement.toString();
			IUPAC complementCode = IUPAC.toIUPAC(genoTypeComplement);
			complementList.add(String.format("IUPAC.%s", complementCode));

			assertEquals(complementCode, orig.complement());
		}

		_logger.trace(StringUtil.join(complementList, ", "));

	}

}
