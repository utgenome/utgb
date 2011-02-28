/*--------------------------------------------------------------------------
 *  Copyright 2011 utgenome.org
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
// TrackDisplay.java
// Since: 2011/02/28
//
//--------------------------------------
package org.utgenome.gwt.utgb.client.view;

import java.util.List;

/**
 * Definition of tracks to display
 * 
 * @author leo
 * 
 */
public class TrackDisplay {

	public static class DB {
		public String path;
	}

	public static class Track {
		public String name;
		public DB db;
	}

	public List<Track> track;

}
