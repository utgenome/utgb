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
// BooleanType.java
// Since: 2007/04/13
//
// $Date$
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.db.datatype;

import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.CheckBox;

public class BooleanType extends DataTypeBase {

	public BooleanType(String name) {
		super(name);
	}

	public InputForm getInputForm() {
		return new BooleanTypeForm();
	}

	public class BooleanTypeForm extends InputForm {
		CheckBox form = new CheckBox();

		public BooleanTypeForm() {
			initWidget(form);
		}

		public String getUserInput() {
			return form.getValue() ? "true" : "false";
		}

		public JSONValue getJSONValue() {
			return JSONBoolean.getInstance(form.getValue());
		}

		public void setValue(String value) {
			form.setValue(value.equals("true"));
		}

		public void addKeyPressHandler(KeyPressHandler listener) {
			form.addKeyPressHandler(listener);
		}

		public void addChangeHandler(final ChangeHandler listener) {

			form.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					listener.onChange(null);
				}
			});
		}

	}

	public String toString(JSONValue json) {
		JSONBoolean b = json.isBoolean();
		if (b == null)
			return super.toString(json);
		else
			return b.booleanValue() ? "true" : "false";
	}

	public String getTypeName() {
		return "boolean";
	}

}
