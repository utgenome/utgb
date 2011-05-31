/*--------------------------------------------------------------------------
 *  Copyright 2008 utgenome.org
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
// GeneCanvas.java
// Since: Jul 8, 2008
//
// $URL: http://svn.utgenome.org/utgb/trunk/utgb/utgb-core/src/main/java/org/utgenome/gwt/utgb/client/canvas/GeneCanvas.java $ 
// $Author: leo $
//--------------------------------------
package org.utgenome.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.List;

import javax.imageio.ImageIO;

import org.utgenome.gwt.utgb.client.bio.CDS;
import org.utgenome.gwt.utgb.client.bio.Exon;
import org.utgenome.gwt.utgb.client.bio.Gene;
import org.utgenome.gwt.utgb.client.bio.Interval;
import org.utgenome.gwt.utgb.client.bio.GenomeRange;
import org.utgenome.gwt.utgb.client.bio.Read;
import org.utgenome.gwt.utgb.client.canvas.PrioritySearchTree;
import org.utgenome.gwt.utgb.server.app.MethylViewer;
import org.xerial.util.Pair;

/**
 * Browser-side graphic canvas for drawing gene objects
 * 
 * @author leo
 * 
 */
public class GeneCanvas {

	private int geneHeight = 10;
	private int geneMargin = 1;

	private GenomeCanvas canvas;

	// ad hoc parameters
	private int thresholdOfNumGenesToDrawLabel = 50;
	private int gapWidth = 5;

	// widget
	private PrioritySearchTree<LocusLayout> locusLayout = new PrioritySearchTree<LocusLayout>();

	public GeneCanvas() {
	}

	public GeneCanvas(int pixelWidth, int pixelHeight, GenomeWindow genomeWindow) {
		canvas = new GenomeCanvas(pixelWidth, pixelHeight, genomeWindow);
	}

	public void setGeneHeight(int height) {
		this.geneHeight = height;
		canvas.setGeneHeight(height);
	}

	public void setPixelHeight(int height) {
		canvas.setPixelHeight(height);
	}

	public static int width(int x1, int x2) {
		return (x1 < x2) ? x2 - x1 : x1 - x2;
	}

	public class LocusLayout {
		private GenomeRange gene;
		private int yOffset;

		public LocusLayout(GenomeRange gene, int yOffset) {
			this.gene = gene;
			this.yOffset = yOffset;
		}

		public GenomeRange getLocus() {
			return gene;
		}

		public int getYOffset() {
			return yOffset;
		}

		@Override
		public String toString() {
			return "yOffset=" + yOffset;
		}
	}

	public void setThresholdGenes(int thresholdGeneNames) {
		this.thresholdOfNumGenesToDrawLabel = thresholdGeneNames;
	}

	public void setGapWidth(int gapWidth) {
		this.gapWidth = gapWidth;
	}

	<T extends GenomeRange> int createLayout(List<T> locusList) {
		int maxYOffset = 0;
		locusLayout.clear();

		boolean drawLabel = locusList.size() < thresholdOfNumGenesToDrawLabel;

		Font f = new Font("SansSerif", Font.PLAIN, geneHeight);

		FontMetrics fontMetrics = canvas.getGraphics().getFontMetrics(f);
		long leftOnGenome = canvas.getGenomeWindow().startIndexOnGenome;

		for (GenomeRange l : locusList) {

			int x1 = l.getStart();
			int x2 = l.getStart() + l.length();

			if (drawLabel) {
				int width = fontMetrics.stringWidth(l.getName()) + gapWidth;
				int fontWidthOnGenome = canvas.getGenomeWindow().toGenomeLength(width, canvas.getWidth());
				if ((x1 - fontWidthOnGenome) < leftOnGenome)
					x2 += fontWidthOnGenome;
				else
					x1 -= fontWidthOnGenome;
			}

			List<LocusLayout> activeLocus = locusLayout.rangeQuery(x1, Integer.MAX_VALUE, x2);

			HashSet<Integer> filledY = new HashSet<Integer>();
			// overlap test
			for (LocusLayout al : activeLocus) {
				filledY.add(al.yOffset);
			}

			int blankY = 0;
			for (; filledY.contains(blankY); blankY++) {
			}

			locusLayout.insert(new LocusLayout(l, blankY), x2, x1);

			if (blankY > maxYOffset)
				maxYOffset = blankY;
		}

		if (maxYOffset <= 0)
			maxYOffset = 1;
		return maxYOffset;
	}

