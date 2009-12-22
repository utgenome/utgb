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
// HorizontalTrackPanel.java
// Since: Apr 24, 2008
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.widget.client;

import org.utgenome.gwt.widget.client.impl.TrackPanelBase;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Track panel that has the horizontal layout.
 * 
 * @author leo
 * 
 */
public class HorizontalTrackFrame extends TrackPanelBase {

	// layout panels
	private final FlexTable layoutTable = new FlexTable();
	private FocusPanel labelFrame = new FocusPanel();
	private SimplePanel trackInfoPanel = new SimplePanel();
	private String trackTitle;
	private FrameBorder trackInfoFrame = new FrameBorder(2, FrameBorder.WEST | FrameBorder.SOUTH);
	private Label trackLabel = new Label();
	private int buttonVisibilityFlag = ~0; // show all buttons in default

	private ScrollPanel scrollPanel = new ScrollPanel();

	public HorizontalTrackFrame() {

		enableResizeWidth(false);

		drawWidget();
		initWidget(layoutTable);
	}

	public Widget getDraggableWidget() {
		return labelFrame;
	}

	protected void drawWidget() {

		setTrackTitle("Track");

		Style.fullWidth(layoutTable);
		layoutTable.setCellPadding(0);
		layoutTable.setCellSpacing(0);

		// label
		Style.fontSize(trackLabel, 11);
		Style.fontFamily(trackLabel, "Arial, Tahoma, Verdana");
		Style.set(trackLabel, "color", "white");
		Style.margin(trackLabel, Style.LEFT, 2);
		Style.verticalAlign(trackLabel, "middle");
		Style.overflowHidden(trackLabel);

		// label frame
		labelFrame.add(trackLabel);
		Style.cursor(labelFrame, Style.CURSOR_MOVE);

		// title bar (label + icon)
		HorizontalPanel hp = new HorizontalPanel();
		Style.fullWidth(hp);
		hp.add(labelFrame);
		hp.setCellWidth(labelFrame, "100%");
		hp.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		hp.add(getIconSetPanel());

		// track info panel
		Style.fullSize(trackInfoPanel);
		Style.backgroundImage(trackInfoPanel, "theme/default/track-base.png");
		Style.backgroundColor(trackInfoPanel, "00a7bf");
		trackInfoPanel.add(hp);

		// track info border
		trackInfoFrame.setWidget(trackInfoPanel);
		trackInfoFrame.setSize("180px", "100%");

		// layout (track info | track content | resize bar)
		layoutTable.setWidget(0, 0, trackInfoFrame);

		// scroll panel
		layoutTable.setWidget(0, 1, scrollPanel);
		layoutTable.getCellFormatter().setHeight(0, 1, "100%");
		layoutTable.getCellFormatter().setWidth(0, 1, "100%");

		// resize bar
		VerticalPanel resizeBarPanel = new VerticalPanel();
		Style.fullSize(resizeBarPanel);
		Style.backgroundColor(resizeBarPanel, "D0F0F0");
		resizeBarPanel.setVerticalAlignment(VerticalPanel.ALIGN_BOTTOM);
		resizeBarPanel.add(getResizeButton());

		// resize bar frame
		FrameBorder resizeBarBorder = new FrameBorder(2, FrameBorder.EAST | FrameBorder.SOUTH);
		resizeBarBorder.setSize("16px", "100%");
		resizeBarBorder.setWidget(resizeBarPanel);
		layoutTable.getCellFormatter().setHeight(0, 2, "100%");
		layoutTable.getCellFormatter().setWidth(0, 2, "100%");
		layoutTable.setWidget(0, 2, resizeBarBorder);

		// Style.hideHorizontalScrollBar(scrollPanel);
	}

	@Override
	public void setHeight(int pixelHeight) {
		if (pixelHeight >= 20) {
			layoutTable.setHeight(pixelHeight + "px");
			scrollPanel.setHeight(pixelHeight + "px");
		}
	}

	@Override
	public void setWidth(int pixelWidth) {
		if (pixelWidth >= 200) {
			layoutTable.setWidth(pixelWidth + "px");
			scrollPanel.setWidth(pixelWidth - 200 + "px");
		}
	}

	/**
	 * Checks the visibility of the button
	 * 
	 * @param buttonType
	 * @return
	 */
	public boolean isVisible(int buttonType) {
		return (buttonVisibilityFlag & buttonType) != 0;
	}

	public String getTrackTitle() {
		return trackTitle;
	}

	public void setTrackTitle(String title) {
		this.trackTitle = title;
		if (trackTitle.length() > 20)
			trackLabel.setText(trackTitle.substring(0, 20) + "...");
		else
			trackLabel.setText(trackTitle);

		trackLabel.setTitle(trackTitle);
	}

	public void setTrackContent(Widget w) {
		scrollPanel.setWidget(w);
	}

}
