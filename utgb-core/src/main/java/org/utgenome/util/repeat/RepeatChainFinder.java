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
// RepeatChain.java
// Since: 2010/10/19
//
//--------------------------------------
package org.utgenome.util.repeat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import org.utgenome.UTGBErrorCode;
import org.utgenome.UTGBException;
import org.utgenome.gwt.utgb.client.bio.Interval;
import org.utgenome.gwt.utgb.client.canvas.IntervalTree;
import org.xerial.ObjectHandlerBase;
import org.xerial.lens.Lens;
import org.xerial.util.graph.AdjacencyList;
import org.xerial.util.graph.Edge;
import org.xerial.util.log.Logger;
import org.xerial.util.opt.Argument;
import org.xerial.util.opt.Option;
import org.xerial.util.opt.OptionParser;
import org.xerial.util.opt.OptionParserException;
import org.xerial.util.text.TabAsTreeParser;

/**
 * Perform chaining of interval fragments
 * 
 * @author leo
 * 
 */
public class RepeatChainFinder {

	private static Logger _logger = Logger.getLogger(RepeatChainFinder.class);

	@Argument
	private File intervalFile;

	@Option(symbol = "t", longName = "threshold", description = "threshold for connecting fragments")
	public int threshold = 100;

	/**
	 * 2D interval
	 * 
	 * @author leo
	 * 
	 */
	public static class Interval2D extends Interval {

		private static final long serialVersionUID = 1L;

		public int y1;
		public int y2;

		public Interval2D() {
		}

		public Interval2D(Interval2D first, Interval2D last) {
			super(first.getStart(), last.getStart());
			this.y1 = first.y1;
			this.y2 = last.y2;
		}

		public int compareTo(Interval2D other) {

			int diff = getStart() - other.getStart();
			if (diff != 0)
				return diff;

			diff = y1 - other.y1;
			if (diff != 0)
				return diff;

			diff = getEnd() - other.getEnd();
			if (diff != 0)
				return diff;

			diff = y2 - other.y2;
			if (diff != 0)
				return diff;

			return 0;
		}

		@Override
		public String toString() {
			return String.format("(%d, %d)-(%d, %d)", getStart(), y1, getEnd(), y2);
		}

		public Interval getStartPoint() {
			return new Interval(getStart(), y1);
		}

		public Interval getEndPoint() {
			return new Interval(getEnd(), y2);
		}

		public boolean isInLowerRightRegion() {
			return getEnd() < getStart();
		}

		public boolean isForward() {
			return y1 <= y2;
		}

		public int forwardDistance(Interval2D other) {
			int xDiff = Math.abs(other.getStart() - this.getEnd());
			//			if (xDiff < 0)
			//				return -1;

			int yDiff = Math.abs(other.y1 - this.y2);
			if (this.isForward()) {
				if (other.isForward())
					return Math.max(xDiff, yDiff);
				else
					return -1;
			}
			else {
				if (other.isForward())
					return -1;
				else
					return Math.max(xDiff, yDiff);
			}
		}

	}

	public static class FlippedInterval2D extends Interval {
		final Interval2D orig;

		public FlippedInterval2D(Interval2D orig) {
			super(orig.y1, orig.y2);
			this.orig = orig;
		}

	}

	public static class IntervalChain implements Comparable<IntervalChain> {

		public List<Interval2D> chain;

		public IntervalChain(List<Interval2D> chain) {
			this.chain = chain;
		}

		public int compareTo(IntervalChain o) {
			return chain.get(0).compareTo(o.chain.get(0));
		}

		public int length() {
			int s = getFirst().getStart();
			int e = getLast().getStart();
			return e - s;
		}

		public Interval2D getFirst() {
			return chain.get(0);
		}

		public Interval2D getLast() {
			return chain.get(chain.size() - 1);
		}

		public Interval2D toRagne() {
			return new Interval2D(getFirst(), getLast());
		}

		@Override
		public String toString() {
			Interval2D first = getFirst();
			Interval2D last = getLast();
			int s = first.getStart();
			int e = last.getStart();
			return String.format("length %,10d: %s - %s", e - s, first.getStartPoint(), last.getEndPoint());
		}

	}

	public static void main(String[] args) {
		RepeatChainFinder finder = new RepeatChainFinder();
		OptionParser opt = new OptionParser(finder);
		try {
			opt.parse(args);
			finder.execute(args);
		}
		catch (OptionParserException e) {
			_logger.error(e);
		}
		catch (Exception e) {
			_logger.error(e);
			e.printStackTrace(System.err);
		}
	}

	final AdjacencyList<Interval2D, Integer> graph = new AdjacencyList<Interval2D, Integer>();
	final ArrayList<Interval2D> rangeList = new ArrayList<Interval2D>();

