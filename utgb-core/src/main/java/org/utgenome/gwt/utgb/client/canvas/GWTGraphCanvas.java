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

import java.util.ArrayList;
import java.util.List;

import org.utgenome.gwt.utgb.client.bio.CompactWIGData;
import org.utgenome.gwt.utgb.client.track.TrackWindow;
import org.utgenome.gwt.widget.client.Style;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.widgetideas.graphics.client.Color;
import com.google.gwt.widgetideas.graphics.client.GWTCanvas;

/**
 * Canvas for drawing bar graph, heat map etc.
 * 
 * @author yoshimura
 * @author leo
 * 
 */
public class GWTGraphCanvas extends Composite {
	// widget

	private int windowHeight = 100;
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
		canvas.setBackgroundColor(new Color(255, 255, 255, 0f));

		Style.padding(panel, 0);
		Style.margin(panel, 0);

		panel.add(canvas, 0, 0);
		initWidget(panel);

		//sinkEvents(Event.ONMOUSEMOVE | Event.ONMOUSEOVER | Event.ONMOUSEDOWN);
	}

	public void clear() {
		canvas.clear();
		for (Label each : graphLabels) {
			each.removeFromParent();
		}
		graphLabels.clear();
	}

	public void drawWigGraph(CompactWIGData data, Color color) {

		canvas.saveContext();

		canvas.setLineWidth(1.0f);
		canvas.setStrokeStyle(color);

		// draw data graph
		for (int i = 0; i < trackWindow.getWindowWidth(); ++i) {
			float value = data.getData()[i];
			if (value == 0.0f)
				continue;

			float y1 = getYPosition(value);
			int x = i;
			if (trackWindow.isReverseStrand()) {
				x = trackWindow.getWindowWidth() - x - 1;
			}

			float height;
			if (y1 == getYPosition(0.0f)) {
				continue;
			}

			if (y1 < 0.0f)
				y1 = 0.0f;
			else if (y1 > windowHeight)
				y1 = windowHeight;

			float y2 = getYPosition(0.0f);

			canvas.saveContext();
			canvas.beginPath();
			canvas.translate(x + 0.5f, 0);
			canvas.moveTo(0, y1);
			canvas.lineTo(0, y2);
			canvas.stroke();
			canvas.restoreContext();
		}
		canvas.restoreContext();
	}

	private List<Label> graphLabels = new ArrayList<Label>();

	public void drawFrame() {

		// draw frame
		canvas.saveContext();
		canvas.setStrokeStyle(new Color(0, 0, 0, 0.5f));
		canvas.setLineWidth(1.0f);
		canvas.beginPath();
		canvas.rect(0, 0, trackWindow.getWindowWidth(), windowHeight);
		canvas.stroke();
		canvas.restoreContext();

		// draw indent line & label
		Indent indent = new Indent(minValue, maxValue);

		{
			canvas.saveContext();
			canvas.setStrokeStyle(Color.BLACK);
			canvas.setGlobalAlpha(0.2f);
			canvas.setLineWidth(0.5f);
			for (int i = 0; i <= indent.nSteps; i++) {
				float value = indent.getIndentValue(i);
				// draw indent line
				canvas.saveContext();
				canvas.beginPath();
				canvas.translate(0, getYPosition(value) + 0.5d);
				canvas.moveTo(0d, 0d);
				canvas.lineTo(trackWindow.getWindowWidth(), 0);
				canvas.stroke();
				canvas.restoreContext();
			}
			{
				// draw zero line
				canvas.saveContext();
				canvas.beginPath();
				canvas.translate(0, getYPosition(0f));
				canvas.moveTo(0, 0);
				canvas.lineTo(trackWindow.getWindowWidth(), 0);
				canvas.stroke();
				canvas.restoreContext();
			}

			canvas.restoreContext();
		}

		int fontHeight = 10;
		for (int i = 0; i <= indent.nSteps; i++) {
			float value = indent.getIndentValue(i);
			Label label = new Label(indent.getIndentString(i));

			Style.fontSize(label, fontHeight);
			Style.textAlign(label, "left");
			Style.fontColor(label, "#006699");

			int labelX = 1;
			int labelY = (int) (getYPosition(value) - fontHeight);

			if (labelY > windowHeight)
				continue;

			graphLabels.add(label);
			panel.add(label, labelX, labelY);

			//			if (label[i].getOffsetWidth() < leftMargin)
			//				labelPosition = leftMargin - label[i].getOffsetWidth();

			//panel.setWidgetPosition(label[i], labelX, 

			//			if (getYPosition(value) < 0.0f || getYPosition(value) > windowHeight) {
			//				panel.remove(label[i]);
			//				continue;
			//			}

		}

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
