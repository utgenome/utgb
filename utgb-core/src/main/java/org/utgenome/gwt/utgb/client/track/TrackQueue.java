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
// TrackQueue.java
// Since: Jun 6, 2007
//
// $URL$ 
// $Author$s
//--------------------------------------
package org.utgenome.gwt.utgb.client.track;

import java.util.Iterator;

import org.utgenome.gwt.utgb.client.UTGBEntryPointBase;
import org.utgenome.gwt.utgb.client.ui.DraggableTable;
import org.utgenome.gwt.utgb.client.ui.WindowBox;
import org.utgenome.gwt.utgb.client.ui.WindowListener;

import com.google.gwt.user.client.ui.Composite;

/**
 * {@link TrackQueue} is a widget that draws tracks
 * 
 * @author leo
 * 
 */
public class TrackQueue extends Composite implements TrackUpdateListener, TrackLayoutManager, WindowListener {
	private TrackGroup _trackGroup;
	private final DraggableTable _trackQueue = new DraggableTable();

	public TrackQueue(TrackGroup trackGroup) {
		this._trackGroup = trackGroup;
		init();
		initWidget(_trackQueue);
	}

	private void init() {
		_trackQueue.setStyleName("track-queue");
		initTrackGroup(_trackGroup);

		//		Window.addResizeHandler(new ResizeHandler() {
		//
		//			public void onResize(ResizeEvent e) {
		//				int width = e.getWidth();
		//				int panelWidth = width - (TrackFrame.INFOPANEL_WIDTH + TrackFrame.SCROLLBAR_WIDTH);
		//				if (panelWidth > 0) {
		//					_trackGroup.setTrackWindowWidth(panelWidth);
		//				}
		//
		//			}
		//		});

	}

	private void initTrackGroup(TrackGroup trackGroup) {
		trackGroup.setTrackLayoutManager(this);
		trackGroup.addTrackUpdateListener(this);
	}

	private void sync(TrackGroup group) {
		// resync TrackQueue and TrackGroup
		for (Iterator<Track> it = group.getTrackList().iterator(); it.hasNext();) {
			Track track = it.next();
			onInsertTrack(track);
		}
		for (Iterator<TrackGroup> it = group.getTrackGroupList().iterator(); it.hasNext();) {
			TrackGroup trackGroup = it.next();
			sync(trackGroup);
		}
	}

	public WindowBox createWindow() {
		WindowBox win = new WindowBox(this);
		win.addWindowListener(this);
		return win;
	}

	public void adjustHeight() {
		int height = _trackGroup.getHeight();
		_trackQueue.setHeight(height + "px");
	}

	public int getTrackIndex(Track track) {
		return _trackQueue.getIndex(track.getFrame());
	}

	public void onRemoveTrack(Track track) {
		_trackQueue.remove(track.getFrame());
		/*
		 * // when track queue is empty if(_trackQueue.empty()) { _trackGroup.addTrack(new ToolBoxTrack()); }
		 */
	}

	public void onInsertTrack(Track track) {
		onInsertTrack(track, -1);
	}

	public void onInsertTrack(Track track, int beforeIndex) {
		TrackFrame frame = new TrackFrame(track, _trackGroup.getTrackWindow().getWindowWidth(), track.getDefaultWindowHeight());
		if (beforeIndex < 0) {
			// insert to the tail of the queue
			_trackQueue.add(frame, frame.getFrameBar());
		}
		else
			_trackQueue.insert(frame, frame.getFrameBar(), beforeIndex);
		// draw the track
		track.refresh();
	}

	public void onAddTrackGroup(TrackGroup trackGroup) {
		initTrackGroup(trackGroup);
		for (Track track : trackGroup.getTrackList()) {
			onInsertTrack(track);
		}
		for (TrackGroup group : trackGroup.getTrackGroupList()) {
			onAddTrackGroup(group);
		}
	}

	public void onRemoveTrackGroup(TrackGroup trackGroup) {
		for (Track track : trackGroup.getTrackList()) {
			onRemoveTrack(track);
		}
		for (TrackGroup group : trackGroup.getTrackGroupList()) {
			onRemoveTrackGroup(group);
		}
	}

	public void onResizeTrack() {
		adjustHeight();
	}

	protected void onLoad() {
		for (Track track : _trackGroup.getAllTrackList()) {
			track.getFrame().adjustFrameHeight();
		}
		adjustHeight();
		// remove loading message
		UTGBEntryPointBase.hideLoadingMessage();
	}

	public void onButtonClick(WindowBox window, int buttonType) {
		// do nothing
	}

	public void onResizeWindow(WindowBox window, int newWindowSize) {
		int trackWindowWidth = newWindowSize - TrackFrame.INFOPANEL_WIDTH;
		if (trackWindowWidth <= 0)
			trackWindowWidth = 1;
		TrackWindow trackWindow = _trackGroup.getTrackWindow();
		TrackGroupPropertyWriter writer = _trackGroup.getPropertyWriter();
		writer.setTrackWindowSize(trackWindowWidth);
	}

	public void onResizeTrackWindow(int newWindowSize) {
		_trackQueue.setWidth((TrackFrame.INFOPANEL_WIDTH + newWindowSize + TrackFrame.SCROLLBAR_WIDTH) + "px");
	}
}
