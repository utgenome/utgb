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
// InfoSilkGenerator.java
// Since: May 25, 2010
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.bio;

/**
 * Generating read information in Silk format, which is used for displaying mouse over message
 * 
 * @author leo
 * 
 */
public class InfoSilkGenerator implements OnGenomeDataVisitor {

	public StringBuilder buf = new StringBuilder();

	public String getSilk() {
		return buf.toString();
	}

	public void visitBSSRead(BSSRead b) {
		visitInterval(b);

	}

	public void visitGene(Gene g) {
		visitInterval(g);

	}

	public void visitInterval(Interval interval) {
		buf.append("-name: " + interval.getName());
		buf.append("\n");
		buf.append("-start: " + interval.getStart());
		buf.append("\n");
		buf.append("-end: " + interval.getEnd());
		buf.append("\n");
		buf.append("-length: " + interval.length());
		buf.append("\n");
	}

	public void visitRead(Read r) {
		visitInterval(r);
		buf.append("-strand: " + r.getStrand());
		buf.append("\n");
	}

	public void visitSAMRead(SAMRead r) {
		visitInterval(r);
		buf.append("-cigar: " + r.cigar);
		buf.append("\n");
		buf.append("-insert size: " + r.iSize);
		buf.append("\n");
		buf.append("-mapq: " + r.mapq);
		buf.append("\n");
		buf.append("-mate start: " + r.mStart);
		buf.append("\n");
		buf.append("-tag\n");
		for (String key : r.tag.keySet()) {
			buf.append(" -");
			buf.append(key);
			buf.append(": ");
			buf.append(r.tag.get(key));
			buf.append("\n");
		}
	}

	public void visitSequence(ReferenceSequence referenceSequence) {
		// TODO Auto-generated method stub

	}

}
