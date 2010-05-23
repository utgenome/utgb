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
// DBSelectTrack.java
// Since: Oct 15, 2009
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.lib;

import java.util.ArrayList;
import java.util.List;

import org.utgenome.gwt.utgb.client.bean.DatabaseEntry;
import org.utgenome.gwt.utgb.client.track.Track;
import org.utgenome.gwt.utgb.client.track.TrackBase;
import org.utgenome.gwt.utgb.client.track.TrackConfig;
import org.utgenome.gwt.utgb.client.track.TrackFrame;
import org.utgenome.gwt.utgb.client.track.TrackGroup;
import org.utgenome.gwt.utgb.client.track.TrackGroupPropertyChange;
import org.utgenome.gwt.utgb.client.track.UTGBProperty;
import org.utgenome.gwt.utgb.client.ui.FormLabel;
import org.utgenome.gwt.utgb.client.util.JSONUtil;
import org.utgenome.gwt.utgb.client.util.Properties;
import org.utgenome.gwt.widget.client.Style;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

public class DBSelectTrack extends TrackBase {

	public static TrackFactory factory() {
		return new TrackFactory() {
			@Override
			public Track newInstance() {
				return new DBSelectTrack();
			}
		};
	}

	TrackConfig config = new TrackConfig(this);

	//VerticalPanel globalLayout = new VerticalPanel();
	FlexTable layout = new FlexTable();
	ListBox groupSelector = new ListBox();
	ListBox dbSelector = new ListBox();
	HorizontalPanel favoritePanel = new HorizontalPanel();

	private List<DatabaseEntry> currentEntry;
	private List<String> favoriteGroup = new ArrayList<String>();

	private String parentDBGroup = "org/utgenome";

