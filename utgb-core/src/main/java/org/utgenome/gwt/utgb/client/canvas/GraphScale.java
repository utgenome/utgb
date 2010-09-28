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
// GraphScale.java
// Since: 2010/09/28
//
//--------------------------------------
package org.utgenome.gwt.utgb.client.canvas;

import java.util.ArrayList;
import java.util.List;

import org.utgenome.gwt.utgb.client.UTGBEntryPointBase;
import org.utgenome.gwt.utgb.client.canvas.GWTGraphCanvas.GraphStyle;
import org.utgenome.gwt.utgb.client.track.TrackWindow;
import org.utgenome.gwt.widget.client.Style;

import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.graphics.client.Color;
import com.google.gwt.widgetideas.graphics.client.GWTCanvas;

/**
 * Scale for graph data
 * 
 * @author leo
 * 
 */
public class GraphScale extends Composite {

	private AbsolutePanel panel = new AbsolutePanel();
	private GWTCanvas frameCanvas = new GWTCanvas();
	private List<Widget> graphLabels = new ArrayList<Widget>();

	public GraphScale() {
		panel.add(frameCanvas, 0, 0);
		initWidget(panel);
	}

	public void clearScaleBar() {
		frameCanvas.clear();
	}

	public void clearScaleLabel() {
		for (Widget each : graphLabels) {
			each.removeFromParent();
		}
		graphLabels.clear();
	}

	public void clear() {
		clearScaleBar();
		clearScaleLabel();
	}

	public void draw(GraphStyle style, TrackWindow viewWindow, float min, float max) {

		// set pixel width & size
		int width = viewWindow.getPixelWidth();
		int height = style.windowHeight;
		panel.setPixelSize(width, height);
		frameCanvas.setPixelSize(width, height);
		frameCanvas.setCoordSize(width, height);

		ScalePainter scalePainter = new ScalePainter(style, viewWindow, min, max);
		scalePainter.draw();

	}

	class ScalePainter {
		private GraphStyle style;
		private TrackWindow viewWindow;
		private final Scale scale;
		private final Indent indent;

		public ScalePainter(GraphStyle style, TrackWindow window, float autoScaledMin, float autoScaledMax) {
			this.style = style;
			this.viewWindow = window;
			scale = new Scale(style, autoScaledMin, autoScaledMax);
			indent = createIndent();
		}

		public void draw() {
			clear();
			if (style.drawScale)
				drawScale();

			if (style.showScaleLabel)
				drawScaleLabel();
		}

		public void drawScale() {

			// draw frame
			frameCanvas.saveContext();
			frameCanvas.setStrokeStyle(new Color(0, 0, 0, 0.5f));
			frameCanvas.setLineWidth(1.0f);
			frameCanvas.beginPath();
			frameCanvas.rect(0, 0, viewWindow.getPixelWidth(), style.windowHeight);
			frameCanvas.stroke();
			frameCanvas.restoreContext();

			// draw indent line & label
			frameCanvas.saveContext();
			frameCanvas.setStrokeStyle(Color.BLACK);
			frameCanvas.setGlobalAlpha(0.2f);
			frameCanvas.setLineWidth(0.5f);
			for (int i = 0; i <= indent.nSteps; i++) {
				float value = indent.getIndentValue(i);
				// draw indent line
				frameCanvas.saveContext();
				frameCanvas.beginPath();
				frameCanvas.translate(0, scale.getYPosition(value) + 0.5d);
				frameCanvas.moveTo(0d, 0d);
				frameCanvas.lineTo(viewWindow.getPixelWidth(), 0);
				frameCanvas.stroke();
				frameCanvas.restoreContext();
			}
			{
				// draw zero line
				frameCanvas.saveContext();
				frameCanvas.beginPath();
				frameCanvas.translate(0, scale.getYPosition(0f));
				frameCanvas.moveTo(0, 0);
				frameCanvas.lineTo(viewWindow.getPixelWidth(), 0);
				frameCanvas.stroke();
				frameCanvas.restoreContext();
			}

			frameCanvas.restoreContext();
		}

		public Indent createIndent() {
			return new Indent(scale.getMin(), scale.getMax(), style);
		}

		public void drawScaleLabel() {

			int fontHeight = 10;

			for (int i = 0; i <= indent.nSteps; i++) {
				float value = indent.getIndentValue(i);
				String labelString = indent.getIndentString(i);
				Label label = new Label(labelString);
				label.setTitle(labelString);
				Style.fontSize(label, fontHeight);
				Style.textAlign(label, "left");
				Style.fontColor(label, "#003366");

				int labelX = 1;
				int labelY = (int) (scale.getYPosition(value) - (fontHeight / 2.0f) - 1);

				if (labelY < 0 && labelY > -fontHeight)
					labelY = -1;

				if (labelY > style.windowHeight - fontHeight) {
					labelY = style.windowHeight - fontHeight;
				}

				graphLabels.add(label);
				panel.add(label, labelX, labelY);
			}
		}

		public class Indent {
			public int exponent = 0;
			public long fraction = 0;

			public int nSteps = 0;

			public float min = 0.0f;
			public float max = 0.0f;

			private GraphStyle style;

			public Indent(float minValue, float maxValue, GraphStyle style) {
				this.style = style;

				final int indentHeight = 10;

				min = minValue < maxValue ? minValue : maxValue;
				max = minValue > maxValue ? minValue : maxValue;

				if (style.logScale) {
					min = scale.getLogValue(min);
					max = scale.getLogValue(max);
				}

				try {
					double tempIndentValue = (max - min) / style.windowHeight * indentHeight;

					if (style.logScale && tempIndentValue < 1.0)
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
				catch (JavaScriptException e) {
					UTGBEntryPointBase.showErrorMessage(e.getMessage());
				}

			}

			public float getIndentValue(int step) {
				double indentValue = min + (step * exponent * Math.pow(10, fraction));

				if (!style.logScale)
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

	}

}
