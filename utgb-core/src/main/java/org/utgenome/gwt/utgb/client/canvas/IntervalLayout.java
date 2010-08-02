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
// IntervalLayout.java
// Since: 2010/05/27
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.canvas;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.utgenome.gwt.utgb.client.bio.Interval;
import org.utgenome.gwt.utgb.client.bio.OnGenome;
import org.utgenome.gwt.utgb.client.bio.OnGenomeDataVisitorBase;
import org.utgenome.gwt.utgb.client.bio.SAMRead;
import org.utgenome.gwt.utgb.client.bio.SAMReadPair;
import org.utgenome.gwt.utgb.client.track.TrackWindow;

/**
 * On-genome data layout
 * 
 * @author leo
 * 
 */
public class IntervalLayout {

	private boolean keepSpaceForLabels = true;
	private boolean hasEnoughHeightForLabels = false;
	private PrioritySearchTree<LocusLayout> locusLayout = new PrioritySearchTree<LocusLayout>();

	private TrackWindow w;

	public class LocusLayout {
		private OnGenome locus;
		private int yOffset;

		public LocusLayout(OnGenome locus, int yOffset) {
			this.locus = locus;
			this.yOffset = yOffset;
		}

		public OnGenome getLocus() {
			return locus;
		}

		public int getYOffset() {
			return yOffset;
		}

		public void scaleHeight(int scale) {
			this.yOffset = this.yOffset * scale;
		}

	}

	public IntervalLayout() {
	}

	public void setTrackWindow(TrackWindow window) {
		this.w = window;
	}

	public static int estimiateLabelWidth(OnGenome l, int geneHeight) {
		String name = l.getName();
		int labelWidth = name != null ? (int) (name.length() * geneHeight * 0.9) : 0;
		if (labelWidth > 150)
			labelWidth = 150;
		return labelWidth;
	}

	public List<OnGenome> activeReads() {

		final ArrayList<OnGenome> activeData = new ArrayList<OnGenome>();

		locusLayout.depthFirstSearch(new PrioritySearchTree.Visitor<LocusLayout>() {
			public void visit(LocusLayout l) {
				OnGenome g = l.locus;
				if (w.overlapWith(g))
					activeData.add(g);
			}
		});
		return activeData;
	}

	public static class IntervalRetriever extends OnGenomeDataVisitorBase {

		public int start = -1;
		public int end = -1;
		public boolean isDefined = false;

		public void clear() {
			isDefined = false;
		}

		@Override
		public void visitInterval(Interval interval) {
			start = interval.getStart();
			end = interval.getEnd();
			isDefined = true;
		}

		@Override
		public void visitSAMRead(SAMRead r) {
			start = r.unclippedStart;
			end = r.unclippedEnd;
			isDefined = true;
		}

		@Override
		public void visitSAMReadPair(SAMReadPair pair) {
			start = pair.getStart();
			end = pair.getEnd();
			isDefined = true;
		}

	}

	<T extends OnGenome> int createLayout(List<T> locusList, int geneHeight) {

		int maxYOffset = 0;
		boolean showLabelsFlag = keepSpaceForLabels && (locusList.size() < 500);
		boolean toContinue = false;

		do {
			toContinue = false;
			maxYOffset = 0;
			locusLayout.clear();

			IntervalRetriever ir = new IntervalRetriever();

			for (OnGenome l : locusList) {

				ir.clear();
				l.accept(ir);
				if (!ir.isDefined)
					continue;

				int x1 = pixelPositionOnWindow(ir.start);
				int x2 = pixelPositionOnWindow(ir.end);

				if (showLabelsFlag) {
					int labelWidth = estimiateLabelWidth(l, geneHeight);
					if (x1 - labelWidth > 0)
						x1 -= labelWidth;
					else
						x2 += labelWidth;
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

				if (blankY > maxYOffset) {
					maxYOffset = blankY;
					if (showLabelsFlag && maxYOffset > 30) {
						showLabelsFlag = false;
						toContinue = true;
						break;
					}
				}
			}
		}
		while (toContinue);

		if (maxYOffset <= 0)
			maxYOffset = 1;

		hasEnoughHeightForLabels = showLabelsFlag;
		return maxYOffset;
	}

	public void setKeepSpaceForLabels(boolean keep) {
		this.keepSpaceForLabels = keep;
	}

	public boolean hasEnoughHeightForLabels() {
		return hasEnoughHeightForLabels;
	}

	public void depthFirstSearch(PrioritySearchTree.Visitor<LocusLayout> visitor) {
		locusLayout.depthFirstSearch(visitor);
	}

	/**
	 * compute the overlapped intervals for the mouse over event
	 * 
	 * @param event
	 * @param xBorder
	 * @return
	 */
	public OnGenome overlappedInterval(int x, int y, int xBorder, int geneHeight) {

		for (LocusLayout gl : locusLayout.rangeQuery(x, Integer.MAX_VALUE, x)) {
			OnGenome g = gl.getLocus();
			int y1 = gl.getYOffset();
			int y2 = y1 + geneHeight;

			if (y1 <= y && y <= y2) {
				int x1 = pixelPositionOnWindow(g.getStart()) - xBorder;
				int x2 = pixelPositionOnWindow(g.getStart() + g.length()) + xBorder;

				if (hasEnoughHeightForLabels) {
					int labelWidth = estimiateLabelWidth(g, geneHeight);
					if (x1 - labelWidth > 0)
						x1 -= labelWidth;
					else
						x2 += labelWidth;
				}

				if (x1 <= x && x <= x2)
					return g;
			}
		}
		return null;

	}

	public int pixelPositionOnWindow(int indexOnGenome) {
		return w.convertToPixelX(indexOnGenome);
	}

	public void clear() {
		locusLayout.clear();
	}

}
