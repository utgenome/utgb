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
// ValueSelectorTrack.java
// Since: Jun 13, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.lib;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.utgenome.gwt.utgb.client.track.Track;
import org.utgenome.gwt.utgb.client.track.TrackBase;
import org.utgenome.gwt.utgb.client.track.TrackFrame;
import org.utgenome.gwt.utgb.client.track.TrackGroup;
import org.utgenome.gwt.utgb.client.track.TrackGroupPropertyChange;
import org.utgenome.gwt.utgb.client.util.CanonicalProperties;
import org.utgenome.gwt.utgb.client.util.JSONUtil;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * {@link ValueSelectorTrack} displays a set of text values. When you click one of these values, its corresponding track
 * property will be changed, then a {@link Track#onChangeTrackGroupProperty(TrackGroupPropertyChange)} event is
 * broadcasted to all tracks in a group.
 * 
 * @author leo
 * 
 */
public class ValueSelectorTrack extends TrackBase {
	public static TrackFactory factory() {
		return new TrackFactory() {
			private String trackName;
			private String targetProperty;
			private List<String> linkList = new ArrayList<String>();

			public Track newInstance() {
				final ValueSelectorTrack track = new ValueSelectorTrack(trackName, targetProperty);

				final int SIZE = linkList.size();
				for (int i = 0; i < SIZE; i++) {
					final String value = (linkList.get(i));
					track.addValue(value);
				}

				return track;
			}

			public void setProperty(String key, String value) {
				if (key.equals("trackName"))
					trackName = value;
				else if (key.equals("targetProperty"))
					targetProperty = value;
				else if (key.equals("value")) {
					linkList.add(value);
				}
			}

			public void clear() {
				linkList.clear();
			}

		};
	}

	private String _targetProperty;
	private String _selectedValue;

	private FlowPanel _linkPanel = new FlowPanel();
	private ArrayList<ValueLink> _linkList = new ArrayList<ValueLink>();

	class ValueLink extends Anchor implements ClickHandler {
		public ValueLink(String value) {
			super(value, value);
			setStyleName("selector-item");
			addClickHandler(this);
		}

		public void onClick(ClickEvent e) {
			getTrackGroup().getPropertyWriter().setProperty(_targetProperty, getText());

			setSelectionStyle(this);
		}

		public String toString() {
			return this.getText();
		}
	}

	public String getTargetProperty() {
		return _targetProperty;
	}

	private void setSelectionStyle(ValueLink link) {
		if (link == null)
			return;

		_selectedValue = link.toString();
		link.setStyleName("selector-item-selected");
		for (Iterator<ValueLink> it = _linkList.iterator(); it.hasNext();) {
			ValueLink v = it.next();
			if (v != link)
				v.setStyleName("selector-item");
		}
	}

	private ValueLink search(String value) {
		for (Iterator<ValueLink> it = _linkList.iterator(); it.hasNext();) {
			ValueLink v = it.next();
			if (v.getText().equals(value))
				return v;
		}
		return null;
	}

	public ValueSelectorTrack(String trackName, String targetProperty) {
		super(trackName);
		_linkPanel.setHeight("15px");
		_linkPanel.setWidth("100%");
		this._targetProperty = targetProperty;
	}

	public void addValue(String value) {
		_linkList.add(new ValueLink(value));
	}

	public void clearValues() {
		_linkList.clear();
	}

	public void clear() {
		_linkPanel.clear();
	}

	public int getDefaultWindowHeight() {
		return 15;
	}

	public Widget getWidget() {
		return _linkPanel;
	}

	protected void updateSelection() {
		setSelectionStyle(search(_selectedValue));
	}

	public void onChangeTrackGroupProperty(TrackGroupPropertyChange change) {
		if (change.contains(_targetProperty)) {
			_selectedValue = change.getProperty(_targetProperty);
			updateSelection();
		}

	}

	public void draw() {
		_linkPanel.clear();
		for (Iterator<ValueLink> it = _linkList.iterator(); it.hasNext();) {
			ValueLink link = it.next();
			_linkPanel.add(link);
		}
		updateSelection();
	}

	public void setUp(TrackFrame trackFrame, TrackGroup group) {
		trackFrame.pack();
	}

	public void saveProperties(CanonicalProperties saveData) {
		saveData.add("targetProperty", _targetProperty);
		saveData.add("valueList", JSONUtil.toJSONArray(_linkList));
		saveData.add("selectedValue", _selectedValue);
	}

	public void restoreProperties(CanonicalProperties properties) {
		_targetProperty = properties.get("targetProperty", _targetProperty);
		_selectedValue = properties.get("selectedValue", _selectedValue);
		ArrayList<String> valueList = JSONUtil.parseJSONArray(properties.get("valueList", "[]"));
		_linkList.clear();
		for (String value : valueList) {
			addValue(value);
		}
	}

}
