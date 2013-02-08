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
// TabTrackPanel.java
// Since: May 1, 2008
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.widget.client;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Tabbed frame
 * 
 * @author leo
 * 
 */
public class TabbedTrackFrame extends Composite {

	private AbsolutePanel boundaryPanel = new AbsolutePanel();
	private TabPanel tabPanel = new TabPanel();
	// private PickupDragController dragController;

	private ArrayList<Tab> tabList = new ArrayList<Tab>();

	public TabbedTrackFrame() {
		Style.fullSize(boundaryPanel);
		Style.fullSize(tabPanel);
		boundaryPanel.add(tabPanel);
		// dragController = new PickupDragController(boundaryPanel, false);

		initWidget(boundaryPanel);
	}

	public Panel getTabContent(int index) {
		return (Panel) tabPanel.getWidget(index);
	}

	private class TabClickListener implements MouseDownHandler {
		private final int tabIndex;

		public TabClickListener(int tabIndex) {
			this.tabIndex = tabIndex;
		}

		public void onMouseDown(MouseDownEvent arg0) {
			selectTab(tabIndex);
		}
	}

	public void addTab(TrackPanel panel, Tab tabWidget) {

		tabPanel.add((Widget) panel, tabWidget);
		int tabIndex = tabPanel.getTabBar().getTabCount() - 1;
		tabList.add(tabWidget);

		tabWidget.setParenTabPanel(tabPanel, (Widget) panel);
		tabWidget.addMouseDownHandler(new TabClickListener(tabIndex));

		// TabDropController tabDropController = new TabDropController(tabWidget, tabPanel, tabIndex);
		// dragController.registerDropController(tabDropController);

	}

	//	
	// public void makeDraggable(TrackFrame trackPanel) {
	// dragController.makeDraggable(trackPanel, trackPanel.getDraggableWidget());
	// }

	public void enableClose(int tabIndex, boolean enableClose) {
		if (tabIndex > tabList.size())
			throw new IndexOutOfBoundsException("out of bound tab index: " + tabIndex);

		tabList.get(tabIndex).enableCloseButton(enableClose);
	}

	public void selectTab(int index) {
		tabPanel.selectTab(index);

		for (int i = 0; i < tabList.size(); i++) {
			Tab tab = tabList.get(i);
			tabList.get(i).setSelect(i == index);
		}
	}

}
