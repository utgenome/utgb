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
// AbsoluteFocusPanel.java
// Since: Jun 13, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.ui;

import org.utgenome.gwt.utgb.client.track.Design;

import com.google.gwt.event.dom.client.HasMouseDownHandlers;
import com.google.gwt.event.dom.client.HasMouseMoveHandlers;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;

/**
 * Absolute panel with Focus panel
 * 
 * @author leo
 * 
 */
public class AbsoluteFocusPanel extends AbsolutePanel implements HasMouseDownHandlers, HasMouseMoveHandlers {
	private Image _focusPanel = new Image(Design.IMAGE_TRANSPARENT);

	public AbsoluteFocusPanel() {
		add(_focusPanel, 0, 0);
		DOM.setStyleAttribute(_focusPanel.getElement(), "zIndex", "1000");
	}

	public void setSize(String width, String height) {
		super.setSize(width, height);
		_focusPanel.setSize(width, height);
	}

	public Image getFocusPanel() {
		return _focusPanel;
	}

	public void clear() {
		super.clear();
		add(_focusPanel, 0, 0);
	}

	public HandlerRegistration addMouseDownHandler(MouseDownHandler m) {
		return _focusPanel.addMouseDownHandler(m);
	}

	public HandlerRegistration addMouseMoveHandler(MouseMoveHandler m) {
		return _focusPanel.addMouseMoveHandler(m);
	}

}
