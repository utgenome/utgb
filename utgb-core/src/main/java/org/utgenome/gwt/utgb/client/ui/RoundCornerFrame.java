/*--------------------------------------------------------------------------
 *  Copyright 2007 utgenome.org
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
// WindowFrame.java
// Since: 2007/11/27
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.ui;

import org.utgenome.gwt.widget.client.Style;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;

/**
 * A frame with round corners. You can set arbitrary widgets to this frame by using {@link #setWidgetPanel(Widget)}.
 * 
 * @author leo
 * 
 */
public class RoundCornerFrame extends Composite {
	private final FlexTable layoutFrame = new FlexTable();
	private Border upperFrame;
	private VerticalBar westBar;
	private final FlexTable contentFrame = new FlexTable();
	private SimplePanel panel = new SimplePanel();
	private VerticalBar eastBar;
	private Border lowerFrame;

	public static final int NORTH = 1;
	public static final int EAST = 1 << 1;
	public static final int WEST = 1 << 2;
	public static final int SOUTH = 1 << 3;
	public static final int BORDER_FULL = NORTH | EAST | WEST | SOUTH;

	private float alpha = 1f;
	private int borderFlag;
	private int cornerRadius;
	private String borderColor;

	public RoundCornerFrame(String color) {
		this(color, 0.9f, 4);
	}

	public RoundCornerFrame(String color, float alpha, int borderWidth) {
		this(color, alpha, borderWidth, BORDER_FULL);
	}

	public RoundCornerFrame(String color, float alpha, int borderWidth, int borderFlag) {
		assert (borderFlag <= BORDER_FULL);

		this.borderFlag = borderFlag;
		this.cornerRadius = borderWidth;
		this.borderColor = color;
		this.alpha = alpha;

		drawFrame();

		initWidget(layoutFrame);
	}

	public void setWidgetPanel(Widget w) {
		panel.setWidget(w);
	}

	public void setBorderWidth(int borderWidth) {
		this.cornerRadius = borderWidth;
		drawFrame();
	}

	private void drawFrame() {
		layoutFrame.clear();
		contentFrame.clear();

		layoutFrame.setPixelSize(1, 1);

		upperFrame = new Border(cornerRadius, Border.UPPER, borderFlag, borderColor, alpha);
		lowerFrame = new Border(cornerRadius, Border.LOWER, borderFlag, borderColor, alpha);
		westBar = new VerticalBar(cornerRadius, borderColor, alpha);
		eastBar = new VerticalBar(cornerRadius, borderColor, alpha);

		layoutFrame.setCellPadding(0);
		layoutFrame.setCellSpacing(0);

		contentFrame.setCellPadding(0);
		contentFrame.setCellSpacing(0);
		contentFrame.setSize("100%", "100%");

		if ((borderFlag & NORTH) != 0) {
			layoutFrame.setWidget(0, 0, upperFrame);
		}

		layoutFrame.setWidget(1, 0, contentFrame);
		if ((borderFlag & WEST) != 0) {
			contentFrame.setWidget(0, 0, westBar);
		}

		contentFrame.setWidget(0, 1, panel);
		Style.semiTransparentBackground(panel, borderColor, alpha);

		contentFrame.getCellFormatter().setWidth(0, 1, "100%");
		if ((borderFlag & EAST) != 0) {
			contentFrame.setWidget(0, 2, eastBar);
		}

		if ((borderFlag & SOUTH) != 0) {
			layoutFrame.setWidget(2, 0, lowerFrame);
		}

		CellFormatter cf = layoutFrame.getCellFormatter();
		cf.setHeight(0, 0, cornerRadius + "px");
		cf.setHeight(1, 0, "100%");
		cf.setWidth(1, 0, "100%");
		cf.setHeight(2, 0, cornerRadius + "px");

	}

	public void setFrameColor(String color, float alpha) {
		this.borderColor = color;
		this.alpha = alpha;
		drawFrame();
	}

	static class VerticalBar extends Label {
		public VerticalBar(int radius, String color, float alpha) {
			this.setSize(radius + "px", "100%");
			Style.fontSize(this, 0);
			Style.semiTransparentBackground(this, color, alpha);
		}
	}

}
