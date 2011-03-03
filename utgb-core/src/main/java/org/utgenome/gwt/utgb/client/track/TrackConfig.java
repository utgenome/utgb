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
// TrackConfig.java
// Since: Jun 19, 2007a
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track;

import java.util.HashMap;

import org.utgenome.gwt.utgb.client.db.datatype.BooleanType;
import org.utgenome.gwt.utgb.client.db.datatype.DataType;
import org.utgenome.gwt.utgb.client.db.datatype.DoubleType;
import org.utgenome.gwt.utgb.client.db.datatype.InputForm;
import org.utgenome.gwt.utgb.client.db.datatype.IntegerType;
import org.utgenome.gwt.utgb.client.db.datatype.StringType;
import org.utgenome.gwt.utgb.client.track.impl.TrackConfigChangeImpl;
import org.utgenome.gwt.utgb.client.ui.DraggableTable;
import org.utgenome.gwt.utgb.client.ui.Icon;
import org.utgenome.gwt.utgb.client.ui.MouseMoveListener;
import org.utgenome.gwt.utgb.client.util.CanonicalProperties;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.widgetideas.client.event.KeyboardHandler;

/**
 * Track configuration panel.
 * 
 * @author leo
 * 
 */
public class TrackConfig extends PopupPanel {

	class CloseButton extends Icon {
		public CloseButton(final PopupPanel popup) {
			super(Design.getIconImage(Design.ICON_CLOSE));
			setStyleName("track-icon");

			addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent e) {
					popup.hide();
				}
			});
		}
	}

	private Track _track;
	private DockPanel _panel = new DockPanel();
	private ConfigurationTable _configTable = new ConfigurationTable();
	private Label _label;
	CanonicalProperties properties = new CanonicalProperties();
	CanonicalProperties defaultValueTable = new CanonicalProperties();

	public TrackConfig(Track track) {
		super(true);
		init(track);
	}

	public void init(Track track) {
		this._track = track;

		_label = new Label(track.getTrackInfo().getTrackName() + " Configuration");
		_label.setStyleName("config-label");
		new MouseMoveListener(this).register(_label);

		DOM.setStyleAttribute(_label.getElement(), "cursor", "move");
		DOM.setStyleAttribute(_label.getElement(), "display", "block");
		DOM.setStyleAttribute(_label.getElement(), "width", "100%");
		CloseButton cb = new CloseButton(this);

		Grid grid = new Grid(1, 2);
		grid.getColumnFormatter().setWidth(0, "15px");
		grid.getColumnFormatter().setWidth(1, "100%");
		grid.setWidth("100%");
		grid.setStyleName("config-frame");
		grid.setWidget(0, 0, cb);
		grid.setWidget(0, 1, _label);

		final DockPanel simplePanel = new DockPanel();
		simplePanel.add(_configTable, DockPanel.CENTER);
		final ScrollPanel scrollPanel = new ScrollPanel(simplePanel) {
			@Override
			protected void onAttach() {
				super.onAttach();
				final int height = getOffsetHeight();
				if (height > 500) {
					// setHeight("500px");
				}
			}
		};
		this.setStyleName("config");
		_panel.add(grid, DockPanel.NORTH);
		_panel.add(scrollPanel, DockPanel.CENTER);
		this.setWidget(_panel);
		setWidth("500px");
	}

	public void addConfigString(String label, String paramName, String defaultValue) {
		addConfig(label, new StringType(paramName), defaultValue);
	}

	public void addConfigInteger(String label, String paramName, int defaultValue) {
		addConfig(label, new IntegerType(paramName), Integer.toString(defaultValue));
	}

	public void addConfigBoolean(String label, String paramName, boolean defaultValue) {
		addConfig(label, new BooleanType(paramName), Boolean.toString(defaultValue));
	}

	public void addConfigDouble(String label, String paramName, double defaultValue) {
		addConfig(label, new DoubleType(paramName), Double.toString(defaultValue));
	}

	public void addConfig(DataType dataType, String defaultValue) {
		addConfig(dataType.getName(), dataType, defaultValue);
	}

	public void addConfig(String label, DataType dataType, String defaultValue) {
		_configTable.addConfiguration(dataType, label, defaultValue);
	}

	public void addHiddenConfig(String paramName, String defaultValue) {
		properties.put(paramName, properties.get(paramName, defaultValue));
	}

	public String getParameter(String parameterName) {
		return properties.get(parameterName);
	}

	/**
	 * Set the parameter value. This method does not notify the configuration change to the {@link TrackConfig}
	 * 
	 * @param parameterName
	 * @param value
	 */
	public void setParameter(String parameterName, String value) {
		properties.put(parameterName, value);
		_configTable.setValue(parameterName, value);
	}

	public String getString(String parameterName, String defaultValue) {
		return properties.get(parameterName, defaultValue);
	}

	public int getInt(String parameterName, int devaultValue) {
		return properties.getInt(parameterName, devaultValue);
	}

	public float getFloat(String parameterName, float defaultValue) {
		return properties.getFloat(parameterName, defaultValue);
	}

	public boolean getBoolean(String parameterName, boolean devaultValue) {
		return properties.getBoolean(parameterName, devaultValue);
	}

	public void notifyConfigChange(String parameterName) {
		_track.onChangeTrackConfig(new TrackConfigChangeImpl(this, parameterName));
	}

	public void saveProperties(CanonicalProperties toSave) {

		for (String key : properties.keySet()) {
			String nKey = CanonicalProperties.toNaturalName(key);
			toSave.put(nKey, properties.get(key));
		}
	}

	public void restoreProperties(CanonicalProperties forLoad) {
		properties.putAll(forLoad);
	}

	public boolean hasProperties() {
		return !properties.isEmpty();
	}

	class ConfigurationTable extends Composite {
		private DraggableTable _table = new DraggableTable();
		private HashMap<String, Entry> _paramToEntryMap = new HashMap<String, Entry>();

		class Entry extends Composite {
			class InputChangeListener implements KeyPressHandler, ChangeHandler {

				public void onKeyPress(KeyPressEvent e) {
					if (e.getCharCode() == KeyboardHandler.KEY_ENTER) {
						properties.put(_parameterName, _form.getUserInput());
						notifyConfigChange(_parameterName);
					}
					else {
						// resize();
					}
				}

				public void onChange(ChangeEvent e) {
					properties.put(_parameterName, _form.getUserInput());
					notifyConfigChange(_parameterName);
				}

			}

			private String _parameterName;
			private Label _label;
			private InputForm _form;
			private DockPanel _layoutPanel = new DockPanel();

			/**
			 * @param label
			 * @param form
			 */
			public Entry(String parameterLabel, String parameterName, InputForm form) {
				this._parameterName = parameterName;
				this._label = new Label(parameterLabel + ":");
				this._form = form;

				_label.setStyleName("form-label");
				_form.setStyleName("form-field");
				// resize();

				_layoutPanel.setStyleName("form");
				_layoutPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

				_layoutPanel.add(_label, DockPanel.WEST);
				_layoutPanel.add(_form, DockPanel.CENTER);

				InputChangeListener listener = new InputChangeListener();
				_form.addKeyPressHandler(listener);
				_form.addChangeHandler(listener);

				initWidget(_layoutPanel);
			}

			public void resize() {
				String input = _form.getUserInput();
				int widgetWidth = _form.getOffsetWidth();
				if (input != null) {
					int width = input.length() * 8;
					width = width > widgetWidth ? width : widgetWidth;
					if (width > 800)
						width = 800;
					_form.setWidth(width + "px");
				}
			}

			public Label getDragEdge() {
				return _label;
			}

			public InputForm getForm() {
				return _form;
			}

		}

		public ConfigurationTable() {
			initWidget(_table);
		}

		public void addConfiguration(DataType dataType, String label, String defaultValue) {
			InputForm form = dataType.getInputForm();
			String currentValue = getParameter(dataType.getName());
			if (currentValue == null)
				currentValue = defaultValue;

			String cKey = CanonicalProperties.toCanonicalName(dataType.getName());
			properties.put(cKey, currentValue);
			form.setValue(currentValue);
			Entry entry = new Entry(label, cKey, form);
			_table.add(entry, entry.getDragEdge());
			_paramToEntryMap.put(cKey, entry);

			if (form.getOffsetWidth() > _table.getOffsetWidth())
				_table.setWidth((form.getOffsetWidth() + 10) + "px");
		}

		private boolean isValidParameterName(String cKey) {
			if (_paramToEntryMap.containsKey(cKey))
				return true;
			else {
				GWT.log("[WARN] no input form for the given parameter name " + cKey + " is found", null);
				return false;
			}
		}

		public String getValue(String parameterName) {
			String cKey = CanonicalProperties.toCanonicalName(parameterName);
			if (!isValidParameterName(cKey))
				return "";

			Entry entry = _paramToEntryMap.get(cKey);
			return entry.getForm().getUserInput();
		}

		public void setValue(String parameterName, String value) {
			String cKey = CanonicalProperties.toCanonicalName(parameterName);

			if (!isValidParameterName(cKey))
				return;

			Entry entry = _paramToEntryMap.get(cKey);
			entry.getForm().setValue(value);
		}

	}

}
