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
// UpperBoundType.java
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

public class UpperBoundType extends DataTypeBase {
	private final UpperBoundInputForm inputForm;

	public UpperBoundType(final String parameterName, final double minValue, final double maxValue) {
		super(parameterName + " display upper bound");

		inputForm = new UpperBoundInputForm(parameterName, maxValue);
	}

	public InputForm getInputForm() {
		return inputForm;
	}

	public String getTypeName() {
		return "real-ubound";
	}

	public void setParameters(Map<String, String> parameterMap) {
		inputForm.setParameters(parameterMap);
	}

	class UpperBoundInputForm extends InputForm {
		private final String parameterName;

		private final TextBox maxBox = new TextBox();

		private final double maxValue;

		UpperBoundInputForm(final String parameterName, final double maxValue) {
			this.parameterName = parameterName;
			this.maxValue = maxValue;

			final Grid panel = new Grid(1, 2);

			panel.setWidget(0, 0, new FormLabel("upper bound[" + maxValue + "]", false));
			panel.setWidget(0, 1, maxBox);
			maxBox.setText(Double.toString(maxValue));
			maxBox.setWidth("50px");

			initWidget(panel);
		}

		public void addChangeHandler(ChangeHandler listener) {
			maxBox.addChangeHandler(listener);
		}

		public void addKeyPressHandler(KeyPressHandler listener) {
			maxBox.addKeyPressHandler(listener);
		}

		public JSONValue getJSONValue() {
			return new JSONString(getUserInput());
		}

		public String getUserInput() {
			return "ubound-" + parameterName + "=" + getInputMaxValue();
		}

		public void setValue(String value) {
		}

		public void setParameters(Map<String, String> parameterMap) {
			final String maxKey = "ubound-" + parameterName;
			parameterMap.put(maxKey, Double.toString(getInputMaxValue()));
		}

		private double getInputMaxValue() {
			final double inputMaxValue = Double.parseDouble(maxBox.getText());

			return Math.min(inputMaxValue, maxValue);
		}
	}
}
