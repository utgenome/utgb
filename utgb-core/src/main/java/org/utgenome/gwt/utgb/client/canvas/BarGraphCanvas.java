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
// BarGraphCanvas.java
// Since: 2010/09/27
//
//--------------------------------------
package org.utgenome.gwt.utgb.client.canvas;

import java.util.List;

import org.utgenome.gwt.utgb.client.bio.CompactWIGData;
import org.utgenome.gwt.utgb.client.canvas.GWTGraphCanvas.GraphStyle;
import org.utgenome.gwt.utgb.client.track.TrackWindow;
import org.utgenome.gwt.widget.client.Style;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.widgetideas.graphics.client.Color;
import com.google.gwt.widgetideas.graphics.client.GWTCanvas;

/**
 * Canvas for drawing {@link CompactWIGData}
 * 
 * @author leo
 * 
 */
public class BarGraphCanvas extends Composite {

	private final String DEFAULT_COLOR = "rgba(12,106,193,0.7)";
	private GWTCanvas canvas = new GWTCanvas();
	private TrackWindow window = new TrackWindow();

	public BarGraphCanvas(TrackWindow window, int windowHeight) {
		this.window = window;
		Style.border(canvas, 1, "solid", "gray");
		initWidget(canvas);

		setPixelSize(window.getPixelWidth(), windowHeight);
	}

	public void clear() {
		canvas.clear();
	}

	public TrackWindow getTrackWindow() {
		return window;
	}

	public void setTrackWindow(TrackWindow window, int newPixelX) {
		TrackWindow prev = this.window;
		this.window = window;

		if (!prev.hasSameScaleWith(window)) {
			Style.scaleXwithAnimation(canvas, (double) window.getPixelWidth() / prev.getPixelWidth(), newPixelX, 0.5);
		}
		else {
			Style.scaleX(canvas, 1);
		}
	}

	private float min = 0;
	private float max = 0;
	private float autoScaledMinValue = 0.0f;
	private float autoScaledMaxValue = 0.0f;

	private List<CompactWIGData> graphData;
	private int span = 1;

	public void redraw(GraphStyle style) {
		if (this.graphData == null)
			return;

		canvas.clear();
		draw(this.graphData, style);
	}

	private void setPixelWidth(int width, int height) {
		int pixelWidthWithSpan = window.convertToPixelLength(window.getSequenceLength() + this.span - 1);
		canvas.setPixelSize(pixelWidthWithSpan, height);
		canvas.setCoordSize(pixelWidthWithSpan, height);
	}

	public void setAutoScaleValue(float min, float max) {
		this.autoScaledMinValue = min;
		this.autoScaledMaxValue = max;
	}

	public List<CompactWIGData> getGraphData() {
		return graphData;
	}

	public void setGraphData(List<CompactWIGData> graphData) {
		this.graphData = graphData;
	}

	public void draw(List<CompactWIGData> graphData, GraphStyle style) {

		for (CompactWIGData each : graphData) {
			if (each.getSpan() > span) {
				this.span = each.getSpan();
			}
		}
		setPixelWidth(window.getPixelWidth(), style.windowHeight);

		for (CompactWIGData data : graphData) {
			// get graph color
			Color graphColor = new Color(DEFAULT_COLOR);
			if (style.color.isDefined()) {
				graphColor = new Color(style.color.get());
			}
			else if (data.getTrack().containsKey("color")) {
				String colorStr = data.getTrack().get("color");
				String c[] = colorStr.split(",");
				if (c.length == 3)
					graphColor = new Color(Integer.valueOf(c[0]), Integer.valueOf(c[1]), Integer.valueOf(c[2]));
			}

			// draw graph
			canvas.saveContext();
			canvas.setLineWidth(1.0f);
			canvas.setStrokeStyle(graphColor);

			min = style.autoScale ? autoScaledMinValue : style.minValue;
			max = style.autoScale ? autoScaledMaxValue : style.maxValue;

			float y2 = getYPosition(0.0f, style);

			// draw data graph
			final boolean isReverse = window.isReverseStrand();
			final int pixelWidth = data.getData().length;

			for (int i = 0; i < pixelWidth; ++i) {
				float value = data.getData()[i];
				float y1;
				if (value == 0.0f) {
					if (!style.drawZeroValue)
						continue;
					else {
						y1 = y2 + ((min < max) ? -0.5f : 0.5f);
					}
				}
				else {
					y1 = getYPosition(value, style);
				}

				int x = i;
				if (isReverse) {
					x = pixelWidth - x - 1;
				}

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

	}

	/**
	 * Return pixel Y position of the given graph value
	 * 
	 * @param value
	 * @return
	 */
	float getYPosition(float value, GraphStyle style) {

		if (min == max)
			return 0.0f;

		float tempMin = max < min ? max : min;
		float tempMax = max > min ? max : min;

		if (style.logScale) {
			value = getLogValue(value, style.logBase);
			tempMax = getLogValue(tempMax, style.logBase);
			tempMin = getLogValue(tempMin, style.logBase);
		}
		float valueHeight = (value - tempMin) / (tempMax - tempMin) * style.windowHeight;

		if (style.isReverseYAxis())
			return valueHeight;
		else
			return style.windowHeight - valueHeight;
	}

	public static float getLogValue(float value, float logBase) {
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

}
