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
// ToolBoxTrack.java
// Since: Jun 13, 2007
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
import org.utgenome.gwt.utgb.client.track.lib.debug.DebugToolBoxTrack;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * {@link ToolBoxTrack} has a list of {@link Track}s. When you click one of the elements, the corresponding
 * {@link Track} will be inserted to the {@link TrackGroup} in which the {@link ToolBoxTrack} belongs.
 * 
 * @author leo
 * 
 */
public class ToolBoxTrack extends TrackBase {
	public static TrackFactory factory() {
		return new TrackFactory() {
			public Track newInstance() {
				return new ToolBoxTrack();
			}
		};
	}

	private FlowPanel _panel = new FlowPanel();
	private ArrayList<TrackLink> trackTable = new ArrayList<TrackLink>();
	private Track _self = this;

	class TrackLink extends Anchor implements ClickHandler {
		TrackFactory trackFactory;

		public TrackLink(String label, TrackFactory trackFactory) {
			super(label, label);
			this.trackFactory = trackFactory;
			setStyleName("selector-item");
			addClickHandler(this);
		}

		public void onClick(ClickEvent e) {
			int trackIndex = getTrackGroup().getTrackIndex(_self);
			Track newTrackInstance = trackFactory.newInstance();
			getTrackGroup().insertTrack(newTrackInstance, trackIndex + 1);
		}
	}

	public ToolBoxTrack() {
		super("Tool Box");
		init();
	}

	public ToolBoxTrack(String trackName) {
		super(trackName);
		init();
	}

	private void init() {
		_panel.setWidth("600px");
		_panel.setStyleName("selector");
		this.setupToolbox();
	}

	/**
	 * override this method to implement your own tool box
	 */
	public void setupToolbox() {

		addTrackFactory("Scroll Button", ScrollButtonTrack.factory());
		addTrackFactory("Window Controller", WindowControlTrack.factory());
		addTrackFactory("Sequence Ruler", SequenceRulerTrack.factory());
		addTrackFactory("Track Ruler", RulerTrack.factory());
		addTrackFactory("Species", SpeciesSelectTrack.factory());
		addTrackFactory("Revision", RevisionSelectTrack.factory());
		addTrackFactory("Target", TargetSelectTrack.factory());
		addTrackFactory("Annotation", AnnotationTrack.factory());
		addTrackFactory("Debug Toolbox", DebugToolBoxTrack.factory());

	}

	public void addTrackFactory(String label, TrackFactory trackFactory) {
		trackTable.add(new TrackLink(label, trackFactory));
	}

	public int getDefaultWindowHeight() {
		return 15;
	}

	public Widget getWidget() {
		return _panel;
	}

	public void draw() {
		_panel.clear();
		for (Iterator<TrackLink> it = trackTable.iterator(); it.hasNext();) {
			TrackLink tl = it.next();
			_panel.add(tl);
		}
	}

	public void setUp(TrackFrame trackFrame, TrackGroup group) {

	}

}
