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
// TrackFrame.java
// Since: Jun 6, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track;

import org.utgenome.gwt.utgb.client.ui.Icon;
import org.utgenome.gwt.utgb.client.ui.IconImage;
import org.utgenome.gwt.widget.client.Style;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasAllMouseHandlers;
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
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * {@link TrackFrame} wraps your {@link Track}, then decorates the track with window manipulation buttons, a track
 * label, drag & drop facility, etc.
 * 
 * @author leo
 * 
 */
public class TrackFrame extends SimplePanel {
	public static final int SCROLLBAR_WIDTH = 25;
	public static final int INFOPANEL_WIDTH = 200;
	private static IconImage _dragBarIcon = Design.getIconImage(Design.TRACK_BORDER_V);
	private static IconImage _resizeBarIcon = Design.getIconImage(Design.TRACK_BORDER_H);

	class DragBar extends Image implements MouseOverHandler, MouseOutHandler {
		public DragBar() {
			super(_dragBarIcon.getImageURL());
			setStyleName("drag-bar");
			setSize("4px", "100%");
			setTitle("drag & drop to change track order");
		}

		public void register(HasAllMouseHandlers w) {
			w.addMouseOverHandler(this);
			w.addMouseOutHandler(this);
		}

		public void onMouseOver(MouseOverEvent e) {
			setUrl(_dragBarIcon.getMouseOverImageURL());
		}

		public void onMouseOut(MouseOutEvent e) {
			setUrl(_dragBarIcon.getImageURL());
		}

		@Override
		protected void onAttach() {
			super.onAttach();
			setUrl(_dragBarIcon.getImageURL());
		}
	}

	class ResizeBar extends Image implements MouseMoveHandler, MouseOverHandler, MouseOutHandler, MouseDownHandler, MouseUpHandler {
		private TrackFrame _targetFrame;
		private boolean isMouseDown = false;
		private int initialY_Position = 0;
		private int initial_height = 0;
		private boolean enabled = true;

		public ResizeBar(final TrackFrame targetWidget) {
			super(_resizeBarIcon.getImageURL());
			this._targetFrame = targetWidget;
			setStyleName("drag-bar");
			setSize(INFOPANEL_WIDTH + "px", "2px");
			enableResize();
			setTitle("Drag this bar to resize tracks");

			register(this);
		}

		private void register(HasAllMouseHandlers w) {
			w.addMouseMoveHandler(this);

			w.addMouseUpHandler(this);
			w.addMouseDownHandler(this);

			w.addMouseOverHandler(this);
			w.addMouseOutHandler(this);
		}

		public void onMouseMove(MouseMoveEvent e) {
			if (!enabled)
				return;

			if (isMouseDown) {
				final int diff = (e.getScreenY()) - initialY_Position;
				final int newSize = initial_height + diff;
				_targetFrame.resize(newSize);
				_frameState.setPreviousFrameHeight(newSize);
			}
		}

		public void onMouseUp(MouseUpEvent e) {
			if (!enabled)
				return;

			if (isMouseDown) {
				isMouseDown = false;
				unpack();
				_frameState.setMinimized(false);
				hideButton.update();
			}
			Event.releaseCapture(e.getRelativeElement());
		}

		public void onMouseDown(MouseDownEvent e) {
			if (!enabled)
				return;

			isMouseDown = true;
			initialY_Position = e.getScreenY();
			initial_height = _targetFrame.getOffsetHeight();
			Event.setCapture(e.getRelativeElement());
		}

		public void onMouseOver(MouseOverEvent e) {
			if (!enabled)
				return;

			setUrl(_resizeBarIcon.getMouseOverImageURL());
		}

		public void onMouseOut(MouseOutEvent e) {
			if (!enabled)
				return;

			setUrl(_resizeBarIcon.getImageURL());
		}

		public void disableResize() {
			enabled = false;
			DOM.setStyleAttribute(this.getElement(), "cursor", "default");
		}

		public void enableResize() {
			enabled = true;
			DOM.setStyleAttribute(this.getElement(), "cursor", "n-resize");
		}
	}

	class WindowButton extends Icon {
		IconImage _icon;

		public WindowButton(IconImage icon) {
			super(icon);
			// setStyleName("track-icon");
		}
	}

	class ConfigButton extends WindowButton implements MouseDownHandler {
		public ConfigButton() {
			super(Design.getIconImage(Design.ICON_CONFIG));
			setTitle("config");

			addMouseDownHandler(this);
		}

		public void onMouseDown(MouseDownEvent e) {
			TrackConfig configPopup = _track.getConfig();
			assert (configPopup != null);
			configPopup.setPopupPosition(this.getAbsoluteLeft() + 2, this.getAbsoluteTop() + 13);
			configPopup.show();
		}
	}

