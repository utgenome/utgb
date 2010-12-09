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
// ReadDepth.java
// Since: 2010/12/09
//
//--------------------------------------
package org.utgenome.util;

import java.util.Iterator;
import java.util.PriorityQueue;

import org.utgenome.gwt.utgb.client.bio.Interval;
import org.utgenome.gwt.utgb.client.bio.OnGenome;
import org.utgenome.gwt.utgb.client.canvas.IntervalTree;
import org.xerial.util.log.Logger;

/**
 * Read depth computation in O(2N) time.
 * 
 * @author leo
 * 
 */
public class ReadDepth {

	private static Logger _logger = Logger.getLogger(ReadDepth.class);

	private int startCursor = 0;
	private int readCount = 0;
	private IntervalTree<Interval> intervals = new IntervalTree<Interval>();

	private PriorityQueue<Integer> boundary = new PriorityQueue<Integer>();
	private int currentDepth = 0;

	private final DepthOutput out;

	protected ReadDepth(DepthOutput out) {
		this.out = out;
	}

	public static interface DepthOutput {
		public void reportDepth(String chr, int start, int end, int depth) throws Exception;
	}

	/**
	 * Compute the read depth of the input read set. The read set must be sorted by start order.
	 * 
	 * @param cursor
	 * @param out
	 * @throws Exception
	 */
	public static void compute(String chr, Iterator<OnGenome> cursor, DepthOutput out) throws Exception {
		new ReadDepth(out).computeDepth(chr, cursor);
	}

	protected void computeDepth(String chr, Iterator<OnGenome> cursor) throws Exception {

		for (; cursor.hasNext();) {
			OnGenome read = cursor.next();
			readCount++;

			if (_logger.isDebugEnabled() && readCount > 0 && (readCount % 10000) == 0) {
				_logger.debug(String.format("processed %d reads", readCount));
			}

			int start = read.getStart();
			int end = read.getEnd();
			boundary.add(end);

			for (; !boundary.isEmpty();) {
				int readEnd = boundary.peek();
				if (readEnd > start)
					break;

				reportDepth(chr, startCursor, readEnd, currentDepth);
				currentDepth--;
				startCursor = readEnd;
				boundary.poll();
			}

			reportDepth(chr, startCursor, start, currentDepth);
			startCursor = start;
			currentDepth++;
		}

		for (; !boundary.isEmpty();) {
			int readEnd = boundary.peek();
			reportDepth(chr, startCursor, readEnd, currentDepth);
			currentDepth--;
			startCursor = readEnd;
			boundary.poll();
		}

	}

	private void reportDepth(String chr, int start, int end, int depth) throws Exception {
		out.reportDepth(chr, start, end, depth);
	}

}
