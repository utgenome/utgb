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

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
import org.xerial.silk.SilkWriter;
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
	private int threshold = 50;

	@Option(symbol = "s", description = "sequence name to read from FASTA")
	private String chr;

	@Option(symbol = "o", description = "output folder")
	private String outFolder;

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

		public Interval startPoint() {
			return new Interval(getStart(), y1);
		}

		public Interval endPoint() {
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

		public void toDotFormat(Writer out) throws IOException {
			out.append(String.format("%d\t%d\t%d\t%d\n", getStart(), y1, getEnd(), y2));
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

	/**
	 * 
	 * @author leo
	 * 
	 */
	public static class FlippedInterval2D extends Interval {
		private static final long serialVersionUID = 1L;
		final Interval2D orig;

		public FlippedInterval2D(Interval2D orig) {
			super(orig.y1, orig.y2);
			this.orig = orig;
		}

	}

	public static class IntervalCluster implements Comparable<IntervalCluster> {

		public int id;
		public final List<Interval2D> component;
		public final int length;

		public IntervalCluster(List<Interval2D> elements) {
			this.component = elements;
			Collections.sort(elements, new Comparator<Interval2D>() {
				public int compare(Interval2D o1, Interval2D o2) {
					return o2.maxLength() - o1.maxLength();
				}
			});

			int maxLength = -1;
			for (Interval2D each : elements) {
				if (maxLength < each.maxLength())
					maxLength = each.maxLength();
			}
			this.length = maxLength;
		}

		public void setId(int id) {
			this.id = id;
		}

		public void validate() throws UTGBException {

			if (component.size() <= 1)
				return;

			for (Interval2D p : component) {
				boolean hasOverlap = false;
				for (Interval2D q : component) {
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
			return component.size();
		}

		public void toDotFile(Writer out) throws IOException {
			for (Interval2D each : component) {
				each.toDotFormat(out);
			}
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

		if (outFolder == null) {
			outFolder = String.format("target/cluster-T%d", threshold);
			new File(outFolder).mkdirs();
		}

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
				findPathsToLeaf(each, each);
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
						Interval key = each.startPoint();
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
						Interval key = each.endPoint();
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

			BufferedWriter pathOut = new BufferedWriter(new FileWriter(new File(outFolder, "paths.dot")));
			try {
				//pathOut.append(String.format("%s\t%s\n", "src", "dest"));
				for (Interval2D each : rangeList) {
					each.toDotFormat(pathOut);
				}
			}
			finally {
				pathOut.flush();
				pathOut.close();
			}

			//_logger.info(StringUtil.join(rangeSet, ",\n"));

			// assign the overlapped intervals to the same cluster
			DisjointSet<Interval2D> clusterSet = new DisjointSet<Interval2D>();
			{
				_logger.info("clustring paths in X-coordinate...");
				IntervalTree<Interval2D> xOverlapChecker = new IntervalTree<Interval2D>();
				for (Interval2D each : rangeList) {
					clusterSet.add(each);
					for (Interval2D overlapped : xOverlapChecker.overlapQuery(each)) {

						if (each.contains(overlapped) || overlapped.contains(each))
							clusterSet.union(overlapped, each);
					}
					xOverlapChecker.add(each);
				}

				_logger.info("# of disjoint sets: " + clusterSet.rootNodeSet().size());
			}

			{
				for (IntervalCluster cluster : createClusters(clusterSet)) {
					BufferedWriter xClusterOut = new BufferedWriter(new FileWriter(new File(outFolder, String.format("x_cluster%03d.dot", cluster.id))));
					try {
						//xClusterOut.append(String.format("%s\t%s\n", "src", "dest"));
						cluster.toDotFile(xClusterOut);
					}
					finally {
						xClusterOut.flush();
						xClusterOut.close();
					}
				}
			}

			{
				_logger.info("clustring paths in Y-coordinate...");
				IntervalTree<FlippedInterval2D> yOverlapChecker = new IntervalTree<FlippedInterval2D>();
				for (Interval2D each : rangeList) {
					FlippedInterval2D flip = new FlippedInterval2D(each);
					for (FlippedInterval2D overlapped : yOverlapChecker.overlapQuery(flip)) {
						if (flip.contains(overlapped) || overlapped.contains(flip))
							clusterSet.union(overlapped.orig, each);
					}
					yOverlapChecker.add(flip);
				}

				{
					for (IntervalCluster cluster : createClusters(clusterSet)) {
						BufferedWriter yClusterOut = new BufferedWriter(new FileWriter(new File(outFolder, String.format("y_cluster%03d.dot", cluster.id))));
						try {
							//yClusterOut.append(String.format("%s\t%s\n", "src", "dest"));
							cluster.toDotFile(yClusterOut);
						}
						finally {
							yClusterOut.flush();
							yClusterOut.close();
						}
					}
				}

				// report clusters
				new SegmentReport().reportCluster(clusterSet);
			}

			_logger.info("done");
		}

	}

	public List<IntervalCluster> createClusters(DisjointSet<Interval2D> clusterSet) {
		List<IntervalCluster> clusterList = new ArrayList<IntervalCluster>();
		Set<Interval2D> clusterRoots = clusterSet.rootNodeSet();

		for (Interval2D root : clusterRoots) {
			IntervalCluster cluster = new IntervalCluster(clusterSet.disjointSetOf(root));
			clusterList.add(cluster);
		}

		Collections.sort(clusterList, new Comparator<IntervalCluster>() {
			public int compare(IntervalCluster o1, IntervalCluster o2) {
				return o2.length - o1.length;
			}
		});

		int clusterCount = 1;
		for (IntervalCluster each : clusterList) {
			each.setId(clusterCount++);
		}
		return clusterList;
	}

	private List<Interval> mergeSegments(List<Interval> intervalList) {
		Collections.sort(intervalList);
		List<Interval> result = new ArrayList<Interval>();
		Interval prev = null;
		for (Interval each : intervalList) {
			if (prev == null) {
				prev = each;
				continue;
			}

			if (prev.hasOverlap(each)) {
				prev = new Interval(prev.getStart(), Math.max(prev.getEnd(), each.getEnd()));
			}
			else {
				result.add(prev);
				prev = each;
			}
		}
		if (prev != null)
			result.add(prev);

		return result;
	}

	private class Segments {
		public List<Interval> segments = new ArrayList<Interval>();
		public List<Interval> reverseSegments = new ArrayList<Interval>();

		public void merge() {
			segments = mergeSegments(segments);
			reverseSegments = mergeSegments(reverseSegments);
		}

		public int max(List<Interval> l) {
			int max = 0;
			for (Interval each : l)
				if (max < each.length())
					max = each.length();
			return max;
		}

		public int maxLength() {
			return Math.max(max(segments), max(reverseSegments));
		}

		public int size() {
			return segments.size() + reverseSegments.size();
		}
	}

	private Segments mergeSegments(IntervalCluster cluster) {

		Segments seg = new Segments();
		for (Interval2D each : cluster.component) {
			int x1 = each.getStart();
			int x2 = each.getEnd();
			seg.segments.add(new Interval(x1, x2));
			if (each.y1 < each.y2)
				seg.segments.add(new Interval(each.y1, each.y2));
			else
				seg.reverseSegments.add(new Interval(each.y1, each.y2));
		}

		seg.merge();

		return seg;
	}

	class SegmentReport {

		int segmentID = 0;
		int clusterID = 0;
		final String sequence;
		final SilkWriter silk;

		public SegmentReport() throws IOException, UTGBException {

			// fasta
			_logger.info("load fasta sequence: " + fastaFile);
			FASTA fasta = new FASTA(fastaFile);
			sequence = fasta.getRawSequence(chr);

			// silk
			File silkFile = new File(outFolder, "cluster-info.silk");
			silk = new SilkWriter(new BufferedOutputStream(new FileOutputStream(silkFile)));
			silk.preamble();
			silk.leaf("date", new Date().toString());
			silk.leaf("threshold", threshold);
			silk.leaf("fasta", fastaFile);
			silk.leaf("dot plot file", intervalFile);
		}

		public void reportCluster(DisjointSet<Interval2D> clusterSet) throws IOException, UTGBException {
			Set<Interval2D> clusterRoots = clusterSet.rootNodeSet();
			_logger.info("# of chains: " + clusterSet.numElements());
			_logger.info("# of disjoint sets: " + clusterRoots.size());

			List<IntervalCluster> clusterList = createClusters(clusterSet);
			for (IntervalCluster cluster : clusterList) {
				clusterID = cluster.id;
				_logger.info(String.format("cluster %d:(%s)", cluster.id, cluster));

				try {
					cluster.validate();
					File outFile = new File(outFolder, String.format("cluster%02d.fa", clusterID));
					_logger.info("output " + outFile);
					BufferedWriter fastaOut = new BufferedWriter(new FileWriter(outFile));
					segmentID = 1;
					Segments seg = mergeSegments(cluster);

					SilkWriter sub = silk.node("cluster").attribute("id", Integer.toString(clusterID))
							.attribute("max length", Integer.toString(seg.maxLength())).attribute("component size", Integer.toString(seg.size()));

					outputSegments(seg.segments, sub, fastaOut, false);
					outputSegments(seg.reverseSegments, sub, fastaOut, true);

					fastaOut.close();
				}
				catch (UTGBException e) {
					_logger.error(e);
				}
			}
			silk.close();
		}

		public void outputSegments(List<Interval> segments, SilkWriter sub, BufferedWriter fastaOut, boolean isReverse) throws IOException, UTGBException {
			for (Interval segment : segments) {
				final int s = segment.getStart();
				final int e = segment.getEnd();
				final int id = segmentID++;
				// output (x1, x2)
				fastaOut.append(String.format(">c%02d-s%04d start:%d, end:%d, len:%d\n", clusterID, id, s, e, e - s));
				String sSeq = sequence.substring(s - 1, e - 1);
				sSeq = CompactACGT.createFromString(sSeq).reverseComplement().toString();
				fastaOut.append(sSeq); // adjust to 0-origin
				fastaOut.append("\n");
				// output silk
				SilkWriter c = sub.node("component").attribute("id", Integer.toString(id)).attribute("x1", Integer.toString(s))
						.attribute("x2", Integer.toString(e)).attribute("strand", isReverse? "-" : "+").attribute("len", Integer.toString(e - s));
				//c.leaf("seq", sSeq);
			}
		}

	}

	private void findPathsToLeaf(Interval2D current, Interval2D pathStart) {

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
				findPathsToLeaf(next, pathStart);
			}
		}

	}

}
