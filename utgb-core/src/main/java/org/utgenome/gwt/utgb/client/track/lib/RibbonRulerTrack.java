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
// RibbonRulerTrack.java
// Since: Jul 13, 2009
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.lib;

import org.utgenome.gwt.utgb.client.canvas.RibbonRuler;
import org.utgenome.gwt.utgb.client.track.Track;
import org.utgenome.gwt.utgb.client.track.TrackBase;
import org.utgenome.gwt.utgb.client.track.TrackFrame;
import org.utgenome.gwt.utgb.client.track.TrackGroup;
import org.utgenome.gwt.utgb.client.track.TrackWindow;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

/**
 * Track for displaying ribbon coordinates
 * 
 * @author leo
 * 
 */
public class RibbonRulerTrack extends TrackBase {
	FlexTable layoutTable = new FlexTable();
	RibbonRuler ribbon = new RibbonRuler();

	public static TrackFactory factory() {
		return new TrackFactory() {
			public Track newInstance() {
				return new RibbonRulerTrack();
			}
		};
	}

	public RibbonRulerTrack() {
		super("Ribbon Track");

		// prepare the widgets
		layoutTable.setCellPadding(0);
		layoutTable.setCellSpacing(0);
		layoutTable.setBorderWidth(0);
		layoutTable.setWidth("100%");
		layoutTable.getCellFormatter().setWidth(0, 0, "100px");
		layoutTable.setWidget(0, 1, ribbon);
	}

	public Widget getWidget() {
		return layoutTable;
	}

	@Override
	public void setUp(TrackFrame trackFrame, TrackGroup group) {

		ribbon.setWindow(group.getTrackWindow());
	}

	@Override
	public void onChangeTrackWindow(TrackWindow newWindow) {
		ribbon.setWindow(newWindow);
	}

}
