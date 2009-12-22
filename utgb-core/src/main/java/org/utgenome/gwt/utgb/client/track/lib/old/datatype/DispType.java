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
// DispType.java
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
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * @author ssksn
 * 
 */
public class DispType extends DataTypeBase {

	private final DispInputForm inputForm;

	public DispType(final String parameterName, final String[] values) {
		super(parameterName + " display setting");

		inputForm = new DispInputForm(parameterName, values);
	}

	public InputForm getInputForm() {
		return inputForm;
	}

	public String getTypeName() {
		return "select-disp";
	}

	public void setParameters(Map<String, String> parameterMap) {
		inputForm.setParameters(parameterMap);
	}

	private class DispInputForm extends ConfigInputForm implements ClickHandler {
		private List<ChangeHandler> changeHandlers = new ArrayList<ChangeHandler>();

		private final String parameterName;

		private CheckBox[] checkBoxs;

		DispInputForm(final String parameterName, final String[] values) {
			this.parameterName = parameterName;
			checkBoxs = new CheckBox[values.length];

			final FlowPanel panel = new FlowPanel();

			for (int i = 0; i < values.length; i++) {
				checkBoxs[i] = new CheckBox(values[i]);
				checkBoxs[i].addClickHandler(this);
				checkBoxs[i].setValue(true);
				panel.add(checkBoxs[i]);
			}

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
			for (int i = 0; i < checkBoxs.length; i++) {
				checkBoxs[i].addKeyPressHandler(listener);
			}
		}

		public JSONValue getJSONValue() {
			return new JSONString(getUserInput());
		}

		public String getUserInput() {
			final StringBuffer buf = new StringBuffer();

			for (int i = 0; i < checkBoxs.length; i++) {
				if (i != 0)
					buf.append('&');
				buf.append("disp-" + parameterName + "-" + checkBoxs[i].getText() + "=");
				if (checkBoxs[i].getValue()) {
					buf.append("on");
				}
				else {
					buf.append("off");
				}
			}

			return buf.toString();
		}

		public void setValue(String value) {
		}

		public void setParameters(Map<String, String> parameterMap) {
			for (int i = 0; i < checkBoxs.length; i++) {
				final String key = "disp-" + parameterName + "-" + checkBoxs[i].getText();
				if (checkBoxs[i].getValue()) {
					parameterMap.put(key, "on");
				}
				else {
					parameterMap.put(key, "off");
				}
			}
		}
	}

}
