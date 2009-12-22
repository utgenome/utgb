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
// ColorType.java
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

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ColorType extends DataTypeBase {

	private final ColorInputForm inputForm;

	public ColorType(final String parameterName, final String[] values, final String prefix) {
		super(parameterName + " color setting");

		inputForm = new ColorInputForm(parameterName, values, prefix);
	}

	public InputForm getInputForm() {
		return inputForm;
	}

	public String getTypeName() {
		return "select-color";
	}

	public void setParameters(Map<String, String> parameterMap) {
		inputForm.setParameters(parameterMap);
	}

	class ColorInputForm extends ConfigInputForm implements ClickHandler {
		private List<ChangeHandler> changeHandlers = new ArrayList<ChangeHandler>();

		private final String parameterName;

		private final ColorSelectPanel[] colorSelectPanels;

		private RadioButton useColorButton;

		private String urlEncode(String s) {
			return s.replaceAll(" ", "%2B").replaceAll("=", "%3D");
		}

		ColorInputForm(final String parameterName, final String[] values, final String prefix) {
			this.parameterName = urlEncode(parameterName);
			colorSelectPanels = new ColorSelectPanel[values.length];

			final VerticalPanel panel = new VerticalPanel();

			for (int i = 0; i < values.length; i++) {
				colorSelectPanels[i] = new ColorSelectPanel(values[i]);

				panel.add(colorSelectPanels[i]);

				if (values[i].equals("plus")) {
					colorSelectPanels[i].firstListBox.setSelectedIndex(2);
					colorSelectPanels[i].onChange(null);
				}
				else if (values[i].equals("minus")) {
					colorSelectPanels[i].firstListBox.setSelectedIndex(3);
					colorSelectPanels[i].onChange(null);
				}
			}

			{
				useColorButton = new RadioButton(prefix, "use this color setting");
				useColorButton.setEnabled(true);
				useColorButton.addClickHandler(this);
				panel.add(useColorButton);
			}

			addWidget(panel);
		}

		public void onClick(ClickEvent e) {
			for (int i = 0; i < changeHandlers.size(); i++) {
				final ChangeHandler changeHandler = (ChangeHandler) (changeHandlers.get(i));
				changeHandler.onChange(null);
			}
		}

		public void addChangeHandler(ChangeHandler Handler) {
			for (int i = 0; i < colorSelectPanels.length; i++) {
				colorSelectPanels[i].addChangeHandler(Handler);
			}
			changeHandlers.add(Handler);
		}

		public void addKeyPressHandler(KeyPressHandler handler) {
			for (int i = 0; i < colorSelectPanels.length; i++) {
				colorSelectPanels[i].addKeyPressHandler(handler);
			}
			useColorButton.addKeyPressHandler(handler);
		}

		public JSONValue getJSONValue() {
			return new JSONString(getUserInput());
		}

		public String getUserInput() {
			final StringBuffer buf = new StringBuffer();

			for (int i = 0; i < colorSelectPanels.length; i++) {
				String colorValue = colorSelectPanels[i].getValue();
				if (colorValue != null) {
					if (i != 0)
						buf.append('&');
					String colorParamName = "color-" + parameterName + "-" + colorSelectPanels[i].getLabel();
					buf.append(urlEncode(colorParamName) + "=(");
					buf.append(colorSelectPanels[i].getValue());
					buf.append(")");
				}
			}

			return buf.toString();
		}

		public void setValue(String value) {
		}

		public void setParameters(Map<String, String> parameterMap) {
			boolean colorIsSpecified = false;
			for (int i = 0; i < colorSelectPanels.length; i++) {
				String colorValue = colorSelectPanels[i].getValue();
				if (colorValue != null) {
					final String key = "color-" + parameterName + "-" + colorSelectPanels[i].getLabel();
					final String value = "(" + colorSelectPanels[i].getValue() + ")";
					String encodedParam = urlEncode(key);
					parameterMap.put(encodedParam, value);
					colorIsSpecified = true;
				}
			}
			if (useColorButton.getValue() && colorIsSpecified) {
				parameterMap.put("usecolor", parameterName + "-color");
			}
		}
	}

	private class ColorSelectPanel extends HorizontalPanel implements ChangeHandler {
		final String label;
		final ListBox firstListBox = new ListBox();

		final ByteListBox redBox = new ByteListBox();
		final ByteListBox greenBox = new ByteListBox();
		final ByteListBox blueBox = new ByteListBox();

		public ColorSelectPanel(final String label) {
			this.label = label;

			add(new FormLabel(label));
			add(firstListBox);
			firstListBox.addChangeHandler(this);
			setItems(firstListBox);

			add(new FormLabel("R"));
			add(redBox);
			add(new FormLabel("G"));
			add(greenBox);
			add(new FormLabel("B"));
			add(blueBox);
		}

		private final void setItems(final ListBox listBox) {
			listBox.addItem("(default)", "default");
			listBox.addItem("black", "0,0,0");
			listBox.addItem("red", "233,52,71");
			listBox.addItem("blue", "0,111,171");
			listBox.addItem("green", "0,154,87");
			listBox.addItem("yellow", "244,213,0");
			listBox.addItem("user input");
		}

		public String getLabel() {
			return label;
		}

		public String getValue() {
			final String firstValue = firstListBox.getValue(firstListBox.getSelectedIndex());
			if (firstValue.equals("default"))
				return null;

			final StringBuffer buf = new StringBuffer();

			buf.append(redBox.getValue(redBox.getSelectedIndex()));
			buf.append(',');
			buf.append(greenBox.getValue(greenBox.getSelectedIndex()));
			buf.append(',');
			buf.append(blueBox.getValue(blueBox.getSelectedIndex()));

			return buf.toString();
		}

		public void onChange(ChangeEvent e) {
			final String firstValue = firstListBox.getValue(firstListBox.getSelectedIndex());

			if (firstValue.equals("user input")) {
				redBox.setEnabled(true);
				greenBox.setEnabled(true);
				blueBox.setEnabled(true);
			}
			else {
				if (!firstValue.equals("default")) {
					final String[] elements = firstValue.split(",");

					redBox.setSelectedIndex(Integer.parseInt(elements[0]));
					greenBox.setSelectedIndex(Integer.parseInt(elements[1]));
					blueBox.setSelectedIndex(Integer.parseInt(elements[2]));
				}

				redBox.setEnabled(false);
				greenBox.setEnabled(false);
				blueBox.setEnabled(false);
			}
		}

		public void addChangeHandler(ChangeHandler Handler) {
			firstListBox.addChangeHandler(Handler);

			redBox.addChangeHandler(Handler);
			greenBox.addChangeHandler(Handler);
			blueBox.addChangeHandler(Handler);
		}

		public void addKeyPressHandler(KeyPressHandler handler) {
			firstListBox.addKeyPressHandler(handler);

			redBox.addKeyPressHandler(handler);
			greenBox.addKeyPressHandler(handler);
			blueBox.addKeyPressHandler(handler);
		}

	}

	private class ByteListBox extends ListBox {
		private static final int SIZE = 256;

		ByteListBox() {
			for (int i = 0; i < SIZE; i++) {
				addItem(Integer.toString(i));
			}
			setEnabled(false);
		}
	}

}
