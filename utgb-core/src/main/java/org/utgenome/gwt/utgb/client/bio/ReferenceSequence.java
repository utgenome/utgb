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
public class ReferenceSequence implements OnGenome {

	private static final long serialVersionUID = 1L;

	public int start;
	public String name;
	public String sequence;

	public ReferenceSequence() {
	}

	public void accept(OnGenomeDataVisitor visitor) {
		visitor.visitSequence(this);
	}

	public int getStart() {
		return start;
	}

	public String getName() {
		return this.name;
	}

	public int length() {
		return sequence != null ? sequence.length() : 0;
	}
}
