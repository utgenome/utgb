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
// SAMReadSweepIterator.java
// Since: 2010/10/12
//
//--------------------------------------
package org.utgenome.format.sam;

import java.util.Iterator;

import org.utgenome.gwt.utgb.client.bio.OnGenome;
import org.utgenome.gwt.utgb.client.canvas.IntervalTree;
import org.xerial.util.log.Logger;

/**
 * Sweeping SAM reads in the start order
 * 
 * @author leo
 * 
 */
public class ReadSweeper {

	private static Logger _logger = Logger.getLogger(ReadSweeper.class);

	private IntervalTree<OnGenome> readSet = new IntervalTree<OnGenome>();
	private int sweepLine = 1;
	private long readCount = 0;

	public static interface ReadSetHandler {
		public void handle(int sweepLine, Iterable<OnGenome> readSet);
	}

	public void sweep(Iterator<OnGenome> cursor, ReadSetHandler handler) {

		readSet.clear();
		sweepLine = 0;
		readCount = 0;

		// assume that read data are sorted in the start order
		for (; cursor.hasNext();) {
			readCount++;

			if (readCount != 0 && (readCount % 1000000) == 0) {
				_logger.info(String.format("processed %,d reads", readCount));
			}

			OnGenome read = cursor.next();
			if (sweepLine < read.getStart()) {
				// we can sweep reads up to sweepEnd
				int sweepEnd = read.getStart();
				sweepUpto(sweepEnd, handler);
			}
			readSet.add(read);
		}

		if (!readSet.isEmpty()) {
			final int maxReadEnd = maxReadEnd(readSet);
			sweepUpto(maxReadEnd, handler);
		}
	}

	private static int maxReadEnd(Iterable<OnGenome> readSet) {
		int maxEnd = -1;
		for (OnGenome each : readSet) {
			if (maxEnd < each.getEnd())
				maxEnd = each.getEnd();
		}
		return maxEnd;
	}

	private void sweepUpto(int sweepEnd, ReadSetHandler handler) {
		for (int i = sweepLine; i < sweepEnd; i++) {
			handler.handle(i, readSet);
			readSet.removeBefore(i);
		}
		sweepLine = sweepEnd;
	}
}
