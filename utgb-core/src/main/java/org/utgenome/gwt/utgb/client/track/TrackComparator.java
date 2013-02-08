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
// TrackComparator.java
// Since: 2007/06/20
//
// $URL$ 
// $Author$ ssksn
//--------------------------------------
package org.utgenome.gwt.utgb.client.track;

import java.util.Comparator;

/**
 * @author ssksn
 * 
 */
public class TrackComparator implements Comparator<TrackEntry> {

	private static final TrackComparator _comparator = new TrackComparator();

	public static TrackComparator getComparator() {
		return _comparator;
	}

	public int compare(TrackEntry arg0, TrackEntry arg1) {
		if (arg0 instanceof TrackGroup) {
			final TrackGroup trackGroup0 = (TrackGroup) arg0;

			if (arg1 instanceof TrackGroup) {
				final TrackGroup trackGroup1 = (TrackGroup) arg1;

				return trackGroup0.compareTo(trackGroup1);
			}
			else if (arg1 instanceof Track) {
				return 1;
			}
		}
		else if (arg0 instanceof Track) {
			final Track track0 = (Track) arg0;

			if (arg1 instanceof TrackGroup) {
				return -1;
			}
			else if (arg1 instanceof Track) {
				final Track track1 = (Track) arg1;

				final TrackInfo trackInfo0 = track0.getTrackInfo();
				final TrackInfo trackInfo1 = track1.getTrackInfo();

				final String trackName0 = trackInfo0.getTrackName();
				final String trackName1 = trackInfo1.getTrackName();

				return trackName0.compareTo(trackName1);
			}
		}

		throw new ClassCastException();
	}

}
