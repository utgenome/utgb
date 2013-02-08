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
// SelectType.java
// Since: 2007/07/11
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.lib.old.datatype;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.utgenome.gwt.utgb.client.db.datatype.DataTypeBase;
import org.utgenome.gwt.utgb.client.db.datatype.InputForm;

import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RadioButton;

public class SelectType extends DataTypeBase {

	private final SelectInputForm inputForm;

	public SelectType(final String parameterName, final String[] values) {
		super(parameterName + " select setting");

		inputForm = new SelectInputForm(parameterName, values);
	}

	public InputForm getInputForm() {
		return inputForm;
	}

	public String getTypeName() {
		return "select-select";
	}

	public void setParameters(Map<String, String> parameterMap) {
		inputForm.setParameters(parameterMap);
	}

	class SelectInputForm extends ConfigInputForm implements ClickHandler {
		private List<ChangeHandler> changeHandlers = new ArrayList<ChangeHandler>();

		private final String parameterName;

		private final String[] values;
		private RadioButton[] radioButtons;

		SelectInputForm(final String parameterName, final String[] values) {
			this.parameterName = parameterName;
			this.values = values;

			final String prefix = Integer.toString(Random.nextInt());

			radioButtons = new RadioButton[values.length];

			final FlowPanel panel = new FlowPanel();

			for (int i = 0; i < values.length; i++) {
				radioButtons[i] = new RadioButton(prefix, values[i]);
				radioButtons[i].addClickHandler(this);
				panel.add(radioButtons[i]);
			}
			radioButtons[0].setValue(true);

			addWidget(panel);
		}

		public void onClick(ClickEvent e) {
			for (int i = 0; i < changeHandlers.size(); i++) {
				final ChangeHandler changeHandler = (ChangeHandler) (changeHandlers.get(i));
				changeHandler.onChange(null);
			}
		}

		public void addChangeHandler(ChangeHandler listener) {
			changeHandlers.add(listener);
		}

		public void addKeyPressHandler(KeyPressHandler listener) {
			for (int i = 0; i < radioButtons.length; i++) {
				radioButtons[i].addKeyPressHandler(listener);
			}
		}

		public JSONValue getJSONValue() {
			return new JSONString(getUserInput());
		}

		public String getUserInput() {
			final StringBuffer buf = new StringBuffer();

			for (int i = 0; i < radioButtons.length; i++) {
				if (radioButtons[i].getValue()) {
					buf.append("select-" + parameterName + "=" + radioButtons[i].getText());
					break;
				}
			}

			return buf.toString();
		}

		public void setValue(String value) {
		}

		public void setParameters(Map<String, String> parameterMap) {
			for (int i = 0; i < radioButtons.length; i++) {
				if (radioButtons[i].getValue()) {
					final String key = "select-" + parameterName;
					final String value = radioButtons[i].getText();
					parameterMap.put(key, value);
					break;
				}
			}
		}
	}
}
