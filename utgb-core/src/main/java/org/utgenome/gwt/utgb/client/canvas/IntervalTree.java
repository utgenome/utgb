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

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.utgenome.gwt.utgb.client.bio.OnGenome;
import org.utgenome.gwt.utgb.client.util.Optional;

/**
 * Interval layout
 * 
 * @author leo
 * 
 */
public class IntervalTree<T extends OnGenome> extends AbstractCollection<T> {

	private PrioritySearchTree<T> pst = new PrioritySearchTree<T>();

	/**
	 * Add an entry [start, end)
	 * 
	 * @param elem
	 */
	@Override
	public boolean add(T elem) {
		// swap start and end when inserting to PST
		pst.insert(elem, elem.getEnd(), elem.getStart());
		return true;
	}

	public boolean remove(T t) {
		return pst.remove(t, t.getEnd(), t.getStart());
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

	public void overlapQuery(OnGenome target, PrioritySearchTree.ResultHandler<T> handler) {
		overlapQuery(target.getStart(), target.getEnd(), handler);
	}

	public List<T> overlapQuery(OnGenome target) {
		return overlapQuery(target.getStart(), target.getEnd());
	}

	public T findOverlap(OnGenome target) {
		final Optional<T> result = new Optional<T>();
		this.overlapQuery(target, new PrioritySearchTree.ResultHandler<T>() {

			boolean toContinue = true;

			public void handle(T overlappedEntry) {
				result.set(overlappedEntry);
				toContinue = false;
			}

			public boolean toContinue() {
				return toContinue;
			}
		});
		return result.isDefined() ? result.get() : null;
	}

	@Override
	public boolean isEmpty() {
		return pst.size() == 0;
	}

	@Override
	public void clear() {
		pst.clear();
	}

	/**
	 * Removes the intervals contained in [-oo, start)
	 * 
	 * @param start
	 */
	public void removeBefore(int start) {
		removeBefore(start, null);
	}

	public void removeBefore(int start, PrioritySearchTree.ResultHandler<T> handler) {
		for (T each : pst.rangeQuery(Integer.MIN_VALUE, start, start - 1)) {
			if (handler != null)
				handler.handle(each);
			pst.remove(each, each.getEnd(), each.getStart());
		}
	}

	@Override
	public Iterator<T> iterator() {
		return pst.iterator();
	}

	/**
	 * return the elements in the tree
	 * 
	 * @return
	 */
	public List<T> elementList() {
		List<T> r = new ArrayList<T>(pst.size());
		for (T each : pst) {
			r.add(each);
		}
		return r;
	}

	@Override
	public int size() {
		return pst.size();
	}
}
