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
package org.utgenome.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.utgenome.gwt.utgb.client.view.TrackView;

/**
 * For reading view.xml files used in UTGB 1.3.x.
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

		public GroupProperties groupProperties = new GroupProperties();
		public List<Prop> property = new ArrayList<Prop>();
		public List<Track> track = new ArrayList<Track>();

		public String getProperty(String key) {
			for (Prop p : groupProperties.property) {
				if (p.key != null && p.key.equals(key)) {
					return p.value;
				}
			}
			return null;
		}

		public Set<String> propKeySet() {
			HashSet<String> keySet = new HashSet<String>();
			for (Prop p : groupProperties.property) {
				if (p.key != null) {
					keySet.add(p.key);
				}
			}
			return keySet;
		}

	}

	public static class GroupProperties {
		public List<Prop> property = new ArrayList<Prop>();
		public TrackWindow trackWindow = new TrackWindow();
	}

	public static class TrackWindow {
		public int start;
		public int end;
		public int width;
	}

	public static class Track {

		public String name;
		public String className;
		public int height;
		public boolean pack;

		public List<Prop> property = new ArrayList<Prop>();
	}

	public TrackGroup trackGroup;

	public TrackView toTrackView() {
		TrackView v = new TrackView();
		v.trackGroup.class_ = trackGroup.className;
		v.trackGroup.id = 1;

		// coordinate
		TrackView.Coordinate c = new TrackView.Coordinate();
		c.start = trackGroup.groupProperties.trackWindow.start;
		c.end = trackGroup.groupProperties.trackWindow.end;
		c.species = trackGroup.getProperty("species");
		c.ref = trackGroup.getProperty("revision");
		c.chr = trackGroup.getProperty("target");

		c.pixelWidth = trackGroup.groupProperties.trackWindow.width;

		v.trackGroup.coordinate = c;
		for (Prop p : trackGroup.property) {
			v.trackGroup.property.put(p.key, p.value);
		}
		for (String key : trackGroup.propKeySet()) {
			if (key == null)
				continue;

			if (key.equals("species") || key.equals("revision") || key.equals("target"))
				continue;

			String val = trackGroup.getProperty(key);
			v.trackGroup.property.put(key, val);
		}

		for (Track each : trackGroup.track) {
			TrackView.Track t = new TrackView.Track();
			t.name = each.name;
			t.height = each.height;
			t.pack = each.pack;
			t.class_ = each.className;
			if (t.class_.startsWith("org.utgenome.gwt.utgb.client.track.lib."))
				t.class_ = t.class_.replace("org.utgenome.gwt.utgb.client.track.lib.", "");

			for (Prop p : each.property) {
				t.property.put(p.key, p.value);
			}

			v.track.add(t);
		}

		return v;
	}
}
