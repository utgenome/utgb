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
// SQLiteTrack.java
// Since: Jun 14, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.lib.debug;

import java.util.ArrayList;

import org.utgenome.gwt.utgb.client.GenomeBrowser;
import org.utgenome.gwt.utgb.client.UTGBClientException;
import org.utgenome.gwt.utgb.client.db.DatabaseCatalog;
import org.utgenome.gwt.utgb.client.db.DatabaseTable;
import org.utgenome.gwt.utgb.client.db.Relation;
import org.utgenome.gwt.utgb.client.track.Track;
import org.utgenome.gwt.utgb.client.track.TrackBase;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A simple SQLlite database viewer
 * 
 * @author leo
 * 
 */
public class SQLiteTrack extends TrackBase {
	public static TrackFactory factory() {
		return new TrackFactory() {
			public Track newInstance() {
				return new SQLiteTrack();
			}
		};
	}

	class TextInputPanel extends Composite {
		private final HorizontalPanel _panel = new HorizontalPanel();
		private final Label _label;
		private final TextBox _textBox = new TextBox();

		public TextInputPanel(String label, int inputBoxSize) {
			_label = new Label(label + ":");
			_textBox.setWidth(inputBoxSize + "px");

			_panel.add(_label);
			_panel.add(_textBox);

			_panel.setStyleName("selector");

			initWidget(_panel);
		}

		public void addKeyPressHandler(KeyPressHandler listener) {
			_textBox.addKeyPressHandler(listener);
		}

		public String getText() {
			return _textBox.getText();
		}

		public void setText(String text) {
			_textBox.setText(text);
		}

	}

	class JDBCSelector extends Composite {
		private final HorizontalPanel _panel = new HorizontalPanel();
		private final TextInputPanel _jdbcAddressPanel = new TextInputPanel("JDBC Address", 250);
		private final Button _submitButton = new Button("connect");

		public JDBCSelector() {
			_jdbcAddressPanel.addKeyPressHandler(new KeyPressHandler() {
				public void onKeyPress(KeyPressEvent e) {
					String jdbcAddress = getJDBCAddress();
					char keyCode = e.getCharCode();
					if (keyCode == KeyCodes.KEY_ENTER && jdbcAddress.length() > 0) {
						updateTable(jdbcAddress);
					}
				}

			});

			_submitButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent e) {
					updateTable(_jdbcAddressPanel.getText());
				}
			});

			_panel.add(_jdbcAddressPanel);
			_panel.add(_submitButton);
			initWidget(_panel);
		}

		public String getJDBCAddress() {
			return _jdbcAddressPanel.getText();
		}

		public void setText(String text) {
			_jdbcAddressPanel.setText(text);
		}
	}

	class TableSelector extends Composite {
		private final HorizontalPanel _panel = new HorizontalPanel();
		private ArrayList<String> _tableList = new ArrayList<String>();

		public TableSelector() {
			_panel.setStyleName("selector");
			initWidget(_panel);
		}

		/**
		 * parse a given JSON array of table names, e.g., ["gene", "alias"].
		 * 
		 * @param tableNameList
		 */
		public void setTableList(ArrayList<String> tableNameList) {
			_tableList.clear();
			_panel.clear();
			for (int i = 0; i < tableNameList.size(); i++) {
				String tableName = tableNameList.get(i).toString();
				_tableList.add(tableName);
				_panel.add(new TableLink(tableName));
			}
		}

	}

	class TableLink extends Hyperlink implements ClickHandler {
		public TableLink(String tableName) {
			super(tableName, tableName);
			setStyleName("selector-item");
			addClickHandler(this);
		}

		public void onClick(ClickEvent e) {
			fetchTable(getTargetHistoryToken());
		}
	}

	private final DockPanel _panel = new DockPanel();
	private final VerticalPanel _layoutPanel = new VerticalPanel();
	private final JDBCSelector _jdbcSelector = new JDBCSelector();
	private final TableSelector _tableSelector = new TableSelector();
	private DatabaseCatalog _catalog = new DatabaseCatalog();

	private DatabaseTable _dbTable = new DatabaseTable();

	public SQLiteTrack() {
		super("SQLite Viewer");
		init();
	}

	private void init() {
		_panel.add(_jdbcSelector, DockPanel.NORTH);
		_panel.add(_layoutPanel, DockPanel.CENTER);
		_layoutPanel.add(_tableSelector);
		_layoutPanel.add(_dbTable);

		_jdbcSelector.setText("mock/tracklist.db");
	}

	private void updateTable(final String jdbcAddress) {
		GenomeBrowser.getService().getDatabaseCatalog(jdbcAddress, new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				GWT.log(jdbcAddress + " retrieval failed", caught);
			}

			public void onSuccess(String json) {
				try {
					_catalog.load(json);
					_tableSelector.setTableList(_catalog.getTableNameList());
					getFrame().onUpdateTrackWidget();
				}
				catch (UTGBClientException e) {
					GWT.log("invalid catalog json data:" + json, e);
				}
			}
		});
	}

	public void fetchTable(final String tableName) {
		GenomeBrowser.getService().getTableData(_jdbcSelector.getJDBCAddress(), tableName, new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				GWT.log(_jdbcSelector.getJDBCAddress() + ", table=" + tableName + " retrieval failed", caught);
			}

			public void onSuccess(String json) {
				Relation r = _catalog.getRelation(tableName);
				if (r == null) {
					GWT.log("no relation found for " + tableName, new UTGBClientException());
					return;
				}
				_layoutPanel.remove(_dbTable);
				_dbTable = new DatabaseTable(r);
				_dbTable.setTableData(json);
				_layoutPanel.add(_dbTable);

				getFrame().onUpdateTrackWidget();
			}
		});
	}

	public void clear() {

	}

	public Widget getWidget() {
		return _panel;
	}

	public int getDefaultWindowHeight() {
		return 50;
	}

	public void draw() {

	}

}
