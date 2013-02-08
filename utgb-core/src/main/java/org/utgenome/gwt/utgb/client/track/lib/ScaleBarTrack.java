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
// ScaleBarTrack.java
// Since: 2010/08/02
//
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.lib;

import org.utgenome.gwt.utgb.client.track.Track;
import org.utgenome.gwt.utgb.client.track.TrackFrame;
import org.utgenome.gwt.utgb.client.track.TrackGroup;

/**
 * Scale bar
 * 
 * @author hatsuda
 * 
 */
public class ScaleBarTrack extends GenomeTrack {

	public static TrackFactory factory() {
		return new TrackFactory() {
			@Override
			public Track newInstance() {
				return new ScaleBarTrack();
			}
		};
	}

	public ScaleBarTrack() {
		super("ScaleBar Track");
	}

	@Override
	public void setUp(TrackFrame trackFrame, TrackGroup group) {
		super.setUp(trackFrame, group);

		String trackBaseURL = "utgb-core/ScaleBar?range=%len&width=%pixelwidth";
		getConfig().setParameter(GenomeTrack.CONFIG_TRACK_BASE_URL, trackBaseURL);
		getConfig().setParameter(GenomeTrack.CONFIG_TRACK_TYPE, "image");
	}

}
