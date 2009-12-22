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
// Aqua Project
//
// TabViewer.java
// Since: 2007/03/24
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.ui.tab;

import java.util.ArrayList;

import org.utgenome.gwt.utgb.client.ui.tab.TabEntry.TabEntryFactory;

import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.TabBar;

/**
 * A TabViewer can have several TabEntry classes.
 * 
 * <code>
 * TabViewer v = new TabViewer();
 * v.addTabEntry(MyTab.factory());
 * v.addTabEntry(MyTab2.facory());
 * </code>
 * 
 * @author leo
 * 
 */
public class TabViewer extends Composite implements BeforeSelectionHandler<Integer>, SelectionHandler<Integer> {

	private ArrayList<TabEntryFactory> _tabEntryList = new ArrayList<TabEntryFactory>();
	private DockPanel _panel = new DockPanel();
	private TabBar _tabBar = new TabBar();
	private DockPanel _entryPanel = new DockPanel();

	public TabViewer() {
		_panel.add(_tabBar, DockPanel.NORTH);

		_tabBar.addSelectionHandler(this);
		_tabBar.addBeforeSelectionHandler(this);

		_panel.add(_entryPanel, DockPanel.CENTER);

		_panel.setWidth("100%");
		_entryPanel.setWidth("100%");

		initWidget(_panel);
	}

	public void selectTab(int index) {
		_tabBar.selectTab(index);
	}

	public void addTabEntry(final TabEntryFactory factory) {
		String name = factory.getTabName();
		_tabEntryList.add(factory);
		_tabBar.addTab(name);
	}

	public void onBeforeSelection(BeforeSelectionEvent<Integer> e) {

		TabEntryFactory factory = _tabEntryList.get(e.getItem());

		// calling focus events
		for (TabEntryFactory f : _tabEntryList) {
			if (f.equals(factory))
				continue; // skip the focused tab entry
			if (f.isInstanciated()) {
				f.getInstance().onLostFocus();
			}
		}
	}

	public void onSelection(SelectionEvent<Integer> e) {

		_entryPanel.clear();
		TabEntryFactory factory = _tabEntryList.get(e.getSelectedItem());
		TabEntry entry = factory.getInstance();
		_entryPanel.add(entry, DockPanel.CENTER);
		entry.onFocus();

	}
}
