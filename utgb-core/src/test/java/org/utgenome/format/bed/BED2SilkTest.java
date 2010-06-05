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
// BED2SilkTest.java
// Since: 2009/05/19
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.format.bed;

import static org.junit.Assert.assertEquals;

import java.io.StringReader;

import org.junit.Test;
import org.xerial.lens.Lens;
import org.xerial.lens.ObjectHandler;
import org.xerial.util.FileResource;
import org.xerial.util.log.Logger;

public class BED2SilkTest {
	private static Logger _logger = Logger.getLogger(BED2SilkTest.class);

	@Test
	public void test() throws Exception {
		BED2Silk b2s = new BED2Silk(FileResource.open(BED2SilkTest.class, "small.bed"));
		String s = b2s.toSilk();
		_logger.info(s);
		Lens.findFromSilk(new StringReader(s), "gene", BEDGene.class, new ObjectHandler<BEDGene>() {
			public void handle(BEDGene g) throws Exception {
				if (g.getName().equals("Pos1")) {
					assertEquals("chr7", g.coordinate);
					assertEquals(127471197, g.getStart());
					assertEquals(127472364, g.getEnd());
					assertEquals('+', g.getStrand());
					assertEquals("#ff0000", g.getColor());
				}
				else if (g.getName().equals("Pos2")) {
					//127472363	127473530	Pos2	200	+	127472363	127473530	255,0,0
					assertEquals("chr7", g.coordinate);
					assertEquals(127472364, g.getStart());
					assertEquals(127473531, g.getEnd());
					assertEquals('+', g.getStrand());
					assertEquals("#ff0000", g.getColor());
				}
			}

		});

	}
}
