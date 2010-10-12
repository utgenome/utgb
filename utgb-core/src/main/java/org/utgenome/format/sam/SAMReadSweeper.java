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

import net.sf.samtools.SAMRecord;
import net.sf.samtools.SAMRecordIterator;

import org.utgenome.gwt.utgb.client.bio.Interval;
import org.xerial.util.ArrayDeque;
import org.xerial.util.Deque;
import org.xerial.util.log.Logger;

/**
 * Sweeping SAM reads in the start order
 * 
 * @author leo
 * 
 */
public class SAMReadSweeper {

	private static Logger _logger = Logger.getLogger(SAMReadSweeper.class);

	private Deque<Interval> readSetInStartOrder = new ArrayDeque<Interval>();
	private String currentChr;

	public static interface ReadSetHandler {
		public void handle(Deque<Interval> readSetInStartOrder);
	}

	static final int maxReadEnd(Collection<Interval> readSet) {
		int maxEnd = -1;
		for (Interval each : readSet) {
			if (maxEnd < each.getEnd())
				maxEnd = each.getEnd();
		}
		return maxEnd;
	}

	public void sweep(SAMRecordIterator cursor, ReadSetHandler handler) {
		
		int sweepLine = 1;
		long readCount = 0;

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
					final int maxReadEnd = maxReadEnd(readSetInStartOrder);
					for(int i=sweepLine; i<maxReadEnd; i++) {
						handler.handle(readSetInStartOrder)
					}
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
}
