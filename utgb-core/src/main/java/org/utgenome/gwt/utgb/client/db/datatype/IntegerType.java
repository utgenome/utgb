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
// UTGB Common Project
//
// IntegerType.java
// Since: 2007/04/13
//
// $Date$
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.db.datatype;

import org.utgenome.gwt.utgb.client.UTGBClientException;
import org.utgenome.gwt.utgb.client.db.Value;
import org.utgenome.gwt.utgb.client.db.ValueDomain;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;

public class IntegerType extends DataTypeBase {
	private ValueDomain _valueDomain = null;

	public IntegerType(String name) {
		super(name);
	}

	public IntegerType(String name, ValueDomain valueDomain) {
		super(name);
		_valueDomain = valueDomain;
	}

	public IntegerType(String name, boolean isPrimaryKey, boolean isNotNull) {
		super(name, isPrimaryKey, isNotNull);
	}

	public InputForm getInputForm() {
		if (_valueDomain == null)
			return new IntegerTypeForm();
		else
			return new IntegerTypeListBox(_valueDomain);
	}

	public class IntegerTypeForm extends InputForm {

		private TextBox form = new TextBox();

		public IntegerTypeForm() {
			initWidget(form);
		}

		public String getUserInput() {
			return form.getText();
		}

		public JSONValue getJSONValue() {
			try {
				int value = Integer.parseInt(form.getText());
				return new JSONNumber(value);
			}
			catch (NumberFormatException e) {
				return new JSONString("");
			}
		}

		public void setValue(String value) {
			try {
				int v = Integer.parseInt(value);
				form.setText(value);
			}
			catch (NumberFormatException e) {
				GWT.log(value + " is not a integer type", e);
			}
		}

		public void addKeyPressHandler(KeyPressHandler listener) {
			form.addKeyPressHandler(listener);
		}

		public void addChangeHandler(ChangeHandler listener) {
			form.addChangeHandler(listener);
		}

	}

	public class IntegerTypeListBox extends InputForm {
		ListBox listBox = new ListBox();

		public IntegerTypeListBox(ValueDomain vd) {
			listBox.setVisibleItemCount(1);
			for (Value v : vd.getValueList()) {
				listBox.addItem(v.getLabel(), v.getValue());
			}

			initWidget(listBox);
		}

		public JSONValue getJSONValue() {
			try {
				int value = Integer.parseInt(getUserInput());
				return new JSONNumber(value);
			}
			catch (NumberFormatException e) {
				return new JSONString("");
			}
		}

		public String getUserInput() {
			return listBox.getValue(listBox.getSelectedIndex());
		}

		public void setValue(String value) {
			for (int i = 0; i < listBox.getItemCount(); i++) {
				if (listBox.getValue(i).equals(value)) {
					listBox.setSelectedIndex(i);
					return;
				}
			}
			// no entry for the given value is found
			GWT.log(value + " is not found in the value domain", new UTGBClientException());
		}

		public void addKeyPressHandler(KeyPressHandler listener) {
			listBox.addKeyPressHandler(listener);
		}

		public void addChangeHandler(ChangeHandler listener) {
			listBox.addChangeHandler(listener);
		}
	}

	public String toString(JSONValue value) {
		JSONNumber n = value.isNumber();
		if (n != null)
			return Integer.toString((int) n.doubleValue());
		else {
			return super.toString(value);
		}

	}

	public String getTypeName() {
		return "integer";
	}

}
