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
// ReadCanvas.java
// Since: 2011/01/06
//
//--------------------------------------
package org.utgenome.graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.imageio.ImageIO;

import org.utgenome.gwt.utgb.client.UTGBClientException;
import org.utgenome.gwt.utgb.client.bio.CDS;
import org.utgenome.gwt.utgb.client.bio.CIGAR;
import org.utgenome.gwt.utgb.client.bio.Exon;
import org.utgenome.gwt.utgb.client.bio.Gap;
import org.utgenome.gwt.utgb.client.bio.Gene;
import org.utgenome.gwt.utgb.client.bio.Interval;
import org.utgenome.gwt.utgb.client.bio.OnGenome;
import org.utgenome.gwt.utgb.client.bio.OnGenomeDataVisitorBase;
import org.utgenome.gwt.utgb.client.bio.ReferenceSequence;
import org.utgenome.gwt.utgb.client.bio.SAMReadLight;
import org.utgenome.gwt.utgb.client.bio.SAMReadPair;
import org.utgenome.gwt.utgb.client.bio.SAMReadPairFragment;
import org.utgenome.gwt.utgb.client.canvas.IntervalLayout;
import org.utgenome.gwt.utgb.client.canvas.IntervalLayout.LocusLayout;
import org.utgenome.gwt.utgb.client.canvas.PrioritySearchTree.Visitor;
import org.utgenome.gwt.utgb.client.track.TrackWindow;
import org.utgenome.gwt.utgb.server.util.graphic.GraphicUtil;
import org.xerial.lens.SilkLens;
import org.xerial.util.log.Logger;

/**
 * For generating {@link BufferedImage} instance of a read layout
 * 
 * @author leo
 * 
 */
public class ReadCanvas {

	private static Logger _logger = Logger.getLogger(ReadCanvas.class);
	private final GenomeWindow window;
	private BufferedImage image;
	private Graphics2D g;

	private IntervalLayout layout = new IntervalLayout();

	public static class DrawStyle {
		public int geneHeight = 2;
		public int geneMargin = 1;
		public boolean overlapPairedReads = true;
		public boolean showStrand = true;
		public boolean drawShadow = true;
		public boolean showLabels = false;
		public int fontWidth = 10;
		public float clippedRegionAlpha = 0.2f;

		public Color COLOR_GAP = new Color(0x66, 0x66, 0x66);
		public Color COLOR_PADDING = new Color(0x33, 0x33, 0x66);
		public Color COLOR_SHADOW = new Color(30, 30, 30, (int) (255 * 0.6f));

		public Color COLOR_READ_DEFAULT = new Color(0xCC, 0xCC, 0xCC);
		public Color COLOR_FORWARD_STRAND = new Color(0xd8, 0x00, 0x67);
		public Color COLOR_REVERSE_STRAND = new Color(0x00, 0x67, 0xd8);

		public Color COLOR_WIRED_READ_F = new Color(0xff, 0x99, 0x66);
		public Color COLOR_WIRED_READ_R = new Color(0x66, 0x99, 0xff);
		public Color COLOR_ORPHAN_READ_F = new Color(0xff, 0x66, 0x99);
		public Color COLOR_ORPHAN_READ_R = new Color(0x66, 0x99, 0xff);

		private HashMap<Character, Color> colorTable = new HashMap<Character, Color>();

		private String colorA = "50B6E8";
		private String colorC = "E7846E";
		private String colorG = "84AB51";
		private String colorT = "FFA930";
		public String colorN = "EEEEEE";
		public int repeatColorAlpha = 50;

		{
			colorTable.put('a', GraphicUtil.parseColor(colorA, repeatColorAlpha));
			colorTable.put('c', GraphicUtil.parseColor(colorC, repeatColorAlpha));
			colorTable.put('g', GraphicUtil.parseColor(colorG, repeatColorAlpha));
			colorTable.put('t', GraphicUtil.parseColor(colorT, repeatColorAlpha));
			colorTable.put('n', GraphicUtil.parseColor(colorN, repeatColorAlpha));
			colorTable.put('A', GraphicUtil.parseColor(colorA));
			colorTable.put('C', GraphicUtil.parseColor(colorC));
			colorTable.put('G', GraphicUtil.parseColor(colorG));
			colorTable.put('T', GraphicUtil.parseColor(colorT));
			colorTable.put('N', GraphicUtil.parseColor(colorN));
		}

