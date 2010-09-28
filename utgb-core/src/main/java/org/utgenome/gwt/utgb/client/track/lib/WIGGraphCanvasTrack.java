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
// WIGGraphCanvasTrack.java
// Since: Dec. 8, 2009
//
// $URL$ 
// $Author$ 
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.lib;

import org.utgenome.gwt.utgb.client.track.Track;

/**
 * WIG Track for displaying graph data, including bar chart, heat map, etc. Note: the implementation of
 * WIGGraphCanvasTrack was moved to {@link WIGTrack}.
 * 
 * 
 * @author yoshimura
 * @author leo
 * 
 */
public class WIGGraphCanvasTrack extends WIGTrack {

	public static TrackFactory factory() {
		return new TrackFactory() {
			@Override
			public Track newInstance() {
				return new WIGGraphCanvasTrack();
			}
		};
	}

	public WIGGraphCanvasTrack() {
		super();
	}

}
