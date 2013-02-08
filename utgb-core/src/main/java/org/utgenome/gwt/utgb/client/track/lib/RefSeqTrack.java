/*--------------------------------------------------------------------------
 *  Copyright 2010 utgenome.org
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
// RefseqTrack.java
// Since: Aug 5, 2010
//
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.lib;

import org.utgenome.gwt.utgb.client.track.Track;
import org.utgenome.gwt.utgb.client.track.TrackConfigChange;
import org.utgenome.gwt.utgb.client.track.TrackFrame;
import org.utgenome.gwt.utgb.client.track.TrackGroup;

/**
 * Reference sequence track
 * 
 * <pre>
 * -track
 *  -class: RefSeqTrack
 *  -property
 *    -path: db/genome/%species-%ref.sqlite
 * </pre>
 * 
 * @author leo
 * 
 */
public class RefSeqTrack extends GenomeTrack {

	public static TrackFactory factory() {
		return new TrackFactory() {
			@Override
			public Track newInstance() {
				return new RefSeqTrack();
			}
		};
	}

	public RefSeqTrack() {
		super("Reference Sequence");
	}

	private static final String CONFIG_PATH = "path";

	@Override
	public void draw() {

		String path = getConfig().getString(CONFIG_PATH, "");
		String trackBaseURL = "utgb-core/Sequence.png?path=" + path + "&%q";
		getConfig().setParameter(GenomeTrack.CONFIG_TRACK_BASE_URL, trackBaseURL);

		super.draw();
	}

	@Override
	public void setUp(TrackFrame trackFrame, TrackGroup group) {
		super.setUp(trackFrame, group);

		getConfig().addConfigString("path", CONFIG_PATH, "");
		getConfig().setParameter(GenomeTrack.CONFIG_TRACK_TYPE, "image");
	}

	@Override
	public void onChangeTrackConfig(TrackConfigChange change) {

		if (change.contains(CONFIG_PATH)) {
			refresh();
		}
	}

}
