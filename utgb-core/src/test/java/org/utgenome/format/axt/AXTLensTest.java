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
// AXTLensTest.java
// Since: 2010/12/07
//
//--------------------------------------
package org.utgenome.format.axt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashSet;

import org.junit.Test;
import org.xerial.ObjectHandlerBase;
import org.xerial.util.FileResource;
import org.xerial.util.log.Logger;

public class AXTLensTest {
	private static Logger _logger = Logger.getLogger(AXTLensTest.class);

	@Test
	public void lens() throws Exception {
		AXTLens.lens(FileResource.open(AXTLensTest.class, "sample.axt"), new ObjectHandlerBase<AXTAlignment>() {
			HashSet<Integer> idSet = new HashSet<Integer>();

			boolean initialized = false;

			@Override
			public void init() throws Exception {
				initialized = true;
			}

			public void handle(AXTAlignment input) throws Exception {
				idSet.add(input.num);
				switch (input.num) {
				case 0:
					assertEquals("scaffold1", input.s_chr);
					assertEquals(5, input.s_start);
					assertEquals(314, input.s_end);
					assertEquals("scaffold10479", input.d_chr);
					assertEquals(9913, input.d_start);
					assertEquals(10221, input.d_end);
					assertEquals("+", input.strand);
					assertEquals(27771, input.score);
					break;
				case 1:
					assertEquals("scaffold1", input.s_chr);
					assertEquals(315, input.s_start);
					assertEquals(1466, input.s_end);
					assertEquals("scaffold10479", input.d_chr);
					assertEquals(11591, input.d_start);
					assertEquals(12713, input.d_end);
					assertEquals("+", input.strand);
					assertEquals(92583, input.score);
					break;
				case 2:
					assertEquals("scaffold1", input.s_chr);
					assertEquals(1763, input.s_start);
					assertEquals(4073, input.s_end);
					assertEquals("scaffold10479", input.d_chr);
					assertEquals(13272, input.d_start);
					assertEquals(15568, input.d_end);
					assertEquals("+", input.strand);
					assertEquals(190211, input.score);
					break;
				case 3:
					assertEquals("scaffold1", input.s_chr);
					assertEquals(4153, input.s_start);
					assertEquals(5023, input.s_end);
					assertEquals("scaffold10479", input.d_chr);
					assertEquals(15910, input.d_start);
					assertEquals(16771, input.d_end);
					assertEquals("+", input.strand);
					assertEquals(71896, input.score);
					break;
				default:
					fail("");
				}
			}

			@Override
			public void finish() throws Exception {
				for (int i = 0; i <= 3; ++i) {
					assertTrue(idSet.contains(i));
				}
				assertTrue("init() has not been called", initialized);
			}

		});
	}

}
