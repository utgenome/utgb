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
// Sam2WigConverter.java
// Since: 2010/09/28
//
//--------------------------------------
package org.utgenome.format.sam;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;

import net.sf.samtools.SAMFileReader;
import net.sf.samtools.SAMFileReader.ValidationStringency;
import net.sf.samtools.SAMRecord;
import net.sf.samtools.SAMRecordIterator;

import org.utgenome.gwt.utgb.client.bio.Interval;
import org.xerial.util.ArrayDeque;
import org.xerial.util.Deque;

/**
 * Converting SAM into WIG (coverage depth)
 * 
 * @author leo
 * 
 */
public class Sam2WigConverter {

	private Deque<Interval> readSetInStartOrder = new ArrayDeque<Interval>();
	private String currentChr = null;
	private int sweepLine = 1;
	private Writer out;

	public void convert(File samOrBam, Writer out) throws IOException {
		this.out = out;
		SAMFileReader.setDefaultValidationStringency(ValidationStringency.SILENT);
		SAMFileReader samReader = new SAMFileReader(samOrBam);

		// map-reduce style code 
		// map
		//   r: SAMRecord -> ((r.chr, x), 1)  (x = r.start, ..., r.end) 
		// reduce
		//   (chr, x), {1, 1, ...}  ->  output ((chr, x), sum of 1s)

		int sweepLine = 1;
		// assume that SAM reads are sorted in the start order
		for (SAMRecordIterator it = samReader.iterator(); it.hasNext();) {
			SAMRecord read = it.next();

			String ref = read.getReferenceName();

			if (currentChr == null || (currentChr != null && !currentChr.equals(ref))) { // moved to the next chromosome
				// flush the cached reads
				if (!readSetInStartOrder.isEmpty()) {
					int sweepEnd = maxReadEnd(readSetInStartOrder);
					sweep(readSetInStartOrder, sweepLine, sweepEnd, out);
				}

				readSetInStartOrder.clear();
				sweepLine = 1;
				currentChr = ref;

				out.write(String.format("fixedStep chrom=%s start=%d step=1 span=1\n", currentChr, 1));
			}

			Interval readInterval = new Interval(read.getAlignmentStart(), read.getAlignmentEnd() + 1);
			if (sweepLine < readInterval.getStart()) {
				// we can sweep reads up to sweepEnd
				int sweepEnd = readInterval.getStart();
				sweep(readSetInStartOrder, sweepLine, sweepEnd, out);
				sweepLine = sweepEnd;

				// remove the reads before the sweep line   
				for (Interval each : readSetInStartOrder) {
					if (each.getStart() >= sweepLine)
						break; // sweep finished
					if (each.getEnd() <= sweepLine)
						readSetInStartOrder.removeFirst();
				}
			}
			readSetInStartOrder.add(readInterval);
		}

		if (!readSetInStartOrder.isEmpty()) {
			sweep(readSetInStartOrder, sweepLine, maxReadEnd(readSetInStartOrder), out);
		}

		out.flush();

		samReader.close();
	}

	public static final int maxReadEnd(Collection<Interval> readSet) {
		int maxEnd = -1;
		for (Interval each : readSet) {
			if (maxEnd < each.getEnd())
				maxEnd = each.getEnd();
		}
		return maxEnd;
	}

	public static void sweep(Collection<Interval> readSet, int start, int end, Writer out) throws IOException {
		if (start >= end)
			return;

		int[] coverage = getReadDepth(new Interval(start, end), readSet);
		for (int i = 0; i < coverage.length; ++i) {
			out.write(Integer.toString(coverage[i]));
			out.write("\n");
		}
	}

	public static int[] getReadDepth(Interval block, Collection<Interval> readsInBlock) {

		int offset = block.getStart();
		final int width = block.length();
		int[] coverage = new int[width];
		// initialize the array
		for (int i = 0; i < coverage.length; ++i)
			coverage[i] = 0;

		// accumulate coverage depth
		for (Interval each : readsInBlock) {
			int posStart = each.getStart() - offset;
			int posEnd = each.getEnd() - offset;

			if (posStart < 0)
				posStart = 0;
			if (posEnd >= width)
				posEnd = width;

			for (int i = posStart; i < posEnd; i++) {
				coverage[i]++;
			}
		}

		return coverage;
	}

}