	public DBSelectTrack() {
		super("DB Selector");

		layout.setWidget(0, 0, new FormLabel("DB Group: "));
		layout.setWidget(0, 1, groupSelector);
		layout.setWidget(1, 0, new FormLabel("DB Name: "));
		layout.setWidget(1, 1, dbSelector);
		layout.setWidget(2, 1, favoritePanel);

		//globalLayout.add(layout);
		//globalLayout.add(favoritePanel);

		groupSelector.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent e) {
				selectDBGroup(groupSelector.getValue(groupSelector.getSelectedIndex()));
			}
		});

		dbSelector.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent e) {
				selectDBName(dbSelector.getValue(dbSelector.getSelectedIndex()));
			}
		});
	}

	private void selectDBGroup(String dbGroup) {
		if (dbGroup != null) {
			for (int i = 0; i < groupSelector.getItemCount(); ++i) {
				String itemText = groupSelector.getItemText(i);
				if (itemText != null && itemText.equals(dbGroup)) {
					groupSelector.setSelectedIndex(i);
					break;
				}
			}
		}
		else
			groupSelector.setSelectedIndex(0);

		setTrackGroupProperty(UTGBProperty.DB_GROUP, groupSelector.getValue(groupSelector.getSelectedIndex()));
	}

	private void selectDBName(String dbName) {

		if (dbName != null) {
			for (int i = 1; i < dbSelector.getItemCount(); ++i) {
				String itemText = dbSelector.getItemText(i);
				if (itemText != null && itemText.equals(dbName)) {
					dbSelector.setSelectedIndex(i);
					break;
				}
			}

		}
		else
			dbSelector.setSelectedIndex(0);

		if (dbSelector.getSelectedIndex() == 0)
			return;

		String selectedDBName = dbSelector.getValue(dbSelector.getSelectedIndex());

		for (DatabaseEntry each : currentEntry) {
			if (each.path.equals(selectedDBName)) {
				if (each.isFile())
					setTrackGroupProperty(UTGBProperty.DB_NAME, selectedDBName);
				else
					setTrackGroupProperty(UTGBProperty.DB_GROUP, DatabaseEntry.newFolder(getTrackGroupProperty(UTGBProperty.DB_GROUP)).dbPath(selectedDBName));
			}
		}

	}

	public Widget getWidget() {
		return layout;
	}

	@Override
	public TrackConfig getConfig() {
		return config;
	}

	@Override
	public void setUp(TrackFrame trackFrame, TrackGroup group) {

		//config.addConfigParameter("Parent DB Group", new StringType(PARENT_DB_GROUP), getTrackGroupProperty(PARENT_DB_GROUP));

		//		if (getTrackGroupProperty(PARENT_DB_GROUP) != null) {
		//			updateDBGroupList();
		//		}

		String parentDBGroup = DatabaseEntry.parent(getTrackGroupProperty(UTGBProperty.DB_GROUP));
		if (!this.parentDBGroup.equals(parentDBGroup))
			updateDBGroupList();

	}

	private void updateDBGroupList() {
		parentDBGroup = DatabaseEntry.parent(getTrackGroupProperty(UTGBProperty.DB_GROUP));
		getBrowserService().getChildDBGroups(parentDBGroup, new AsyncCallback<List<String>>() {
			public void onFailure(Throwable e) {
				GWT.log(e.getMessage(), e);
			}

			public void onSuccess(List<String> result) {
				groupSelector.clear();

				for (String each : result)
					groupSelector.addItem(each);

				DatabaseEntry d = DatabaseEntry.newFolder(getTrackGroupProperty(UTGBProperty.DB_GROUP));
				d = d.parent();
				while (d != null) {
					groupSelector.addItem(d.path);
					d = d.parent();
				}

				if (!result.isEmpty()) {
					selectDBGroup(getTrackGroupProperty(UTGBProperty.DB_GROUP));
				}
			}
		});

	}

	private void updateDBNameList() {
		getBrowserService().getDBEntry(getTrackGroupProperty(UTGBProperty.DB_GROUP), new AsyncCallback<List<DatabaseEntry>>() {
			public void onFailure(Throwable e) {
				GWT.log(e.getMessage(), e);
			}

			public void onSuccess(List<DatabaseEntry> result) {
				dbSelector.clear();

				DBSelectTrack.this.currentEntry = result;
				dbSelector.addItem("(select)");
				for (DatabaseEntry each : result) {
					dbSelector.addItem(each.leaf());
				}

				if (!result.isEmpty()) {
					selectDBName(getTrackGroupProperty(UTGBProperty.DB_NAME));
				}
			}
		});
	}

	@Override
	public void onChangeTrackGroupProperty(TrackGroupPropertyChange change) {
		if (change.contains(UTGBProperty.DB_GROUP)) {
			String p = DatabaseEntry.parent(change.getProperty(UTGBProperty.DB_GROUP));

			if (this.parentDBGroup != null && !this.parentDBGroup.equals(p)) {
				updateDBGroupList();
			}
		}

		if (change.contains(UTGBProperty.DB_GROUP)) {
			updateDBNameList();
		}

	}

	private class FavGroupClickHandler implements ClickHandler {
		private final String group;

		public FavGroupClickHandler(String group) {
			this.group = group;
		}

		public void onClick(ClickEvent arg0) {
			setTrackGroupProperty(UTGBProperty.DB_GROUP, group);
		}

	}

	public void updateFavoriteGroup() {
		favoritePanel.clear();

		for (String each : favoriteGroup) {
			Label l = new Label(each);
			Style.fontSize(l, 11);
			Style.fontColor(l, "#336699");
			Style.set(l, "textDecoration", "underline");
			Style.cursor(l, Style.CURSOR_POINTER);
			Style.margin(l, Style.LEFT, 5);
			l.addClickHandler(new FavGroupClickHandler(each));
			favoritePanel.add(l);
		}
	}

	@Override
	public void restoreProperties(Properties properties) {

		favoriteGroup.clear();

		String f = properties.get("favoriteGroup", "[]");
		for (String each : JSONUtil.parseJSONArray(f)) {
			favoriteGroup.add(each);
		}

		updateFavoriteGroup();

	}

	@Override
	public void saveProperties(Properties saveData) {
		saveData.add("favoriteGroup", JSONUtil.toJSONArray(favoriteGroup));
	}

}
