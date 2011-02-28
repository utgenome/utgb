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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.utgenome.graphics.GenomeWindow;
import org.utgenome.graphics.ReadCanvas;
import org.utgenome.graphics.ReadCanvas.DrawStyle;
import org.utgenome.gwt.utgb.client.bio.ChrLoc;
import org.utgenome.gwt.utgb.client.bio.GenomeDB;
import org.utgenome.gwt.utgb.client.bio.OnGenome;
import org.utgenome.gwt.utgb.client.bio.ReadQueryConfig;
import org.utgenome.gwt.utgb.client.view.TrackDisplay;
import org.utgenome.gwt.utgb.client.view.TrackDisplay.DB;
import org.utgenome.gwt.utgb.client.view.TrackDisplay.Track;
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

	@Option(longName = "view", description = "view definition file")
	private File viewFile;

	@Option(symbol = "b", description = "background color in #FFFFFF format. default= transparent")
	private String backgroundColor;

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
		if (readFile == null && viewFile == null) {
			throw new UTGBShellException("No read file (-f) or view file (--view) is specified");
		}

		if (query != null) {
			ChrLoc loc = RegionQueryExpr.parse(query);

			createPNG(viewFile, loc);
			return;
		}

		if (queryFile != null) {
			SilkLens.findFromSilk(new BufferedReader(new FileReader(queryFile)), "region", ChrLoc.class, new ObjectHandler<ChrLoc>() {
				public void init() throws Exception {
					_logger.info("reading " + queryFile);
				}

				public void handle(ChrLoc loc) throws Exception {
					createPNG(viewFile, loc);
				}

				public void finish() throws Exception {
					_logger.info("finished reading " + queryFile);
				}
			});
			return;
		}

		throw new UTGBShellException("No query is given");

	}

	public BufferedImage createReadAlignmentImage(ChrLoc loc, String dbPath) {

		GenomeDB db = new GenomeDB(dbPath, "");
		ReadQueryConfig config = new ReadQueryConfig();
		config.pixelWidth = pixelWidth;
		config.maxmumNumberOfReadsToDisplay = Integer.MAX_VALUE;

		List<OnGenome> readSet = ReadView.overlapQuery(null, db, loc, config);

		// draw graphics
		ReadCanvas canvas = new ReadCanvas(pixelWidth, 1, new GenomeWindow(loc.start, loc.end));
		DrawStyle style = canvas.getStyle();
		if (!dbPath.endsWith(".bam")) {
			style.geneHeight = 10;
			style.geneMargin = 2;
			canvas.setStyle(style);
		}

		canvas.draw(readSet);
		return canvas.getBufferedImage();

	}

	void createPNG(File viewFile, ChrLoc loc) throws Exception {
		_logger.info(String.format("query: %s", loc));
		TrackDisplay display;
		if (viewFile == null) {
			display = new TrackDisplay();
			Track t = new Track();
			t.name = "";
			t.db = new DB();
			t.db.path = readFile;
			display.track.add(t);
		}
		else
			display = SilkLens.loadSilk(TrackDisplay.class, viewFile.toURI().toURL());

		List<BufferedImage> trackImage = new ArrayList<BufferedImage>();

		for (Track track : display.track) {
			DB db = track.db;
			if (db == null || db.path == null) {
				continue;
			}

			trackImage.add(createReadAlignmentImage(loc, db.path));
		}

		// Compute the canvas height
		int pixelHeight = 0;
		for (BufferedImage each : trackImage)
			pixelHeight += each.getHeight();

		// Prepare a large canvas
		BufferedImage image = new BufferedImage(pixelWidth, pixelHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) image.getGraphics();

		// background
		if (backgroundColor != null) {
			Color bg = Color.decode(backgroundColor);
			g.setColor(bg);
			g.fillRect(0, 0, image.getWidth(), image.getHeight());
		}

		// Paste track images
		int yOffset = 0;
		int yMargin = 1;
		for (BufferedImage each : trackImage) {

			int h = each.getHeight();
			int w = each.getWidth();
			_logger.debug(String.format("w:%d, h:%d", w, h));
			g.drawImage(each, 0, yOffset, w, yOffset + h, 0, 0, w, h, null);
			yOffset += h + yMargin;
		}

		// output the graphics as a PNG file
		File outPNG = new File(outputFolder, outFile == null ? String.format("region-%s-%d-%d.png", loc.chr, loc.start, loc.end) : outFile);
		_logger.info("output " + outPNG);
		ImageIO.write(image, "PNG", outPNG);

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
