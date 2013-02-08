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
// WindowControlTrack.java
// Since: Jun 19, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.lib;

import java.util.ArrayList;
import java.util.Iterator;

import org.utgenome.gwt.utgb.client.track.Track;
import org.utgenome.gwt.utgb.client.track.TrackBase;
import org.utgenome.gwt.utgb.client.track.TrackFrame;
import org.utgenome.gwt.utgb.client.track.TrackGroup;
import org.utgenome.gwt.utgb.client.ui.FormLabel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Track window control buttons
 * 
 * @author leo
 * 
 */
public class WindowControlTrack extends TrackBase {

	public static TrackFactory factory() {
		return new TrackFactory() {
			public Track newInstance() {
				return new WindowControlTrack();
			}
		};
	}

	private HorizontalPanel _panel = new HorizontalPanel();
	private Button _hideAndShowButton = new Button("minimize tracks");
	private Button _packAndUnPackButton = new Button("pack tracks");
	private Button _closeAllButton = new Button("close tracks");
	private TextBox _windowSizeInput = new TextBox();
	private TrackGroup _rootTrackGroup;
	private final Track _self = this;

	public WindowControlTrack() {
		super("Window Controller");

		_hideAndShowButton.addClickHandler(new ClickHandler() {
			private boolean _minimized = false;

			public void onClick(ClickEvent e) {
				_rootTrackGroup.setResizeNotification(false);
				if (!_minimized) {
					for (Track track : getTrackGroup().getAllTrackList()) {
						if (track.equals(_self))
							continue;
						track.getFrame().minimize();
					}
					_hideAndShowButton.setText("open tracks");
				}
				else {
					for (Track track : getTrackGroup().getAllTrackList()) {
						if (track.equals(_self))
							continue;
						track.getFrame().open();
					}
					_hideAndShowButton.setText("minimize tracks");
				}
				_rootTrackGroup.setResizeNotification(true);
				_rootTrackGroup.notifyResize();

				_minimized = !_minimized;
			}
		});

		_packAndUnPackButton.addClickHandler(new ClickHandler() {
			private boolean _packed = false;

			public void onClick(ClickEvent e) {
				_rootTrackGroup.setResizeNotification(false);
				if (!_packed) {
					for (Track track : getTrackGroup().getAllTrackList()) {
						if (track.equals(_self))
							continue;
						track.getFrame().pack();
					}
					_packAndUnPackButton.setText("unpack tracks");
				}
				else {
					for (Track track : getTrackGroup().getAllTrackList()) {
						if (track.equals(_self))
							continue;
						track.getFrame().unpack();
					}
					_packAndUnPackButton.setText("pack tracks");
				}
				_rootTrackGroup.setResizeNotification(true);
				_rootTrackGroup.notifyResize();
				_packed = !_packed;
			}
		});

		_closeAllButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent e) {
				_rootTrackGroup.setResizeNotification(false);

				// create a list of tracks to be closed
				ArrayList<Track> closeTarget = new ArrayList<Track>();
				for (Track track : getTrackGroup().getAllTrackList()) {
					closeTarget.add(track);
				}

				// close tracks
				TrackGroup trackGroup = getTrackGroup();
				for (Iterator<Track> it = closeTarget.iterator(); it.hasNext();) {
					Track track = it.next();
					trackGroup.removeTrack(track);
				}
				_rootTrackGroup.setResizeNotification(true);
				_rootTrackGroup.notifyResize();
			}
		});

		_windowSizeInput.setMaxLength(4);
		_windowSizeInput.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent e) {
				if (e.getCharCode() == KeyCodes.KEY_ENTER) {
					try {
						int windowSize = Integer.parseInt(_windowSizeInput.getText());
						if (windowSize >= 500) {
							getTrackGroup().setTrackWindowWidth(windowSize);
						}
					}
					catch (NumberFormatException ex) {
						GWT.log(_windowSizeInput.getText() + " is not a integer", ex);
					}
				}
			}

		});

		_panel.add(_hideAndShowButton);
		_panel.add(_packAndUnPackButton);
		_panel.add(_closeAllButton);
		_panel.add(new FormLabel("window size (>= 500): "));
		_panel.add(_windowSizeInput);
	}

	public Widget getWidget() {
		return _panel;
	}

	public void setUp(TrackFrame trackFrame, TrackGroup group) {
		trackFrame.pack();
		trackFrame.disablePack();

		_rootTrackGroup = group.getRootTrackGroup();
	}

}
