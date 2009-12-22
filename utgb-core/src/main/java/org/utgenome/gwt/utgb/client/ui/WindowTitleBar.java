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
// WindowTitleBar.java
// Since: 2007/11/28
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class WindowTitleBar extends Composite {

	public final FlexTable layoutFrame = new FlexTable();
	public final Label leftCorner = new Label();
	public final FlexTable titleBar = new FlexTable();
	public final Label title = new Label("Title");
	public final Label rightCorner = new Label();
	
	public WindowTitleBar() {

		setupWidget();
		initWidget(layoutFrame);
	}
	
	protected void setupWidget()
	{
		// layout widgets
		layoutFrame.clear();
		layoutFrame.setWidget(0, 0, leftCorner);
		layoutFrame.setWidget(0, 1, titleBar);
		layoutFrame.setWidget(0, 2, rightCorner);

		// layout frame design
		layoutFrame.setCellPadding(0);
		layoutFrame.setCellSpacing(0);
		layoutFrame.getCellFormatter().setWidth(0, 1, "100%");
		layoutFrame.setHeight("100%");
		
		// title bar
		titleBar.setCellPadding(0);
		titleBar.setCellSpacing(0);
		titleBar.setWidth("100%");
		titleBar.setHeight("23px");
		titleBar.setWidget(0, 0, title);
		titleBar.getRowFormatter().setVerticalAlign(0, VerticalPanel.ALIGN_MIDDLE);
		titleBar.getCellFormatter().setWidth(0, 0, "100%");
		CSS.cursor(title, CSS.CURSOR_MOVE);

		// CSS 
        leftCorner.setPixelSize(9, 23);
        rightCorner.setPixelSize(9, 23);
        CSS.fontSize(leftCorner, 0);
        CSS.fontSize(rightCorner, 0);
        CSS.fontSize(title, 12);
        CSS.nowrap(title);
        CSS.backgroundImage(leftCorner, GWT.getModuleBaseURL() + "theme/mac/tl.gif");
        CSS.backgroundImage(titleBar, GWT.getModuleBaseURL() + "theme/mac/t.gif");
        CSS.backgroundImage(rightCorner, GWT.getModuleBaseURL() + "theme/mac/tr.gif");
        CSS.backgroundNoRepeat(leftCorner);
        CSS.backgroundNoRepeat(rightCorner);
	}
	
}




