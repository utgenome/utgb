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
// utgb-core Project
//
// UTGBTrack.java
// Since: Feb 7, 2008
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.lib;

import org.utgenome.gwt.utgb.client.track.TrackBase;
import org.utgenome.gwt.utgb.client.track.TrackFrame;
import org.utgenome.gwt.utgb.client.track.TrackGroup;
import org.utgenome.gwt.utgb.client.track.TrackWindow;
import org.utgenome.gwt.widget.client.Style;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class UTGBTrack extends TrackBase {

	private static final int INDEX_PANEL_WIDTH = 100;
	private FlexTable layoutFrame = new FlexTable();
	private SimplePanel indexPanel = new SimplePanel();
	private SimplePanel graphicPanel = new SimplePanel();

	public UTGBTrack() {
		super("UTGB Track");

		init();
	}

	public void init() {
		layoutFrame.setHeight("100%");
		layoutFrame.setCellPadding(0);
		layoutFrame.setCellSpacing(0);
		Style.borderCollapse(layoutFrame);

		Style.fontSize(indexPanel, 0);
		indexPanel.setWidth(INDEX_PANEL_WIDTH + "px");
		Style.fontSize(graphicPanel, 0);
		Style.fullWidth(graphicPanel);

		// layout widgets
		layoutFrame.setWidget(0, 0, indexPanel);
		layoutFrame.setWidget(1, 0, graphicPanel);

	}

	public Widget getWidget() {
		return layoutFrame;
	}

	public void setUp(TrackFrame trackFrame, TrackGroup group) {
		TrackWindow window = group.getTrackWindow();
		int width = window.getWindowWidth();
		layoutFrame.setWidth(width + "px");

	}

}
