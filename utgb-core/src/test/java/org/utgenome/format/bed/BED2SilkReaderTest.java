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
// BED2SilkReaderTest.java
// Since: 2009/05/27
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.format.bed;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xerial.lens.Lens;
import org.xerial.util.FileResource;
import org.xerial.util.log.Logger;

public class BED2SilkReaderTest {

	private static Logger _logger = Logger.getLogger(BED2SilkReaderTest.class);

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGen() throws Exception {
		BED2SilkReader r = new BED2SilkReader(FileResource.open(BED2SilkReaderTest.class, "sample.bed"));
		Lens.loadSilk(new BEDQuery() {
			public void addGene(BEDGene gene) {
				_logger.info(Lens.toSilk(gene));
			}

			public void addTrack(BEDTrack track) {
				_logger.info(Lens.toSilk(track));
			}

			public void reportError(Exception e) {
				fail(e.getMessage());
			}
		}, r);
	}

}
