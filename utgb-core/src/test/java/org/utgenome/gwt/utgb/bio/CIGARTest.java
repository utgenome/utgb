/*--------------------------------------------------------------------------
 *  Copyright 2009 utgenome.org
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
// CIGARTest.java
// Since: Mar 15, 2010
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.bio;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.utgenome.gwt.utgb.client.bio.CIGAR;
import org.utgenome.gwt.utgb.client.bio.CIGAR.Type;

public class CIGARTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void parse() throws Exception {
		CIGAR c = new CIGAR("5M24I9M62S");
		assertEquals(4, c.size());
		CIGAR.Element e = c.get(0);
		assertEquals(5, e.length);
		assertEquals(CIGAR.Type.Matches, e.type);

		e = c.get(1);
		assertEquals(24, e.length);
		assertEquals(CIGAR.Type.Insertions, e.type);

		e = c.get(2);
		assertEquals(9, e.length);
		assertEquals(CIGAR.Type.Matches, e.type);

		e = c.get(3);
		assertEquals(62, e.length);
		assertEquals(CIGAR.Type.SoftClip, e.type);

	}

	@Test
	public void construct() throws Exception {
		CIGAR c = new CIGAR();
		c.add(10, Type.SoftClip);
		c.add(5, Type.Matches);
		c.add(10, Type.Insertions);
		c.add(2, Type.Matches);
		c.add(3, Type.Deletions);
		c.add(43, Type.SkippedRegion);
		c.add(100, Type.Matches);
		c.add(1, Type.Padding);
		c.add(134, Type.HardClip);

		assertEquals(9, c.size());
		assertEquals("10S5M10I2M3D43N100M1P134H", c.toCIGARString());
		assertEquals("10S5M10I2M3D43N100M1P134H", c.toString());
	}

}
