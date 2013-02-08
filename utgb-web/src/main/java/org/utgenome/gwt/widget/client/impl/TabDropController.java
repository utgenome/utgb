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
// TabDropController.java
// Since: May 1, 2008
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.widget.client.impl;

import java.util.Iterator;

import org.utgenome.gwt.widget.client.TrackFrame;
import org.utgenome.gwt.widget.client.TrackPanel;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.drop.AbstractDropController;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Tab drop
 * 
 * @author leo
 * 
 */
public class TabDropController extends AbstractDropController {

	private final TabPanel tabPanel;
	private int tabIndex;

	public TabDropController(Widget dropTarget, TabPanel tabPanel, int tabIndex) {
		super(dropTarget);
		this.tabPanel = tabPanel;
		this.tabIndex = tabIndex;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onDrop(DragContext context) {

		TrackPanel tab = (TrackPanel) tabPanel.getWidget(tabIndex);

		for (Iterator it = context.selectedWidgets.iterator(); it.hasNext();) {
			TrackFrame widget = (TrackFrame) it.next();
			tab.add(widget);
		}
		super.onDrop(context);
	}

	@Override
	public void onEnter(DragContext context) {
		super.onEnter(context);
		tabPanel.selectTab(tabIndex);
		context.dragController.resetCache();
	}

}
