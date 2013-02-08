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
// AminoAcid.java
// Since: 2010/10/07
//
//--------------------------------------
package org.utgenome.gwt.utgb.client.bio;

/**
 * AminoAcid table
 * 
 * @author leo
 * 
 */
public enum AminoAcid {

	Ala("A", "Alanine"), Arg("R", "Arginine"), Asn("N", "Asparagine"), Asp("D", "Aspartic acid"), Cys("C", "Cysteine"), Glu("E", "Glutamic acid"), Gln("Q",
			"Glutamine"), Gly("G", "Glycine"), His("H", "Histidine"), Ile("I", "Isoleucine"), Leu("L", "Leucine"), Lys("K", "Lysine"), Phe("F",
			"Phenylanlanine"), Pro("P", "Proline"), Ser("S", "Serine"), Thr("T", "Theronine"), Trp("W", "Tryptophan"), Tyr("Y", "Tyrosine"), Val("V", "Valine"),

	// start codon
	Met("M", "Methionine"),

	// 21st and 22nd amino acids
	Sec("U", "Selenocysteine"), Pyl("O", "Pyrrolysine"),

	// ambiguous amino acids
	Asx("B", "Asparagine or aspartic acid"), Glx("Z", "Glutamine or glutamic acid"), Xle("J", "Leucine or Isoleucine"), Xaa("X",
			"Unspecified or unknown amino acid"),

	// stop codons
	Ochre("-", "Stop codon: Ochre"), Opal("-", "Stop codon: Opal"), Amber("-", "Stop codon: Amber"),

	// for non-coding region
	NA("N/A", "not available");

	public final String symbol;
	public final String fullName;

	private AminoAcid(String symbol, String fullName) {
		this.symbol = symbol;
		this.fullName = fullName;
	}

	public boolean isStopCodon() {
		return this == Ochre || this == Opal || this == Amber;
	}
}
