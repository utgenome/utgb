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
// DisjointSet.java
// Since: 2010/10/19
//
//--------------------------------------
package org.utgenome.util.repeat;

import java.util.ArrayList;
import java.util.List;

import org.xerial.util.IndexedSet;

/**
 * Union-Find disjoint set implementation
 * 
 * @author leo
 * 
 */
public class DisjointSet<E> {

	private IndexedSet<E> elementIndex = new IndexedSet<E>();

	private List<Integer> parentID = new ArrayList<Integer>();
	private List<Integer> rank = new ArrayList<Integer>();

	/**
	 * @param element
	 */
	public void add(E element) {
		boolean isNewElement = elementIndex.add(element);
		if (isNewElement) {
			int id = elementIndex.getID(element);
			rank.add(0);
		}
	}

	public void union(E x, E y) {
		linkByID(find(x), find(y));
	}

	public void link(E x, E y) {
		int xID = elementIndex.getID(x);
		int yID = elementIndex.getID(y);

		linkByID(xID, yID);
	}

	private void linkByID(int xID, int yID) {
		if (rank.get(xID) > rank.get(yID)) {
			parentID.set(yID, xID);
		}
		else {
			parentID.set(xID, yID);
			if (rank.get(xID) == rank.get(yID))
				rank.set(yID, rank.get(yID) + 1);
		}
	}

	public int find(E x) {
		return findByID(elementIndex.getID(x));
	}

	private int findByID(int id) {
		if (id != parentID.get(id))
			parentID.set(id, findByID(parentID.get(id)));
		return parentID.get(id);
	}

}
