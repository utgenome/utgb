/*--------------------------------------------------------------------------
 *  Copyright 2009 utgenome.org
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
// ReferenceSequence.java
// Since: 2009/04/27
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.bio;

/**
 * reference genome sequence to which genome reads are aligned
 * 
 * @author leo
 * 
 */
public class ReferenceSequence extends Interval {

	private static final long serialVersionUID = 1L;

	public String name;
	public String sequence;

	public ReferenceSequence() {
	}

	public ReferenceSequence(int start, String name, String sequence) {
		super(start, sequence != null ? start + sequence.length() : start);
		this.name = name;
		this.sequence = sequence;
	}

	@Override
	public void accept(GenomeRangeVisitor visitor) {
		visitor.visitSequence(this);
	}

	@Override
	public String getName() {
		return this.name;
	}

}
