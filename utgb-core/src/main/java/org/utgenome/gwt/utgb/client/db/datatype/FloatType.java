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
// DoubleType.java
// Since: 2009/12/14
//
// $Date$
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.db.datatype;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.TextBox;

public class FloatType extends DataTypeBase {
	public FloatType(String name) {
		super(name);
	}

	public InputForm getInputForm() {
		return new FloatTypeForm();
	}

	public class FloatTypeForm extends InputForm {
		private TextBox form = new TextBox();

		public FloatTypeForm() {
			initWidget(form);
		}

		public String getUserInput() {
			return form.getText();
		}

		public JSONValue getJSONValue() {
			try {
				double value = Float.parseFloat(form.getText());
				return new JSONNumber(value);
			}
			catch (NumberFormatException e) {
				return new JSONString("");
			}
		}

		public void setValue(String value) {
			try {
				float v = Float.parseFloat(value);
				form.setText(value);
			}
			catch (NumberFormatException e) {
				GWT.log(value + " is not a float type", e);
			}
		}

		public void addKeyPressHandler(KeyPressHandler listener) {
			form.addKeyPressHandler(listener);
		}

		public void addChangeHandler(ChangeHandler listener) {
			form.addChangeHandler(listener);
		}
	}

	public String getTypeName() {
		return "float";
	}

}
