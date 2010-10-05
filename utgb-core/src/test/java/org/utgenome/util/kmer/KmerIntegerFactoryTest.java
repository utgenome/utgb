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
// KmerIntegerFactoryTest.java
// Since: 2010/10/05
//
//--------------------------------------
package org.utgenome.util.kmer;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class KmerIntegerFactoryTest {
	@Test
	public void factory() throws Exception {
		KmerIntegerFactory f = new KmerIntegerFactory(4);

		assertEquals(0x1B, f.parseString("ACGT"));
		assertEquals(0x00, f.parseString("AAAA"));

		assertEquals("TTTT", f.toString(f.reverseComplement(f.parseString("AAAA"))));
		assertEquals("ACGT", f.toString(f.reverseComplement(f.parseString("ACGT"))));
		assertEquals("ATTT", f.toString(f.reverseComplement(f.parseString("AAAT"))));

	}
}
