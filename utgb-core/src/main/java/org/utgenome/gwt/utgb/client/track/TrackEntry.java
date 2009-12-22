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
// TrackEntry.java
// Since: Jun 25, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track;


/**
 * TrackEntry is a common interface for {@link Track} and {@link TrackGroup}.
 * @author leo
 *
 */
public interface TrackEntry {
	/**
	 * Test whether this entry is {@link Track} or not. 
	 * @return true if this entry is {@link Track}, othewise false.
	 */
	boolean isTrack();
	
	/**
	 * Test whether this entry is {@link TrackGroup} or not
	 * @return true if this entyr is {@link TrackGroup}, otherwise false.
	 */ 
	boolean isTrackGroup();
	

	/**
	 * Get the entry name
	 * @return the entry name
	 */
	String getName();
}




