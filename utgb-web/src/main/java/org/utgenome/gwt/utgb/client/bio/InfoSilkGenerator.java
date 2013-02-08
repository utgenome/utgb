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

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Generating read information in Silk format, which is used for displaying mouse over message
 * 
 * @author leo
 * 
 */
public class InfoSilkGenerator extends GenomeRangeVisitorBase {

	public ArrayList<String> lines = new ArrayList<String>();

	public String getSilk() {
		StringBuilder buf = new StringBuilder();
		for (String each : lines) {
			buf.append(each);
			buf.append("\n");
		}
		return buf.toString();
	}

	public List<String> getLines() {
		return lines;
	}

	public void addLine(String text) {
		lines.add(text);
	}

	@Override
	public void visitBEDGene(BEDGene g) {
		visitGene(g);
		addLine("-score:" + g.score);

		if (!g.getCDS().isEmpty()) {
			// Note that only one CDS region is contained in BED files.
			CDS cds = g.getCDS().get(0);
			addLine("-thick start: " + cds.start);
			addLine("-thick end: " + cds.end);
			addLine("-thick width: " + (cds.end - cds.start));

			// compute the CDS sequence length
			int cdsLength = 0;
			for (Exon e : g.getExon()) {
				Interval overlap = e.intersect(cds);
				if (overlap == null)
					continue;
				cdsLength += overlap.length();
			}
			addLine("-thick length: " + cdsLength);

			int len_UTR5 = 0;
			for (Exon e : g.getExon()) {
				Interval overlap = e.intersect(cds);
				if (overlap == null) {
					len_UTR5 += e.length();
					continue;
				}
				else {
					len_UTR5 += (overlap.getStart() - e.getStart());
					break;
				}
			}

			int len_UTR3 = 0;
			for (ListIterator<Exon> it = g.getExon().listIterator(g.getExon().size()); it.hasPrevious();) {
				Exon e = it.previous();
				Interval overlap = e.intersect(cds);
				if (overlap == null) {
					len_UTR3 += e.length();
					continue;
				}
				else {
					len_UTR3 += (e.getEnd() - overlap.getEnd());
					break;
				}
			}

			if (g.isAntiSense()) {
				// swap
				int tmp = len_UTR5;
				len_UTR5 = len_UTR3;
				len_UTR3 = tmp;
			}

			if (len_UTR5 > 0)
				addLine("-UTR length 5': " + len_UTR5);
			if (len_UTR3 > 0)
				addLine("-UTR length 3': " + len_UTR3);
		}

	}

	@Override
	public void visitGap(Gap g) {

	}

	@Override
	public void visitInterval(Interval interval) {
		addLine("-name: " + interval.getName());
		addLine("-start: " + interval.getStart());
		addLine("-end: " + interval.getEnd());
		addLine("-length: " + interval.length());
	}

	@Override
	public void visitRead(Read r) {
		visitInterval(r);
		addLine("-strand: " + r.getStrand());
	}

	@Override
	public void visitSAMRead(SAMRead r) {
		visitSAMReadLight(r);
		//addLine("-QV: " + r.qual);
		addLine("-mapq: " + r.mapq);
		if (r.isPairedRead()) {
			addLine("-mate ref: " + r.mrnm);
			addLine("-mate start: " + r.mStart);
		}

		addLine("-tag");
		for (String key : r.tag.keySet()) {
			addLine("  -" + key + ": " + r.tag.get(key));
		}
	}

	@Override
	public void visitSAMReadLight(SAMReadLight r) {
		visitInterval(r);
		addLine("-flag: " + Integer.toBinaryString(r.flag));
		addLine("-strand: " + (r.isSense() ? "+" : "-"));
		addLine("-cigar: " + r.cigar);
		if (r.iSize != 0)
			addLine("-insert size: " + r.iSize);
	}

	@Override
	public void visitSequence(ReferenceSequence referenceSequence) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visitReadCoverage(ReadCoverage readCoverage) {
		addLine("-name: " + readCoverage.getName());
		addLine("-start: " + readCoverage.getStart());
		addLine("-end: " + readCoverage.getEnd());
		addLine("-pixel width: " + readCoverage.pixelWidth);
	}

	@Override
	public void visitSAMReadPairFragment(SAMReadPairFragment fragment) {
		super.visitSAMReadPairFragment(fragment);
		addLine("-mate start: " + fragment.mateStart);

	}

	@Override
	public void visitGraph(GraphData graph) {
		addLine("graph data");
	}

	@Override
	public void visitReadList(ReadList readList) {
		visitInterval(readList);
	}

}
