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
// MenuOperationItem.java
// Since: 2007/06/14
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.operation;

import org.utgenome.gwt.utgb.client.track.lib.old.Utilities;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;

/**
 * @author ssksn
 * 
 */
public class MenuOperationItem extends EventImpl implements ClickHandler {
	public String caption;
	private PopupPanel _popupPanel;

	public MenuOperationItem(final Node menuItemNode) {
		this(Utilities.getAttributeValue(menuItemNode, "caption"));
	}

	public MenuOperationItem(final String caption) {
		this.caption = caption;
	}

	public Widget getWidget(final PopupPanel popupPanel) {
		final Button widget = new Button(caption);
		widget.addClickHandler(this);

		_popupPanel = popupPanel;

		return widget;
	}

	public void onClick(ClickEvent e) {
		final int operationSize = operations.size();
		for (int i = 0; i < operationSize; i++) {
			final Operation operation = (Operation) (operations.get(i));
			operation.execute((Widget) e.getSource(), -1, -1);
		}

		_popupPanel.hide();
	}
}
