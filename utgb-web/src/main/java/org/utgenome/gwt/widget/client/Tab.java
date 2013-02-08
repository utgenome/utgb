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
// Tab.java
// Since: May 2, 2008
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.widget.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Tab widget for the tab panel
 * 
 * @author leo
 * 
 */
public class Tab extends Composite implements MouseOutHandler, MouseOverHandler {

	private FocusPanel focusPanel = new FocusPanel();
	private FlexTable layoutFrame = new FlexTable();
	private Label left = new Label();
	private HorizontalPanel center = new HorizontalPanel();
	private Label right = new Label();
	private Label centerLabel = new Label();

	UTGBDesignFactory designFactory = new UTGBDesignFactory();
	private boolean enableClose = true;
	private Icon closeButton;

	private TabPanel parentTabPanel = null;
	private Widget tabWidget = null;

	public Tab(String label) {
		drawWidget(label);

		centerLabel.addMouseOutHandler(this);
		centerLabel.addMouseOverHandler(this);

		initWidget(focusPanel);
	}

	public void addMouseDownHandler(MouseDownHandler handler) {
		centerLabel.addMouseDownHandler(handler);
	}

	private void drawWidget(String label) {

		layoutFrame.setCellPadding(0);
		layoutFrame.setCellSpacing(0);

		Style.backgroundNoRepeat(left);
		Style.fontSize(left, 0);

		left.setPixelSize(9, 21);

		centerLabel.setText(label);
		center.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);
		center.add(centerLabel);

		Style.fullWidth(center);
		Style.textAlign(center, "center");

		Style.backgroundRepeatX(center);
		Style.fontSize(centerLabel, 13);
		Style.fontFamily(centerLabel, "Arial, Helvetica, Tahoma");
		Style.bold(centerLabel);
		Style.fontColor(centerLabel, "#666666");
		Style.nowrap(centerLabel);
		Style.fullWidth(centerLabel);
		center.setHeight("21px");

		Style.backgroundNoRepeat(right);
		Style.backgroundPosition(right, "top right");
		Style.fontSize(right, 0);

		center.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
		closeButton = designFactory.getTabCloseButton();
		Style.padding(closeButton, Style.LEFT, 3);
		Style.padding(closeButton, Style.TOP, 2);
		center.add(closeButton);

		closeButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent arg0) {
				if (!enableClose)
					return;
				if (parentTabPanel != null) {
					if (tabWidget != null)
						parentTabPanel.remove(tabWidget);
				}
			}

		});

		right.setPixelSize(9, 21);

		layoutFrame.setHeight("21px");
		layoutFrame.setWidth("100px");
		layoutFrame.setWidget(0, 0, left);
		layoutFrame.setWidget(0, 1, center);
		layoutFrame.setWidget(0, 2, right);
		layoutFrame.getRowFormatter().setVerticalAlign(0, VerticalPanel.ALIGN_MIDDLE);
		layoutFrame.getCellFormatter().setWidth(0, 1, "100%");

		setBackground(false);

		focusPanel.setWidget(layoutFrame);
	}

	public void enableCloseButton(boolean enableClose) {
		this.enableClose = enableClose;
		//		
		// if (enableClose)
		// closeButton.setIcon(designFactory.getTabCloseButton());
		// else
		// closeButton.setIconImage(designFactory.getUTGBImageBundle().disabledIcon().createImage());

		closeButton.setVisible(enableClose);

	}

	public void setParenTabPanel(TabPanel tabPanel, Widget tabWidet) {
		this.parentTabPanel = tabPanel;
		this.tabWidget = tabWidet;
	}

	private void setBackground(boolean onMouse) {
		if (onMouse) {
			Style.backgroundImage(left, "theme/default/wframe_lw.gif");
			Style.backgroundImage(center, "theme/default/wframe_cw.gif");
			Style.backgroundImage(right, "theme/default/wframe_rw.gif");
		}
		else {
			Style.backgroundImage(left, "theme/default/wframe_l.gif");
			Style.backgroundImage(center, "theme/default/wframe_c.gif");
			Style.backgroundImage(right, "theme/default/wframe_r.gif");
		}
	}

	private boolean isSelected = false;

	public void setSelect(boolean selected) {
		isSelected = selected;
		setBackground(selected);
	}

	public void onMouseOver(MouseOverEvent arg0) {
		setBackground(true);
	}

	public void onMouseOut(MouseOutEvent arg0) {
		setBackground(isSelected | false);
	}

}
