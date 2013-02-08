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
// ParameterTrack.java
// Since: 2007/06/15
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.lib;

import java.util.ArrayList;

import org.utgenome.gwt.utgb.client.track.Track;
import org.utgenome.gwt.utgb.client.track.TrackBase;
import org.utgenome.gwt.utgb.client.track.TrackFrame;
import org.utgenome.gwt.utgb.client.track.TrackGroup;
import org.utgenome.gwt.utgb.client.track.TrackGroupProperty;
import org.utgenome.gwt.utgb.client.track.TrackGroupPropertyChange;
import org.utgenome.gwt.utgb.client.track.TrackGroupPropertyWriter;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author ssksn
 * 
 */
public class ParameterTrack extends TrackBase {
	public static TrackFactory factory() {
		return new TrackFactory() {
			public Track newInstance() {
				return new ParameterTrack();
			}
		};
	}

	protected ArrayList<KeyAndEntry> entries = new ArrayList<KeyAndEntry>();

	protected FlexTable gridPanel = new FlexTable();

	public ParameterTrack() {
		super("Parameter Track");
		gridPanel.setStyleName("parameter-track");
		gridPanel.setWidth("100%");
	}

	// @see org.utgenome.gwt.utgb.client.track.Track#getWidget()
	public Widget getWidget() {
		return gridPanel;
	}

	public int getDefaultWindowHeight() {
		return 80;
	}

	public void addParameter(final String key) {
		addParameter(key, key);
	}

	public void addParameter(final String key, final String displayName) {
		entries.add(new KeyAndEntry(key, displayName));
	}

	class KeyAndEntry {
		private String key;
		private String entry;

		public KeyAndEntry(final String key, final String entry) {
			this.key = key;
			this.entry = entry;
		}

		public String getKey() {
			return key;
		}

		public String getEntry() {
			return entry;
		}
	}

	public void draw() {
		gridPanel.clear();

		final TrackGroupProperty propertyReader = getTrackGroup().getPropertyReader();
		final TrackGroupPropertyWriter propertyWriter = getTrackGroup().getPropertyWriter();

		final int SIZE = entries.size();
		for (int i = 0; i < SIZE; i++) {
			final KeyAndEntry entry = (entries.get(i));

			final String key = entry.getKey();
			final String displayName = entry.getEntry();

			final String propertyValue = propertyReader.getProperty(key);

			gridPanel.setText(i / 3, (2 * i) % 6, displayName);
			final TextBox textBox = new TextBox();
			textBox.setText(propertyValue);
			textBox.addKeyUpHandler(new KeyUpHandler() {

				public void onKeyUp(KeyUpEvent e) {
					if (e.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
						propertyWriter.setProperty(key, textBox.getText());
					}
				}
			});
			gridPanel.setWidget(i / 3, (2 * i + 1) % 6, textBox);
		}
	}

	public void onChangeTrackGroupProperty(final TrackGroupPropertyChange change) {
		final int SIZE = entries.size();
		final String[] array = new String[SIZE];
		for (int i = 0; i < SIZE; i++) {
			final KeyAndEntry entry = (entries.get(i));

			final String key = entry.getKey();
			array[i] = key;
		}

		if (change.containsOneOf(array))
			draw();
	}

	public void setUp(TrackFrame trackFrame, TrackGroup group) {
		trackFrame.pack();
	}

}
