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
// IntervalTree.java
// Since: 2010/10/12
//
//--------------------------------------
package org.utgenome.gwt.utgb.client.canvas;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.utgenome.gwt.utgb.client.bio.OnGenome;

public class IntervalTree<T extends OnGenome> implements Iterable<T> {

	private PrioritySearchTree<T> pst = new PrioritySearchTree<T>();

	/**
	 * Add an entry [start, end)
	 * 
	 * @param elem
	 */
	public void add(T elem) {
		// swap start and end when inserting to PST
		pst.insert(elem, elem.getEnd(), elem.getStart());
	}

	/**
	 * Get entries overlapping with [start, end)
	 * 
	 * @param start
	 *            query start (inclusive)
	 * @param end
	 *            query end (exclusive)
	 * @return
	 */
	public List<T> overlapQuery(int start, int end) {
		return pst.rangeQuery(start + 1, Integer.MAX_VALUE, end - 1);
	}

	/**
	 * Get entries overlapping with [start, end)
	 * 
	 * @param start
	 *            query start (inclusive)
	 * @param end
	 *            query end (exclusive)
	 * @param handler
	 */
	public void overlapQuery(int start, int end, PrioritySearchTree.ResultHandler<T> handler) {
		pst.rangeQuery(start + 1, Integer.MAX_VALUE, end - 1, handler);
	}

	/**
	 * Removes the intervals contained in [-oo, start)
	 * 
	 * @param start
	 */
	public void removeBefore(int start) {
		for (T each : pst.rangeQuery(Integer.MIN_VALUE, start, start)) {
			pst.remove(each, each.getEnd(), each.getStart());
		}
	}

	public Iterator<T> iterator() {
		return pst.iterator();
	}

	public List<T> elementList() {
		List<T> r = new ArrayList<T>(pst.size());
		for (T each : pst) {
			r.add(each);
		}
		return r;
	}
}
