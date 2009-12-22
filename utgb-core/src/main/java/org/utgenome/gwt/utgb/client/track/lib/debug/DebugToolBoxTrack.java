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
// DebugToolBoxTrack.java
// Since: Jun 14, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.lib.debug;

import org.utgenome.gwt.utgb.client.track.Track;
import org.utgenome.gwt.utgb.client.track.lib.LegacyTrack;
import org.utgenome.gwt.utgb.client.track.lib.LoadAndStoreTrack;
import org.utgenome.gwt.utgb.client.track.lib.OperationTrack;
import org.utgenome.gwt.utgb.client.track.lib.ToolBoxTrack;
import org.utgenome.gwt.utgb.client.track.lib.old.OldUTGBAddTrackTrack;

/**
 * A tool box for debug purpose tracks
 * 
 * @author leo
 * 
 */
public class DebugToolBoxTrack extends ToolBoxTrack {
	public static TrackFactory factory() {
		return new TrackFactory() {
			public Track newInstance() {
				return new DebugToolBoxTrack();
			}
		};
	}

	public DebugToolBoxTrack() {
		super("Debug Toolbox");
	}

	public void setupToolbox() {
		addTrackFactory("Operation", OperationTrack.factory("http://medaka3.gi.k.u-tokyo.ac.jp/~kobayashi/EnsemblPredGene/EnsemblTranscript.xml"));
		addTrackFactory("Browser", BrowserTrack.factory());
		addTrackFactory("Property", PropertyEditTrack.factory());
		addTrackFactory("SQLite", SQLiteTrack.factory());
		addTrackFactory("URL Query", URLQueryArgumentTrack.factory());
		addTrackFactory("Legacy Track", LegacyTrack.factory());
		addTrackFactory("Legacy Track Loader", OldUTGBAddTrackTrack.factory());
		addTrackFactory("Load/Store", LoadAndStoreTrack.factory());
	}

}
