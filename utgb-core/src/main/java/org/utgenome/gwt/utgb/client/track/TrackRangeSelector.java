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
// UTGB Gallery Project
//
// TrackRangeSelector.java
// Since: Apr 3, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track;

import org.utgenome.gwt.utgb.client.ui.AbsoluteFocusPanel;
import org.utgenome.gwt.widget.client.Style;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

/**
 * {@link TrackRangeSelector} supports range selection on a displayed track. In order to use {@link TrackRangeSelector},
 * your track must implement the {@link RangeSelectable} interface.
 * 
 * @author leo
 * 
 */
public class TrackRangeSelector implements MouseDownHandler, MouseMoveHandler {
	boolean isInRangeSelectMode = false;
	HTML rangeIndicator = new HTML();

	RangeSelectable track;
	AbsoluteFocusPanel trackPanel;
	int x1 = -1;
	int x2 = -1;
	int windowWidth = 800;

	public TrackRangeSelector(RangeSelectable track) {
		this.track = track;
		this.trackPanel = track.getAbsoluteFocusPanel();

		Style.fontSize(rangeIndicator, 8);
		Style.border(rangeIndicator, 1, "solid", "#66CCFF");
		Style.margin(rangeIndicator, 0);
		Style.padding(rangeIndicator, 0);
		Style.zIndex(rangeIndicator, 2000);
		//rangeIndicator.setStyleName("track-range");
		rangeIndicator.setHeight("10px");

		trackPanel.addMouseMoveHandler(this);
		trackPanel.addMouseDownHandler(this);

	}

	public void setWindowWidth(int windowWidth) {
		this.windowWidth = windowWidth;
	}

	/**
	 * An event handler when a mouse down event is reported from a child widget of the trackPanel.
	 * 
	 * @param x
	 * @param y
	 */
	public void onMouseDownFromChild(Widget sender, int x, int y) {
		//		int xOnWindow = x - trackPanel.getFocusPanel().getAbsoluteLeft();
		//		if (xOnWindow < 0)
		//			xOnWindow = 0;
		//		onMouseDown(trackPanel.getFocusPanel(), xOnWindow, y);
		onMouseDown(trackPanel.getFocusPanel().getElement(), x + sender.getAbsoluteLeft() - trackPanel.getAbsoluteLeft(), y);
	}

	public void onMouseDownFromChild(MouseDownEvent e) {
		onMouseDown(trackPanel.getFocusPanel().getElement(), e.getScreenX() - trackPanel.getAbsoluteLeft(), e.getY());

	}

	public void onMouseDown(MouseDownEvent e) {
		onMouseDown((Element) e.getRelativeElement().cast(), e.getX(), e.getY());
	}

	public void onMouseDown(Element element, int x, int y) {

		if (!isInRangeSelectMode) {
			GWT.log("range start: x1= " + x, null);
			Event.setCapture(element);
			x1 = x;
			rangeIndicator.setWidth("1px");
			trackPanel.add(rangeIndicator, x1, 0);
		}
		else {
			Event.releaseCapture(element);
			x2 = (x < windowWidth) ? x : windowWidth;
			GWT.log("range   end: x2= " + x2, null);
			trackPanel.remove(rangeIndicator);
			if (x1 >= 0 && x2 >= 0)
				track.onRangeSelect(x1, x2);
		}

		isInRangeSelectMode = !isInRangeSelectMode;
	}

	public void onMouseMove(MouseMoveEvent e) {
		if (!isInRangeSelectMode)
			return;

		int x = e.getX();
		int width = x - x1;
		if (width <= 0) {
			rangeIndicator.removeFromParent();
			trackPanel.add(rangeIndicator, x, 0);
			width = -width;
		}
		if (width >= windowWidth)
			width = windowWidth;
		rangeIndicator.setWidth(width + "px");
	}

}