		public void setBaseColor(char base, Color c) {
			colorTable.put(Character.toUpperCase(base), c);
			colorTable.put(Character.toLowerCase(base), new Color(c.getRed(), c.getGreen(), c.getBlue(), repeatColorAlpha));
		}

		public Color getBaseColor(char base) {
			if (colorTable.containsKey(base))
				return colorTable.get(base);
			else
				return Color.white;
		}

		public Color getReadColor(OnGenome g) {

			if (SAMReadLight.class.isAssignableFrom(g.getClass())) {
				return getSAMReadColor(SAMReadLight.class.cast(g));
			}
			return getReadColor_internal(g);
		}

		private Color getReadColor_internal(OnGenome g) {
			if (showStrand) {
				if (g instanceof Interval) {
					Interval r = (Interval) g;
					return r.isSense() ? COLOR_FORWARD_STRAND : COLOR_REVERSE_STRAND;
				}
			}
			return COLOR_READ_DEFAULT;
		}

		public Color getClippedReadColor(OnGenome g) {
			Color c = getReadColor(g);
			return new Color(c.getRed(), c.getGreen(), c.getBlue(), (int) (255 * clippedRegionAlpha));
		}

		private Color getSAMReadColor(SAMReadLight r) {
			if (r.isPairedRead()) {
				if (r.isMappedInProperPair())
					return getReadColor_internal(r);
				else
					return r.isSense() ? COLOR_WIRED_READ_F : COLOR_WIRED_READ_R;
			}
			else {
				return r.isSense() ? COLOR_ORPHAN_READ_F : COLOR_ORPHAN_READ_R;
			}
		}

	}

	private DrawStyle style;

	public ReadCanvas(int width, int height, GenomeWindow window) {
		this(width, height, window, new DrawStyle());
	}

	public ReadCanvas(int width, int height, GenomeWindow window, DrawStyle style) {
		this.window = window;
		this.style = style;
		setPixelSize(width, height);
	}

	public DrawStyle getStyle() {
		return style;
	}

	public void setStyle(DrawStyle style) {
		this.style = style;
	}

	public void setPixelSize(int width, int height) {
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		g = image.createGraphics();

		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setStroke(new BasicStroke(0.5f));
	}

	public Graphics2D getGraphics() {
		return g;
	}

	public BufferedImage getBufferedImage() {
		return image;
	}

	public void toPNG(OutputStream out) throws IOException {
		ImageIO.write(image, "png", out);
	}

	public void toPNG(File out) throws IOException {
		ImageIO.write(image, "png", out);
	}

	public void draw(List<OnGenome> dataSet) {
		layout.setAllowOverlapPairedReads(style.overlapPairedReads);
		layout.setKeepSpaceForLabels(style.showLabels);
		layout.setTrackWindow(new TrackWindow(getPixelWidth(), (int) window.startIndexOnGenome, (int) window.endIndexOnGenome));

		if (_logger.isDebugEnabled())
			_logger.debug("Creating a layout");
		int maxOffset = layout.reset(dataSet, style.geneHeight);
		if (_logger.isDebugEnabled())
			_logger.debug("Done.");

		final int h = style.geneHeight + style.geneMargin;
		final int canvasHeight = (maxOffset + 1) * h;
		setPixelSize(image.getWidth(), canvasHeight);

		final ReadPainter painter = new ReadPainter();
		layout.depthFirstSearch(new Visitor<IntervalLayout.LocusLayout>() {
			public void visit(LocusLayout layout) {
				painter.setLocusLayout(layout);
				layout.getLocus().accept(painter);
			}
		});
	}

	private int getReadHeight() {
		return style.geneHeight + style.geneMargin;
	}

	private class ReadPainter extends OnGenomeDataVisitorBase {
		private LocusLayout currentLayout;

		public void setLocusLayout(LocusLayout layout) {
			this.currentLayout = layout;
		}

		public int getYPos() {
			return currentLayout.scaledHeight(getReadHeight());
		}

		public int getYPos(int y) {
			return LocusLayout.scaledHeight(y, getReadHeight());
		}

