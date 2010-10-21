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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import org.utgenome.UTGBErrorCode;
import org.utgenome.UTGBException;
import org.utgenome.format.fasta.CompactACGT;
import org.utgenome.format.fasta.FASTA;
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

	@Argument(index = 0)
	private File intervalFile;
	@Argument(index = 1)
	private File fastaFile;

	@Option(symbol = "t", longName = "threshold", description = "threshold for connecting fragments")
	public int threshold = 50;

	@Option(symbol = "s", description = "sequence name to read from FASTA")
	public String chr;

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

		public Interval2D(int x1, int y1, int x2, int y2) {
			super(x1, x2);
			this.y1 = y1;
			this.y2 = y2;
		}

		public Interval2D(Interval2D first, Interval2D last) {
			super(first.getStart(), last.getEnd());
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

		public int maxLength() {
			return Math.max(super.length(), Math.abs(y2 - y1));
		}

		@Override
		public String toString() {
			return String.format("max len:%d, (%d, %d)-(%d, %d)", maxLength(), getStart(), y1, getEnd(), y2);
		}

		public Interval getStartPoint() {
			return new Interval(getStart(), y1);
		}

		public Interval getEndPoint() {
			return new Interval(getEnd(), y2);
		}

		public boolean isInLowerRightRegion() {
			return y1 < getStart();
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

		@Override
		public boolean equals(Object o) {
			Interval2D other = Interval2D.class.cast(o);
			return this.getStart() == other.getStart() && this.getEnd() == other.getEnd() && this.y1 == other.y1 && this.y2 == other.y2;
		}

		@Override
		public int hashCode() {
			int hash = 3;
			hash += 137 * this.getStart();
			hash += 137 * this.getEnd();
			hash += 137 * this.y1;
			hash += 137 * this.y2;
			return hash;
		}

	}

	public static class FlippedInterval2D extends Interval {
		private static final long serialVersionUID = 1L;
		final Interval2D orig;

		public FlippedInterval2D(Interval2D orig) {
			super(orig.y1, orig.y2);
			this.orig = orig;
		}

	}

	public static class IntervalCluster implements Comparable<IntervalCluster> {

		public final List<Interval2D> elements;
		public final int length;

		public IntervalCluster(List<Interval2D> elements) {
			this.elements = elements;

			int maxLength = -1;
			for (Interval2D each : elements) {
				if (maxLength < each.maxLength())
					maxLength = each.maxLength();
			}
			this.length = maxLength;
		}

		public void validate() throws UTGBException {

			if (elements.size() <= 1)
				return;

			for (Interval2D p : elements) {
				boolean hasOverlap = false;
				for (Interval2D q : elements) {
					if (p == q)
						continue;
					if (p.hasOverlap(q)) {
						hasOverlap = true;
						break;
					}
					else {
						Interval py = new Interval(p.y1, p.y2);
						Interval qy = new Interval(q.y1, q.y2);
						if (py.hasOverlap(qy)) {
							hasOverlap = true;
							break;
						}
					}
				}
				if (!hasOverlap)
					throw new UTGBException(UTGBErrorCode.ValidationFailure);
			}
		}

		public int compareTo(IntervalCluster o) {
			return this.length - o.length;
		}

		public int size() {
			return elements.size();
		}

		@Override
		public String toString() {
			return String.format("length:%d, size:%d", length, size());
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

			_logger.info("chain threshold: " + threshold);

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
			{
				final IntervalTree<Interval2D> intervalTree = new IntervalTree<Interval2D>();
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

			// remove paths sharing the same start or end points
			{
				//Collections.sort(rangeList);
				TreeMap<Interval, Interval2D> longestRange = new TreeMap<Interval, Interval2D>(new Comparator<Interval>() {
					public int compare(Interval o1, Interval o2) {
						int diff = o1.getStart() - o2.getStart();
						if (diff == 0)
							return o1.getEnd() - o2.getEnd();
						else
							return diff;
					}
				});
				{
					// merge paths sharing start points
					for (Interval2D each : rangeList) {
						Interval key = each.getStartPoint();
						if (longestRange.containsKey(key)) {
							Interval2D prev = longestRange.get(key);
							if (prev.maxLength() < each.maxLength()) {
								longestRange.remove(key);
								longestRange.put(key, each);
							}
						}
						else
							longestRange.put(key, each);
					}
				}
				rangeList.clear();
				rangeList.addAll(longestRange.values());

				longestRange.clear();
				{
					// merge paths sharing end points
					for (Interval2D each : rangeList) {
						Interval key = each.getEndPoint();
						if (longestRange.containsKey(key)) {
							Interval2D prev = longestRange.get(key);
							if (prev.maxLength() < each.maxLength()) {
								longestRange.remove(key);
								longestRange.put(key, each);
							}
						}
						else
							longestRange.put(key, each);
					}
				}

				rangeList.clear();
				rangeList.addAll(longestRange.values());

			}
			_logger.info("# of unique paths : " + rangeList.size());
			//_logger.info(StringUtil.join(rangeSet, ",\n"));

			// assign the overlapped intervals to the same cluster
			DisjointSet<Interval2D> clusterSet = new DisjointSet<Interval2D>();
			{
				_logger.info("clustring paths in X-coordinate...");
				IntervalTree<Interval2D> xOverlapChecker = new IntervalTree<Interval2D>();
				for (Interval2D each : rangeList) {
					clusterSet.add(each);
					for (Interval2D overlapped : xOverlapChecker.overlapQuery(each)) {
						clusterSet.union(overlapped, each);
					}
					xOverlapChecker.add(each);
				}

				_logger.info("# of disjoint sets: " + clusterSet.rootNodeSet().size());
			}

			{
				_logger.info("clustring paths in Y-coordinate...");
				IntervalTree<FlippedInterval2D> yOverlapChecker = new IntervalTree<FlippedInterval2D>();
				for (Interval2D each : rangeList) {
					FlippedInterval2D flip = new FlippedInterval2D(each);
					for (FlippedInterval2D overlapped : yOverlapChecker.overlapQuery(flip)) {
						clusterSet.union(overlapped.orig, each);
					}
					yOverlapChecker.add(flip);
				}

				// report clusters
				reportCluster(clusterSet);
			}

			_logger.info("done");
		}

	}

	private void reportCluster(DisjointSet<Interval2D> clusterSet) throws IOException, UTGBException {
		Set<Interval2D> clusterRoots = clusterSet.rootNodeSet();
		_logger.info("# of chains: " + clusterSet.numElements());

		_logger.info("# of disjoint sets: " + clusterRoots.size());
		int clusterCount = 1;
		List<IntervalCluster> clusterList = new ArrayList<IntervalCluster>();
		for (Interval2D root : clusterRoots) {
			IntervalCluster cluster = new IntervalCluster(clusterSet.disjointSetOf(root));
			clusterList.add(cluster);
		}

		Collections.sort(clusterList, new Comparator<IntervalCluster>() {
			public int compare(IntervalCluster o1, IntervalCluster o2) {
				return o2.length - o1.length;
			}
		});

		_logger.info("load fasta sequence: " + fastaFile);
		FASTA fasta = new FASTA(fastaFile);
		String sequence = fasta.getRawSequence(chr);

		for (IntervalCluster cluster : clusterList) {
			int clusterID = clusterCount++;
			_logger.info(String.format("cluster %d:(%s)", clusterID, cluster));
			try {
				cluster.validate();
				File outFile = new File("target", String.format("cluster%d.fa", clusterID));
				_logger.info("output " + outFile);
				BufferedWriter fastaOut = new BufferedWriter(new FileWriter(outFile));
				int segmentID = 1;

				// TODO collect subsequence from both (x1, x2) and (y1, y2)
				for (Interval2D segment : cluster.elements) {

				}

				for (Interval2D segment : cluster.elements) {
					final int s = segment.getStart();
					final int e = segment.getEnd();
					fastaOut.append(String.format(">segment%d (%d,%d):%d => (%d,%d):%d\n", segmentID++, s, e, e - s, segment.y1, segment.y2, segment.y2
							- segment.y1));
					if (segment.y1 < segment.y2) {
						fastaOut.append(sequence.substring(segment.y1, segment.y2));
					}
					else {
						// reverse complement
						String seq = sequence.substring(segment.y2, segment.y1);
						String rc = CompactACGT.createFromString(seq).reverseComplement().toString();
						if (seq.length() != rc.length())
							throw new UTGBException(UTGBErrorCode.AssertionFailure, "reverse complement has an wrong length");
						fastaOut.append(rc);
					}
					fastaOut.append("\n");
				}
				fastaOut.close();
			}
			catch (UTGBException e) {
				_logger.error(e);
			}
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
