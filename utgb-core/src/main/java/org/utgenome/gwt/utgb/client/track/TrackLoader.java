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
// TrackLoader.java
// Since: 2007/07/18
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track;

import org.utgenome.gwt.utgb.client.UTGBClientErrorCode;
import org.utgenome.gwt.utgb.client.UTGBClientException;
import org.utgenome.gwt.utgb.client.UTGBEntryPointBase;
import org.utgenome.gwt.utgb.client.track.HasFactory.TrackGroupFactory;
import org.utgenome.gwt.utgb.client.track.Track.TrackFactory;
import org.utgenome.gwt.utgb.client.track.impl.TrackWindowImpl;
import org.utgenome.gwt.utgb.client.util.Properties;
import org.utgenome.gwt.utgb.client.view.TrackView;
import org.utgenome.gwt.utgb.client.view.TrackView.Coordinate;

public class TrackLoader {

	private TrackLoader() {
	}

	/**
	 * Create a track group from a given {@link TrackView}
	 * 
	 * @param view
	 * @return
	 * @throws UTGBClientException
	 */
	public static TrackGroup createTrackGroup(TrackView view) throws UTGBClientException {

		TrackView.TrackGroup g = view.trackGroup;
		if (g == null)
			g = new TrackView.TrackGroup();
		String groupClass = view.trackGroup.class_;
		if (groupClass == null)
			groupClass = "org.utgenome.gwt.utgb.client.track.TrackGroup";

		// instantiate a track group
		TrackGroupFactory trackGroupFactory = TrackFactoryHolder.getTrackGroupFactory(groupClass);
		final TrackGroup group = trackGroupFactory.newInstance();

		// set track group properties

		Properties p = new Properties();
		p.putAll(g.property);
		p.put(UTGBProperty.SPECIES, g.coordinate.species);
		p.put(UTGBProperty.REVISION, g.coordinate.ref);
		p.put(UTGBProperty.TARGET, g.coordinate.chr);

		group.getPropertyWriter().setProperty(p);

		// set track window (coordinate)
		Coordinate c = g.coordinate;
		if (c.pixelWidth < 0)
			c.pixelWidth = UTGBEntryPointBase.computeTrackWidth();
		group.setTrackWindow(new TrackWindowImpl(c.pixelWidth, c.start, c.end));

		// instantiate tracks in the track group 
		for (TrackView.Track t : view.track) {
			String className = t.class_;
			TrackFactory trackFactory = TrackFactoryHolder.getTrackFactory(className);
			if (trackFactory == null)
				throw new UTGBClientException(UTGBClientErrorCode.UNKNOWN_TRACK, "unknown track class: " + className);

			Track track = trackFactory.newInstance();
			track.loadView(t);
			group.addTrack(track);
		}

		return group;

	}

}