	class HideButton extends WindowButton implements ClickHandler {
		/**
		 * @param imageURL
		 * @param mouseOverImageURL
		 */
		public HideButton() {
			super(Design.getIconImage(Design.ICON_HIDE));
			setTitle("minimize");
			addClickHandler(this);
		}

		public void onClick(ClickEvent e) {
			switchMinimization();
			update();
		}

		public void update() {
			if (_frameState.isMinimized()) {
				setIcon(Design.getIconImage(Design.ICON_SHOW));
				setTitle("restore");
			}
			else {
				setIcon(Design.getIconImage(Design.ICON_HIDE));
				setTitle("minimize");
			}
		}
	}

	class PackButton extends WindowButton implements ClickHandler {
		public PackButton() {
			super(Design.getIconImage(Design.ICON_PACK));
			setTitle("adjust height");
			addClickHandler(this);
		}

		public void onClick(ClickEvent e) {
			switchPackUnpack();
			update();
		}

		public void update() {
			// setTitle(_frameState.isPacked() ? "free height" : "automatically adjust height");
			updateIcon();
		}

		public void updateIcon() {
			if (_trackWidgetFrame.getOffsetHeight() <= _layoutPanel.getOffsetHeight())
				setIcon(Design.getIconImage(Design.ICON_PACK));
			else
				setIcon(Design.getIconImage(Design.ICON_UNPACK));
		}
	}

	class CloseButton extends WindowButton implements ClickHandler {
		TrackFrame _frame;

		public CloseButton(TrackFrame frame) {
			super(Design.getIconImage(Design.ICON_CLOSE));
			setTitle("close");
			this._frame = frame;
			addClickHandler(this);
		}

		public void onClick(ClickEvent e) {
			getTrack().getTrackGroup().removeTrack(_track);
		}
	}

	private final Track _track;
	private final HorizontalPanel _layoutPanel = new HorizontalPanel();
	private final DragBar _dragBar = new DragBar();
	private final ResizeBar _resizeBar = new ResizeBar(this);
	private final TrackInfoPanel _infoPanel;
	private final ScrollPanel _scrollPanel = new ScrollPanel();
	private final DockPanel _trackWidgetFrame = new DockPanel();
	private final Label _messageLabel = new Label();
	private final ConfigButton configButton = new ConfigButton();
	private final HideButton hideButton = new HideButton();
	private final PackButton packButton = new PackButton();
	private final CloseButton closeButton = new CloseButton(this);
	private TrackFrameState _frameState = new TrackFrameState();

	/**
	 * <pre>
	 *       150px
	 * -------------------
	 * | |         |icon |
	 * | |               |
	 * | | label         |
	 * | |               |
	 * | |---------------|
	 * | | resize bar    |
	 * -------------------
	 * </pre>
	 * 
	 * @author leo
	 * 
	 */
	class TrackInfoPanel extends Composite implements TrackInfoChangeListener {
		private final AbsolutePanel basePanel = new AbsolutePanel();
		private final HorizontalPanel _labelFrame = new HorizontalPanel();
		private final VerticalPanel frameWithResizeBar = new VerticalPanel();
		private final Image _loadingIcon = new Image(Design.IMAGE_NOW_LOADING);
		private final HTML _trackLabel = new HTML();
		private final int DRAGBAR_WIDTH = 4;
		private final int RESIZE_BAR_HEIGHT = 2;
		private boolean _disablePackButton = false;
		private boolean _disableConfigButton = false;
		private boolean _disableCloseButton = false;
		private boolean _disableHideButton = false;
		private boolean _nowLoading = false;

		public TrackInfoPanel(TrackFrame trackFrame, int height) {
			String h = height + "px";
			basePanel.setStyleName("track-info");
			basePanel.setSize(INFOPANEL_WIDTH + "px", h);
			// drag bars
			basePanel.add(_dragBar, 0, 0);
			_dragBar.register(_trackLabel);
			// track label

			_trackLabel.setStyleName("track-label");
			_trackLabel.setWidth((INFOPANEL_WIDTH - 20) + "px");
			Style.trimOverflowedText(_trackLabel);

			updateTrackLabel();
			_labelFrame.setSize("100%", "100%");
			_labelFrame.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
			_labelFrame.setHorizontalAlignment(HorizontalPanel.ALIGN_LEFT);
			_labelFrame.add(_trackLabel);

			frameWithResizeBar.setSize((INFOPANEL_WIDTH - DRAGBAR_WIDTH) + "px", "100%");
			frameWithResizeBar.setVerticalAlignment(VerticalPanel.ALIGN_BOTTOM);
			frameWithResizeBar.add(_labelFrame);
			frameWithResizeBar.add(_resizeBar);
			basePanel.add(frameWithResizeBar, DRAGBAR_WIDTH, 0);
			// icon
			drawIcon();
			// listen to the track information change
			getTrack().getTrackInfo().addChangeListener(this);
			initWidget(basePanel);
		}

