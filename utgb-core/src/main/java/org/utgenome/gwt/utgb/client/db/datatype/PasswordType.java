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
// PasswordType.java
// Since: 2007/04/13
//
// $Date$
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.db.datatype;

import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.PasswordTextBox;

public class PasswordType extends DataTypeBase {

	public PasswordType(String name) {
		super(name);
	}

	public InputForm getInputForm() {
		return new PasswordTypeForm();
	}

	public class PasswordTypeForm extends InputForm {
		PasswordTextBox form = new PasswordTextBox();

		/**
		 * @param form
		 */
		public PasswordTypeForm() {
			initWidget(form);
		}

		public JSONValue getJSONValue() {
			return new JSONString(getUserInput());
		}

		public String getUserInput() {
			return form.getText();
		}

		public void setValue(String value) {
			form.setText(value);
		}

		public void setStyleName(String styleName) {
			form.setStyleName(styleName);
		}

		public void addKeyPressHandler(KeyPressHandler listener) {
			form.addKeyPressHandler(listener);
		}

		public void addChangeHandler(ChangeHandler listener) {
			form.addChangeHandler(listener);
		}

	}

	public String getTypeName() {
		return "password";
	}

}
