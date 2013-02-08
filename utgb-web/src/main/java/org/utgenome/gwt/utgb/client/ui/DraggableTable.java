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
// DraggableTable.java
// Since: May 31, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.ui;

import java.util.Iterator;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.IndexedDropController;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * DraggabeleTable supports drag & drop replacement of internal widgets.
 * 
 * Note: it is better to set the width of the internal widgets to prevent them from expanding to the entire window.
 * 
 * @author leo
 * 
 */
public class DraggableTable extends Composite {
	public static final String TABLE_STYLE = "dg-table";

	private AbsolutePanel _boundaryPanel = new AbsolutePanel();
	private VerticalPanel _verticalPanel = new VerticalPanel();

	private PickupDragController widgetDragController;
	private IndexedDropController widgetDropController;

	public DraggableTable() {
		_boundaryPanel.setSize("100%", "100%");
		_boundaryPanel.add(_verticalPanel);
		_verticalPanel.setStyleName(TABLE_STYLE);

		// do not allow drop for _boundaryPanel
		widgetDragController = new PickupDragController(_boundaryPanel, false);
		widgetDropController = new IndexedDropController(_verticalPanel);

		widgetDragController.setBehaviorConstrainedToBoundaryPanel(false);
		widgetDragController.registerDropController(widgetDropController);

		initWidget(_boundaryPanel);
	}

	/**
	 * Widget must implements SourcesMouseEvents
	 * 
	 * @param w
	 *            widget
	 */
	public void add(Widget w) {
		widgetDragController.makeDraggable(w);
		_verticalPanel.add(w);
	}

	/**
	 * Get the index of the specified child widget position
	 * 
	 * @param w
	 * @return
	 */
	public int getIndex(Widget w) {
		return _verticalPanel.getWidgetIndex(w);
	}

	/**
	 * Add the widge with the specified drag handle before the location beforeIndex
	 * 
	 * @param w
	 * @param dragHandle
	 * @param beforeIndex
	 */
	public void insert(Widget w, Widget dragHandle, int beforeIndex) {
		widgetDragController.makeDraggable(w, dragHandle);
		_verticalPanel.insert(w, beforeIndex);
	}

	/**
	 * Add the widget with the specified drag hanel
	 * 
	 * @param w
	 * @param dragHandle
	 *            draggable part in the Widget w
	 */
	public void add(Widget w, Widget dragHandle) {
		widgetDragController.makeDraggable(w, dragHandle);
		_verticalPanel.add(w);
	}

	public Iterator<Widget> iterator() {
		return _verticalPanel.iterator();
	}

	public void remove(Widget w) {
		_verticalPanel.remove(w);
	}

	public boolean empty() {
		return _verticalPanel.getWidgetCount() == 0;
	}

	public void clear() {
		_verticalPanel.clear();
	}

}
