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
// TrackView.java
// Since: 2009/11/27
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.view;

import java.io.Serializable;
import java.util.ArrayList;

import org.utgenome.gwt.utgb.client.util.Properties;

/**
 * view definition of tracks
 * 
 * @author leo
 * 
 */
public class TrackView implements Serializable {

	private static final long serialVersionUID = 1L;

	public TrackGroup trackGroup = new TrackGroup();
	public ArrayList<Track> track = new ArrayList<Track>();

	//public Map<TrackGroup, Track> trackGroup_track = new HashMap<TrackGroup, Track>();

	public TrackView() {
	}

	/**
	 * Track Group is a set of tracks that share the same coordinate
	 * 
	 * @author leo
	 * 
	 */
	public static class TrackGroup implements Serializable {
		private static final long serialVersionUID = 1L;

		/**
		 * ID of this track group
		 */
		public int id;
		/**
		 * Java class name of this track group. The underscore (_) is necessary to differentiate this parameter name
		 * from the same name Java keyword, <i>class</i>
		 */
		public String class_;

		/**
		 * 
		 */
		public Coordinate coordinate;

		public Properties property = new Properties();
	}

	public static class Track implements Serializable {
		private static final long serialVersionUID = 1L;

		/**
		 * belonging track group
		 */
		public TrackGroup trackGroup;

		public String name;
		public int height;
		public boolean pack;

		/**
		 * Java class name of this track. The underscore (_) is necessary to differentiate this parameter name from the
		 * same name Java keyword, <i>class</i>
		 */
		public String class_;
		public Properties property = new Properties();

	}

	/**
	 * Coordinate system
	 * 
	 * @author leo
	 * 
	 */
	public static class Coordinate implements Serializable {

		private static final long serialVersionUID = 1L;

		/**
		 * reference sequence name
		 */
		public String ref;
		/**
		 * name of chromosome, contig, scaffold, etc.
		 */
		public String chr;

		/**
		 * 1-based start position (inclusive)
		 */
		public int start;
		/**
		 * 1-based end position (inclusive)
		 */
		public int end;

		/**
		 * ribbon string showing in/del states
		 */
		public String ribbon;

		/**
		 * pixel width of the track window
		 */
		public int pixelWidth;
	}

}