		void disablePackButton() {
			_disablePackButton = true;
			basePanel.remove(packButton);
			drawIcon();
		}

		void disableConfigButton() {
			_disableConfigButton = true;
			basePanel.remove(configButton);
			drawIcon();
		}

		void disableCloseButton() {
			_disableCloseButton = true;
			basePanel.remove(closeButton);
			drawIcon();
		}

		void disableHideButton() {
			_disableHideButton = true;
			basePanel.remove(hideButton);
			drawIcon();
		}

		static final int ICON_WIDTH = 15 + 1;

		void drawIcon() {
			final int yOffset = 1;
			int xPos = INFOPANEL_WIDTH;
			if (!_disableCloseButton) {
				xPos -= ICON_WIDTH;
				basePanel.add(closeButton, xPos, yOffset);
			}
			if (!_disableHideButton) {
				xPos -= ICON_WIDTH;
				basePanel.add(hideButton, xPos, yOffset);
			}
			if (!_disablePackButton) {
				xPos -= ICON_WIDTH;
				basePanel.add(packButton, xPos, yOffset);
			}
			if (_track.getConfig() != null && !_disableConfigButton) {
				xPos -= ICON_WIDTH;
				basePanel.add(configButton, xPos, yOffset);
			}
		}

		public void nowLoading(boolean nowLoading) {
			_nowLoading = nowLoading;
			if (_nowLoading) {
				int xPos = INFOPANEL_WIDTH;
				int numIcon = 0;
				if (!_disableCloseButton)
					numIcon++;
				if (!_disableHideButton)
					numIcon++;
				if (!_disablePackButton)
					numIcon++;
				if (_track.getConfig() != null && !_disableConfigButton)
					numIcon++;
				xPos -= ICON_WIDTH * (numIcon + 1);
				basePanel.add(_loadingIcon, xPos, 1);
			}
			else {
				basePanel.remove(_loadingIcon);
			}
		}

		void updateTrackLabel() {
			TrackInfo info = getTrack().getTrackInfo();
			_trackLabel.setText(info.getTrackName());
			_trackLabel.setTitle(info.getTrackName() + ": " + info.getDescription());

			String linkURL = info.getLinkURL();
			if (linkURL.length() > 0) {
				_trackLabel.setHTML("<a href=\"" + linkURL + "\" target=\"_blank\">" + info.getTrackName() + "</a>");
			}
		}

		public void onChange(TrackInfo info) {
			updateTrackLabel();
		}
	}

	public TrackFrame(Track track, int width, int height) {
		this._track = track;
		_frameState.setMinFrameHeight(track.getMinimumWindowHeight());
		_frameState.setPreviousFrameHeight(height);
		_infoPanel = new TrackInfoPanel(this, height);
		// _layoutPanel.setStyleName("track");
		// DOM.setStyleAttribute(_layoutPanel.getElement(), "overflow-x", "hidden");
		// CSS.hideHorizontalScrollBar(_layoutPanel);
		// layout the track components
		_layoutPanel.add(_infoPanel);
		// CSS.hideHorizontalScrollBar(_scrollPanel);
		// DOM.setStyleAttribute(_scrollPanel.getElement(), "overflow-x", "hidden");
		_trackWidgetFrame.setWidth("100%");
		_trackWidgetFrame.setStyleName("track");
		_trackWidgetFrame.add(_track.getWidget(), DockPanel.CENTER);
		_messageLabel.setStyleName("trackframe-message");
		_messageLabel.setWidth("100%");
		_messageLabel.setHorizontalAlignment(Label.ALIGN_CENTER);
		_scrollPanel.setWidget(_trackWidgetFrame);
		_scrollPanel.setSize((width + SCROLLBAR_WIDTH) + "px", height + "px");
		_layoutPanel.add(_scrollPanel);
		this.add(_layoutPanel);
		setFrameHeight(height);
		_track.setFrame(this);
	}

	public TrackFrame(Track track, int width) {
		this(track, width, track.getDefaultWindowHeight());
	}

	public void setFrameHeight(int height) {
		String h = _frameState.resizeFrameHeight(height) + "px";
		_infoPanel.setHeight(h);
		_scrollPanel.setHeight(h);
		_layoutPanel.setHeight(h);
	}

	public void resize(int height) {
		setFrameHeight(height);
		packButton.updateIcon();
		_track.getTrackGroup().notifyResize();
	}

