/*--------------------------------------------------------------------------
 *  Copyright 2008 utgenome.org
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
// utgb-widget Project
//
// WindowFrame.java
// Since: Apr 30, 2008
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.widget.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;

/**
 * Bordered Window
 * 
 * @author leo
 * 
 */
public class FrameBorder extends Composite {

	public static final int NORTH = 1;
	public static final int EAST = 1 << 1;
	public static final int WEST = 1 << 2;
	public static final int SOUTH = 1 << 3;
	public static final int BORDER_FULL = NORTH | EAST | WEST | SOUTH;

	private final FlexTable layoutFrame = new FlexTable();
	private HorizontalBorder upperFrame;
	private VerticalBar westBar;
	private final FlexTable contentFrame = new FlexTable();
	private Widget widget = new SimplePanel();
	private VerticalBar eastBar;
	private HorizontalBorder lowerFrame;

	private int borderFlag;
	private int cornerRadius;
	private String borderColor;
	private String borderColorDark;

	public FrameBorder(String color) {
		this(color, 4);
	}

	public FrameBorder(String color, int borderWidth) {
		this(color, color, borderWidth, BORDER_FULL);
	}

	public FrameBorder(int borderWidth, int borderFlag) {
		this(UTGBDesignFactory.getWindowBorderColor(), UTGBDesignFactory.getWindowBorderColorDark(), borderWidth, borderFlag);
	}

	public FrameBorder(String borderColor, String borderColorDark, int borderWidth, int borderFlag) {
		assert (borderFlag <= BORDER_FULL);

		this.borderFlag = borderFlag;
		this.cornerRadius = borderWidth;
		this.borderColor = borderColor;
		this.borderColorDark = borderColorDark;

		drawFrame();

		initWidget(layoutFrame);
	}

	public void setWidget(Widget w) {
		this.widget = w;
		contentFrame.setWidget(0, 1, widget);
	}

	public void setBorderWidth(int borderWidth) {
		this.cornerRadius = borderWidth;
		drawFrame();
	}

	private void drawFrame() {
		layoutFrame.clear();
		contentFrame.clear();

		layoutFrame.setPixelSize(100, 50);

		upperFrame = new HorizontalBorder(cornerRadius, HorizontalBorder.UPPER, borderFlag, borderColor);
		lowerFrame = new HorizontalBorder(cornerRadius, HorizontalBorder.LOWER, borderFlag, borderColorDark);
		westBar = new VerticalBar(cornerRadius, borderColor);
		eastBar = new VerticalBar(cornerRadius, borderColorDark);

		layoutFrame.setCellPadding(0);
		layoutFrame.setCellSpacing(0);
		// layoutFrame.setBorderWidth(1);

		contentFrame.setCellPadding(0);
		contentFrame.setCellSpacing(0);
		// contentFrame.setBorderWidth(1);
		contentFrame.setSize("100%", "100%");

		CellFormatter cf = layoutFrame.getCellFormatter();
		cf.setHeight(1, 0, "100%");
		cf.setWidth(1, 0, "100%");

		if ((borderFlag & NORTH) != 0) {
			layoutFrame.setWidget(0, 0, upperFrame);
			cf.setHeight(0, 0, cornerRadius + "px");
		}

		layoutFrame.setWidget(1, 0, contentFrame);
		if ((borderFlag & WEST) != 0) {
			contentFrame.setWidget(0, 0, westBar);
		}
		contentFrame.setWidget(0, 1, widget);
		contentFrame.getCellFormatter().setWidth(0, 1, "100%");
		if ((borderFlag & EAST) != 0) {
			contentFrame.setWidget(0, 2, eastBar);
		}

		if ((borderFlag & SOUTH) != 0) {
			layoutFrame.setWidget(2, 0, lowerFrame);
			cf.setHeight(2, 0, cornerRadius + "px");
		}

	}

	static class VerticalBar extends Label {
		public VerticalBar(int radius, String color) {
			this.setSize(radius + "px", "100%");
			Style.fontSize(this, 0);
			Style.backgroundColor(this, color);
		}
	}

}