		@Override
		public void visitSAMReadPair(SAMReadPair pair) {

			SAMReadLight first = pair.getFirst();
			SAMReadLight second = pair.getSecond();

			int y1 = getYPos();
			int y2 = y1;

			if (!style.overlapPairedReads && first.unclippedSequenceHasOverlapWith(second)) {
				if (first.unclippedStart > second.unclippedStart) {
					SAMReadLight tmp = first;
					first = second;
					second = tmp;
				}
				y2 = getYPos(currentLayout.getYOffset() + 1);
			}
			else {
				visitGap(pair.getGap());
			}

			drawSAMRead(first, y1);
			drawSAMRead(second, y2);
		}

		@Override
		public void visitSAMReadLight(SAMReadLight r) {
			drawSAMRead(r, getYPos());
		}

		@Override
		public void visitSAMReadPairFragment(SAMReadPairFragment fragment) {
			visitGap(fragment.getGap());
			drawSAMRead(fragment.oneEnd, getYPos());
		}

		@Override
		public void visitGap(Gap p) {
			drawPadding(p.getStart(), p.getEnd(), getYPos(), style.COLOR_GAP);
		}

		@Override
		public void visitGene(Gene g) {
			drawGene(g, getYPos());
			drawLabel(g, getYPos());
		}

		@Override
		public void visitSequence(ReferenceSequence referenceSequence) {
			_logger.debug("draw :" + SilkLens.toSilk(referenceSequence));
			drawBases(referenceSequence.getStart(), getYPos(), referenceSequence.sequence, null);
		}
	}

	private int pixelPositionOnCanvas(int indexOnGenome) {
		return window.pixelPositionOnWindow(indexOnGenome, image.getWidth());
	}

	public void drawGene(Gene gene, int y) {

		Color geneColor = style.getReadColor(gene);
		if (gene.getExon() == null || gene.getExon().isEmpty()) {
			drawGeneRect(gene.getStart(), gene.getEnd(), y, geneColor);
			return;
		}

		for (Exon e : gene.getExon()) {
			drawGeneRect(e.getStart(), e.getEnd(), y, style.getClippedReadColor(gene));

			// draw UTR region
			if (gene.getCDS() != null && !gene.getCDS().isEmpty()) {
				CDS cds = gene.getCDS().get(0);
				if (e.getStart() <= cds.getEnd() && e.getEnd() >= cds.getStart()) {
					int cdsStart = (e.getStart() <= cds.getStart()) ? cds.getStart() : e.getStart();
					int cdsEnd = (e.getEnd() <= cds.getEnd()) ? e.getEnd() : cds.getEnd();
					drawGeneRect(cdsStart, cdsEnd, y, geneColor);
				}
			}
		}

		// draw the arrow between exons
		g.setColor(geneColor);
		int arrowHeight = (int) (style.geneHeight / 2.0);

		for (int i = 0; i < gene.getExon().size() - 1; i++) {
			Exon prev = gene.getExon(i);
			Exon next = gene.getExon(i + 1);
			int x1 = pixelPositionOnCanvas(prev.getEnd());
			int x2 = pixelPositionOnCanvas(next.getStart());
			int yAxis = (int) (y + (style.geneHeight / 2.0f));

			g.drawLine(x1, yAxis, x2, yAxis);

			for (int x = x1; x + 4 <= x2; x += 5) {
				if (gene.isSense()) {
					g.drawLine(x, y + 1, x + 3, y + arrowHeight);
					g.drawLine(x + 3, y + arrowHeight, x, y + 2 * arrowHeight - 1);
				}
				else {
					g.drawLine(x + 3, y + 1, x, y + arrowHeight);
					g.drawLine(x, y + arrowHeight, x + 3, y + 2 * arrowHeight - 1);
				}
			}

		}

	}

	public void drawRegion(OnGenome region, int y) {
		drawGeneRect(region.getStart(), region.getEnd(), y, style.getReadColor(region));
	}

