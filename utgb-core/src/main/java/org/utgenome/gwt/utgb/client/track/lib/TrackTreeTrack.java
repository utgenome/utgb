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
// TrackTreeTrack.java
// Since: 2007/07/20
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.lib;

import java.util.List;

import org.utgenome.gwt.utgb.client.track.Track;
import org.utgenome.gwt.utgb.client.track.TrackBase;
import org.utgenome.gwt.utgb.client.track.TrackFrame;
import org.utgenome.gwt.utgb.client.track.TrackGroup;
import org.utgenome.gwt.utgb.client.track.TrackInfo;
import org.utgenome.gwt.utgb.client.track.TrackUpdateListener;
import org.utgenome.gwt.utgb.client.util.CanonicalProperties;

import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;

public class TrackTreeTrack extends TrackBase implements TrackUpdateListener {

	public static TrackFactory factory() {
		return new TrackFactory() {
			public Track newInstance() {
				return new TrackTreeTrack();
			}
		};
	}

	private TrackGroup rootTrackGroup = null;

	private Tree trackTree = new Tree();

	public TrackTreeTrack() {
		super("Track Tree Track");
	}

	public TrackTreeTrack(String trackName) {
		super(trackName);
	}

	public TrackTreeTrack(TrackInfo trackInfo) {
		super(trackInfo);
	}

	public Widget getWidget() {
		return trackTree;
	}

	public void draw() {
		trackTree.clear();

		if (rootTrackGroup != null) {
			final TreeItem rootItem = getTreeItem(rootTrackGroup);
			trackTree.addItem(rootItem);
		}
	}

	private TreeItem getTreeItem(final TrackGroup trackGroup) {
		final TreeItem item = new TreeItem(trackGroup.getTrackGroupName());

		{
			final List<TrackGroup> trackGroups = trackGroup.getTrackGroupList();
			final int SIZE = trackGroups.size();
			for (int i = 0; i < SIZE; i++) {
				final TrackGroup _group = (TrackGroup) (trackGroups.get(i));
				final TreeItem _item = getTreeItem(_group);

				item.addItem(_item);
			}
		}

		{
			final List<Track> tracks = trackGroup.getTrackList();
			final int SIZE = tracks.size();
			for (int i = 0; i < SIZE; i++) {
				final Track _track = (Track) (tracks.get(i));
				final TreeItem _item = getTreeItem(_track);

				item.addItem(_item);
			}
		}

		return item;
	}

	public void setUp(TrackFrame trackFrame, TrackGroup group) {
		this.rootTrackGroup = group.getRootTrackGroup();
	}

	private TreeItem getTreeItem(final Track track) {
		return new TreeItem(track.getTrackInfo().getTrackName());
	}

	public void onAddTrackGroup(TrackGroup trackGroup) {
		refresh();
	}

	public void onInsertTrack(Track track, int beforeIndex) {
		refresh();
	}

	public void onInsertTrack(Track track) {
		refresh();
	}

	public void onRemoveTrack(Track track) {
		refresh();
	}

	public void onRemoveTrackGroup(TrackGroup trackGroup) {
		refresh();
	}

	public void onResizeTrack() {
	}

	public void saveProperties(CanonicalProperties saveData) {
		if (rootTrackGroup != null)
			saveData.add("rootTrackGroup", rootTrackGroup.getClass().getName());
	}

	public void onResizeTrackWindow(int newWindowSize) {
		// TODO Auto-generated method stub

	}

	public void onDetachedFromTrackGroup(TrackGroup trackGroup) {

	}

}
