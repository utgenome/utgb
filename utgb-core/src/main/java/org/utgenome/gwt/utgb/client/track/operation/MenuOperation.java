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
// MenuOperation.java
// Since: 2007/06/14
//
// $URL$ 
// $Author$ ssksn
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.operation;

import java.util.ArrayList;

import org.utgenome.gwt.utgb.client.track.lib.old.Utilities;

import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;

/**
 * @author ssksn
 * 
 */
public class MenuOperation implements Operation {
	final String title;
	final ArrayList<MenuOperationItem> menuItems = new ArrayList<MenuOperationItem>();

	public MenuOperation(final Node menuOperationNode) {
		this(Utilities.getAttributeValue(menuOperationNode, "title"));
	}

	public MenuOperation(final String title) {
		this.title = title;
	}

	class MenuPopupWindow extends PopupPanel {
		public MenuPopupWindow(final String title, final boolean autoHide, final ArrayList<MenuOperationItem> _menuItems) {
			super(autoHide);

			setTitle(title);

			final DockPanel _panel = new DockPanel();
			_panel.setHorizontalAlignment(DockPanel.ALIGN_CENTER);
			_panel.setVerticalAlignment(DockPanel.ALIGN_MIDDLE);
			_panel.setStyleName("menu-popup");
			// _panel.setSize("150px", "150px");

			final Label titleLabel = new Label(title);
			titleLabel.setStyleName("menu-popup-title");
			_panel.add(titleLabel, DockPanel.NORTH);

			final VerticalPanel verticalPanel = new VerticalPanel();
			verticalPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
			verticalPanel.setVerticalAlignment(VerticalPanel.ALIGN_TOP);
			verticalPanel.setWidth("100%");

			{ // add menu items
				final int itemNum = menuItems.size();
				for (int i = 0; i < itemNum; i++) {
					final MenuOperationItem menuItem = (MenuOperationItem) (menuItems.get(i));

					final Widget menuWidget = menuItem.getWidget(this);
					menuWidget.setWidth("100%");
					verticalPanel.add(menuWidget);
				}
			}

			final ScrollPanel scrollPanel = new ScrollPanel(verticalPanel);
			scrollPanel.setAlwaysShowScrollBars(false);
			_panel.add(scrollPanel, DockPanel.CENTER);
			scrollPanel.setWidth("100%");
			scrollPanel.setHeight("150px");
			setWidget(_panel);

			// setSize("150px", "150px");
		}
	}

	public void execute(Widget sender, int x, int y) {
		final MenuPopupWindow menuWindow = new MenuPopupWindow(title, true, menuItems);

		{ // set location
			final int offsetX = sender.getAbsoluteLeft();
			final int offsetY = sender.getAbsoluteTop();

			menuWindow.setPopupPosition(offsetX + x, offsetY + y);
		}

		menuWindow.show();
	}

	public void addMenuItem(final MenuOperationItem menuItem) {
		menuItems.add(menuItem);
	}

}
