/*--------------------------------------------------------------------------
 *  Copyright 2008 utgenome.org
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
// IntervalLayout.java
// Since: Sep 10, 2008
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.canvas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

class IntervalLocation<T> {
	T interval;
	int y;

	public IntervalLocation(T interval, int y) {
		this.interval = interval;
		this.y = y;
	}
}

/**
 * layout of intervals
 * 
 * @author leo
 * 
 */
public class IntervalLayout<T extends Interval> {

	private ArrayList<IntervalLocation<T>> intervalLayoutList = new ArrayList<IntervalLocation<T>>();
	private final int intervalHeight;
	private int canvasHeight = 0;

	public IntervalLayout(int intervalHeight, List<T> intervalList) {
		this.intervalHeight = intervalHeight;
		layout(intervalList);
	}

	void layout(List<T> intervalList) {
		Collections.sort(intervalList, new OrderByStartThenLength<T>());

		for (T interval : intervalList) {
			layout(intervalLayoutList, interval);
		}

	}

	private void layout(List<IntervalLocation<T>> currentLayoutOrderedByStart, T newInterval) {

		TreeSet<Integer> unavailableY = new TreeSet<Integer>();
		for (IntervalLocation<T> each : currentLayoutOrderedByStart) {
			if (newInterval.intersectsWith(each.interval)) {
				unavailableY.add(each.y);
			}
		}

		int y = 0;
		for (;;) {
			if (unavailableY.contains(y))
				y += intervalHeight;
			else
				break;
		}

		currentLayoutOrderedByStart.add(new IntervalLocation<T>(newInterval, y));
		if (y > canvasHeight)
			canvasHeight = y;

	}

	public List<IntervalLocation<T>> getIntervalLayoutList() {
		return intervalLayoutList;
	}

	public int getCanvasHeight() {
		return canvasHeight;
	}

	public static class OrderByStartThenLength<T extends Interval> implements Comparator<T> {
		public int compare(T o1, T o2) {

			int diff = o1.start - o2.start;
			if (diff != 0)
				return diff;
			else
				return o1.length() - o2.length();
		}

	}

}
