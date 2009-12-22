/*--------------------------------------------------------------------------
 *  Copyright 2009 utgenome.org
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
// utgb-core Project
//
// MiniBrowserTrack.java
// Since: May 26, 2009
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.lib;

import org.utgenome.gwt.utgb.client.db.datatype.StringType;
import org.utgenome.gwt.utgb.client.track.Design;
import org.utgenome.gwt.utgb.client.track.Track;
import org.utgenome.gwt.utgb.client.track.TrackBase;
import org.utgenome.gwt.utgb.client.track.TrackConfig;
import org.utgenome.gwt.utgb.client.track.TrackConfigChange;
import org.utgenome.gwt.utgb.client.track.TrackFrame;
import org.utgenome.gwt.utgb.client.track.TrackGroup;
import org.utgenome.gwt.utgb.client.ui.CSS;
import org.utgenome.gwt.utgb.client.util.Properties;

import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class MiniBrowserTrack extends TrackBase {
	private String type = "frame";
	private String trackBaseURL;
	private int leftMargin = 0;
	private FlexTable layoutPanel = new FlexTable();
	private Image trackImage = new Image();
	private Frame frame = new Frame();
	private boolean isWidgetReady = false;

	protected TrackConfig config = new TrackConfig(this);

	public static TrackFactory factory() {
		return new TrackFactory() {
			public Track newInstance() {
				return new MiniBrowserTrack();
			}
		};
	}

	private class ContentFrame extends Frame {
		@Override
		protected void onLoad() {
		}

		@Override
		public void onBrowserEvent(Event event) {

			if (event.getTypeInt() == Event.ONCHANGE) {
				TrackFrame frame = getFrame();
				if (frame != null) {
					frame.loadingDone();
				}
			}
		}
	}

	public MiniBrowserTrack() {
		super("Mini Browser");

		CSS.fullWidth(layoutPanel);
		layoutPanel.setCellPadding(0);
		layoutPanel.setCellSpacing(0);
		CSS.fontSize(layoutPanel, 0);

		CSS.fullWidth(frame);
		CSS.margin(frame, 0);
		CSS.padding(frame, 0);
		DOM.setElementProperty(frame.getElement(), "align", "left");
		DOM.setElementPropertyInt(frame.getElement(), "marginHight", 0);
		DOM.setElementPropertyInt(frame.getElement(), "marginWidth", 0);
		DOM.setElementPropertyInt(frame.getElement(), "frameBorder", 0);

		CSS.hideHorizontalScrollBar(layoutPanel);
		CSS.hideHorizontalScrollBar(frame);
	}

	public Widget getWidget() {
		return layoutPanel;
	}

	@Override
	public void draw() {

		//layoutPanel.getCellFormatter().setWidth(0, 0, (leftMargin > 0 ? leftMargin + "px" : "1px"));

		String trackURL = getTrackURL();
		if (type.equals("frame")) {
			if (!isWidgetReady) {
				layoutPanel.setWidget(0, 0, frame);
				isWidgetReady = true;
			}
			frame.setUrl(trackURL);
		}
		else if (type.equals("image")) {
			// use image 
			if (!isWidgetReady) {
				layoutPanel.setWidget(0, 0, trackImage);
				isWidgetReady = true;
			}
			trackImage.setUrl(getTrackURL());
			getFrame().setNowLoading();
		}
	}

	protected String getTrackURL() {
		return trackBaseURL;
	}

	@Override
	public TrackConfig getConfig() {
		return config;
	}

	@Override
	public void setUp(TrackFrame trackFrame, TrackGroup group) {

		trackImage.addLoadHandler(new LoadHandler() {
			public void onLoad(LoadEvent e) {
				getFrame().onUpdateTrackWidget();
				getFrame().loadingDone();
			}
		});
		trackImage.addErrorHandler(new ErrorHandler() {
			public void onError(ErrorEvent e) {
				trackImage.setUrl(Design.IMAGE_NOT_AVAILABLE);
				getFrame().loadingDone();
			}
		});

		int height = this.getDefaultWindowHeight();
		if (height > 0) {
			frame.setHeight(height + "px");
			layoutPanel.setHeight(height + "px");
		}

		// set up the configuration panel

		config.addConfigParameter("URL", new StringType("URL"), trackBaseURL);
	}

	@Override
	public void onChangeTrackConfig(TrackConfigChange change) {
		if (change.contains("URL")) {
			trackBaseURL = change.getValue("URL");
			draw();
		}
	}

	@Override
	public void saveProperties(Properties saveData) {
		saveData.add("type", type);
		saveData.add("URL", trackBaseURL);
	}

	@Override
	public void restoreProperties(Properties properties) {
		trackBaseURL = properties.get("URL", trackBaseURL);
		type = properties.get("type", type);
	}
}
