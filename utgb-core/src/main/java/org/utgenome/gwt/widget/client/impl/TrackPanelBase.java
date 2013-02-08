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
// TrackPanelBase.java
// Since: May 1, 2008
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.widget.client.impl;

import org.utgenome.gwt.widget.client.TrackButtonListener;
import org.utgenome.gwt.widget.client.TrackFrame;
import org.utgenome.gwt.widget.client.UTGBDesignFactory;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

/**
 * base implementation of the track panel
 * 
 * @author leo
 * 
 */
public abstract class TrackPanelBase extends TrackFrame {

	protected IconSetPanel iconSet = new IconSetPanel(this);
	private TrackResizeButton resizeButton;

	private boolean enableResizeX = true;
	private boolean enableResizeY = true;

	public TrackPanelBase() {
		UTGBDesignFactory designFactory = new UTGBDesignFactory();
		Image resizeBar = new Image(designFactory.getUTGBImageBundle().windowResizeIcon());
		resizeBar.setTitle("drag this to resize");

		resizeButton = new TrackResizeButton(this, resizeBar);
	}

	public void enableResizeWidth(boolean enable) {
		this.enableResizeX = enable;
	}

	public void enableResizeHeight(boolean enable) {
		this.enableResizeY = enable;
	}

	class TrackResizeButton extends Composite {
		private final TrackFrame resizeTarget;

		private Image resizeBar;

		private int initialY_Position = 0;
		private int initialX_Position = 0;
		private int initial_height = 0;
		private int initial_width = 0;

		private boolean isFocusing = false;
		private boolean isCapturing = false;

		public TrackResizeButton(TrackFrame resizeTarget, Image resizeBar) {
			this.resizeTarget = resizeTarget;
			this.resizeBar = resizeBar;

			initWidget(resizeBar);
		}

		@Override
		public void onBrowserEvent(Event event) {

			int type = DOM.eventGetType(event);
			switch (type) {
			case Event.ONMOUSEDOWN:
				isFocusing = true;
				// onClickStart();
				DOM.setCapture(getElement());
				isCapturing = true;
				// Prevent dragging (on some browsers);
				DOM.eventPreventDefault(event);

				initialX_Position = event.getClientX();
				initialY_Position = event.getClientY();
				initial_width = resizeTarget.getOffsetWidth();
				initial_height = resizeTarget.getOffsetHeight();
				break;
			case Event.ONMOUSEUP:
				if (isCapturing) {
					isCapturing = false;
					DOM.releaseCapture(getElement());
				}
				break;
			case Event.ONMOUSEMOVE:
				if (isCapturing) {
					// Prevent dragging (on other browsers);
					DOM.eventPreventDefault(event);

					final int diffX = event.getClientX() - initialX_Position;
					final int diffY = event.getClientY() - initialY_Position;

					final int newWidth = initial_width + diffX;
					final int newHeight = initial_height + diffY;
					if (enableResizeX && newWidth >= 0)
						resizeTarget.setWidth(newWidth);
					if (enableResizeY && newHeight >= 0)
						resizeTarget.setHeight(newHeight);
				}
				break;
			case Event.ONMOUSEOUT:
				break;
			case Event.ONMOUSEOVER:
				break;
			case Event.ONCLICK:
				// we handle clicks ourselves
				return;
			case Event.ONBLUR:
				if (isFocusing) {
					isFocusing = false;
				}
				break;
			case Event.ONLOSECAPTURE:
				if (isCapturing) {
					isCapturing = false;
				}
				break;
			}

			super.onBrowserEvent(event);

		}
	}

	public IconSetPanel getIconSetPanel() {
		return iconSet;
	}

	public Widget getResizeButton() {
		return resizeButton;
	}

	public void addTrackButtonListener(TrackButtonListener listener) {
		iconSet.addTrackButtonListener(listener);
	}

	public void setLoading(boolean loading) {
		iconSet.setLoading(loading);
	}

	public void setVisible(int buttonSet, boolean visible) {
		iconSet.setVisible(buttonSet, visible);
	}

	public void showAdjustHightButton(boolean show) {
		iconSet.setVisible(TrackFrame.BUTTON_ADJUSTHEIGHT, show);
	}

	public void showCloseButton(boolean show) {
		iconSet.setVisible(TrackFrame.BUTTON_CLOSE, show);
	}

	public void showConfigButton(boolean show) {
		iconSet.setVisible(TrackFrame.BUTTON_CONFIG, show);
	}

	public void showMinimizeButton(boolean show) {
		iconSet.setVisible(TrackFrame.BUTTON_MINIMIZE, show);
	}

	public void showReloadButton(boolean show) {
		iconSet.setVisible(TrackFrame.BUTTON_RELOAD, show);
	}

}
