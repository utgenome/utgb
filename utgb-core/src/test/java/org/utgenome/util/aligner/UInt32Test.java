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
// UInt32Test.java
// Since: 2010/10/28
//
//--------------------------------------
package org.utgenome.util.aligner;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.xerial.util.log.Logger;

public class UInt32Test {

	private static Logger _logger = Logger.getLogger(UInt32Test.class);

	@Test
	public void construct() throws Exception {

		UInt32 a = new UInt32(0xFFFFFFFF);
		assertEquals(4294967295L, a.toLong());
		UInt32 b = new UInt32(0x7FFFFFFF);
		assertEquals(2147483647L, b.toLong());
		UInt32 c = new UInt32(1);
		assertEquals(1, c.toLong());

	}

}
