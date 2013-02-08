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
// Window.java
// Since: Jun 27, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.ui;

import java.util.ArrayList;
import java.util.Iterator;

import org.utgenome.gwt.utgb.client.track.Design;
import org.utgenome.gwt.widget.client.Style;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HTMLTable.ColumnFormatter;

public class WindowBox extends PopupPanel implements SourcesWindowEvents {
	public static int BUTTON_CONFIG = 1;
	public static int BUTTON_PACK = 1 << 1;
	public static int BUTTON_MINIMIZE = 1 << 2;
	public static int BUTTON_CLOSE = 1 << 3;
	// listener
	private ArrayList<WindowListener> _listenerList = new ArrayList<WindowListener>();
	// widgets
	private final Widget _content;
	private final WindowImpl _windowImpl;

	public WindowBox(Widget content) {
		super();
		this._content = content;
		this.setStyleName("window");
		_windowImpl = new WindowImpl(this);
		this.setWidget(_windowImpl);
	}

	public Widget getContent() {
		return _content;
	}

	public void setWindowTitle(String title) {
		_windowImpl.getNavigationBar().setWindowTitle(title);
	}

	public void addWindowListener(WindowListener listener) {
		_listenerList.add(listener);
	}

	public void removeWindowListener(WindowListener listener) {
		_listenerList.remove(listener);
	}

	ArrayList<WindowListener> getWindowListenerList() {
		return _listenerList;
	}
}

/**
 * 
 * @author leo
 * 
 */
class WindowImpl extends Composite {
	private final WindowBox _window;
	private final RoundCornerFrame _windowFrame;
	private final WindowNavigationBar _navigationBar;
	private int _currentWindowWidth = -1;
	private int _currentWindowHeight = -1;
	public static int BORDER_THICKNESS = 4;
	private static int SCROLLBAR_THICKNESS = 20;

	public WindowImpl(WindowBox window) {
		this._window = window;
		_navigationBar = new WindowNavigationBar(_window);
		this._windowFrame = new RoundCornerFrame("CCCCFF");
		this._windowFrame.setPixelSize(200, 300);
		// _windowFrame.add(_navigationBar, 0, 0);
		// _windowFrame.add(_window.getContent(), 0, 20);
		initWidget(_windowFrame);
	}

	protected void onLoad() {
		super.onLoad();
		// setWindowSize(_window.getpanel.getOffsetWidth(), panel.getOffsetHeight());
	}

	public WindowNavigationBar getNavigationBar() {
		return _navigationBar;
	}

	public int getCurrentWindowWidth() {
		return _currentWindowWidth;
	}

	public int getCurrentWindowHeight() {
		return _currentWindowHeight;
	}

	public WindowBox getWindow() {
		return _window;
	}

	/*
	 * public void setSize(int width, int height) { setWindowSize(width - BORDER_THICKNESS, height -
	 * _navigationBar.getOffsetHeight() - BORDER_THICKNESS); }
	 */
	public void setWindowSize(int width, int height) {
		if (width <= 0)
			width = 1;
		if (height <= 0)
			height = 1;
		if (_currentWindowWidth != width) {
			_currentWindowWidth = width;
			_navigationBar.setWidth(_currentWindowWidth + "px");
		}
		if (_currentWindowHeight != height) {
			_currentWindowHeight = height;
		}
	}
}

class ResizeBar extends FocusPanel implements MouseDownHandler, MouseOverHandler, MouseOutHandler, MouseMoveHandler, MouseUpHandler {
	public static final int DIRECTION_H = 1;
	public static final int DIRECTION_V = 1 << 1;
	private boolean _resizeH = false;
	private boolean _resizeV = false;
	private final IconImage _iconImage;
	private boolean isDragged = false;
	private int dragStartX;
	private int dragStartY;
	private int initialWindowWidth;
	private int initialWindowHeight;
	private final WindowImpl _windowFrame;

	public ResizeBar(WindowImpl windowFrame, final IconImage iconImage, int directionFlag) {
		super();
		this._windowFrame = windowFrame;
		this._iconImage = iconImage;
		// Style.backgroundImage(this, iconImage.getImageURL());
		// Style.backgroundRepeat(this);
		// Style.border(this, 1, Style.BORDER_SOLID, "#CCFFFF");
		_resizeH = (directionFlag & DIRECTION_H) != 0;
		_resizeV = (directionFlag & DIRECTION_V) != 0;
		if (_resizeH) {
			Style.cursor(this, Style.CURSOR_RESIZE_E);
		}
		if (_resizeV) {
			Style.cursor(this, Style.CURSOR_RESIZE_N);
		}
		if (_resizeH || _resizeV) {
			addMouseDownHandler(this);
			addMouseOverHandler(this);
			addMouseMoveHandler(this);
			addMouseUpHandler(this);
		}
	}

	public void onMouseDown(MouseDownEvent e) {
		isDragged = true;
		dragStartX = e.getX() + this.getAbsoluteLeft();
		dragStartY = e.getY() + this.getAbsoluteTop();
		initialWindowWidth = _windowFrame.getOffsetWidth();
		initialWindowHeight = _windowFrame.getOffsetHeight();
		DOM.setCapture(this.getElement());
	}

	public void onMouseOver(MouseOverEvent e) {
		Style.backgroundImage(this, _iconImage.getMouseOverImageURL());
	}

	public void onMouseOut(MouseOutEvent e) {
		Style.backgroundImage(this, _iconImage.getImageURL());
	}

