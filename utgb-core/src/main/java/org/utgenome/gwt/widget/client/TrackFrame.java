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
// TrackPanel.java
// Since: Apr 24, 2008
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.widget.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * A common interface for the UTGB track panel widget
 * 
 * @author leo
 * 
 */
/**
 * @author leo
 * 
 */
public abstract class TrackFrame extends Composite {

	public static final int BUTTON_RELOAD = 1;
	public static final int BUTTON_CONFIG = 1 << 1;
	public static final int BUTTON_ADJUSTHEIGHT = 1 << 2;
	public static final int BUTTON_MINIMIZE = 1 << 3;
	public static final int BUTTON_CLOSE = 1 << 4;
	public static final int BUTTON_ALL = BUTTON_RELOAD | BUTTON_CONFIG | BUTTON_ADJUSTHEIGHT | BUTTON_MINIMIZE | BUTTON_CLOSE;

	/**
	 * Set the visibility of the specified button
	 * 
	 * @param buttonSet
	 *            set of buttons (OR value of BUTTON_RELOAD, BUTTON_CONFIG, BUTTON_ADJUSTHEIGHT, BUTTON_MINIMIZE,
	 *            BUTTON_CLOSE)
	 * @param visible
	 */
	public abstract void setVisible(int buttonSet, boolean visible);

	public abstract void showCloseButton(boolean show);

	public abstract void showConfigButton(boolean show);

	public abstract void showMinimizeButton(boolean show);

	public abstract void showAdjustHightButton(boolean show);

	public abstract void showReloadButton(boolean show);

	/**
	 * Set the track title
	 * 
	 * @param title
	 */
	public abstract void setTrackTitle(String title);

	/**
	 * Gets the track title
	 * 
	 * @return
	 */
	public abstract String getTrackTitle();

	// button listner
	public abstract void addTrackButtonListener(TrackButtonListener listener);

	/**
	 * Start or stop the rotation of the loading button.
	 * 
	 * @param loading
	 *            true: start rotate, false: stop rotate
	 */
	public abstract void setLoading(boolean loading);

	/**
	 * Set the track content widget
	 * 
	 * @param w
	 */
	public abstract void setTrackContent(Widget w);

	/**
	 * Get the draggable part of this widget
	 * 
	 * @return
	 */
	public abstract Widget getDraggableWidget();

	/**
	 * Enable resize of the track width
	 * 
	 * @param enable
	 */
	public abstract void enableResizeWidth(boolean enable);

	/**
	 * Enable resize of the track height
	 * 
	 * @param enable
	 */
	public abstract void enableResizeHeight(boolean enable);

	
	/**
	 * Set the track frame width. Use this method instead of {@link Widget#setWidth(String)}.
	 * @param pixelWidth
	 */
	public abstract void setWidth(int pixelWidth);
	
	/**
	 * @param pixelHeight
	 */
	public abstract void setHeight(int pixelHeight);
	
	
	public void setWidth(String width)
	{
		throw new UnsupportedOperationException("setWidth(String) cannot be used. Instead, use setWidth(int)");		
	}
	
	public void setHeight(String height)
	{
		throw new UnsupportedOperationException("setHeight(String) cannot be used. Instead, use setHeight(int)");
	}
	
	public void setSize(int pixelWidth, int pixelHeight)
	{
		setWidth(pixelWidth);
		setHeight(pixelHeight);
	}
	
	public void setSize(String width, String height)
	{
		throw new UnsupportedOperationException("setSize(String, String) cannot be used. Instead, use setSize(int, int)");
	}
	
	
	
}
