/*--------------------------------------------------------------------------
 *  Copyright 2008 utgenome.org
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
// utgb-widget Project
//
// HorizontalDraggablePanel.java
// Since: May 1, 2008
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.widget.client;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.VerticalPanelDropController;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.Iterator;

/**
 * VerticalPanel with Drag & Drop support
 * 
 * @author leo
 * 
 */
public class VerticalDraggableTrackPanel extends Composite implements TrackPanel {

	private class MyDragController extends PickupDragController {
		public MyDragController(AbsolutePanel boundaryPanel, boolean allowDroppingOnBoundaryPanel) {
			super(boundaryPanel, allowDroppingOnBoundaryPanel);
		}

		@Override
		public void dragStart() {
			Style.overflowHidden(absolutePanel);
			super.dragStart();
		}

		@Override
		public void dragEnd() {
			Style.overflowAuto(absolutePanel);
			super.dragEnd();
		}
	}

	public static final String TABLE_STYLE = "dg-table";

	private AbsolutePanel absolutePanel = new AbsolutePanel();
	private VerticalPanel verticalPanel = new VerticalPanel();

	private MyDragController widgetDragController;
	private VerticalPanelDropController widgetDropController;

	public VerticalDraggableTrackPanel() {

		initWidget(absolutePanel);
		drawWidget();

		widgetDragController = new MyDragController(absolutePanel, false);
		widgetDropController = new VerticalPanelDropController(verticalPanel);

		widgetDragController.setBehaviorConstrainedToBoundaryPanel(false);
		widgetDragController.registerDropController(widgetDropController);

	}

	public void setSpacing(int spacing) {
		verticalPanel.setSpacing(0);
	}

	public void setBorderWidth(int borderWidth) {
		verticalPanel.setBorderWidth(0);
	}

	protected void drawWidget() {
		Style.fullSize(absolutePanel);
		Style.overflowAuto(absolutePanel);
		absolutePanel.add(verticalPanel);
	}

	/**
	 * Do not make the widget draggable
	 * 
	 * @param w
	 */
	public void add(Widget w) {
		if (w instanceof TrackFrame) {
			TrackFrame frame = (TrackFrame) w;
			add(frame, frame.getDraggableWidget());
		}
		else
			verticalPanel.add(w);
	}

	/**
	 * Get the index of the specified child widget position
	 * 
	 * @param w
	 * @return
	 */
	public int getIndex(Widget w) {
		return verticalPanel.getWidgetIndex(w);
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
		verticalPanel.insert(w, beforeIndex);
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
		verticalPanel.add(w);
	}

	public Iterator<Widget> iterator() {
		return verticalPanel.iterator();
	}

	public boolean remove(Widget w) {
		return verticalPanel.remove(w);
	}

	public boolean empty() {
		return verticalPanel.getWidgetCount() == 0;
	}

	public void clear() {
		verticalPanel.clear();
	}

}