	public void onMouseMove(MouseMoveEvent e) {
		if (isDragged) {
			int offsetX = this.getAbsoluteLeft() + e.getX() - dragStartX;
			int offsetY = this.getAbsoluteTop() + e.getY() - dragStartY;
			int newWidth = _resizeH ? initialWindowWidth + offsetX : initialWindowWidth;
			int newHeight = _resizeV ? initialWindowHeight + offsetY : initialWindowHeight;
			// newHeight = newHeight -
			// _windowFrame.getNavigationBar().getOffsetHeight();
			// _windowFrame.setWindowSize(newWidth, newHeight);
			// _windowFrame.setSize(newWidth, newHeight);
		}
	}

	public void onMouseUp(MouseUpEvent e) {
		if (isDragged) {
			int newWindowWidth = _windowFrame.getCurrentWindowWidth();
			int newWindowHeight = _windowFrame.getCurrentWindowHeight();
			for (Iterator<WindowListener> it = _windowFrame.getWindow().getWindowListenerList().iterator(); it.hasNext();) {
				WindowListener listener = it.next();
				listener.onResizeWindow(_windowFrame.getWindow(), newWindowWidth);
			}
		}
		isDragged = false;
		DOM.releaseCapture(this.getElement());
	}

}

class WindowNavigationBar extends Composite {
	private int _enabledButttonFlag = WindowBox.BUTTON_CONFIG | WindowBox.BUTTON_PACK | WindowBox.BUTTON_MINIMIZE | WindowBox.BUTTON_CLOSE;
	private Grid _iconGrid;
	private Label _windowLabel = new Label(" ");
	private Icon _configButton = new Icon(Design.getIconImage(Design.ICON_CONFIG));
	private Icon _packButton = new Icon(Design.getIconImage(Design.ICON_PACK));
	private Icon _minimizeButton = new Icon(Design.getIconImage(Design.ICON_HIDE));
	private Icon _closeButton = new Icon(Design.getIconImage(Design.ICON_CLOSE));
	// private HashMap _buttonTable = new HashMap();
	private ArrayList<WindowListener> _buttonListener;
	private WindowBox _window;

	public WindowNavigationBar(WindowBox window) {
		this._window = window;
		this._buttonListener = window.getWindowListenerList();
		init();
	}

	private int numButtonOnTheBar() {
		int count = 0;
		for (int i = 0; i < 4; i++) {
			if ((_enabledButttonFlag & (1 << i)) != 0)
				count++;
		}
		return count;
	}

	private boolean isEnabled(int buttonType) {
		return (_enabledButttonFlag & buttonType) != 0;
	}

	public void setWindowTitle(String title) {
		_windowLabel.setText(title);
	}

	public void init() {
		// prepare button table
		int numButton = numButtonOnTheBar();
		_iconGrid = new Grid(1, numButton + 1);
		_iconGrid.setWidth("100%");
		_iconGrid.getRowFormatter().setVerticalAlign(0, VerticalPanel.ALIGN_MIDDLE);
		// _iconGrid.setHeight("21px");
		Style.borderCollapse(_iconGrid);
		_iconGrid.setCellPadding(0);
		_iconGrid.setCellSpacing(0);
		_iconGrid.setBorderWidth(0);
		_iconGrid.setStyleName("window-navbar");
		// set the label & icon width
		{
			ColumnFormatter formatter = _iconGrid.getColumnFormatter();
			for (int i = 0; i < numButton; i++) {
				formatter.setWidth(i + 1, "14px");
			}
		}
		int x = 0;
		_iconGrid.setWidget(0, x++, _windowLabel);
		if (isEnabled(WindowBox.BUTTON_CONFIG)) {
			_iconGrid.setWidget(0, x++, _configButton);
			_configButton.addClickHandler(new IconButtonListner(WindowBox.BUTTON_CONFIG));
		}
		if (isEnabled(WindowBox.BUTTON_PACK)) {
			_iconGrid.setWidget(0, x++, _packButton);
			_packButton.addClickHandler(new IconButtonListner(WindowBox.BUTTON_PACK));
		}
		if (isEnabled(WindowBox.BUTTON_MINIMIZE)) {
			_iconGrid.setWidget(0, x++, _minimizeButton);
			_minimizeButton.addClickHandler(new IconButtonListner(WindowBox.BUTTON_MINIMIZE));
		}
		if (isEnabled(WindowBox.BUTTON_CLOSE)) {
			_iconGrid.setWidget(0, x++, _closeButton);
			_closeButton.addClickHandler(new IconButtonListner(WindowBox.BUTTON_CLOSE));
		}
		// set CSS style
		_windowLabel.setStyleName("window-title");
		Style.fullBlock(_windowLabel);
		Style.cursor(_windowLabel, Style.CURSOR_MOVE);
		Style.overflowHidden(_windowLabel);
		// drag & drop mover

		new MouseMoveListener(_window).register(_windowLabel);
		// layout
		initWidget(_iconGrid);
	}

	class IconButtonListner implements ClickHandler {
		private int buttonType;

		/**
		 * @param buttonType
		 */
		public IconButtonListner(int buttonType) {
			this.buttonType = buttonType;
		}

		public void onClick(ClickEvent e) {
			for (Iterator<WindowListener> it = _buttonListener.iterator(); it.hasNext();) {
				WindowListener listener = it.next();
				listener.onButtonClick(_window, buttonType);
			}
		}
	}
}