	public <E extends GenomeRange> void draw(List<E> geneList) {

		// create a gene layout
		int maxOffset = createLayout(geneList);

		int height = (maxOffset + 1) * (geneHeight + geneMargin);
		if (height <= 0) {
			height = geneHeight + geneMargin;
		}

		// set the canvas size appropriately to adjust the track frame height when refresh() is called
		setPixelHeight(height);

		// draw genes according the locus layout
		locusLayout.depthFirstSearch(new PrioritySearchTree.Visitor<LocusLayout>() {
			final int h = geneHeight + geneMargin;

			public void visit(LocusLayout layout) {
				layout.yOffset = layout.yOffset * h;
				GenomeRange l = layout.getLocus();
				long lx = l.getStart();
				long lx2 = l.getStart() + l.length();

				long geneWidth = lx2 - lx;
				if (geneWidth <= 10) {
					draw(l, layout.getYOffset());
				}
				else {
					if (Gene.class.isInstance(l)) {
						Gene gene = (Gene) l;
						CDS cds = gene.getCDS().size() > 0 ? gene.getCDS().get(0) : null;
						draw(gene, gene.getExon(), cds, layout.getYOffset());
					}
					else if (MethylViewer.MethlEntry.class.isInstance(l)) {
						MethylViewer.MethlEntry m = MethylViewer.MethlEntry.class.cast(l);

						drawGeneRect(l.getStart(), l.getStart() + l.length(), layout.getYOffset(), getGeneColor(l, 0.7f));

						int s = canvas.getXPosOnWindow(l.getStart());
						int e = canvas.getXPosOnWindow(l.getStart() + l.length());
						int w = e - s;
						if (w < 0)
							w = -w;

						Pair<List<Integer>, List<Integer>> mPosAndCPos = m.getMPos();

						if (w > 3) {
							for (int cOffset : mPosAndCPos.getSecond()) {
								long cPos = l.getStart() + cOffset;
								drawGeneRect(cPos, cPos + 1, layout.getYOffset(), hexValue2Color("#6666FF", 0.3f));
							}
						}
						for (int mOffset : mPosAndCPos.getFirst()) {
							long mPos = l.getStart() + mOffset;
							drawGeneRect(mPos, mPos + 1, layout.getYOffset(), hexValue2Color("#F80033", 0.1f));
						}

						if (locusLayout.size() <= thresholdOfNumGenesToDrawLabel) {
							canvas.drawLocusLabel(Integer.toString(m.frequency), lx, lx2, layout.getYOffset() + geneHeight, 9.0f, hexValue2Color("#6030F0",
									0.0f));
						}

					}
					else if (Interval.class.isInstance(l)) {
						draw(l, layout.getYOffset());
					}
				}
				if (l.getName() != null && locusLayout.size() <= thresholdOfNumGenesToDrawLabel) {
					canvas.drawText(l.getName(), lx, lx2, layout.getYOffset(), geneHeight - 1, getExonColor(l));
				}
			}
		});

	}

	public void draw(GenomeRange locus, int yOffset) {
		if (Gene.class.isInstance(locus))
			draw((Gene) locus, yOffset);
		else
			drawGeneRect(locus.getStart(), locus.getStart() + locus.length(), yOffset, getGeneColor(locus));
	}

	public void draw(Gene gene, List<Exon> exonList, CDS cds, int yPosition) {
		// assumption: exonList are sorted

		drawGeneRect(gene.getStart(), gene.getEnd(), yPosition, getGeneColor(gene));

		//GWT.log("exon: ", null);
		for (Exon e : exonList) {
			draw(gene, e, cds, yPosition);
		}

		// draw arrow between exons
		for (int i = 0; i < exonList.size() - 1; i++) {
			Exon prev = exonList.get(i);
			Exon next = exonList.get(i + 1);

			long x1 = prev.getEnd();
			long x2 = next.getStart();
			long yAxis = yPosition + (geneHeight / 2);
			long xMiddle = (x1 + x2) / 2;

			long range = x2 - x1;

			Color exonColor = getExonColor(gene);
			if (range > 10) {
				canvas.drawLine(x1, yAxis, xMiddle, yPosition, exonColor);
				canvas.drawLine(xMiddle, yPosition, x2, yAxis, exonColor);
			}
			else
				canvas.drawLine(x1, yAxis, x2, yAxis, exonColor);

		}

	}

	public void draw(Gene gene, int yPosition) {
		drawGeneRect(gene.getStart(), gene.getEnd(), yPosition, getCDSColor(gene));
	}

	public void draw(Gene gene, Exon exon, CDS cds, int yPosition) {
		long ex = exon.getStart();
		long ex2 = exon.getEnd();

		drawGeneRect(ex, ex2, yPosition, getExonColor(gene));

		if (cds != null) {
			long cx = cds.getStart();
			long cx2 = cds.getEnd();

			if (cx <= cx2) {
				if (ex <= cx2 && ex2 >= cx) {
					long cdsStart = (ex <= cx) ? cx : ex;
					long cdsEnd = (ex2 <= cx2) ? ex2 : cx2;
					drawGeneRect(cdsStart, cdsEnd, yPosition, getCDSColor(gene));
				}
			}

		}
	}

	public void drawGeneRect(long x1, long x2, int y, Color c) {
		canvas.drawGeneRect(x1, x2, y, geneHeight, c);
	}

	public void toPNG(OutputStream out) throws IOException {
		ImageIO.write(canvas.getBufferedImage(), "png", out);
	}

	public Color getExonColor(GenomeRange g) {
		return hexValue2Color(getExonColorText(g), 0.3f);
	}

	public Color getGeneColor(GenomeRange g) {
		return hexValue2Color(getExonColorText(g), 0.7f);
	}

	public Color getGeneColor(GenomeRange g, float offset) {
		return hexValue2Color(getExonColorText(g), offset);
	}

	public Color getCDSColor(GenomeRange g) {
		return hexValue2Color(getExonColorText(g), 0.5f);
	}

	public Color getIntronColor(GenomeRange g) {
		return hexValue2Color(getExonColorText(g), 0.5f);
	}

	public String getExonColorText(GenomeRange g) {
		if (g instanceof Read) {
			Read r = (Read) g;
			if (r.getColor() == null) {
				if (r.isSense()) {
					return "#d80067";
				}
				else {
					return "#0067d8";
				}
			}
			else
				return r.getColor();

		}
		else
			return "#d80067";
	}

	public Color hexValue2Color(String hex, float offset) {
		int r_value = Integer.parseInt(hex.substring(1, 3), 16);
		int g_value = Integer.parseInt(hex.substring(3, 5), 16);
		int b_value = Integer.parseInt(hex.substring(5, 7), 16);
		return new Color(colorOffset(r_value, offset), colorOffset(g_value, offset), colorOffset(b_value, offset));
	}

	public int colorOffset(int color, float offset) {
		return (int) (color + ((255 - color) * offset));
	}

}
