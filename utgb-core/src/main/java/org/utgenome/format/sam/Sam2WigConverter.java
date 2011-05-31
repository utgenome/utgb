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
import java.util.Iterator;

import net.sf.samtools.SAMFileHeader;
import net.sf.samtools.SAMFileReader;
import net.sf.samtools.SAMFileReader.ValidationStringency;
import net.sf.samtools.SAMRecord;
import net.sf.samtools.SAMRecordIterator;
import net.sf.samtools.SAMSequenceRecord;

import org.utgenome.UTGBException;
import org.utgenome.gwt.utgb.client.bio.Interval;
import org.utgenome.gwt.utgb.client.bio.GenomeRange;
import org.utgenome.gwt.utgb.client.bio.GenomeRangeVisitor;
import org.utgenome.util.ReadDepth;
import org.utgenome.util.ReadDepth.DepthOutput;
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

	/**
	 * Convert the input SAM file records into WIG format of read depth
	 * 
	 * @param samOrBam
	 * @param out
	 * @throws IOException
	 * @throws UTGBException
	 */
	public void convert(File samOrBam, Writer out) throws UTGBException {
		try {
			this.reporter = new CoverageWriter(out);
			SAMFileReader.setDefaultValidationStringency(ValidationStringency.SILENT);
			SAMFileReader samReader = new SAMFileReader(samOrBam);

			try {
				// for each chromosome
				SAMFileHeader fileHeader = samReader.getFileHeader();
				for (SAMSequenceRecord each : fileHeader.getSequenceDictionary().getSequences()) {
					String chr = each.getSequenceName();
					_logger.info("processing " + chr);
					convert(samOrBam, chr, 1, each.getSequenceLength());
				}
			}
			finally {
				samReader.close();
				this.reporter.flush();
			}
		}
		catch (IOException e) {
			throw UTGBException.convert(e);
		}
		_logger.info("done.");
	}

	/**
	 * Iterator of SAMRecord
	 * 
	 * @author leo
	 * 
	 */
	public static class SAMRecordCursor implements Iterator<GenomeRange> {

		private static class SAMRecordWrap extends Interval {

			private static final long serialVersionUID = 1L;
			final SAMRecord read;

			public SAMRecordWrap(SAMRecord read) {
				super(read.getAlignmentStart(), read.getAlignmentEnd() + 1);
				this.read = read;
			}

			@Override
			public String getName() {
				return read.getReadName();
			}

			@Override
			public void accept(GenomeRangeVisitor visitor) {
				visitor.visitInterval(this);
			}

		}

		private final Deque<SAMRecord> queue = new ArrayDeque<SAMRecord>();
		private final SAMRecordIterator cursor;

		public SAMRecordCursor(SAMRecordIterator cursor) {
			this.cursor = cursor;
		}

		public boolean hasNext() {
			if (!queue.isEmpty())
				return true;

			for (; cursor.hasNext();) {
				SAMRecord next = cursor.next();
				if (next.getReadUnmappedFlag()) {
					continue;
				}
				queue.add(next);
				return true;
			}

			return false;
		}

		public GenomeRange next() {
			if (hasNext())
				return new SAMRecordWrap(queue.pollFirst());

			return null;
		}

		public void remove() {
			throw new UnsupportedOperationException("remove");

		}

		public void close() {
			cursor.close();
		}

	}

	public void convert(File samOrBam, String chr, int start, int end) throws UTGBException {

		SAMFileReader.setDefaultValidationStringency(ValidationStringency.SILENT);
		SAMFileReader samReader = new SAMFileReader(samOrBam, SAMReader.getBamIndexFile(samOrBam));
		SAMRecordCursor cursor = null;
		try {
			cursor = new SAMRecordCursor(samReader.queryOverlapping(chr, start, end));
			reporter.switchChr();
			ReadDepth.compute(chr, cursor, reporter);
		}
		catch (Exception e) {
			throw UTGBException.convert(e);
		}
		finally {
			if (cursor != null)
				cursor.close();
			samReader.close();
		}
	}

	private CoverageWriter reporter;

	public static class CoverageWriter implements DepthOutput {
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
			state = State.LEADING_ZEROs;
		}

		public void outputHeader(String chr, int start) throws IOException {
			out.write(String.format("fixedStep chrom=%s start=%d step=1 span=1\n", chr, start));
			state = State.AFTER_HEADER;
		}

		public void flush() throws IOException {
			this.out.flush();
		}

		public void reportDepth(String chr, int start, int end, int depth) throws IOException {

			// skip leading zeros
			if (state == State.LEADING_ZEROs) {
				if (depth == 0)
					return;
				else
					outputHeader(chr, start);
			}

			// output data entry
			for (int i = start; i < end; ++i) {
				out.write(Integer.toString(depth));
				out.write("\n");
			}
		}

	}

}