	public void adjustFrameHeight() {
		if (_frameState.isPacked()) {
			// int newHeight = _track.getWidget().getOffsetHeight();
			int newHeight = _trackWidgetFrame.getOffsetHeight();
			setFrameHeight(newHeight);
		}
		resizeTrackAreaWidth(_track.getTrackGroup().getTrackWindow().getWindowWidth());
	}

	public void onUpdateTrackWidget() {
		adjustFrameHeight();
		packButton.updateIcon();
		_track.getTrackGroup().notifyResize();
	}

	public void resizeTrackAreaWidth(int newWidth) {
		_scrollPanel.setWidth((newWidth + SCROLLBAR_WIDTH) + "px");
	}

	public void open() {
		if (_frameState.isMinimized())
			switchMinimization();
	}

	public void minimize() {
		if (!_frameState.isMinimized()) {
			switchMinimization();
		}
	}

	private int appropriateWidgetHeight(boolean toMinimize, boolean toPack) {
		if (toMinimize)
			// return minimumFrameHeight;
			return TrackFrameState.DEFAULT_MIN_TRACKFRAME_HEIGHT;
		else if (toPack) {
			if (_frameState.isMinimized())
				return _frameState.getPreviousFrameHeight();
			else
				// return _track.getWidget().getOffsetHeight();
				return _trackWidgetFrame.getOffsetHeight();
		}
		else
			return _frameState.getPreviousFrameHeight();
	}

	public void switchMinimization() {
		int height = appropriateWidgetHeight(!_frameState.isMinimized(), _frameState.isPacked());
		if (_frameState.isMinimized()) {
			// _layoutPanel.add(_scrollPanel);
		}
		else {
			_frameState.setPreviousFrameHeight(this.getOffsetHeight());
			// _layoutPanel.remove(_scrollPanel); // hide the track
		}
		_frameState.setMinimized(!_frameState.isMinimized());
		resize(height);
		hideButton.update();
	}

	public void switchPackUnpack() {
		open();
		int height = appropriateWidgetHeight(_frameState.isMinimized(), !_frameState.isPacked());
		resize(height);
		_frameState.setPacked(!_frameState.isPacked());
		packButton.update();
	}

	/**
	 * Pack (fullly display: no scroll bar) the track content
	 */
	public void pack() {
		if (!_frameState.isPacked())
			switchPackUnpack();
	}

	/**
	 * Use scroll bar to display the track content
	 */
	public void unpack() {
		if (_frameState.isPacked())
			switchPackUnpack();
	}

	public void setPacked(boolean packed) {
		if (packed)
			pack();
		else
			unpack();
	}

	public void disableConfig() {
		_infoPanel.disableConfigButton();
	}

	public boolean isPacked() {
		return _frameState.isPacked();
	}

	public void disablePack() {
		_infoPanel.disablePackButton();
	}

	public void disableResize() {
		_resizeBar.disableResize();
	}

	public void enableResize() {
		_resizeBar.enableResize();
	}

	public void disableClose() {
		_infoPanel.disableCloseButton();
	}

	public void disableHide() {
		_infoPanel.disableHideButton();
	}

	public Track getTrack() {
		return _track;
	}

	public Label getFrameBar() {
		return _infoPanel._trackLabel;
	}

	public Label getMessageLabel() {
		return _messageLabel;
	}

	public void setVisibleMessageLabel(final boolean visible) {
		if (visible) {
			if (_trackWidgetFrame.getWidgetIndex(_messageLabel) == -1) {
				_trackWidgetFrame.add(_messageLabel, DockPanel.NORTH);
			}
			_messageLabel.setText("");
		}
		else {
			if (_trackWidgetFrame.getWidgetIndex(_messageLabel) != -1) {
				_trackWidgetFrame.remove(_messageLabel);
			}
		}
	}

	public void writeMessage(String message) {
		if (_trackWidgetFrame.getWidgetIndex(_messageLabel) == -1) {
			_trackWidgetFrame.add(_messageLabel, DockPanel.NORTH);
		}
		_messageLabel.setText(message);
	}

	public void eraseMessage(boolean eraseLabel) {
		if (_trackWidgetFrame.getWidgetIndex(_messageLabel) != -1) {
			if (eraseLabel)
				_trackWidgetFrame.remove(_messageLabel);
			else
				_messageLabel.setText("");
		}
	}

	public void setFrameState(TrackFrameState frameState) {
		_frameState = frameState;
		packButton.update();
		hideButton.update();
		// setHeight(_frameState.getPreviousFrameHeight() + "px");
	}

	public void setNowLoading() {
		_infoPanel.nowLoading(true);
	}

	public void loadingDone() {
		_infoPanel.nowLoading(false);
	}
}
