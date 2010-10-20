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
		Unknown, PointMutation, Insertion, Deletion
	};

	// locus information ((start, end) values are in the parent Interval.class)
	public VariationType type = VariationType.Unknown;
	public String chr;
	public String allele;

	/**
	 * @param type
	 * @param chr
	 * @param start
	 *            1-origin (inclusive)
	 * @param end
	 *            1-origin (exclusive)
	 * @param allele
	 *            A, C, AC, ACGT, *, etc.
	 */
	public GeneticVariation(VariationType type, String chr, int start, int end, String allele) {
		super(start, end);
		this.type = type;
		this.chr = chr;
		this.allele = allele;
	}

	public GeneticVariation(GeneticVariation other) {
		super(other);
		this.type = other.type;
		this.chr = other.chr;
		this.allele = other.allele;
	}

	@Override
	public String toString() {
		return Lens.toSilk(this);
	}

}
