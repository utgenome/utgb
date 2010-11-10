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
// GeneticVariation.java
// Since: 2010/10/13
//
//--------------------------------------
package org.utgenome.util.sv;

import org.utgenome.gwt.utgb.client.bio.IUPAC;
import org.xerial.lens.Lens;
import org.xerial.util.log.Logger;

/**
 * genetic variation location [start, end), chr and allele (genotype) information
 * 
 * @author leo
 * 
 */
public class GeneticVariation {

	private static Logger _logger = Logger.getLogger(GeneticVariation.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static enum VariationType {
		NotAvailable, Mutation, Insertion, Deletion;
	};

	// locus information 
	public VariationType variationType;
	public String chr;
	public int start = -1; // 1-origin (inclusive, -1 means undefined value)
	public String refBase;

	// parameters to be determined automatically 
	private String genotype;
	private IUPAC altBase;

	public int indelLength = 0;

	public GeneticVariation() {
	}

	/**
	 * @param type
	 * @param chr
	 * @param start
	 *            1-origin (inclusive)
	 * @param end
	 *            1-origin (exclusive)
	 * @param genotype
	 *            A, C, AC, ACGT (genotype), *, +A (insertion to reference), -aatT (deletion from reference), etc.
	 */
	public GeneticVariation(String chr, int start, String genotype) {
		this.chr = chr;
		this.start = start;
		setGenotype(genotype);
	}

	public GeneticVariation(GeneticVariation other) {
		this.start = other.start;
		this.variationType = other.variationType;
		this.chr = other.chr;
		this.genotype = other.genotype;
		this.altBase = other.altBase;
		this.refBase = other.refBase;
		this.indelLength = other.indelLength;
	}

	VariationType detectVariationType(String allele) {
		if (allele == null)
			return VariationType.NotAvailable;

		String[] alleleList = allele.split("/");

		for (String each : alleleList) {
			if (each.startsWith("+")) {
				indelLength = each.length() - 1;
				return VariationType.Insertion;
			}
			else if (each.startsWith("-")) {
				indelLength = each.length() - 1;
				return VariationType.Deletion;
			}
			else {
				try {
					altBase = IUPAC.find(allele);
				}
				catch (IllegalArgumentException e) {
					// unknown IUPAC code
					_logger.warn("unkonwn IUPAC code: " + allele);
					altBase = IUPAC.None;
				}
				return VariationType.Mutation;
			}
		}

		return VariationType.NotAvailable;
	}

	public IUPAC getAltBase() {
		return altBase;
	}

	public String getGenotype() {
		return genotype;
	}

	public void setGenotype(String genotype) {
		this.genotype = genotype;
		this.variationType = detectVariationType(genotype);
	}

	@Override
	public String toString() {
		return Lens.toSilk(this);
	}

}
