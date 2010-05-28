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
// GWTGraphCanvas.java
// Since: 2010/05/28
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.canvas;

import org.utgenome.gwt.utgb.client.bio.WigGraphData;
import org.utgenome.gwt.utgb.client.track.TrackWindow;
import org.utgenome.gwt.utgb.client.ui.FormLabel;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.widgetideas.graphics.client.Color;
import com.google.gwt.widgetideas.graphics.client.GWTCanvas;

/**
 * Canvas for drawing bar graph, heat map etc.
 * 
 * @author leo
 * 
 */
public class GWTGraphCanvas extends Composite {
	// widget

	private int windowHeight = 100;
	private FlexTable layoutTable = new FlexTable();
	private GWTCanvas canvas = new GWTCanvas();
	private AbsolutePanel panel = new AbsolutePanel();
	private TrackWindow trackWindow;

	private int indentHeight = 0;

	private float maxValue = 20.0f;
	private float minValue = 0.0f;
	private boolean isLog = false;

	public GWTGraphCanvas() {

		init();
	}

	private void init() {
		layoutTable.setBorderWidth(0);
		layoutTable.setCellPadding(0);
		layoutTable.setCellSpacing(0);

		panel.add(canvas, 0, 0);
		layoutTable.setWidget(0, 1, panel);
		initWidget(layoutTable);

		sinkEvents(Event.ONMOUSEMOVE | Event.ONMOUSEOVER | Event.ONMOUSEDOWN);
	}

	public void clear() {
		canvas.clear();
	}

	public void drawWigGraph(WigGraphData data, Color color) {

		int span = 1;
		if (data.getTrack().containsKey("span")) {
			span = Integer.parseInt(data.getTrack().get("span"));
		}
		// draw data graph
		for (int pos : data.getData().keySet()) {
			float value = data.getData().get(pos);
			if (value == 0.0f)
				continue;

			int x1 = trackWindow.calcXPositionOnWindow(pos);
			float y1 = getYPosition(value);
			int width = trackWindow.calcXPositionOnWindow(pos + span) - x1;

			if (width <= 1) {
				width = 1;
			}

			if (trackWindow.isReverseStrand()) {
				width *= -1.0f;
				x1 = trackWindow.getWindowWidth() - x1;
			}

			float height;
			if (y1 == getYPosition(0.0f)) {
				continue;
			}
			else {
				if (y1 < 0.0f)
					y1 = 0.0f;
				else if (y1 > windowHeight)
					y1 = windowHeight;

				height = getYPosition(0.0f) - y1;
			}

			canvas.setFillStyle(color);
			canvas.fillRect(x1, y1, width, height);
		}
	}

	public void drawFrame(AbsolutePanel panel, int leftMargin) {
		// draw frame
		canvas.setFillStyle(Color.BLACK);
		canvas.fillRect(0, 0, 1, windowHeight);
		canvas.fillRect(trackWindow.getWindowWidth() - 1, 0, 1, windowHeight);
		canvas.fillRect(0, 0, trackWindow.getWindowWidth(), 1);
		canvas.fillRect(0, windowHeight - 1, trackWindow.getWindowWidth(), 1);

		// draw indent line & label
		Indent indent = new Indent(minValue, maxValue);

		FormLabel[] label = new FormLabel[indent.nSteps + 1];
		for (int i = 0; i <= indent.nSteps; i++) {
			float value = indent.getIndentValue(i);

			label[i] = new FormLabel();
			label[i].setStyleName("search-label");
			label[i].setText(indent.getIndentString(i));

			panel.add(label[i], 0, 0);

			int labelPosition = 0;
			if (label[i].getOffsetWidth() < leftMargin)
				labelPosition = leftMargin - label[i].getOffsetWidth();

			panel.setWidgetPosition(label[i], labelPosition, (int) (getYPosition(value) - (label[i].getOffsetHeight() - indentHeight) / 2.0));

			if (getYPosition(value) < 0.0f || getYPosition(value) > windowHeight) {
				panel.remove(label[i]);
				continue;
			}

			// draw indent line
			canvas.setGlobalAlpha(0.2);
			canvas.fillRect(0, getYPosition(value), trackWindow.getWindowWidth(), 1);
			// draw zero line
			canvas.setGlobalAlpha(1.0);
		}

		canvas.fillRect(0, getYPosition(0.0f), trackWindow.getWindowWidth(), 1);
	}

	public class Indent {
		public int exponent = 0;
		public long fraction = 0;

		public int nSteps = 0;

		public float min = 0.0f;
		public float max = 0.0f;

