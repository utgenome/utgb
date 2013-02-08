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
// LowerBoundType.java
// Since: 2007/07/11
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.lib.old.datatype;

import java.util.Map;

import org.utgenome.gwt.utgb.client.db.datatype.DataTypeBase;
import org.utgenome.gwt.utgb.client.db.datatype.InputForm;
import org.utgenome.gwt.utgb.client.ui.FormLabel;

import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.TextBox;

public class LowerBoundType extends DataTypeBase {
	private final LowerBoundInputForm inputForm;

	public LowerBoundType(final String parameterName, final double minValue, final double maxValue) {
		super(parameterName + " display lower bound");

		inputForm = new LowerBoundInputForm(parameterName, minValue);
	}

	public InputForm getInputForm() {
		return inputForm;
	}

	public String getTypeName() {
		return "real-lbound";
	}

	public void setParameters(Map<String, String> parameterMap) {
		inputForm.setParameters(parameterMap);
	}

	class LowerBoundInputForm extends InputForm {
		private final String parameterName;

		private final TextBox minBox = new TextBox();

		private final double minValue;

		LowerBoundInputForm(final String parameterName, final double minValue) {
			this.parameterName = parameterName;
			this.minValue = minValue;

			final Grid panel = new Grid(1, 2);

			panel.setWidget(0, 0, new FormLabel("lower bound[" + minValue + "]", false));
			panel.setWidget(0, 1, minBox);
			minBox.setText(Double.toString(minValue));
			minBox.setWidth("50px");

			initWidget(panel);
		}

		public void addChangeHandler(ChangeHandler listener) {
			minBox.addChangeHandler(listener);
		}

		public void addKeyPressHandler(KeyPressHandler listener) {
			minBox.addKeyPressHandler(listener);
		}

		public JSONValue getJSONValue() {
			return new JSONString(getUserInput());
		}

		public String getUserInput() {
			return "lbound-" + parameterName + "=" + getInputMinValue();
		}

		public void setValue(String value) {
		}

		public void setParameters(Map<String, String> parameterMap) {
			final String minKey = "lbound-" + parameterName;
			parameterMap.put(minKey, Double.toString(getInputMinValue()));
		}

		private double getInputMinValue() {
			final double inputMinValue = Double.parseDouble(minBox.getText());

			return Math.max(inputMinValue, minValue);
		}
	}
}
