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
// EnhancedGeneticVariationTest.java
// Since: 2010/11/10
//
//--------------------------------------
package org.utgenome.util.sv;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class EnhancedGeneticVariationTest {

	@Test
	public void create() throws Exception {
		GeneticVariation v = new GeneticVariation("chr1", 10000, "+ATTT");
		EnhancedGeneticVariation ev = new EnhancedGeneticVariation(v);

		assertEquals(4, ev.indelLength);
		assertEquals("ATTT", ev.getGenotype().substring(1));
		assertEquals(GeneticVariation.VariationType.Insertion, v.variationType);
		assertEquals(10000, ev.start);

	}
}