		public Indent(float minValue, float maxValue) {
			if (indentHeight == 0)
				indentHeight = 10;

			min = minValue < maxValue ? minValue : maxValue;
			max = minValue > maxValue ? minValue : maxValue;

			if (isLog) {
				min = getLogValue(min);
				max = getLogValue(max);
			}

			double tempIndentValue = (max - min) / windowHeight * indentHeight;

			if (isLog && tempIndentValue < 1.0)
				tempIndentValue = 1.0;

			fraction = (long) Math.floor(Math.log10(tempIndentValue));
			exponent = (int) Math.ceil(Math.round(tempIndentValue / Math.pow(10, fraction - 3)) / 1000.0);

			if (exponent <= 5)
				;
			//			else if(exponent <= 7)
			//				exponent = 5;
			else {
				exponent = 1;
				fraction++;
			}
			double stepSize = exponent * Math.pow(10, fraction);
			max = (float) (Math.floor(max / stepSize) * stepSize);
			min = (float) (Math.ceil(min / stepSize) * stepSize);

			nSteps = (int) Math.abs((max - min) / stepSize);
		}

		public float getIndentValue(int step) {
			double indentValue = min + (step * exponent * Math.pow(10, fraction));

			if (!isLog)
				return (float) indentValue;
			else if (indentValue == 0.0f)
				return 0.0f;
			else if (indentValue >= 0.0f)
				return (float) Math.pow(2, indentValue - 1);
			else
				return (float) -Math.pow(2, -indentValue - 1);
		}

		public String getIndentString(int step) {
			float indentValue = getIndentValue(step);

			if (indentValue == (int) indentValue)
				return String.valueOf((int) indentValue);
			else {
				int exponent_tmp = (int) Math.ceil(Math.round(indentValue / Math.pow(10, fraction - 3)) / 1000.0);
				int endIndex = String.valueOf(exponent_tmp).length() + 1;
				if (fraction < 0)
					endIndex -= fraction;
				endIndex = Math.min(String.valueOf(indentValue).length(), endIndex);

				return String.valueOf(indentValue).substring(0, endIndex);
			}
		}
	}

	public float getYPosition(float value) {
		if (maxValue == minValue)
			return 0.0f;

		float tempMin = maxValue < minValue ? maxValue : minValue;
		float tempMax = maxValue > minValue ? maxValue : minValue;

		if (isLog) {
			value = getLogValue(value);
			tempMax = getLogValue(tempMax);
			tempMin = getLogValue(tempMin);
		}
		float valueHeight = (value - tempMin) / (tempMax - tempMin) * windowHeight;

		if (maxValue < minValue)
			return valueHeight;
		else
			return windowHeight - valueHeight;
	}

	private float logBase = 2.0f;

	public float getLogBase() {
		return logBase;
	}

	public void setLogBase(float logBase) {
		this.logBase = logBase;
	}

	public float getLogValue(float value) {
		if (Math.log(logBase) == 0.0)
			return value;

		float temp = 0.0f;
		if (value > 0.0f) {
			temp = (float) (Math.log(value) / Math.log(logBase) + 1.0);
			if (temp < 0.0f)
				temp = 0.0f;
		}
		else if (value < 0.0f) {
			temp = (float) (Math.log(-value) / Math.log(logBase) + 1.0);
			if (temp < 0.0f)
				temp = 0.0f;
			temp *= -1.0f;
		}
		return temp;
	}

	public void setTrackWindow(TrackWindow w) {
		trackWindow = w;
	}

	public void setWindowHeight(int windowHeight) {
		this.windowHeight = windowHeight;
		setPixelSize(trackWindow.getWindowWidth(), windowHeight);
	}

	@Override
	public void setPixelSize(int width, int height) {
		canvas.setCoordSize(width, height);
		canvas.setPixelWidth(width);
		canvas.setPixelHeight(height);
		panel.setPixelSize(width, height);
	}

	public int getWindowHeight() {
		return windowHeight;
	}

	public void setPanelHeight(int height) {
		if (height > 0) {
			panel.setHeight(height + "px");
			panel.setWidgetPosition(canvas, 0, indentHeight / 2);
		}
	}

	public float getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(float maxValue) {
		this.maxValue = maxValue;
	}

	public float getMinValue() {
		return minValue;
	}

	public void setMinValue(float minValue) {
		this.minValue = minValue;
	}

	public boolean getIsLog() {
		return isLog;
	}

	public void setIsLog(boolean isLog) {
		this.isLog = isLog;
	}

	public int getIndentHeight() {
		return indentHeight;
	}

	public void setIndentHeight(int indentHeight) {
		this.indentHeight = indentHeight;
	}

}
