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
// IntervalTest.java
// Since: Sep 11, 2008
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.utgenome.gwt.utgb.client.bio.Interval;

public class IntervalTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void constructor() {
		Interval i = new Interval(10, 200);
		assertEquals(10, i.getStart());
		assertEquals(200, i.getEnd());
	}

	@Test
	public void intersect() {
		Interval i = new Interval(10, 20);
		Interval i2 = new Interval(15, 30);
		Interval i3 = new Interval(5, 8);
		Interval i4 = new Interval(9, 10);
		Interval i5 = new Interval(0, 100);

		assertEquals(true, i.intersectsWith(i));
		assertEquals(true, i3.intersectsWith(i3));

		assertEquals(true, i.intersectsWith(i2));
		assertEquals(true, i2.intersectsWith(i));
		assertEquals(false, i.intersectsWith(i3));
		assertEquals(false, i3.intersectsWith(i));

		assertEquals(true, i.intersectsWith(i4));
		assertEquals(true, i4.intersectsWith(i));

		assertEquals(true, i.intersectsWith(i5));
		assertEquals(true, i5.intersectsWith(i));

	}

	@Test
	public void contains() {
		Interval i = new Interval(10, 20);
		Interval i2 = new Interval(15, 30);
		Interval i3 = new Interval(5, 8);
		Interval i4 = new Interval(9, 10);
		Interval i5 = new Interval(0, 100);

		assertEquals(false, i.contains(i2));
		assertEquals(false, i.contains(i3));
		assertEquals(false, i.contains(i4));
		assertEquals(false, i.contains(i5));
		assertEquals(true, i5.contains(i));

		assertEquals(true, i.contains(i));
	}
}
