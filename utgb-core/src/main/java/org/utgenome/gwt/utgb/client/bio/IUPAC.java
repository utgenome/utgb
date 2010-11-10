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
// IUPAC.java
// Since: 2010/10/07
//
//--------------------------------------
package org.utgenome.gwt.utgb.client.bio;

/**
 * IUPAC: allele variation notation
 * 
 * @author leo
 * 
 */
public enum IUPAC {

	None("*", 0x00), A("A", 0x01), C("C", 0x02), G("G", 0x04), T("T", 0x08), M("A/C", 0x03), R("A/G", 0x05), W("A/T", 0x09), S("C/G", 0x06), Y("C/T", 0x0A), K(
			"G/T", 0x0C), V("A/C/G", 0x07), H("A/C/T", 0x0B), D("A/G/T", 0x0D), B("C/G/T", 0x0E), N("A/C/G/T", 0x0F);

	private final static IUPAC[] acgtToIUPACTable = new IUPAC[16];

	static {
		for (IUPAC each : IUPAC.values()) {
			acgtToIUPACTable[each.bitFlag & 0x0F] = each;
		}
	}

	public final String variation;
	public final int bitFlag;

	private IUPAC(String variation, int bitFlag) {
		this.variation = variation;
		this.bitFlag = bitFlag;
	}

	/**
	 * Convert this IUPAC code the concatenation of allele bases (e.g., AC, AT, CGT, etc.)
	 * 
	 * @return
	 */
	public String toGenoType() {

		StringBuilder genoType = new StringBuilder();
		int flag = 0x01;
		for (int i = 0; i < 4; i++, flag <<= 1) {
			if ((bitFlag & flag) != 0) {
				genoType.append(ACGTEncoder.toBase(i));
			}
		}
		return genoType.toString();
	}

	/**
	 * Translate a genotype to the corresponding IUPAC code
	 * 
	 * @param genoType
	 *            concatenation of ACGT characters
	 * @return
	 */
	public static IUPAC toIUPAC(String genoType) {

		int flag = 0;

		for (int i = 0; i < genoType.length(); ++i) {
			byte code = ACGTEncoder.to2bitCode(genoType.charAt(i));
			if (code >= 4)
				continue;

			int bit = 0x01 << code;
			flag |= bit;
		}

		return acgtToIUPACTable[flag & 0x0F];
	}

	public static IUPAC find(String iupacCode) {
		IUPAC iupac = IUPAC.valueOf(IUPAC.class, iupacCode);

		if (iupac == null)
			return IUPAC.None;
		else
			return iupac;
	}

}
