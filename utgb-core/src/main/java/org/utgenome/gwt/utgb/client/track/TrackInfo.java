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
// TrackInfo.java
// Since: Jun 6, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * TrackInfo class holds track name, track id, descriptions etc.
 * 
 * @author leo
 * 
 */
public class TrackInfo {
	private String trackName;
	private String description = "";
	private String linkURL = "";

	private ArrayList<TrackInfoChangeListener> _listenerList = new ArrayList<TrackInfoChangeListener>();

	public TrackInfo(String trackName) {
		setTrackName(trackName);
		description = trackName;
	}

	/**
	 * @param trackName
	 * @param description
	 */
	public TrackInfo(String trackName, String description) {
		this.trackName = trackName;
		this.description = description;
	}

	/**
	 * add a change listener
	 * 
	 * @param listener
	 */
	public void addChangeListener(TrackInfoChangeListener listener) {
		_listenerList.add(listener);
	}

	/**
	 * remove the specified change listener
	 * 
	 * @param listner
	 */
	public void removeChangeListner(TrackInfoChangeListener listner) {
		_listenerList.remove(listner);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
		notifyTheChange();
	}

	public String getTrackName() {
		return trackName;
	}

	public void setTrackName(final String trackName) {
		this.trackName = trackName;
		notifyTheChange();
	}

	public String getLinkURL() {
		return linkURL;
	}

	/**
	 * Set a link for this track
	 * 
	 * @param linkURL
	 */
	public void setLinkURL(String linkURL) {
		this.linkURL = linkURL;
		notifyTheChange();
	}

	private void notifyTheChange() {
		for (Iterator<TrackInfoChangeListener> it = _listenerList.iterator(); it.hasNext();) {
			TrackInfoChangeListener listener = it.next();
			listener.onChange(this);
		}
	}
}
