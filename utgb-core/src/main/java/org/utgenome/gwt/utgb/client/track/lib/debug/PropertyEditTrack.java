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
// PropertyEditTrack.java
// Since: Jun 14, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.lib.debug;

import java.util.ArrayList;
import java.util.Iterator;

import org.utgenome.gwt.utgb.client.track.Track;
import org.utgenome.gwt.utgb.client.track.TrackBase;
import org.utgenome.gwt.utgb.client.track.TrackEntry;
import org.utgenome.gwt.utgb.client.track.TrackFrame;
import org.utgenome.gwt.utgb.client.track.TrackGroup;
import org.utgenome.gwt.utgb.client.track.TrackGroupProperty;
import org.utgenome.gwt.utgb.client.track.TrackGroupPropertyChange;
import org.utgenome.gwt.utgb.client.track.TrackGroupPropertyChangeListener;
import org.utgenome.gwt.utgb.client.track.TrackGroupPropertyChangeListenerAdapter;
import org.utgenome.gwt.utgb.client.track.TrackGroupPropertyWriter;
import org.utgenome.gwt.utgb.client.track.TrackUpdateListenerAdapter;
import org.utgenome.gwt.utgb.client.ui.EditableTable;
import org.utgenome.gwt.utgb.client.ui.TableChangeListener;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A track for editing {@link TrackGroupProperty}
 * 
 * @author leo
 * 
 */
public class PropertyEditTrack extends TrackBase {
	private final VerticalPanel _layoutPanel = new VerticalPanel();
	private final HorizontalPanel _panel = new HorizontalPanel();
	private final Label _changeNotification = new Label();

	// listen target
	private TrackGroup _peekingGroup;
	private TrackGroupPropertyChangeListener _propertyListener;
	private final ListBox _groupListBox = new ListBox();
	private final ArrayList<TrackGroup> _groupList = new ArrayList<TrackGroup>();

	private final EditableTable _propertyTable = new EditableTable(new String[] { "key", "value" }, 0);

	public static TrackFactory factory() {
		return new TrackFactory() {
			public Track newInstance() {
				return new PropertyEditTrack();
			}
		};
	}

	public PropertyEditTrack() {
		super("Property");

		_panel.setStyleName("selector");

		DOM.setStyleAttribute(_changeNotification.getElement(), "color", "#FF9999");

		HorizontalPanel hp = new HorizontalPanel();
		hp.add(new Label("track group: "));
		hp.add(_groupListBox);
		hp.setStyleName("selector");

		_layoutPanel.add(hp);
		_layoutPanel.add(_panel);
		_layoutPanel.add(_propertyTable);
		_layoutPanel.setWidth("100%");

		_propertyTable.enableInsertionOfNewRow();
		_propertyTable.addTableChangeListener(new TableChangeListener() {
			private String getKey(Object key) {
				return key.toString();
			}

			public void onDeleteRow(Object rowKey) {
				// TODO implementation
			}

			public void onInsertNewRow(Object[] newRowData) {
				TrackGroupPropertyWriter writer = getTrackGroup().getPropertyWriter();
				writer.setProperty(getKey(newRowData[0]), newRowData[1].toString());
			}

			public void onUpdateRow(Object rowKey, String columnName, String newData) {
				TrackGroupPropertyWriter writer = getTrackGroup().getPropertyWriter();
				writer.setProperty(getKey(rowKey), newData);
			}
		});
	}

	public Widget getWidget() {
		return _layoutPanel;
	}

	public int getDefaultWindowHeight() {
		return 25;
	}

	public void draw() {
		_propertyTable.removeAllRows();

		TrackGroupProperty reader = _peekingGroup.getPropertyReader();
		int rowId = 0;
		for (Iterator<String> it = reader.keySet().iterator(); it.hasNext(); rowId++) {
			String key = (String) it.next();
			String value = reader.getProperty(key, "");
			_propertyTable.addRow(new Object[] { key, value });
		}
	}

	private void updateGroupList() {
		// get the root track group
		TrackGroup rootGroup = getTrackGroup().getRootTrackGroup();

		// list up track groups
		assert (rootGroup != null);
		ArrayList<GroupEntry> trackGroupList = new ArrayList<GroupEntry>();
		findTrackGroups(rootGroup, "", trackGroupList);

		// update track group list
		_groupList.clear();
		_groupListBox.clear();
		for (Iterator<GroupEntry> it = trackGroupList.iterator(); it.hasNext();) {
			GroupEntry entry = it.next();
			_groupListBox.addItem(entry.label);
			_groupList.add(entry.group);
		}
	}

	class GroupEntry {
		String label;
		TrackGroup group;

		public GroupEntry(TrackGroup group, String label) {
			super();
			this.label = label;
			this.group = group;
		}

	}

	public void setUp(TrackFrame trackFrame, TrackGroup group) {
		trackFrame.pack();
		_peekingGroup = group;

		// set up the track group change listener
		_propertyListener = new TrackGroupPropertyChangeListenerAdapter() {
			public void onChangeTrackGroupProperty(TrackGroupPropertyChange change) {
				refresh();
			}
		};
		_peekingGroup.addTrackGroupPropertyChangeListener(_propertyListener);

		_groupListBox.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent e) {
				int index = _groupListBox.getSelectedIndex();
				if (index >= 0 && index < _groupList.size()) {
					TrackGroup selectedGroup = _groupList.get(index);

					// remove the previous change listener
					_peekingGroup.removeTrackGroupPropertyChangeListener(_propertyListener);

					// switch the listening target
					_peekingGroup = selectedGroup;
					_peekingGroup.addTrackGroupPropertyChangeListener(_propertyListener);
					refresh();
				}
			}
		});

		updateGroupList();

		getTrackGroup().getRootTrackGroup().addTrackUpdateListener(new TrackUpdateListenerAdapter() {
			public void onAddTrackGroup(TrackGroup trackGroup) {
				updateGroupList();
			}

			public void onRemoveTrackGroup(TrackGroup trackGroup) {
				updateGroupList();
			}
		});

	}

	private void findTrackGroups(TrackGroup group, String groupPrefix, ArrayList<GroupEntry> result) {
		if (group == null)
			return;

		String newPrefix = groupPrefix + group.getName();
		result.add(new GroupEntry(group, newPrefix));

		for (TrackEntry entry : group.getTrackEntryList()) {
			if (entry.isTrackGroup())
				findTrackGroups((TrackGroup) entry, newPrefix + ".", result);
		}
	}

}