	public void drawRegion(int startOnGenome, int endOnGenome, int y, Color c, boolean drawShadow) {
		int x1 = pixelPositionOnCanvas(startOnGenome);
		int x2 = pixelPositionOnCanvas(endOnGenome);

		int boxWidth = x2 - x1;
		if (boxWidth <= 0)
			boxWidth = 1;

		AffineTransform saved = g.getTransform();
		g.translate(x1, y);
		g.setColor(c);
		if (style.geneHeight > 5) {

			LinearGradientPaint lg = new LinearGradientPaint(new Point2D.Float(0, 0), new Point2D.Float(0, style.geneHeight),
					new float[] { 0, 0.05f, 0.5f, 1 }, new Color[] { c, Color.WHITE, c, c });
			g.setPaint(lg);
		}
		g.fillRect(0, 0, boxWidth, style.geneHeight);
		g.setTransform(saved);

		if (_logger.isTraceEnabled())
			_logger.trace(String.format("-gene rect - x:%d, y:%d, width:%d, height:%d, color:%s", x1, y, boxWidth, style.geneHeight, c.toString()));

		if (drawShadow) {
			g.setColor(style.COLOR_SHADOW);
			//g.setStroke(new BasicStroke(1f));

			saved = g.getTransform();
			g.translate(x1, y);
			g.drawLine(1, style.geneHeight, boxWidth, style.geneHeight);
			g.drawLine(boxWidth, style.geneHeight, boxWidth, 0);
			g.setTransform(saved);
		}

	}

	public void drawGeneRect(int startOnGenome, int endOnGenome, int y, Color c) {
		drawRegion(startOnGenome, endOnGenome, y, c, style.drawShadow);
	}

	public void drawPadding(int startOnGenome, int endOnGenome, int y, Color c) {

		g.setColor(c);
		g.setStroke(new BasicStroke(0.5f));
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		int yPos = (int) (y + (style.geneHeight / 2) + 0.5f);
		g.drawLine((int) (pixelPositionOnCanvas(startOnGenome) + 0.5f), yPos, (int) (pixelPositionOnCanvas(endOnGenome) + 0.5f), yPos);
	}

	private static class PostponedInsertion {
		final int start;
		final String subseq;

		public PostponedInsertion(int start, String subseq) {
			this.start = start;
			this.subseq = subseq;
		}

	}

	public void drawSAMRead(SAMReadLight r, int y) {

		try {
			int cx1 = pixelPositionOnCanvas(r.unclippedStart);
			int cx2 = pixelPositionOnCanvas(r.unclippedEnd);

			int gx1 = pixelPositionOnCanvas(r.getStart());
			int gx2 = pixelPositionOnCanvas(r.getEnd());

			int width = gx2 - gx1;

			if ((cx2 - cx1) <= 5) {
				// when the pixel range is narrow, draw a rectangle only 
				drawRegion(r, y);
			}
			else {

				boolean drawBase = window.getGenomeRange() <= (image.getWidth() / style.fontWidth);

				CIGAR cigar = new CIGAR(r.cigar);
				int readStart = r.getStart();
				int seqIndex = 0;

				// Drawing insertions should be postponed after all of he read bases are painted.
				List<PostponedInsertion> postponed = new ArrayList<PostponedInsertion>();
				for (int cigarIndex = 0; cigarIndex < cigar.size(); cigarIndex++) {
					CIGAR.Element e = cigar.get(cigarIndex);
					int readEnd = readStart + e.length;
					switch (e.type) {
					case Deletions:
						// ref : AAAAAA
						// read: ---AAA
						// cigar: 3D3M
						drawPadding(readStart, readEnd, y, style.getReadColor(r));
						break;
					case Insertions:
						// ref : ---AAA
						// read: AAAAAA
						// cigar: 3I3M
						if (r.getSequence() != null)
							postponed.add(new PostponedInsertion(readStart, r.getSequence().substring(seqIndex, seqIndex + e.length)));
						readEnd = readStart;
						seqIndex += e.length;
						break;
					case Padding:
						// ref : AAAAAA
						// read: ---AAA
						// cigar: 3P3M
						readEnd = readStart;
						drawPadding(readStart, readStart + 1, y, style.COLOR_PADDING);
						break;
					case Matches: {

						if (drawBase && r.getSequence() != null) {
							drawBases(readStart, y, r.getSequence().substring(seqIndex, seqIndex + e.length),
									r.getQV() != null ? r.getQV().substring(seqIndex, seqIndex + e.length) : null);
						}
						else {
							drawGeneRect(readStart, readEnd, y, style.getReadColor(r));
						}

						seqIndex += e.length;
					}
						break;
					case SkippedRegion:
						drawPadding(readStart, readEnd, y, style.getReadColor(r));
						break;
					case SoftClip: {
						int softclipStart = cigarIndex == 0 ? readStart - e.length : readStart;
						int softclipEnd = cigarIndex == 0 ? readStart : readStart + e.length;
						readEnd = softclipEnd;

						if (drawBase && r.getSequence() != null) {
							drawBases(softclipStart, y, r.getSequence().substring(seqIndex, seqIndex + e.length).toLowerCase(), r.getQV() != null ? r.getQV()
									.substring(seqIndex, seqIndex + e.length) : null);
						}
						else {
							drawGeneRect(softclipStart, softclipEnd, y, style.getClippedReadColor(r));
						}

						seqIndex += e.length;
					}
						break;
					case HardClip:
						break;
					}
					readStart = readEnd;
				}

				for (PostponedInsertion each : postponed) {
					drawGeneRect(each.start, each.start + 1, y, new Color(0x11, 0x11, 0x11));
				}
			}

		}
		catch (UTGBClientException e) {
			// when parsing CIGAR string fails, simply draw a rectangle
			drawRegion(r, y);
		}
	}

