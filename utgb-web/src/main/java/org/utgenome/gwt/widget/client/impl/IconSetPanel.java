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
// IconSetPanel.java
// Since: Apr 30, 2008
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.widget.client.impl;

import java.util.ArrayList;

import org.utgenome.gwt.widget.client.Icon;
import org.utgenome.gwt.widget.client.NowLoadingIcon;
import org.utgenome.gwt.widget.client.Switch;
import org.utgenome.gwt.widget.client.TrackButtonListener;
import org.utgenome.gwt.widget.client.TrackFrame;
import org.utgenome.gwt.widget.client.UTGBDesignFactory;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;

/**
 * Icon set
 * 
 * 
 * @author leo
 * 
 */
public class IconSetPanel extends HorizontalPanel {

	private final TrackFrame parentTrackPanel;

	private int visibleButtonFlag = TrackFrame.BUTTON_ALL;

	// icon
	private NowLoadingIcon loadButton;
	private Icon configButton;
	private Switch adjustHightButton;
	private Switch minimizeButton;
	private Icon closeButton;

	// listener
	private ArrayList<TrackButtonListener> buttonListenerList = new ArrayList<TrackButtonListener>();

	public IconSetPanel(TrackFrame parentTrackPanel) {
		this.parentTrackPanel = parentTrackPanel;

		// layout
		UTGBDesignFactory iconFactory = new UTGBDesignFactory();

		loadButton = iconFactory.getNowLoadingIcon();
		configButton = iconFactory.getConfigButton();
		adjustHightButton = iconFactory.getAdjustHightSwitch();
		minimizeButton = iconFactory.getOpenHideSwith();
		closeButton = iconFactory.getCloseButton();

		loadButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				fireOnClickReloadButton();
			}
		});
		configButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent arg0) {
				fireOnClickConfigButton();
			}
		});
		adjustHightButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent arg0) {
				fireOnClickAdjustHightButton();
			}
		});
		minimizeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent arg0) {
				fireOnClickMinimizeButton();
			}
		});

		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent arg0) {
				fireOnClickCloseButton();
			}
		});

		drawWidget();
	}

	private void drawWidget() {
		this.clear();

		this.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);

		if ((visibleButtonFlag & TrackFrame.BUTTON_RELOAD) != 0)
			this.add(loadButton);
		if ((visibleButtonFlag & TrackFrame.BUTTON_CONFIG) != 0)
			this.add(configButton);
		if ((visibleButtonFlag & TrackFrame.BUTTON_ADJUSTHEIGHT) != 0)
			this.add(adjustHightButton);
		if ((visibleButtonFlag & TrackFrame.BUTTON_MINIMIZE) != 0)
			this.add(minimizeButton);
		if ((visibleButtonFlag & TrackFrame.BUTTON_CLOSE) != 0)
			this.add(closeButton);

	}

	private void fireOnClickReloadButton() {
		for (TrackButtonListener listener : buttonListenerList)
			listener.onClickReloadButton(parentTrackPanel);
	}

	private void fireOnClickConfigButton() {
		for (TrackButtonListener listener : buttonListenerList)
			listener.onClickConfigButton(parentTrackPanel);
	}

	private void fireOnClickAdjustHightButton() {
		for (TrackButtonListener listener : buttonListenerList)
			listener.onClickAdjustHightButton(parentTrackPanel);
	}

	private void fireOnClickMinimizeButton() {
		for (TrackButtonListener listener : buttonListenerList)
			listener.onClickMinimizeButton(parentTrackPanel);
	}

	private void fireOnClickCloseButton() {
		for (TrackButtonListener listener : buttonListenerList)
			listener.onClickCloseButton(parentTrackPanel);
	}

	public void addTrackButtonListener(TrackButtonListener listener) {
		buttonListenerList.add(listener);
	}

	public void setVisible(int buttonType, boolean visible) {
		if (visible)
			visibleButtonFlag |= buttonType;
		else
			visibleButtonFlag &= ~buttonType;

		drawWidget();
	}

	public void setLoading(boolean rotate) {
		loadButton.setLoading(rotate);

	}

}
