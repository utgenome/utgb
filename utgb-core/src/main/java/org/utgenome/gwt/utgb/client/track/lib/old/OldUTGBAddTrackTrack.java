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
// GenomeBrowser Project
//
// OldUTGBAddTrackTrack.java
// Since: 2007/06/20
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.lib.old;

import org.utgenome.gwt.utgb.client.track.Track;
import org.utgenome.gwt.utgb.client.track.TrackBase;
import org.utgenome.gwt.utgb.client.track.TrackFrame;
import org.utgenome.gwt.utgb.client.track.TrackGroup;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author ssksn
 * 
 */
public class OldUTGBAddTrackTrack extends TrackBase implements ClickHandler {

	public static TrackFactory factory() {
		return new TrackFactory() {
			public Track newInstance() {
				return new OldUTGBAddTrackTrack();
			}
		};
	}

	private final HorizontalPanel _panel = new HorizontalPanel();
	private final Button addButton = new Button("Add");
	private final TextBox descriptionXMLURLBox = new TextBox();

	public OldUTGBAddTrackTrack() {
		super("Add Track with Desc XML");

		_panel.setSize("100%", "50px");
		_panel.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		_panel.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
		_panel.setStyleName("utgb-addtrack");
		descriptionXMLURLBox.setVisibleLength(100);

		_panel.add(addButton);
		_panel.add(descriptionXMLURLBox);

		addButton.addClickHandler(this);
	}

	public Widget getWidget() {
		return _panel;
	}

	public int getMinimumWindowHeight() {
		return 50;
	}

	public void setUp(TrackFrame trackFrame, TrackGroup group) {
		trackFrame.pack();
	}

	public void onClick(ClickEvent e) {
		final String descriptionXMLURL = descriptionXMLURLBox.getText();

		if (descriptionXMLURL != null && descriptionXMLURL.length() > 0) {
			final OldUTGBTrack newTrack = new OldUTGBTrack();
			newTrack.setDescriptionXML(descriptionXMLURL);

			_trackGroup.addTrack(newTrack);
		}
	}

}
