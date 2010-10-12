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
// IntervalTreeTest.java
// Since: 2010/10/12
//
//--------------------------------------
package org.utgenome.gwt.utgb.canvas;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.utgenome.gwt.utgb.client.bio.Read;
import org.utgenome.gwt.utgb.client.canvas.IntervalTree;

public class IntervalTreeTest {

	IntervalTree<Read> t;
	final static Read A = new Read("A", 1, 2);
	final static Read B = new Read("B", 2, 4);
	final static Read C = new Read("C", 1, 8);
	final static Read D = new Read("D", 3, 3);
	final static Read E = new Read("E", 5, 6);

	@Before
	public void setUp() throws Exception {
		t = new IntervalTree<Read>();
		t.add(A);
		t.add(B);
		t.add(C);
		t.add(D);
		t.add(E);
	}

	@Test
	public void query() throws Exception {
		List<Read> r = t.overlapQuery(2, 5); // [2, 5)
		assertTrue(!r.contains(A));
		assertTrue(r.contains(B));
		assertTrue(r.contains(C));
		assertTrue(r.contains(D));
		assertTrue(!r.contains(E));
	}

	@Test
	public void query2() throws Exception {
		List<Read> r = t.overlapQuery(4, 10);
		assertTrue(!r.contains(A));
		assertTrue(!r.contains(B));
		assertTrue(r.contains(C));
		assertTrue(!r.contains(D));
		assertTrue(r.contains(E));
	}

	@Test
	public void sweepBefore() throws Exception {
		t.removeBefore(2);
		List<Read> r = t.elementList();
		assertTrue(!r.contains(A));
		assertTrue(r.contains(B));
		assertTrue(r.contains(C));
		assertTrue(r.contains(D));
		assertTrue(r.contains(E));
	}

	@Test
	public void sweepBefore2() throws Exception {
		t.removeBefore(5);
		List<Read> r = t.elementList();
		assertTrue(!r.contains(A));
		assertTrue(!r.contains(B));
		assertTrue(r.contains(C));
		assertTrue(!r.contains(D));
		assertTrue(r.contains(E));
	}

}
