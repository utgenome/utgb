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
// MouseMoveListener.java
// Since: Jun 27, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.ui;

import com.google.gwt.event.dom.client.HasAllMouseHandlers;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.PopupPanel;

public class MouseMoveListener implements MouseDownHandler, MouseMoveHandler, MouseUpHandler {

	PopupPanel widgetToMove;
	boolean isDragged = false;
	int dragStartX;
	int dragStartY;

	public MouseMoveListener(PopupPanel widgetToMove) {
		this.widgetToMove = widgetToMove;
	}

	public void register(HasAllMouseHandlers w) {
		w.addMouseDownHandler(this);
		w.addMouseMoveHandler(this);
		w.addMouseUpHandler(this);
	}

	public void onMouseDown(MouseDownEvent e) {
		isDragged = true;
		dragStartX = e.getX();
		dragStartY = e.getY();
		Event.setCapture(e.getRelativeElement());
	}

	public void onMouseMove(MouseMoveEvent e) {
		if (isDragged) {
			widgetToMove.setPopupPosition(widgetToMove.getAbsoluteLeft() + e.getX() - dragStartX, widgetToMove.getAbsoluteTop() + e.getY() - dragStartY);
		}
	}

	public void onMouseUp(MouseUpEvent e) {
		isDragged = false;
		Event.releaseCapture(e.getRelativeElement());
	}

}
