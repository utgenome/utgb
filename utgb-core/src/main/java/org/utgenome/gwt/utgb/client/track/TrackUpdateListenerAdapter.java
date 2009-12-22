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
// TrackUpdateListenerAdapter.java
// Since: Jun 25, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track;

/**
 * An adapter class for implementing {@link TrackUpdateListener}
 * @author leo
 *
 */
public abstract class TrackUpdateListenerAdapter implements TrackUpdateListener {

	public void onAddTrackGroup(TrackGroup trackGroup) {
		
	}

	public void onInsertTrack(Track track, int beforeIndex) {
		onInsertTrack(track);
	}

	public void onInsertTrack(Track track) {
		
	}

	public void onRemoveTrack(Track track) {
		
	}

	public void onRemoveTrackGroup(TrackGroup trackGroup) {
		
	}

	public void onResizeTrack() {
		
	}

    public void onResizeTrackWindow(int newWindowSize)
    {
        
    }

    public void onDetachedFromTrackGroup(TrackGroup trackGroup)
    {
        
    }



}