	public void drawBases(int startOnGenome, int y, String seq, String qual) {

		Font f = new Font("SansSerif", Font.PLAIN, 1);
		f = f.deriveFont((float) style.fontWidth);
		g.setFont(f);

		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		boolean drawBase = true;
		if (style.geneHeight < style.fontWidth - 2) {
			drawBase = false;
		}
		else {
			// Check whether the pixel length of each character is smaller than pixel width for the range [startOn, startOnGenome+1)
			int start = window.getXPosOnWindow(startOnGenome, getPixelWidth());
			int end = window.getXPosOnWindow(startOnGenome + 1, getPixelWidth());
			int pixelWidth = end - start;

			FontMetrics fontMetrics = g.getFontMetrics();
			HashSet<Character> checkedChar = new HashSet<Character>();
			for (int i = 0; i < seq.length(); ++i) {
				char ch = seq.charAt(i);
				if (checkedChar.contains(ch))
					continue;

				checkedChar.add(ch);
				int textWidth = fontMetrics.stringWidth(Character.toString(ch));
				if (textWidth > pixelWidth - 2) {
					drawBase = false;
					break;
				}
			}
		}

		for (int i = 0; i < seq.length(); i++) {
			int baseIndex = 8;
			char base = seq.charAt(i);
			Color c = style.getBaseColor(base);

			drawRegion(startOnGenome + i, startOnGenome + i + 1, y, c, false);
			if (drawBase)
				drawBase(base, startOnGenome + i, y, Color.WHITE);
		}

	}

	public int getPixelWidth() {
		return image.getWidth();
	}

	public void drawRuler() {
		long s = window.startIndexOnGenome;
		long e = window.endIndexOnGenome;

	}

	public void drawBase(char base, int startIndexOnGenome, int yOffset, Color color) {
		int start = window.getXPosOnWindow(startIndexOnGenome, getPixelWidth());
		int end = window.getXPosOnWindow(startIndexOnGenome + 1, getPixelWidth());
		int drawStart;

		int pixelWidth = end - start;

		String b = Character.toString(base);
		g.setColor(color);
		FontMetrics fontMetrics = g.getFontMetrics();
		int textWidth = fontMetrics.stringWidth(b);
		int textHeight = fontMetrics.getHeight();
		if (textHeight > style.geneHeight)
			textHeight = style.geneHeight;

		drawStart = (int) (start + (end - start - textWidth) / 2.0f);

		g.drawString(b, drawStart, yOffset + textHeight - 1);
	}

	public void drawLabel(OnGenome region, int yOffset) {
		if (!style.showLabels)
			return;

		int start = pixelPositionOnCanvas(region.getStart());
		int end = pixelPositionOnCanvas(region.getEnd());

		int drawStart;

		Font f = new Font("Arial", Font.PLAIN, 1);
		f = f.deriveFont((float) style.fontWidth);
		g.setFont(f);

		FontMetrics fontMetrics = g.getFontMetrics();
		int textWidth = fontMetrics.stringWidth(region.getName());
		int textHeight = fontMetrics.getHeight();
		if (textHeight > style.geneHeight)
			textHeight = style.geneHeight;

		g.setColor(style.getReadColor(region));
		drawStart = (start - textWidth) - 1;
		if (drawStart < 0) {
			drawStart = end + 1;
		}

		g.drawString(region.getName(), drawStart, yOffset + textHeight - 1);

	}

}
