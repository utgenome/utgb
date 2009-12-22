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

import org.utgenome.gwt.utgb.client.UTGBClientException;
import org.utgenome.gwt.utgb.client.db.datatype.DataType;
import org.utgenome.gwt.utgb.client.db.datatype.InputForm;
import org.utgenome.gwt.utgb.client.track.impl.TrackConfigChangeImpl;
import org.utgenome.gwt.utgb.client.ui.DraggableTable;
import org.utgenome.gwt.utgb.client.ui.Icon;
import org.utgenome.gwt.utgb.client.ui.MouseMoveListener;

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
	private ConfigurationTable _configTable = new ConfigurationTable(this);
	private Label _label;
	private TrackConfig _self = this;

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

	public void addConfigParameter(DataType dataType, String defaultValue) {
		_configTable.addConfiguration(dataType, defaultValue);
	}

	public void addConfigParameter(String label, DataType dataType, String defaultValue) {
		_configTable.addConfiguration(dataType, label, defaultValue);
	}

	public String getParameter(String parameterName) {
		return _configTable.getValue(parameterName);
	}

	/**
	 * Set teh parameter value. This method does not notify the configuration change to the {@link TrackConfig}
	 * 
	 * @param parameterName
	 * @param value
	 */
	public void setParameter(String parameterName, String value) {
		_configTable.setValue(parameterName, value);
	}

	public void notifyConfigChange(String parameterName) {
		_track.onChangeTrackConfig(new TrackConfigChangeImpl(this, parameterName));
	}

}

class ConfigurationTable extends Composite {
	private TrackConfig _config;
	private DraggableTable _table = new DraggableTable();
	private HashMap<String, Entry> _paramToEntryMap = new HashMap<String, Entry>();

	class Entry extends Composite {
		class InputChangeListener implements KeyPressHandler, ChangeHandler {

			public void onKeyPress(KeyPressEvent e) {
				if (e.getCharCode() == KeyboardHandler.KEY_ENTER) {
					_config.notifyConfigChange(_parameterName);
				}
				else {
					// resize();
				}
			}

			public void onChange(ChangeEvent e) {
				_config.notifyConfigChange(_parameterName);
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
			_layoutPanel.setVerticalAlignment(DockPanel.ALIGN_MIDDLE);

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

	public ConfigurationTable(TrackConfig config) {
		_config = config;
		initWidget(_table);
	}

	public void addConfiguration(DataType dataType, String defaultValue) {
		addConfiguration(dataType, dataType.getName(), defaultValue);
	}

	public void addConfiguration(DataType dataType, String label, String defaultValue) {
		InputForm form = dataType.getInputForm();
		form.setValue(defaultValue);
		Entry entry = new Entry(label, dataType.getName(), form);
		_table.add(entry, entry.getDragEdge());
		_paramToEntryMap.put(dataType.getName(), entry);

		if (form.getOffsetWidth() > _table.getOffsetWidth())
			_table.setWidth((form.getOffsetWidth() + 10) + "px");
	}

	private boolean isValidParameterName(String parameterName) {
		if (_paramToEntryMap.containsKey(parameterName))
			return true;
		else {
			GWT.log("no input form for the given parameter name " + parameterName + " is found", new UTGBClientException());
			return false;
		}
	}

	public String getValue(String parameterName) {
		if (!isValidParameterName(parameterName))
			return "";

		Entry entry = _paramToEntryMap.get(parameterName);
		return entry.getForm().getUserInput();
	}

	public void setValue(String parameterName, String value) {
		if (!isValidParameterName(parameterName))
			return;

		Entry entry = _paramToEntryMap.get(parameterName);
		entry.getForm().setValue(value);
	}

}
