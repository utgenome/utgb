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
// BarGraph.java
// Since: 2009/01/15
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.bio;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Data set for drawing a bar graph
 * 
 * @author leo
 * 
 */
public class BarGraphData implements IsSerializable {

	private static final int AUTO = Integer.MIN_VALUE;

	int xMin = 0;
	int yMin = 0;
	int xMax = AUTO;
	int yMax = AUTO;

	String xLabel = "x";
	String yLabel = "y";

	String graphTitle = "bar graph";

	ArrayList<BarGraphPoint> graphData = new ArrayList<BarGraphPoint>();

	public int getXMin() {
		return xMin;
	}

	public void setXMin(int min) {
		xMin = min;
	}

	public int getYMin() {
		return yMin;
	}

	public void setYMin(int min) {
		yMin = min;
	}

	public int getXMax() {
		return xMax;
	}

	public void setXMax(int max) {
		xMax = max;
	}

	public int getYMax() {
		return yMax;
	}

	public void setYMax(int max) {
		yMax = max;
	}

	public String getXLabel() {
		return xLabel;
	}

	public void setXLabel(String label) {
		xLabel = label;
	}

	public String getYLabel() {
		return yLabel;
	}

	public void setYLabel(String label) {
		yLabel = label;
	}

	public String getGraphTitle() {
		return graphTitle;
	}

	public void setGraphTitle(String graphTitle) {
		this.graphTitle = graphTitle;
	}

	public ArrayList<BarGraphPoint> getGraphData() {
		return graphData;
	}

	public void addGraphData(BarGraphPoint value) {
		this.graphData.add(value);
	}

}
