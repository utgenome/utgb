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
// LoadAndStoreTrack.java
// Since: 2007/07/19
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.lib;

import org.utgenome.gwt.utgb.client.track.Track;
import org.utgenome.gwt.utgb.client.track.TrackBase;
import org.utgenome.gwt.utgb.client.track.TrackFrame;
import org.utgenome.gwt.utgb.client.track.TrackGroup;
import org.utgenome.gwt.utgb.client.track.TrackLoader;
import org.utgenome.gwt.utgb.client.util.xml.XMLWriter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.XMLParser;

public class LoadAndStoreTrack extends TrackBase {

	public static TrackFactory factory() {
		return new TrackFactory() {
			public Track newInstance() {
				return new LoadAndStoreTrack();
			}
		};
	}

	private final DockPanel panel = new DockPanel();

	private final HorizontalPanel buttonPanel = new HorizontalPanel();

	private final Button storeButton = new Button("store");
	private final Button loadButton = new Button("load");

	private final RichTextArea textArea = new RichTextArea();

	public LoadAndStoreTrack() {
		super("Load and Store Track");

		textArea.setSize("500px", "200px");
		DOM.setStyleAttribute(panel.getElement(), "margin", "5px");

		buttonPanel.add(storeButton);
		buttonPanel.add(loadButton);
		panel.add(buttonPanel, DockPanel.NORTH);
		panel.add(textArea, DockPanel.CENTER);

		storeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent arg0) {
				final TrackGroup parentGroup = getTrackGroup();

				XMLWriter xmlWriter = new XMLWriter();
				parentGroup.toXML(xmlWriter);
				textArea.setText(xmlWriter.toString());
			}
		});
		loadButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent arg0) {
				final String xmlStr = textArea.getText().trim();
				if (xmlStr.length() > 0) {
					final Document document = XMLParser.parse(xmlStr);

					final Node topLevelNode = document.getFirstChild();

					final TrackGroup trackGroup = TrackLoader.loadTrackGroupFromXML(topLevelNode);
					final TrackGroup parentGroup = getTrackGroup();
					parentGroup.addTrackGroup(trackGroup);
				}
			}
		});
	}

	public Widget getWidget() {
		return panel;
	}

	public void setUp(TrackFrame trackFrame, TrackGroup group) {
		trackFrame.pack();
	}

}
