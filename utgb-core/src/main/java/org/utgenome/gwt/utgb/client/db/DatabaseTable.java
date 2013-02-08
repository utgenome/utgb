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
// BugReport Project
//
// DatabaseTable.java
// Since: 2007/03/28
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.db;

import org.utgenome.gwt.utgb.client.UTGBClientException;
import org.utgenome.gwt.utgb.client.db.datatype.DataType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONException;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;

/**
 * A DatabaseTable makes easier to display table data, the schema of which is given by a Relation object, and its raw
 * data are described with the JSON format.
 * 
 * For example, a JSON string
 * 
 * <code>String s = "{\"data\" : [{\"name\" : \"leo\", \"id\" : 1 }, {\"name\" : \"ahsan\", \"id\" : 2 }]}"
 * 
 * holds two persons' data : (leo, 1) and (ahsan, 2).
 * 
 * Thus, 
 * <code>
 * DatabaseTable t(r);  //r = (name:string, id:integer)
 * t.setTableData(s);
 * </code>
 * 
 * will provide the following table: ----------------- | name | id | ----------------- | leo | 1 | | ahsan | 2 |
 * -----------------
 * 
 * @author leo
 * 
 */
public class DatabaseTable extends Composite {
	Relation _relation;
	FlexTable _table = new FlexTable();
	public static final int LABEL_ROW = 0;

	public DatabaseTable() {
		_table.setStyleName("table");
		initWidget(_table);
	}

	public DatabaseTable(Relation relation) {
		setRelation(relation);
		_table.setStyleName("table");
		initWidget(_table);
	}

	public void setRelation(Relation relation) {
		this._relation = relation;

		// set table labels
		int columnIndex = 0;
		for (DataType dt : _relation.getDataTypeList()) {
			_table.setText(LABEL_ROW, columnIndex, dt.getName());
			columnIndex++;
		}
		_table.getRowFormatter().setStyleName(LABEL_ROW, "table-label");
	}

	public void setTableData(String jsonData) {
		// clear the row data
		while (_table.getRowCount() > 1)
			_table.removeRow(_table.getRowCount() - 1);

		try {
			JSONValue json = JSONParser.parse(jsonData);
			JSONObject root;
			if ((root = json.isObject()) != null) {
				JSONValue array = root.get("data");
				if (array == null)
					return;
				JSONArray rowArray;
				if ((rowArray = array.isArray()) != null) {
					for (int i = 0; i < rowArray.size(); i++) {
						addRow(rowArray.get(i));
					}
				}
			}
		}
		catch (JSONException e) {
			GWT.log("JSON error", e);
		}
	}

	public void addRow(String jsonData) {
		try {
			JSONValue json = JSONParser.parse(jsonData);
			addRow(json);
		}
		catch (JSONException e) {
			GWT.log("JSON error", e);
		}
	}

	public void addRow(JSONValue rowValue) {
		JSONObject rowData;

		int row = _table.getRowCount();
		if ((rowData = rowValue.isObject()) != null) {
			int columnIndex = 0;
			for (DataType dt : _relation.getDataTypeList()) {
				JSONValue jsonValue = rowData.get(dt.getName());
				setValue(row, columnIndex, jsonValue);
				columnIndex++;
			}
			_table.getRowFormatter().setStyleName(row, "table-data");
		}
		else {
			GWT.log("invalid json data", new UTGBClientException());
		}
	}

	public void setValue(int row, int column, JSONValue value) {
		if (value == null)
			return;

		DataType dataType = _relation.getDataType(column);

		JSONBoolean bool;
		if ((bool = value.isBoolean()) != null) {
			CheckBox cb = new CheckBox();
			cb.setValue(bool.booleanValue());
			_table.setWidget(row, column, cb);
		}
		else {
			_table.setText(row, column, dataType.toString(value));
		}
	}

}
