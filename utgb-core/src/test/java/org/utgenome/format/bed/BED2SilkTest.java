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
import org.utgenome.gwt.utgb.client.bio.CDS;
import org.xerial.ObjectHandlerBase;
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
		_logger.debug(s);

		Lens.findFromSilk(new StringReader(s), "track", BEDTrack.class, new ObjectHandlerBase<BEDTrack>() {

			public void handle(BEDTrack input) throws Exception {
				//track name="Item,RGB,Demo2" description="Item RGB demonstration2" visibility=2 itemRgb="On" useScore=1	color=0,128,0 url="http://genome.ucsc.edu/goldenPath/help/clones.html#$$"
				assertEquals("Item,RGB,Demo2", input.name);
				assertEquals("Item RGB demonstration2", input.description);
				assertEquals(2, input.visibility);
				assertEquals("On", input.itemRgb);
				assertEquals("http://genome.ucsc.edu/goldenPath/help/clones.html#$$", input.url);
				assertEquals(1, input.useScore);
				assertEquals("0,128,0", input.color);
			}

		});

		Lens.findFromSilk(new StringReader(s), "gene", BEDEntry.class, new ObjectHandler<BEDEntry>() {

			int geneCount = 0;

			public void init() throws Exception {
				geneCount = 0;
			}

			public void handle(BEDEntry g) throws Exception {
				if (g.getName().equals("Pos1")) {
					assertEquals("chr7", g.coordinate);
					assertEquals(127471197, g.getStart());
					assertEquals(127472364, g.getEnd());
					assertEquals('+', g.getStrand());
					assertEquals("#ff0000", g.getColor());
					assertEquals(300, g.score);
					assertEquals(1, g.getCDS().size());
					CDS cds = g.getCDS().get(0);
					// 127471196 127472363 (BED is 0-origin)
					assertEquals(127471197, cds.getStart());
					assertEquals(127472364, cds.getEnd());
					geneCount++;
				}
				else if (g.getName().equals("Pos2")) {
					//127472363	127473530	Pos2	200	+	127472363	127473530	255,0,0
					assertEquals("chr7", g.coordinate);
					assertEquals(127472364, g.getStart());
					assertEquals(127473531, g.getEnd());
					assertEquals('+', g.getStrand());
					assertEquals("#ffff00", g.getColor());
					// 2	200	+	127472363	127473530	
					assertEquals(200, g.score);
					assertEquals(1, g.getCDS().size());
					CDS cds = g.getCDS().get(0);
					// 2	200	+	127472363	127473530	
					assertEquals(127472364, cds.getStart());
					assertEquals(127473531, cds.getEnd());

					geneCount++;
				}
			}

			public void finish() throws Exception {
				assertEquals(2, geneCount);
			}

		});

	}

	@Test
	public void intervalList() throws Exception {
		BED2Silk b2s = new BED2Silk(FileResource.open(BED2SilkTest.class, "intervallist.bed"));
		String s = b2s.toSilk();
		_logger.debug(s);

		Lens.findFromSilk(new StringReader(s), "track", BEDTrack.class, new ObjectHandlerBase<BEDTrack>() {

			public void handle(BEDTrack input) throws Exception {
				assertEquals("HCT116_H4(K5/8/12/16)_Ac", input.name);
			}

		});

		Lens.findFromSilk(new StringReader(s), "gene", BEDEntry.class, new ObjectHandler<BEDEntry>() {
			int geneCount = 0;

			public void init() throws Exception {

			}

			public void handle(BEDEntry input) throws Exception {
				geneCount++;
			}

			public void finish() throws Exception {
				assertEquals(416, geneCount);
			}
		});

	}

	@Test
	public void testForErroneousBED() throws Exception {

		// proceeds parsing even if the data contain some errors
		BED2Silk b2s = new BED2Silk(FileResource.open(BED2SilkTest.class, "test_for_error.bed"));
		String s = b2s.toSilk();
		_logger.debug(s);

		Lens.findFromSilk(new StringReader(s), "track", BEDTrack.class, new ObjectHandlerBase<BEDTrack>() {

			public void handle(BEDTrack input) throws Exception {
				//track name="Item,RGB,Demo2" description="Item RGB demonstration2" visibility=2 itemRgb="On" useScore=1	color=0,128,0 url="http://genome.ucsc.edu/goldenPath/help/clones.html#$$"
				assertEquals("Item,RGB,Demo2", input.name);
				assertEquals("Item RGB demonstration2", input.description);
				assertEquals(2, input.visibility);
				assertEquals("On", input.itemRgb);
				assertEquals("http://genome.ucsc.edu/goldenPath/help/clones.html#$$", input.url);
				assertEquals(1, input.useScore);
				assertEquals("0,128,0", input.color);
			}

		});

		Lens.findFromSilk(new StringReader(s), "gene", BEDEntry.class, new ObjectHandler<BEDEntry>() {

			int geneCount = 0;

			public void init() throws Exception {

			}

			public void handle(BEDEntry g) throws Exception {
				if (g.getName().equals("AF071353.1")) {
					assertEquals("chrIV", g.coordinate);
					assertEquals(17339775, g.getStart());
					assertEquals(17339830, g.getEnd());
					assertEquals('-', g.getStrand());
					assertEquals(null, g.getColor());
					assertEquals(2, g.score);
					assertEquals(0, g.getCDS().size());
					geneCount++;
				}
				else if (g.getName().equals("AF071356.1")) {
					assertEquals("chrV", g.coordinate);
					assertEquals(15922528, g.getStart());
					assertEquals(15922545, g.getEnd());
					assertEquals('+', g.getStrand());
					assertEquals(null, g.getColor());
					assertEquals(1, g.score);
					assertEquals(0, g.getCDS().size());
					geneCount++;
				}
			}

			public void finish() throws Exception {
				assertEquals(2, geneCount);
			}
		});

	}

}
