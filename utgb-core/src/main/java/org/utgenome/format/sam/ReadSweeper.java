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

import java.util.Collection;
import java.util.Iterator;

import org.utgenome.gwt.utgb.client.bio.GenomeRange;
import org.utgenome.gwt.utgb.client.canvas.IntervalTree;
import org.xerial.util.log.Logger;

/**
 * Sweeping SAM reads in their start order
 * 
 * @author leo
 * 
 */
public class ReadSweeper<T extends GenomeRange> {

	private static Logger _logger = Logger.getLogger(ReadSweeper.class);

	private IntervalTree<T> readSet = new IntervalTree<T>();
	private int sweepLine = 1;
	private long readCount = 0;

	public interface ReadSetHandler<T extends GenomeRange> {
		public void handle(int sweepLine, Collection<T> readSet);
	}

	public void sweep(Iterator<T> cursor, ReadSetHandler<T> handler) {

		readSet.clear();
		sweepLine = 1;
		readCount = 0;

		// assume that read data are sorted in the start order
		for (; cursor.hasNext();) {
			readCount++;

			if ((readCount % 1000000) == 0) {
				_logger.info(String.format("processed %,d reads", readCount));
			}

			T read = cursor.next();
			int readStart = read.getStart();
			if (sweepLine < readStart) {
				// we can sweep reads up to sweepEnd
				sweepUpto(readStart, handler);
			}
			readSet.add(read);
		}

		if (!readSet.isEmpty()) {
			sweepUpto(maxReadEnd(readSet), handler);
		}
	}

	private int maxReadEnd(Iterable<T> readSet) {
		int maxEnd = -1;
		for (GenomeRange each : readSet) {
			if (maxEnd < each.getEnd())
				maxEnd = each.getEnd();
		}
		return maxEnd;
	}

	private void sweepUpto(int sweepEnd, ReadSetHandler<T> handler) {
		for (; sweepLine < sweepEnd; sweepLine++) {
			handler.handle(sweepLine, readSet);
			readSet.removeBefore(sweepLine);
		}
	}
}
