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
// GradationType.java
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
import org.utgenome.gwt.utgb.client.ui.FormLabel;

import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;

public class GradationType extends DataTypeBase {
	private final GradationInputForm inputForm;

	public GradationType(final String parameterName, final double minValue, final double maxValue, final String prefix) {
		super(parameterName + " color gradation setting");

		inputForm = new GradationInputForm(parameterName, minValue, maxValue, prefix);
	}

	public InputForm getInputForm() {
		return inputForm;
	}

	public String getTypeName() {
		return "real-gradation";
	}

	public void setParameters(Map<String, String> parameterMap) {
		inputForm.setParameters(parameterMap);
	}

	class GradationInputForm extends InputForm implements ClickHandler {
		private List<ChangeHandler> changeHandlers = new ArrayList<ChangeHandler>();

		private final String parameterName;

		private final TextBox minBox = new TextBox();
		private final TextBox maxBox = new TextBox();

		private final double minValue;
		private final double maxValue;

		private RadioButton useColorButton;

		GradationInputForm(final String parameterName, final double minValue, final double maxValue, final String prefix) {
			this.parameterName = parameterName;
			this.minValue = minValue;
			this.maxValue = maxValue;

			final DockPanel _panel = new DockPanel();
			_panel.setHorizontalAlignment(DockPanel.ALIGN_LEFT);

			final HorizontalPanel panel = new HorizontalPanel();
			panel.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);

			panel.add(new FormLabel("min[" + minValue + "]", false));
			panel.add(minBox);
			minBox.setText(Double.toString(minValue));

			panel.add(new FormLabel("max[" + maxValue + "]", false));
			panel.add(maxBox);
			maxBox.setText(Double.toString(maxValue));

			minBox.setWidth("50px");
			maxBox.setWidth("50px");

			_panel.add(panel, DockPanel.NORTH);

			useColorButton = new RadioButton(prefix, "use this color setting");
			useColorButton.addClickHandler(this);
			_panel.add(useColorButton, DockPanel.CENTER);

			initWidget(_panel);
		}

		public void onClick(ClickEvent sender) {
			for (int i = 0; i < changeHandlers.size(); i++) {
				final ChangeHandler changeHandler = (ChangeHandler) (changeHandlers.get(i));
				changeHandler.onChange(null);
			}
		}

		public void addChangeHandler(ChangeHandler listener) {
			minBox.addChangeHandler(listener);
			maxBox.addChangeHandler(listener);
			changeHandlers.add(listener);
		}

		public void addKeyPressHandler(KeyPressHandler listener) {
			minBox.addKeyPressHandler(listener);
			maxBox.addKeyPressHandler(listener);
			useColorButton.addKeyPressHandler(listener);
		}

		public JSONValue getJSONValue() {
			return new JSONString(getUserInput());
		}

		public String getUserInput() {
			return "gradation-" + parameterName + "-min=" + getInputMinValue() + "&gradation-" + parameterName + "-max=" + getInputMaxValue();
		}

		public void setValue(String value) {
		}

		public void setParameters(Map<String, String> parameterMap) {
			final String minKey = "gradation-" + parameterName + "-min";
			parameterMap.put(minKey, Double.toString(getInputMinValue()));

			final String maxKey = "gradation-" + parameterName + "-max";
			parameterMap.put(maxKey, Double.toString(getInputMaxValue()));

			if (useColorButton.getValue()) {
				parameterMap.put("usecolor", parameterName + "-gradation");
			}
		}

		private double getInputMinValue() {
			final double inputMinValue = Double.parseDouble(minBox.getText());

			return Math.max(inputMinValue, minValue);
		}

		private double getInputMaxValue() {
			final double inputMaxValue = Double.parseDouble(maxBox.getText());

			return Math.min(inputMaxValue, maxValue);
		}
	}
}
