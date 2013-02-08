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
// DragListener.java
// Since: May 1, 2008
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.widget.client;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Mouse drag listener support
 * 
 * @author leo
 * 
 */
public class DragListener {

	PopupPanel widgetToMove;
	boolean isDragged = false;
	int dragStartX;
	int dragStartY;

	public DragListener(PopupPanel widgetToMove) {
		this.widgetToMove = widgetToMove;
	}

	public void onMouseDown(Widget sender, int x, int y) {
		isDragged = true;
		dragStartX = x;
		dragStartY = y;
		DOM.setCapture(sender.getElement());
	}

	public void onMouseMove(Widget sender, int x, int y) {
		if (isDragged) {
			widgetToMove.setPopupPosition(widgetToMove.getAbsoluteLeft() + x - dragStartX, widgetToMove.getAbsoluteTop() + y - dragStartY);
		}
	}

	public void onMouseUp(Widget sender, int x, int y) {
		isDragged = false;
		DOM.releaseCapture(sender.getElement());
	}

	public void onMouseEnter(Widget sender) {

	}

	public void onMouseLeave(Widget sender) {

	}

}
