/*--------------------------------------------------------------------------
 *  Copyright 2011 utgenome.org
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
// ReadCanvasTest.java
// Since: 2011/01/06
//
//--------------------------------------
package org.utgenome.graphics;

import java.io.File;
import java.util.List;

import org.junit.Test;
import org.utgenome.format.sam.SAMReader;
import org.utgenome.graphics.ReadCanvas.DrawStyle;
import org.utgenome.gwt.utgb.client.bio.ChrLoc;
import org.utgenome.gwt.utgb.client.bio.OnGenome;
import org.utgenome.gwt.utgb.client.bio.ReadQueryConfig;
import org.utgenome.util.TestHelper;

public class ReadCanvasTest {

	@Test
	public void draw() throws Exception {
		GenomeWindow w = new GenomeWindow(9500, 10300);
		DrawStyle style = new DrawStyle();
		style.geneHeight = 2;
		ReadCanvas canvas = new ReadCanvas(700, 100, w, style);
		File bam = TestHelper.createTempFileFrom(ReadCanvasTest.class, "paired.bam");
		File bai = new File(bam.getAbsolutePath() + ".bai");
		TestHelper.createTempFileFrom(ReadCanvasTest.class, "paired.bam.bai", bai);

		ReadQueryConfig config = new ReadQueryConfig();
		config.pixelWidth = canvas.getPixelWidth();
		config.maxmumNumberOfReadsToDisplay = Integer.MAX_VALUE;
		List<OnGenome> dataSet = SAMReader.overlapQuery(bam, new ChrLoc("chr1", (int) w.startIndexOnGenome, (int) w.endIndexOnGenome), canvas.getPixelWidth(),
				config);

		canvas.draw(dataSet);
		canvas.toPNG(new File("target", "sample.png"));
	}
}
