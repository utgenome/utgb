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
// StringType.java
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
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;

public class StringType extends DataTypeBase {
	transient ValueDomain valueDomain = null;

	public StringType(String name) {
		super(name);
	}

	public StringType(String name, ValueDomain valueDomain) {
		super(name);
		this.valueDomain = valueDomain;
	}

	private boolean hasValueDomain() {
		return valueDomain != null;
	}

	public InputForm getInputForm() {
		if (!hasValueDomain())
			return new StringTypeForm();
		else
			return new StringTypeListBox(valueDomain);
	}

	public class StringTypeForm extends InputForm {
		TextBox form = new TextBox();

		public StringTypeForm() {

			form.setWidth("300px");
			initWidget(form);
		}

		public String getUserInput() {
			return form.getText();
		}

		public JSONValue getJSONValue() {
			return new JSONString(form.getText());
		}

		public void setValue(String value) {
			form.setText(value);
		}

		public void addKeyPressHandler(KeyPressHandler listener) {
			form.addKeyPressHandler(listener);
		}

		public void addChangeHandler(ChangeHandler listener) {
			form.addChangeHandler(listener);
		}

	}

	public class StringTypeListBox extends InputForm {
		ListBox listBox = new ListBox();

		public StringTypeListBox(ValueDomain vd) {
			listBox.setVisibleItemCount(1);
			for (Value v : vd.getValueList()) {
				listBox.addItem(v.getLabel(), v.getValue());
			}

			initWidget(listBox);
		}

		public JSONValue getJSONValue() {
			return new JSONString(getUserInput());
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

	public String getTypeName() {
		return "string";
	}

}
