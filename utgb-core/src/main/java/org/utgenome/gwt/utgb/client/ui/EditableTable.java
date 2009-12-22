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
// EditableTable.java
// Since: Jun 18, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.utgenome.gwt.utgb.client.track.Design;
import org.utgenome.gwt.utgb.client.util.TableData;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.event.KeyboardHandler;

/**
 * You can insert table data into a {@link EditableTable}. Inserted data must have a key column (default "id")
 * 
 * @author leo
 * 
 */
public class EditableTable extends Composite {
	private final static int LABEL_ROW_INDEX = 0;
	private FlexTable _table = new FlexTable();
	private boolean _enableInsertionOfNewRows = false;
	private HashSet<Integer> _disabledColumn = new HashSet<Integer>();
	private ArrayList<TextBox> _newRowBox = new ArrayList<TextBox>();

	// table data
	private TableData _tableData;
	private ArrayList<TableChangeListener> _tableChangeListenerList = new ArrayList<TableChangeListener>();

	class RowChangeListener implements ChangeHandler, KeyPressHandler {
		public void onChange(ChangeEvent e) {
			onChange((Widget) e.getSource());
		}

		private void onChange(Widget sender) {
			EditableLabel label = (EditableLabel) sender;
			CellReference cell = label.getCellReference();
			Object key = cell.getKey();
			String column = cell.getColumnName();

			for (Iterator<TableChangeListener> it = _tableChangeListenerList.iterator(); it.hasNext();) {
				TableChangeListener listener = it.next();
				listener.onUpdateRow(key, column, label.getText());
			}

		}

		public void onKeyPress(KeyPressEvent e) {
			if (e.getCharCode() == KeyboardHandler.KEY_ENTER) {
				onChange((Widget) e.getSource());
			}
		}
	}

	class NewRowListener implements KeyPressHandler {

		public void onKeyPress(KeyPressEvent e) {
			if (e.getCharCode() == KeyboardHandler.KEY_ENTER) {
				Object[] row = new Object[_tableData.getColumnCount()];
				int col = 0;
				for (Iterator<TextBox> it = _newRowBox.iterator(); it.hasNext(); col++) {
					TextBox textBox = it.next();
					row[col] = new String(textBox.getText()); // clone the input string
					textBox.setText("");
				}

				// notify the insertion
				for (Iterator<TableChangeListener> it = _tableChangeListenerList.iterator(); it.hasNext();) {
					TableChangeListener listener = it.next();
					listener.onInsertNewRow(row);
				}

			}
		}

	}

	public EditableTable(String[] columnLabel) {
		_tableData = new TableData(columnLabel);
		init(columnLabel);
	}

	public EditableTable(String[] columnLabel, int keyColumn) {
		_tableData = new TableData(columnLabel, keyColumn);
		init(columnLabel);
	}

	private void init(String[] columnLabel) {
		for (int i = 0; i < _tableData.getColumnCount(); i++)
			_table.setText(0, i + 1, _tableData.getColumnLabel(i));

		_table.setStyleName("editable-table");
		_table.getRowFormatter().setStyleName(LABEL_ROW_INDEX, "table-label");

		int keyColumn = _tableData.getKeyColumn();
		disableEdit(keyColumn);

		initWidget(_table);
	}

	public void disableEdit(int column) {
		if (column > _table.getCellCount(LABEL_ROW_INDEX))
			return;

		_disabledColumn.add(new Integer(column));

		for (int row = 1; row < _table.getRowCount(); row++) {
			EditableLabel label = (EditableLabel) _table.getWidget(row, column + 1);
			label.setEnabled(false);
		}
	}

	private boolean isEditable(int column) {
		return !_disabledColumn.contains(new Integer(column));
	}

	public void enableInsertionOfNewRow() {
		if (_enableInsertionOfNewRows)
			return; // already enabled

		_enableInsertionOfNewRows = true;
		_newRowBox.clear();
		int newRowPos = _table.getRowCount();
		for (int i = 0; i < _tableData.getColumnCount(); i++) {
			TextBox input = new TextBox();
			input.setWidth("100%");
			input.addKeyPressHandler(new NewRowListener());
			_newRowBox.add(input);
			//if(isEditable(i))
			_table.setWidget(newRowPos, i + 1, input);
		}
	}

	public void insertRow(Object[] row, int beforeIndex) {
		Object key = _tableData.addRow(row);
		int rowPos = _table.insertRow(beforeIndex);

		for (int col = 0; col < row.length; col++) {
			EditableLabel text = new EditableLabel(new CellReference(key, _tableData.getColumnLabel(col)), row[col] != null ? row[col].toString() : "");
			if (!isEditable(col))
				text.setEnabled(false);
			else {
				RowChangeListener listener = new RowChangeListener();
				text.addChangeHandler(listener);
				text.addKeyPressHandler(listener);
			}
			_table.setWidget(rowPos, col + 1, text);
		}

		Image deleteButton = new Image(Design.IMAGE_DELETE_BUTTON);
		_table.setWidget(rowPos, 0, deleteButton);

	}

	public void addRow(Object[] row) {
		insertRow(row, _enableInsertionOfNewRows ? _table.getRowCount() - 1 : _table.getRowCount());
	}

	private Map<String, String> createRowDataMap(int row) {
		Map<String, String> rowDataMap = new HashMap<String, String>();
		for (int col = 0; col < _tableData.getColumnCount(); col++) {
			String data = _table.getText(row + 1, col + 1);
			rowDataMap.put(_tableData.getColumnLabel(col), data);
		}
		return rowDataMap;
	}

	public void removeAllRows() {
		_tableData.clear();
		int maxRowPos = _enableInsertionOfNewRows ? _table.getRowCount() - 2 : _table.getRowCount() - 1;
		for (int i = maxRowPos; i >= 1; i--) {
			_table.removeRow(i);
		}
	}

	public void addTableChangeListener(TableChangeListener listener) {
		_tableChangeListenerList.add(listener);
	}

	public void removeTableChangeListener(TableChangeListener listener) {
		_tableChangeListenerList.remove(listener);
	}

}

class CellReference {
	private Object key;
	private String columnName;

	public CellReference(Object key, String columnName) {
		super();
		this.key = key;
		this.columnName = columnName;
	}

	public String getColumnName() {
		return columnName;
	}

	public Object getKey() {
		return key;
	}

}
