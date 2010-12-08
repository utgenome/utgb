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
// NucleotideVariationTest.java
// Since: 2010/10/13
//
//--------------------------------------
package org.utgenome.util.sv;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xerial.lens.SilkLens;
import org.xerial.util.FileResource;
import org.xerial.util.ObjectHandler;
import org.xerial.util.log.Logger;

public class GeneticVariationTest {

	private static Logger _logger = Logger.getLogger(GeneticVariationTest.class);

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void mutation() throws Exception {
		GeneticVariation v = new GeneticVariation("chr1", 3, "A");
		assertEquals(GeneticVariation.VariationType.Mutation, v.variationType);
		assertEquals(0, v.indelLength);
		assertEquals("A", v.getGenotype());
	}

	@Test
	public void indel() throws Exception {
		GeneticVariation v = new GeneticVariation("chr1", 10000, "+ATTT");
		assertEquals(GeneticVariation.VariationType.Insertion, v.variationType);
		assertEquals(4, v.indelLength);
		assertEquals("ATTT", v.getGenotype().substring(1));

		GeneticVariation v2 = new GeneticVariation("chr1", 10000, "-CGCGCG");
		assertEquals(GeneticVariation.VariationType.Deletion, v2.variationType);
		assertEquals(6, v2.indelLength);
		assertEquals("CGCGCG", v2.getGenotype().substring(1));

	}

	@Test
	public void testVar() throws Exception {
		SilkLens.findFromSilk(FileResource.open(GeneticVariationTest.class, "var_input.silk"), "variation", GeneticVariation.class,
				new ObjectHandler<GeneticVariation>() {

					public void init() throws Exception {

					}

					public void handle(GeneticVariation input) throws Exception {
						_logger.debug(input);

					}

					public void finish() throws Exception {

					}
				});
	}

}
