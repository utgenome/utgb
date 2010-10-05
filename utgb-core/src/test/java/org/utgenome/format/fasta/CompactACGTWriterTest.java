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
// CompactACGTWriterTest.java
// Since: Feb 22, 2010
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.format.fasta;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CompactACGTWriterTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void genTable() {
		CompactACGTWriter.generateCharTo2BitACGTTable();
	}

	@Test
	public void to2bitindex() throws Exception {
		assertEquals(0, CompactACGTWriter.to2bitCode('A'));
		assertEquals(1, CompactACGTWriter.to2bitCode('C'));
		assertEquals(2, CompactACGTWriter.to2bitCode('G'));
		assertEquals(3, CompactACGTWriter.to2bitCode('T'));
		assertEquals(0, CompactACGTWriter.to2bitCode('a'));
		assertEquals(1, CompactACGTWriter.to2bitCode('c'));
		assertEquals(2, CompactACGTWriter.to2bitCode('g'));
		assertEquals(3, CompactACGTWriter.to2bitCode('t'));

		assertEquals(4, CompactACGTWriter.to2bitCode('N'));

	}

}
