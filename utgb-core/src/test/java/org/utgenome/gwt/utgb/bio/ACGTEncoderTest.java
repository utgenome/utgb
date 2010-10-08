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
// ACGTEncoderTest.java
// Since: 2010/10/08
//
//--------------------------------------
package org.utgenome.gwt.utgb.bio;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.utgenome.gwt.utgb.client.bio.ACGTEncoder;

public class ACGTEncoderTest {

	@Test
	public void encode() throws Exception {
		assertEquals(0, ACGTEncoder.to2bitCode('A'));
		assertEquals(1, ACGTEncoder.to2bitCode('C'));
		assertEquals(2, ACGTEncoder.to2bitCode('G'));
		assertEquals(3, ACGTEncoder.to2bitCode('T'));
		assertEquals(3, ACGTEncoder.to2bitCode('U'));
		assertEquals(4, ACGTEncoder.to2bitCode('N'));

		assertEquals(0, ACGTEncoder.to2bitCode('a'));
		assertEquals(1, ACGTEncoder.to2bitCode('c'));
		assertEquals(2, ACGTEncoder.to2bitCode('g'));
		assertEquals(3, ACGTEncoder.to2bitCode('t'));
		assertEquals(3, ACGTEncoder.to2bitCode('u'));
		assertEquals(4, ACGTEncoder.to2bitCode('n'));
	}

	@Test
	public void toBase() throws Exception {
		assertEquals('A', ACGTEncoder.toBase(0));
		assertEquals('C', ACGTEncoder.toBase(1));
		assertEquals('G', ACGTEncoder.toBase(2));
		assertEquals('T', ACGTEncoder.toBase(3));
		assertEquals('N', ACGTEncoder.toBase(4));
	}

	@Test
	public void encodeSeq() throws Exception {

		assertEquals(0, ACGTEncoder.toKmerInt(2, "AA"));
		assertEquals(1, ACGTEncoder.toKmerInt(2, "AC"));
		assertEquals(2, ACGTEncoder.toKmerInt(2, "AG"));
		assertEquals(3, ACGTEncoder.toKmerInt(2, "AT"));
		assertEquals(4, ACGTEncoder.toKmerInt(2, "CA"));
		assertEquals(5, ACGTEncoder.toKmerInt(2, "CC"));
		assertEquals(6, ACGTEncoder.toKmerInt(2, "CG"));
		assertEquals(7, ACGTEncoder.toKmerInt(2, "CT"));

	}

}
