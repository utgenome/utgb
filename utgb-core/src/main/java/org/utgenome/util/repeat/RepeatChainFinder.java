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
import java.util.List;

import org.utgenome.UTGBErrorCode;
import org.utgenome.UTGBException;
import org.utgenome.gwt.utgb.client.bio.Interval;
import org.utgenome.gwt.utgb.client.canvas.IntervalTree;
import org.utgenome.gwt.utgb.client.canvas.PrioritySearchTree.ResultHandler;
import org.xerial.ObjectHandlerBase;
import org.xerial.lens.Lens;
import org.xerial.util.graph.AdjacencyList;
import org.xerial.util.graph.DepthFirstSearch;
import org.xerial.util.graph.DepthFirstSearchBase;
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
	public int threshold = 10;

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

		@Override
		public String toString() {
			return String.format("(%d, %d)-(%d, %d)", getStart(), y1, getEnd(), y2);
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

	public static class IntervalChain {

		public final int id;
		public List<Interval2D> chain = new ArrayList<Interval2D>();

		public IntervalChain(int id) {
			this.id = id;
		}

		public void add(Interval2D interval) {
			chain.add(interval);
		}

		public void output() {
			_logger.info(String.format("output repead id:%,d", id));
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

	final IntervalTree<Interval2D> intervalTree = new IntervalTree<Interval2D>();
	final AdjacencyList<Interval2D, Integer> graph = new AdjacencyList<Interval2D, Integer>();

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
			// sweep the intervals
			for (Interval2D current : intervals) {

				// sweep intervals in [-infinity, current.start - threshold)    
				intervalTree.removeBefore(current.getStart() - threshold, new ResultHandler<RepeatChainFinder.Interval2D>() {
					public void handle(Interval2D elem) {
						sweep(elem);
					}

					public boolean toContinue() {
						return true;
					}
				});

				// connect to the closest edge
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
			for (Interval2D each : graph.getNodeLabelSet()) {
				if (!graph.getInEdgeSet(each).isEmpty())
					continue;

				// create chain
				findPath(each, new ArrayList<Interval2D>());
			}

			_logger.info("DFS");
			// find connected components from the graph
			DepthFirstSearch<Interval2D, Integer> dfs = new DepthFirstSearchBase<RepeatChainFinder.Interval2D, Integer>() {

				@Override
				public void discoverNode(Interval2D node) {
					//_logger.info(String.format("find node: %s", node));
				}
			};
			dfs.run(graph);

			_logger.info("done");
		}

	}

	private void findPath(Interval2D startNode, ArrayList<Interval2D> pathStack) {

		List<Interval2D> outNodeList = graph.outNodeList(startNode);
		if (outNodeList.isEmpty()) {
			// report path
			_logger.info(String.format("path: %s", pathStack));
		}
		else {
			// traverse children
			for (Interval2D next : outNodeList) {
				ArrayList<Interval2D> cloneOfPathStack = new ArrayList<Interval2D>();
				cloneOfPathStack.addAll(pathStack);
				cloneOfPathStack.add(startNode);
				findPath(next, cloneOfPathStack);
			}
		}

	}

	void sweep(Interval2D removeTarget) {

	}

}