	public void execute(String[] args) throws Exception {

		if (intervalFile == null)
			throw new UTGBException(UTGBErrorCode.MISSING_FILES, "no input file is given");

		{
			int numChain = 0;
			final List<Interval2D> intervals = new ArrayList<Interval2D>();

			_logger.info("loading intervals..");
			// import (x1, y1, x2, y2) tab-separated data

			BufferedReader in = new BufferedReader(new FileReader(intervalFile));
			try {
				TabAsTreeParser t = new TabAsTreeParser(in);
				List<String> label = new ArrayList<String>();
				label.add("start");
				label.add("y1");
				label.add("end");
				label.add("y2");
				t.setColunLabel(label);
				t.setRowNodeName("entry");
				// load 2D intervals
				Lens.find(Interval2D.class, "entry", new ObjectHandlerBase<Interval2D>() {
					public void handle(Interval2D interval) throws Exception {
						if (!interval.isInLowerRightRegion())
							intervals.add(interval);
					}

					@Override
					public void finish() throws Exception {
						_logger.info(String.format("loaded %d intervals", intervals.size()));
					}
				}, t);
			}
			finally {
				in.close();
			}

			_logger.info("sorting intervals...");
			// sort intervals by their start order
			Collections.sort(intervals);

			_logger.info("sweeping intervals...");
			final IntervalTree<Interval2D> intervalTree = new IntervalTree<Interval2D>();
			// sweep the intervals
			for (Interval2D current : intervals) {

				// sweep intervals in [-infinity, current.start - threshold)    
				intervalTree.removeBefore(current.getStart() - threshold);

				// connect to the close intervals
				for (Interval2D each : intervalTree) {
					final int dist = each.forwardDistance(current);
					if (dist > 0 && dist < threshold) {
						graph.addEdge(each, current, dist);
					}
				}

				intervalTree.add(current);

			}

			if (_logger.isTraceEnabled())
				_logger.trace("graph:\n" + graph.toGraphViz());

			// creating a chain graph
			_logger.info("chaining...");
			for (Interval2D node : graph.getNodeLabelSet()) {
				List<Interval2D> adjacentNodes = new ArrayList<Interval2D>();
				for (Edge each : graph.getOutEdgeSet(node)) {
					adjacentNodes.add(graph.getNodeLabel(each.getDestNodeID()));
				}

				if (_logger.isTraceEnabled())
					_logger.trace(String.format("node %s -> %s", node, adjacentNodes));
			}

			// enumerate paths
			_logger.info("enumerating connected paths...");
			for (Interval2D each : graph.getNodeLabelSet()) {
				if (!graph.getInEdgeSet(each).isEmpty())
					continue;

				// create chain
				findPath(each, each);
			}
			_logger.info("# of paths : " + rangeList.size());

			// remove duplicate
			TreeSet<Interval2D> rangeSet = new TreeSet<Interval2D>(new Comparator<Interval2D>() {
				public int compare(Interval2D o1, Interval2D o2) {
					return o1.compareTo(o2);
				}
			});

			for (Interval2D eachRange : rangeList) {
				rangeSet.add(eachRange);
			}
			_logger.info("# of unique paths : " + rangeSet.size());
			//_logger.info(StringUtil.join(rangeSet, ",\n"));

			// assign the overlapped intervals to the same cluster
			DisjointSet<Interval2D> clusterSet = new DisjointSet<Interval2D>();
			{
				_logger.info("clustring paths in X-coordinate...");
				IntervalTree<Interval2D> xOverlapChecker = new IntervalTree<Interval2D>();
				for (Interval2D each : rangeSet) {
					clusterSet.add(each);
					for (Interval2D overlapped : xOverlapChecker.overlapQuery(each)) {
						clusterSet.link(overlapped, each);
					}
					xOverlapChecker.add(each);
				}
			}
			_logger.info("# of disjoint sets: " + clusterSet.rootNodeSet().size());

			{
				_logger.info("clustring paths in Y-coordinate...");
				IntervalTree<FlippedInterval2D> yOverlapChecker = new IntervalTree<FlippedInterval2D>();
				for (Interval2D each : rangeSet) {
					FlippedInterval2D flip = new FlippedInterval2D(each);
					clusterSet.add(each);
					for (FlippedInterval2D overlapped : yOverlapChecker.overlapQuery(flip)) {
						clusterSet.link(overlapped.orig, flip.orig);
					}
					yOverlapChecker.add(flip);
				}
			}

			_logger.info("# of disjoint sets: " + clusterSet.rootNodeSet().size());

			_logger.info("done");
		}

	}

	private void findPath(Interval2D current, Interval2D pathStart) {

		// TODO cycle detection
		List<Interval2D> outNodeList = graph.outNodeList(current);
		if (outNodeList.isEmpty()) {
			// if this node is a leaf, report the path
			Interval2D range = new Interval2D(pathStart, current);
			rangeList.add(range);
		}
		else {
			// traverse children
			for (Interval2D next : outNodeList) {
				findPath(next, pathStart);
			}
		}

	}

}
