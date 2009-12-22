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
// Icon.java
// Since: Apr 24, 2008
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.widget.client;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Image;

/**
 * Icon image which flips on mouse over events
 * 
 * @author leo
 * 
 */
public class Icon extends FocusPanel {

	private Element currentFace = null;
	private Image iconImage;
	private Image mouseOverIconImage;

	//private ArrayList<ClickHandler> clickListenerList = new ArrayList<ClickHandler>();

	// icon state
	private boolean isFocusing = false;
	private boolean isCapturing = false;
	private boolean isHovering = false;

	public Icon(Image iconImage, Image mouseOverIconImage) {
		this.iconImage = iconImage;
		this.mouseOverIconImage = mouseOverIconImage;

		Style.cursor(this, Style.CURSOR_POINTER);

		updateIconFace(iconImage.getElement());
	}

	public void setIcon(Icon newIcon) {
		this.iconImage = newIcon.getIconImage();
		this.mouseOverIconImage = newIcon.getMouseOverIconImage();

		updateIconFace(iconImage.getElement());
	}

	public void setIconImage(Image newIconImage) {
		this.iconImage = newIconImage;
		this.mouseOverIconImage = newIconImage;

		updateIconFace(iconImage.getElement());
	}

	private void updateIconFace(Element newFace) {
		if (currentFace != newFace) {
			if (currentFace != null)
				DOM.removeChild(getElement(), currentFace);

			currentFace = newFace;
			DOM.appendChild(getElement(), currentFace);
		}
	}

	public Image getIconImage() {
		return iconImage;
	}

	public Image getMouseOverIconImage() {
		return mouseOverIconImage;
	}

	public void addClickHanlder(ClickHandler listener) {
		this.addClickHandler(listener);
	}

	private void setHovering(boolean hovering) {
		this.isHovering = hovering;
		updateIconFace(isHovering ? mouseOverIconImage.getElement() : iconImage.getElement());
	}

	/**
	 * Called when the user finishes clicking on this button. The default behavior is to fire the click event to
	 * listeners. Subclasses that override {@link #onClickStart()} should override this method to restore the normal
	 * widget display.
	 */
	protected void onClick() {
		NativeEvent e = Document.get().createClickEvent(1, 0, 0, 0, 0, false, false, false, false);

	}

	/**
	 * Called when the user aborts a click in progress; for example, by dragging the mouse outside of the button before
	 * releasing the mouse button. Subclasses that override {@link #onClickStart()} should override this method to
	 * restore the normal widget display.
	 */
	protected void onClickCancel() {

	}

	/**
	 * Called when the user begins to click on this button. Subclasses may override this method to display the start of
	 * the click visually; such subclasses should also override {@link #onClick()} and {@link #onClickCancel()} to
	 * restore normal visual state. Each <code>onClickStart</code> will eventually be followed by either
	 * <code>onClick</code> or <code>onClickCancel</code>, depending on whether the click is completed.
	 */
	protected void onClickStart() {

	}

	@Override
	public void onBrowserEvent(Event event) {
		// Should not act on button if disabled.

		int type = DOM.eventGetType(event);
		switch (type) {
		case Event.ONMOUSEDOWN:
			isFocusing = true;
			onClickStart();
			// DOM.setCapture(getElement());
			isCapturing = true;
			// Prevent dragging (on some browsers);
			DOM.eventPreventDefault(event);
			break;
		case Event.ONMOUSEUP:
			if (isCapturing) {
				isCapturing = false;
				// DOM.releaseCapture(getElement());
				if (isHovering) {
					onClick();
				}
			}
			break;
		case Event.ONMOUSEMOVE:
			if (isCapturing) {
				// Prevent dragging (on other browsers);
				DOM.eventPreventDefault(event);
			}
			break;
		case Event.ONMOUSEOUT:
			Element to = DOM.eventGetToElement(event);
			if (DOM.isOrHasChild(getElement(), DOM.eventGetTarget(event)) && (to == null || !DOM.isOrHasChild(getElement(), to))) {
				if (isCapturing) {
					onClickCancel();
					// DOM.releaseCapture(getElement());
				}
			}
			setHovering(false);
			break;
		case Event.ONMOUSEOVER:
			if (DOM.isOrHasChild(getElement(), DOM.eventGetTarget(event))) {
				setHovering(true);
				if (isCapturing) {
					onClickStart();
				}
			}
			break;
		case Event.ONCLICK:
			// we handle clicks ourselves
			return;
		case Event.ONBLUR:
			if (isFocusing) {
				isFocusing = false;
				onClickCancel();
			}
			break;
		case Event.ONLOSECAPTURE:
			if (isCapturing) {
				isCapturing = false;
				onClickCancel();
			}
			break;
		case Event.ONERROR:
			setHovering(false);
			break;
		}

		super.onBrowserEvent(event);

		// Synthesize clicks based on keyboard events AFTER the normal key handling.
		char keyCode = (char) DOM.eventGetKeyCode(event);
		switch (type) {
		case Event.ONKEYDOWN:
			if (keyCode == ' ') {
				isFocusing = true;
				onClickStart();
			}
			break;
		case Event.ONKEYUP:
			if (isFocusing && keyCode == ' ') {
				isFocusing = false;
				onClick();
			}
			break;
		case Event.ONKEYPRESS:
			if (keyCode == '\n' || keyCode == '\r') {
				onClickStart();
				onClick();
			}
			break;
		}
	}

}
