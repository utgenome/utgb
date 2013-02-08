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
// TrackGenTrack.java
// Since: 2009/05/21
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.lib;

import org.utgenome.gwt.utgb.client.track.Track;
import org.utgenome.gwt.utgb.client.track.TrackBase;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

public class TrackGenTrack extends TrackBase {

	public static TrackFactory factory() {
		return new TrackFactory() {
			public Track newInstance() {
				return new TrackGenTrack();
			}
		};
	}

	final FlexTable layoutTable = new FlexTable();
	final ListBox trackSelector = new ListBox();

	public TrackGenTrack() {
		super("Track Factory");

		trackSelector.addItem("DAS Track", "das");
		trackSelector.addItem("BED Track", "bed");
		trackSelector.addItem("Read Track", "read");
		trackSelector.addItem("Image Track", "image");
		trackSelector.addItem("IFrame Track", "frame");

		layoutTable.setWidget(0, 0, trackSelector);
	}

	public Widget getWidget() {

		return layoutTable;
	}

}
