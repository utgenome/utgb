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
// utgb-shell Project
//
// ScreenShot.java
// Since: 2011/01/06
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.shell;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.utgenome.graphics.GenomeWindow;
import org.utgenome.graphics.ReadCanvas;
import org.utgenome.gwt.utgb.client.bio.ChrLoc;
import org.utgenome.gwt.utgb.client.bio.GenomeDB;
import org.utgenome.gwt.utgb.client.bio.OnGenome;
import org.utgenome.gwt.utgb.client.bio.ReadQueryConfig;
import org.utgenome.gwt.utgb.server.app.ReadView;
import org.xerial.lens.SilkLens;
import org.xerial.util.ObjectHandler;
import org.xerial.util.log.Logger;
import org.xerial.util.opt.Argument;
import org.xerial.util.opt.Option;

/**
 * screenshot commant to take a image data of the read view
 * 
 * @author leo
 * 
 */
public class ScreenShot extends UTGBShellCommand {

	private static Logger _logger = Logger.getLogger(ScreenShot.class);

	@Option(symbol = "i", longName = "input", description = "read file to query (BAM/BED, etc.)")
	private String readFile;

	@Option(symbol = "o", longName = "output", description = "output PNG file path")
	private String outFile;

	@Option(longName = "outdir", description = "output folder. default is the current directory")
	private File outputFolder;

	@Option(symbol = "q", description = "query file in Silk format -region(chr, start, end)")
	private File queryFile;

	@Option(longName = "pixelwidth", description = "pixel width. default=1000")
	private int pixelWidth = 1000;

	@Argument(index = 0, name = "query")
	private String query;

	@Override
	public void execute(String[] args) throws Exception {

		if (query == null && queryFile == null)
			throw new UTGBShellException("No query is given.");

		if (outputFolder != null) {
			if (!outputFolder.exists()) {
				_logger.info("create dir: " + outputFolder);
				outputFolder.mkdirs();
			}
		}

		if (readFile == null) {
			throw new UTGBShellException("No read file (-f) is specified");
		}

		if (query != null) {
			ChrLoc loc = RegionQueryExpr.parse(query);

			createPNG(loc);
			return;
		}

		if (queryFile != null) {
			SilkLens.findFromSilk(new BufferedReader(new FileReader(queryFile)), "region", ChrLoc.class, new ObjectHandler<ChrLoc>() {
				public void init() throws Exception {
					_logger.info("reading " + queryFile);
				}

				public void handle(ChrLoc loc) throws Exception {
					createPNG(loc);
				}

				public void finish() throws Exception {
					_logger.info("finished reading " + queryFile);
				}
			});
			return;
		}

		throw new UTGBShellException("No query is given");

	}

	void createPNG(ChrLoc loc) throws IOException {
		// query read set
		GenomeDB db = new GenomeDB(readFile, "");
		ReadQueryConfig config = new ReadQueryConfig();
		config.pixelWidth = pixelWidth;
		config.maxmumNumberOfReadsToDisplay = Integer.MAX_VALUE;
		_logger.info(String.format("query: %s, %s", readFile, loc));
		List<OnGenome> readSet = ReadView.overlapQuery(null, db, loc, config);

		// draw graphics
		ReadCanvas canvas = new ReadCanvas(pixelWidth, 1, new GenomeWindow(loc.start, loc.end));
		canvas.draw(readSet);

		// output the graphics as a PNG file
		File outPNG = new File(outputFolder, outFile == null ? String.format("region-%s-%d-%d.png", loc.chr, loc.start, loc.end) : outFile);
		_logger.info("output " + outPNG);
		canvas.toPNG(outPNG);
	}

	@Override
	public String name() {
		return "screenshot";
	}

	@Override
	public String getOneLinerDescription() {
		return "take the screenshot of the specified region";
	}

}
