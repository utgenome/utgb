/*--------------------------------------------------------------------------
 *  Copyright 2009 utgenome.org
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
// OldViewXML.java
// Since: 2010/04/22
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.view;

import java.util.ArrayList;
import java.util.List;

/**
 * For reading view.xml files used utgb 1.3.x.
 * 
 * @author leo
 * 
 */
public class OldViewXML {

	public static class Prop {
		public String key;
		public String value;
	}

	public static class TrackGroup {
		public String className;
		public List<Prop> groupProperties = new ArrayList<Prop>();
		public List<Prop> property = new ArrayList<Prop>();
		public List<Track> track = new ArrayList<Track>();
	}

	public static class Track {
		public String className;
		public int height;
		public boolean pack;
		public String name;
		public List<Prop> property = new ArrayList<Prop>();
	}

	public TrackGroup trackGroup;

	public TrackView toTrackView() {
		TrackView v = new TrackView();
		v.trackGroup.class_ = trackGroup.className;

		return v;
	}

}
