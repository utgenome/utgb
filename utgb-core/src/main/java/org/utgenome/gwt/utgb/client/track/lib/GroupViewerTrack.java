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
// GroupViewerTrack.java
// Since: Jun 25, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.lib;

import java.util.Iterator;
import java.util.List;

import org.utgenome.gwt.utgb.client.track.Track;
import org.utgenome.gwt.utgb.client.track.TrackBase;
import org.utgenome.gwt.utgb.client.track.TrackEntry;
import org.utgenome.gwt.utgb.client.track.TrackFrame;
import org.utgenome.gwt.utgb.client.track.TrackGroup;
import org.utgenome.gwt.utgb.client.track.TrackUpdateListener;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;

public class GroupViewerTrack extends TrackBase implements TrackUpdateListener {

	public static TrackFactory factory() {
		return new TrackFactory() {
			public Track newInstance() {
				return new GroupViewerTrack();
			}
		};
	}

	private Tree _tree = new Tree();
	private TreeItem _treeRoot = new TreeItem();

	public GroupViewerTrack() {
		super("Track Group");
		_tree.setStyleName("selector");
	}

	public Widget getWidget() {
		return _tree;
	}

	public void draw() {
		_tree.clear();
		_treeRoot.removeItems();
		_tree.addItem(_treeRoot);

		depthFirstTraversalWithinATrackGroup(_treeRoot, getTrackGroup().getRootTrackGroup());

		_treeRoot.setState(true);
	}

	private void depthFirstTraversalWithinATrackGroup(TreeItem node, TrackGroup group) {
		node.setText(group.getName());

		List<TrackEntry> trackList = group.getTrackEntryList();
		for (Iterator<TrackEntry> it = trackList.iterator(); it.hasNext();) {
			depthFirstTraversal(node, it.next());
		}
		node.setState(true);
	}

	private void depthFirstTraversal(TreeItem node, TrackEntry entryCursor) {
		if (entryCursor == null)
			return;

		if (entryCursor.isTrackGroup()) {
			TreeItem groupNode = new TreeItem();
			depthFirstTraversalWithinATrackGroup(groupNode, (TrackGroup) entryCursor);
			node.addItem(groupNode);
		}
		else {
			Label trackLabel = new Label(entryCursor.getName());
			trackLabel.setStyleName("selector-item");
			node.addItem(trackLabel);
		}
	}

	public void setUp(TrackFrame trackFrame, TrackGroup group) {
		trackFrame.pack();
		group.addTrackUpdateListener(this);
	}

	public void onAddTrackGroup(TrackGroup trackGroup) {
		refresh();
	}

	public void onInsertTrack(Track track) {
		refresh();
	}

	public void onInsertTrack(Track track, int beforeIndex) {
		refresh();
	}

	public void onRemoveTrack(Track track) {
		refresh();
	}

	public void onRemoveTrackGroup(TrackGroup trackGroup) {
		refresh();
	}

	public void onResizeTrack() {
		// do nothing
	}

	public void onResizeTrackWindow(int newWindowSize) {
		// TODO Auto-generated method stub

	}

	public void onDetachedFromTrackGroup(TrackGroup trackGroup) {

	}

}
