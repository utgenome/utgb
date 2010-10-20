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
import org.utgenome.gwt.utgb.client.bio.Interval;
import org.xerial.lens.Lens;

/**
 * genetic variation location [start, end), chr and allele (genotype) information
 * 
 * @author leo
 * 
 */
public class GeneticVariation extends Interval {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static enum VariationType {
		NA("unknown"), M("point mutation"), I("insertion"), D("deletion");

		public final String description;

		private VariationType(String description) {
			this.description = description;
		}
	};

	// locus information ((start, end) values are in the parent Interval.class)
	public VariationType variationType;
	public String chr;
	private String genotype;
	public IUPAC iupac = IUPAC.None;
	public String refBase;

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
	public GeneticVariation(String chr, int start, int end, String genotype) {
		super(start, end);
		this.chr = chr;
		setGenotype(genotype);
	}

	public GeneticVariation(GeneticVariation other) {
		super(other);
		this.variationType = other.variationType;
		this.chr = other.chr;
		this.genotype = other.genotype;
		this.iupac = other.iupac;
		this.refBase = other.refBase;
	}

	VariationType detectVariationType(String allele) {
		if (allele == null)
			return VariationType.NA;

		String[] alleleList = allele.split("/");
		if (alleleList.length == 1) {
			// IUPAC
			iupac = IUPAC.find(allele);
		}
		else
			iupac = IUPAC.None;

		if (iupac != IUPAC.None)
			return VariationType.M;

		for (String each : alleleList) {
			if (allele.startsWith("+"))
				return VariationType.I;
			else if (allele.startsWith("-"))
				return VariationType.D;
		}

		return VariationType.NA;
	}

	public String getGenotype() {
		return genotype;
	}

	public void setGenotype(String genotype) {
		this.genotype = genotype;
		this.variationType = detectVariationType(genotype);
	}

	public void setPos(int pos) {
		setStartAndEnd(pos, pos);
	}

	@Override
	public String toString() {
		return Lens.toSilk(this);
	}

	@Override
	public String getName() {
		return String.format("%s", variationType.description);
	}

}
