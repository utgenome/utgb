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
// CodonTable.java
// Since: 2010/10/07
//
//--------------------------------------
package org.utgenome.gwt.utgb.client.bio;

/**
 * Codon table
 * 
 * @author leo
 * 
 */
public class CodonTable {

	public final static AminoAcid[] kmerIntToAminoAcidTable = new AminoAcid[] { AminoAcid.Lys, AminoAcid.Asn, AminoAcid.Lys, AminoAcid.Asn, AminoAcid.Thr,
			AminoAcid.Thr, AminoAcid.Thr, AminoAcid.Thr, AminoAcid.Arg, AminoAcid.Ser, AminoAcid.Arg, AminoAcid.Ser, AminoAcid.Ile, AminoAcid.Ile,
			AminoAcid.Met, AminoAcid.Ile, AminoAcid.Gln, AminoAcid.His, AminoAcid.Glu, AminoAcid.His, AminoAcid.Pro, AminoAcid.Pro, AminoAcid.Pro,
			AminoAcid.Pro, AminoAcid.Arg, AminoAcid.Arg, AminoAcid.Arg, AminoAcid.Arg, AminoAcid.Leu, AminoAcid.Leu, AminoAcid.Leu, AminoAcid.Leu,
			AminoAcid.Glu, AminoAcid.Asp, AminoAcid.Glu, AminoAcid.Asp, AminoAcid.Ala, AminoAcid.Ala, AminoAcid.Ala, AminoAcid.Ala, AminoAcid.Gly,
			AminoAcid.Gly, AminoAcid.Gly, AminoAcid.Gly, AminoAcid.Val, AminoAcid.Val, AminoAcid.Val, AminoAcid.Val, AminoAcid.Ochre, AminoAcid.Tyr,
			AminoAcid.Amber, AminoAcid.Tyr, AminoAcid.Ser, AminoAcid.Ser, AminoAcid.Ser, AminoAcid.Ser, AminoAcid.Opal, AminoAcid.Cys, AminoAcid.Trp,
			AminoAcid.Cys, AminoAcid.Leu, AminoAcid.Phe, AminoAcid.Leu, AminoAcid.Phe };

	public static AminoAcid toAminoAcid(int codon3merInt) {
		return kmerIntToAminoAcidTable[codon3merInt & 0x3F];
	}

	public static AminoAcid toAminoAcid(String codon3mer) {

		int codonInt = 0;
		if (codon3mer.length() != 3)
			return AminoAcid.Xaa;

		for (int i = 0; i < 3; ++i) {
			int code2bit = ACGT.to2bitCode(codon3mer.charAt(i));
			if (code2bit >= 4)
				return AminoAcid.Xaa;

			codonInt <<= 2;
			codonInt |= code2bit;
		}
		return toAminoAcid(codonInt);
	}

