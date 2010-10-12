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
import org.xerial.util.log.Logger;

/**
 * Converting SAM into WIG (coverage depth)
 * 
 * @author leo
 * 
 */
public class Sam2WigConverter {

	private static Logger _logger = Logger.getLogger(Sam2WigConverter.class);

	private Deque<Interval> readSetInStartOrder = new ArrayDeque<Interval>();
	private String currentChr = null;
	private int sweepLine = 1;
	private CoverageWriter reporter;

	public static class CoverageWriter {
		private int skipLength = 0;

		private enum State {
			LEADING_ZEROs, AFTER_HEADER
		}

		private State state = State.LEADING_ZEROs;
		private final Writer out;

		public CoverageWriter(Writer out) throws IOException {
			this.out = out;

			out.write(String.format("track type=wiggle_0\n"));
		}

		public void switchChr() {
			skipLength = 0;
			state = State.LEADING_ZEROs;
		}

		public void outputHeader(String chr, int start) throws IOException {
			out.write(String.format("fixedStep chrom=%s start=%d step=1 span=1\n", chr, start));
			state = State.AFTER_HEADER;
		}

		public void flush() throws IOException {
			this.out.flush();
		}

		public void report(String chr, int start, int value) throws IOException {
			if (state == State.LEADING_ZEROs) {
				if (value == 0)
					skipLength++;
				else {
					outputHeader(chr, start);
					skipLength = 0;
				}
			}

			// output data entry
			out.write(Integer.toString(value));
			out.write("\n");
		}

	}

	/**
	 * Convert the input SAM file records into WIG format of read depth
	 * 
	 * @param samOrBam
	 * @param out
	 * @throws IOException
	 */
	public void convert(File samOrBam, Writer out) throws IOException {
		this.reporter = new CoverageWriter(out);
		SAMFileReader.setDefaultValidationStringency(ValidationStringency.SILENT);
		SAMFileReader samReader = new SAMFileReader(samOrBam);
		try {
			convert(samReader.iterator());
			_logger.info("done.");
		}
		finally {
			samReader.close();
			this.reporter.flush();
		}
	}

	public void convert(SAMRecordIterator cursor) throws IOException {

		// map-reduce style code (much simpler if we have a framework to execute MapReduce) 
		// map
		//   r: SAMRecord -> ((r.chr, x), 1)  (x = r.start, ..., r.end) 
		// reduce
		//   (chr, x), {1, 1, ...}  ->  output ((chr, x), sum of 1s)

		long readCount = 0;

		int sweepLine = 1;
		// assume that SAM reads are sorted in the start order
		for (; cursor.hasNext();) {
			readCount++;

			if (readCount != 0 && (readCount % 1000000) == 0) {
				_logger.info(String.format("processed %,d reads", readCount));
			}

			SAMRecord read = cursor.next();

			String ref = read.getReferenceName();

			if (currentChr == null || (currentChr != null && !currentChr.equals(ref))) { // moved to the next chromosome
				// flush the cached reads
				if (!readSetInStartOrder.isEmpty()) {
					sweep(readSetInStartOrder, sweepLine, maxReadEnd(readSetInStartOrder));
				}

				readSetInStartOrder.clear();
				sweepLine = 1;
				currentChr = ref;
				reporter.switchChr();
				_logger.info(String.format("processing %s", currentChr));

			}

			Interval readInterval = new Interval(read.getAlignmentStart(), read.getAlignmentEnd() + 1);
			if (sweepLine < readInterval.getStart()) {
				// we can sweep reads up to sweepEnd
				int sweepEnd = readInterval.getStart();
				sweep(readSetInStartOrder, sweepLine, sweepEnd);
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
			sweep(readSetInStartOrder, sweepLine, maxReadEnd(readSetInStartOrder));
		}

	}

	public static final int maxReadEnd(Collection<Interval> readSet) {
		int maxEnd = -1;
		for (Interval each : readSet) {
			if (maxEnd < each.getEnd())
				maxEnd = each.getEnd();
		}
		return maxEnd;
	}

	void sweep(Collection<Interval> readSet, int start, int end) throws IOException {
		if (start >= end)
			return;

		int[] coverage = getReadDepth(new Interval(start, end), readSet);
		for (int i = 0; i < coverage.length; ++i) {
			reporter.report(currentChr, start + i, coverage[i]);
		}
	}

	/**
	 * Compute the read depth in the specified block
	 * 
	 * @param block
	 * @param readsInBlock
	 * @return
	 */
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
