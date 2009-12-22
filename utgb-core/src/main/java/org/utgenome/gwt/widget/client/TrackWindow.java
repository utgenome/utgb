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
// WindowTrackPanel.java
// Since: May 1, 2008
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.widget.client;

import org.utgenome.gwt.widget.client.impl.TrackPanelBase;

import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Track panel of the window shape
 * 
 * 
 * @author leo
 * 
 */
public class TrackWindow extends TrackPanelBase {

	private FrameBorder border;
	private ScrollPanel scrollPanel = new ScrollPanel();
	private FocusPanel labelFrame = new FocusPanel();
	private HorizontalPanel titleFrame = new HorizontalPanel();
	private VerticalPanel layoutPanel = new VerticalPanel();
	private String title;
	private Label titleLabel = new Label();

	public TrackWindow() {
		drawWidget();

		initWidget(layoutPanel);
	}

	protected void drawWidget() {

		// track label
		setTrackTitle("Track");
		Style.fontSize(titleLabel, 11);
		Style.fontFamily(titleLabel, "Tahoma, Arial, Verdana");
		Style.set(titleLabel, "color", "white");
		Style.overflowHidden(titleLabel);
		Style.fullBlock(titleLabel);
		labelFrame.add(titleLabel);

		// label frame
		Style.fullWidth(labelFrame);
		Style.cursor(labelFrame, Style.CURSOR_MOVE);

		// title bar
		titleFrame.setHeight("23px");
		
		Style.fullWidth(titleFrame);
		Label windowLeftCorner = new Label();
		windowLeftCorner.setSize("9px", "23px");
		Style.fontSize(windowLeftCorner, 0);
		Style.backgroundImage(windowLeftCorner, "theme/default/tdl.gif");
		Style.backgroundNoRepeat(windowLeftCorner);
		Label windowRightCorner = new Label(); 
		windowRightCorner.setSize("9px", "23px");
		Style.fontSize(windowRightCorner, 0);
		Style.backgroundImage(windowRightCorner, "theme/default/tdr.gif");
		Style.backgroundNoRepeat(windowRightCorner);
		

		HorizontalPanel titleBarFrame = new HorizontalPanel();
		titleBarFrame.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);
		titleBarFrame.setHeight("23px");
		titleBarFrame.add(labelFrame);
		titleBarFrame.setCellWidth(labelFrame, "100%");
		titleBarFrame.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
		titleBarFrame.add(getIconSetPanel());
		Style.fullWidth(titleBarFrame);
		Style.backgroundImage(titleBarFrame, "theme/default/td.gif");
		Style.backgroundRepeatX(titleBarFrame);
		Style.backgroundColor(titleBarFrame, "EEEEEE");


		titleFrame.add(windowLeftCorner);
		titleFrame.add(titleBarFrame);
		titleFrame.setCellWidth(titleBarFrame, "100%");
		titleFrame.add(windowRightCorner);

		
		VerticalPanel borderContent = new VerticalPanel();
		//Style.fullSize(borderContent);
		
		// whole window
		Style.fullSize(layoutPanel);
		layoutPanel.setVerticalAlignment(VerticalPanel.ALIGN_TOP);
		layoutPanel.add(titleFrame);

		// scroll panel
		borderContent.add(scrollPanel);
		//borderContent.setCellHeight(scrollPanel, "100%");
		//borderContent.setCellWidth(scrollPanel, "100%");

		// resize bar
		HorizontalPanel hp = new HorizontalPanel();
		Style.fullWidth(hp);
		//Style.backgroundColor(hp, "D0F0F0");
		Style.backgroundColor(hp, "E0E0E0");
		hp.setHeight("16px");
		hp.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
		hp.add(getResizeButton());
		borderContent.add(hp);

		// window border
		border = new FrameBorder(2, FrameBorder.EAST | FrameBorder.SOUTH | FrameBorder.WEST);
		Style.fullWidth(border);
		border.setWidget(borderContent);
		layoutPanel.add(border);
		
		// set default widget height
		setWidth(200);
		setHeight(100);
	}

	public void setWidth(int pixelWidth) {
		if (pixelWidth >= 0) {
			layoutPanel.setWidth(pixelWidth + "px");
			scrollPanel.setWidth(pixelWidth - 4 + "px");
		}
	}

	public void setHeight(int pixelHeight) {
		if (pixelHeight >= 41) {
			layoutPanel.setWidth(pixelHeight + "px");
			border.setHeight(pixelHeight - 23 + "px");
			scrollPanel.setHeight(pixelHeight - 41 + "px");
		}
	}

	public void addTrackButtonListener(TrackButtonListener listener) {
		iconSet.addTrackButtonListener(listener);
	}

	public Widget getDraggableWidget() {
		return labelFrame;
	}

	public String getTrackTitle() {
		return title;
	}

	public void setTrackTitle(String title) {
		this.title = title;
		titleLabel.setText(title);
		titleLabel.setTitle(title);
	}

	public void setTrackContent(Widget w) {
		scrollPanel.setWidget(w);
	}

}
