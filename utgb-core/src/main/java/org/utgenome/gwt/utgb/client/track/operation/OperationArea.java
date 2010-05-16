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
// OperationArea.java
// Since: 2007/06/13
//
// $URL$ 
// $Author$ ssksn
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.operation;

import org.utgenome.gwt.utgb.client.util.Utilities;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.xml.client.Node;

/**
 * 
 * 
 * @author ssksn
 * @since GWT 1.4
 * @version 0.1
 */
public class OperationArea extends Image implements MouseOverHandler, MouseOutHandler {
	protected final int startX;
	protected final int startY;
	protected final int endX;
	protected final int endY;

	protected final String activeImageURL;
	protected final String inactiveImageURL;

	private static final String DEFAULT_ACTIVE_IMAGE_URL = "theme/image/pink.gif";
	private static final String DEFAULT_INACTIVE_IMAGE_URL = "theme/image/transparent.gif";

	public OperationArea(final int startX, final int startY, final int endX, final int endY, final String activeImageURL, final String inactiveImageURL) {
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		this.activeImageURL = activeImageURL;
		this.inactiveImageURL = inactiveImageURL;

		setUrl(this.inactiveImageURL);
		setStyleName("operation-area");
		setPixelSize(getWidth(), getHeight());

		addMouseOverHandler(this);
		addMouseOutHandler(this);
	}

	public OperationArea(final int startX, final int startY, final int endX, final int endY) {
		this(startX, startY, endX, endY, DEFAULT_ACTIVE_IMAGE_URL, DEFAULT_INACTIVE_IMAGE_URL);
	}

	public static OperationArea newInstance(final Node rectAreaNode) {
		final String nodeName = rectAreaNode.getNodeName();
		if (!nodeName.equals("rect_area"))
			throw new IllegalArgumentException(nodeName);

		// set area
		final String rectStr = Utilities.getAttributeValue(rectAreaNode, "rect");
		{
			final String _trimmedStr = rectStr.trim();
			final String trimmedStr = _trimmedStr.substring(1, _trimmedStr.length() - 1);

			final String[] elements = trimmedStr.split(",");

			final int startX = (int) (Math.floor(Double.parseDouble(elements[0])));
			final int startY = (int) (Math.floor(Double.parseDouble(elements[1])));
			final int endX = (int) (Math.ceil(Double.parseDouble(elements[2])));
			final int endY = (int) (Math.ceil(Double.parseDouble(elements[3])));

			return new OperationArea(startX, startY, endX, endY);
		}
	}

	public int getStartX() {
		return startX;
	}

	public int getStartY() {
		return startY;
	}

	public int getEndX() {
		return endX;
	}

	public int getEndY() {
		return endY;
	}

	public int getWidth() {
		return endX - startX;
	}

	public int getHeight() {
		return endY - startY;
	}

	public void addEventHandler(final EventHandler eventHandler) {
		if (eventHandler instanceof MouseOverHandler) {
			final MouseOverHandler mouseHandler = (MouseOverHandler) eventHandler;
			addMouseOverHandler(mouseHandler);
		}
		if (eventHandler instanceof MouseOutHandler) {
			final MouseOutHandler mouseHandler = (MouseOutHandler) eventHandler;
			addMouseOutHandler(mouseHandler);
		}

		if (eventHandler instanceof ClickHandler) {
			final ClickHandler clickHandler = (ClickHandler) eventHandler;
			addClickHandler(clickHandler);
		}
	}

	public void onMouseOver(MouseOverEvent e) {
		DOM.setCapture(getElement());
		setActive();
	}

	public void onMouseOut(MouseOutEvent e) {
		setInactive();
		DOM.releaseCapture(getElement());
	}

	public static void add(final AbsolutePanel absolutePanel, final OperationArea operationArea) {
		absolutePanel.add(operationArea, operationArea.getStartX(), operationArea.getStartY());
	}

	protected void setActive() {
		setUrl(activeImageURL);
	}

	protected void setInactive() {
		setUrl(inactiveImageURL);
	}
}