	//	static {
	//		
	//		// AA
	//		codonToAminoAcidTable.put("AAA", AminoAcid.Lys);
	//		codonToAminoAcidTable.put("AAC", AminoAcid.Asn);
	//		codonToAminoAcidTable.put("AAG", AminoAcid.Lys);
	//		codonToAminoAcidTable.put("AAT", AminoAcid.Asn);
	//
	//		// AC
	//		codonToAminoAcidTable.put("ACA", AminoAcid.Thr);
	//		codonToAminoAcidTable.put("ACC", AminoAcid.Thr);
	//		codonToAminoAcidTable.put("ACG", AminoAcid.Thr);
	//		codonToAminoAcidTable.put("ACT", AminoAcid.Thr);
	//
	//		// AG
	//		codonToAminoAcidTable.put("AGA", AminoAcid.Arg);
	//		codonToAminoAcidTable.put("AGC", AminoAcid.Ser);
	//		codonToAminoAcidTable.put("AGG", AminoAcid.Arg);
	//		codonToAminoAcidTable.put("AGT", AminoAcid.Ser);
	//
	//		// AT
	//		codonToAminoAcidTable.put("ATA", AminoAcid.Ile);
	//		codonToAminoAcidTable.put("ATC", AminoAcid.Ile);
	//		codonToAminoAcidTable.put("ATG", AminoAcid.Met);
	//		codonToAminoAcidTable.put("ATT", AminoAcid.Ile);
	//
	//		// CA
	//		codonToAminoAcidTable.put("CAA", AminoAcid.Gln);
	//		codonToAminoAcidTable.put("CAC", AminoAcid.His);
	//		codonToAminoAcidTable.put("CAG", AminoAcid.Glu);
	//		codonToAminoAcidTable.put("CAT", AminoAcid.His);
	//
	//		// CC
	//		codonToAminoAcidTable.put("CCA", AminoAcid.Pro);
	//		codonToAminoAcidTable.put("CCC", AminoAcid.Pro);
	//		codonToAminoAcidTable.put("CCG", AminoAcid.Pro);
	//		codonToAminoAcidTable.put("CCT", AminoAcid.Pro);
	//
	//		// CG
	//		codonToAminoAcidTable.put("CGA", AminoAcid.Arg);
	//		codonToAminoAcidTable.put("CGC", AminoAcid.Arg);
	//		codonToAminoAcidTable.put("CGG", AminoAcid.Arg);
	//		codonToAminoAcidTable.put("CGT", AminoAcid.Arg);
	//
	//		// CT
	//		codonToAminoAcidTable.put("CTA", AminoAcid.Leu);
	//		codonToAminoAcidTable.put("CTC", AminoAcid.Leu);
	//		codonToAminoAcidTable.put("CTG", AminoAcid.Leu);
	//		codonToAminoAcidTable.put("CTT", AminoAcid.Leu);
	//
	//		// GA
	//		codonToAminoAcidTable.put("GAA", AminoAcid.Glu);
	//		codonToAminoAcidTable.put("GAC", AminoAcid.Asp);
	//		codonToAminoAcidTable.put("GAG", AminoAcid.Glu);
	//		codonToAminoAcidTable.put("GAT", AminoAcid.Asp);
	//
	//		// GC
	//		codonToAminoAcidTable.put("GCA", AminoAcid.Ala);
	//		codonToAminoAcidTable.put("GCC", AminoAcid.Ala);
	//		codonToAminoAcidTable.put("GCG", AminoAcid.Ala);
	//		codonToAminoAcidTable.put("GCT", AminoAcid.Ala);
	//
	//		// GG
	//		codonToAminoAcidTable.put("GGA", AminoAcid.Gly);
	//		codonToAminoAcidTable.put("GGC", AminoAcid.Gly);
	//		codonToAminoAcidTable.put("GGG", AminoAcid.Gly);
	//		codonToAminoAcidTable.put("GGT", AminoAcid.Gly);
	//
	//		// GT
	//		codonToAminoAcidTable.put("GTA", AminoAcid.Val);
	//		codonToAminoAcidTable.put("GTC", AminoAcid.Val);
	//		codonToAminoAcidTable.put("GTG", AminoAcid.Val);
	//		codonToAminoAcidTable.put("GTT", AminoAcid.Val);
	//
	//		// TA
	//		codonToAminoAcidTable.put("TAA", AminoAcid.Ochre);
	//		codonToAminoAcidTable.put("TAC", AminoAcid.Tyr);
	//		codonToAminoAcidTable.put("TAG", AminoAcid.Amber);
	//		codonToAminoAcidTable.put("TAT", AminoAcid.Tyr);
	//
	//		// TC
	//		codonToAminoAcidTable.put("TCA", AminoAcid.Ser);
	//		codonToAminoAcidTable.put("TCC", AminoAcid.Ser);
	//		codonToAminoAcidTable.put("TCG", AminoAcid.Ser);
	//		codonToAminoAcidTable.put("TCT", AminoAcid.Ser);
	//
	//		// TG
	//		codonToAminoAcidTable.put("TGA", AminoAcid.Opal);
	//		codonToAminoAcidTable.put("TGC", AminoAcid.Cys);
	//		codonToAminoAcidTable.put("TGG", AminoAcid.Trp);
	//		codonToAminoAcidTable.put("TGT", AminoAcid.Cys);
	//
	//		// TT
	//		codonToAminoAcidTable.put("TTA", AminoAcid.Leu);
	//		codonToAminoAcidTable.put("TTC", AminoAcid.Phe);
	//		codonToAminoAcidTable.put("TTG", AminoAcid.Leu);
	//		codonToAminoAcidTable.put("TTT", AminoAcid.Phe);
	//
	//	}

}
