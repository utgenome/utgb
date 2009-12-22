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
// TrackFrameEventListener.java
// Since: Jun 18, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track;

/**
 * An interface for handling addition and removal of tracks
 * 
 * @author leo
 * 
 */
public interface TrackUpdateListener
{
    /**
     * When the track is removed from the group
     * 
     * @param track
     */
    public void onRemoveTrack(Track track);

    /**
     * When a new track is inserted to the group
     * 
     * @param track
     */
    public void onInsertTrack(Track track);

    /**
     * 
     * @param trackGroup
     */
    public void onAddTrackGroup(TrackGroup trackGroup);

    /**
     * @param trackGroup
     */
    public void onRemoveTrackGroup(TrackGroup trackGroup);

    /**
     * When a new track is inserted to the group
     * 
     * @param track
     * @param beforeIndex
     */
    public void onInsertTrack(Track track, int beforeIndex);

    /**
     * When the size of a track changes
     */
    public void onResizeTrack();

    public void onResizeTrackWindow(int newWindowSize);

}
