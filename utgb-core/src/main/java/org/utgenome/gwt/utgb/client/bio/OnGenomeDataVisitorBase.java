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
// OnGenomeDataVisitorBase.java
// Since: May 26, 2010
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.bio;

/**
 * Refereece implementation of the {@link OnGenomeDataVisitor}. This visitor redirects the visiting event to the parent
 * class of each data type.
 * 
 * @author leo
 * 
 */
public class OnGenomeDataVisitorBase implements OnGenomeDataVisitor {

	public void visitGap(Gap p) {
		visitInterval(p);
	}

	public void visitGene(Gene g) {
		visitRead(g);
	}

	public void visitBEDGene(BEDGene g) {
		visitGene(g);
	}

	public void visitInterval(Interval interval) {
		// do nothing in default
	}

	public void visitRead(Read r) {
		visitInterval(r);
	}

	public void visitReadCoverage(ReadCoverage readCoverage) {
		visitInterval(readCoverage);
	}

	public void visitSAMRead(SAMRead r) {
		visitSAMReadLight(r);
	}

	public void visitSequence(ReferenceSequence referenceSequence) {
		visitInterval(referenceSequence);
	}

	public void visitGraph(GraphData graph) {

	}

	public void visitSAMReadPair(SAMReadPair pair) {
		visitSAMReadLight(pair.getFirst());
		visitSAMReadLight(pair.getSecond());
	}

	public void visitSAMReadPairFragment(SAMReadPairFragment fragment) {
		visitSAMReadLight(fragment.oneEnd);
		visitGap(fragment.getGap());
	}

	public void visitReadList(ReadList readList) {

	}

	public void visitSAMReadLight(SAMReadLight r) {
		visitInterval(r);
	}

}
