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
// Locus.java
// Since: 2009/02/17
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.bio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * wig format data
 * 
 * @author yoshimura
 * 
 */
public class WigGraphData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	int trackId = -1;
	float maxValue = Float.MIN_VALUE;
	float minValue = Float.MAX_VALUE;

	ArrayList<String> browser = null;
	HashMap<String, String> track = null;
	HashMap<Integer, Float> data = null;

	public int getTrack_id() {
		return trackId;
	}

	public void setTrack_id(int track_id) {
		this.trackId = track_id;
	}

	public float getMinValue() {
		return minValue;
	}

	public void setMinValue(float minValue) {
		this.minValue = minValue;
	}

	public float getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(float maxValue) {
		this.maxValue = maxValue;
	}

	public ArrayList<String> getBrowser() {
		return browser;
	}

	public void setBrowser(ArrayList<String> browser) {
		this.browser = browser;
	}

	public HashMap<String, String> getTrack() {
		return track;
	}

	public void setTrack(HashMap<String, String> track) {
		this.track = track;
	}

	public HashMap<Integer, Float> getData() {
		return data;
	}

	public void setData(HashMap<Integer, Float> data) {
		this.data = data;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();

		if (track.isEmpty())
			return null;

		sb.append("TrackID:" + trackId + "(");
		if (!browser.isEmpty()) {
			for (String s : browser) {
				sb.append(s + ", ");
			}
			sb.delete(sb.lastIndexOf(","), sb.length()).append("), (");
		}

		if (!track.isEmpty()) {
			for (String s : track.keySet()) {
				sb.append(s + "=" + track.get(s) + ", ");
			}
			sb.delete(sb.lastIndexOf(","), sb.length()).append("), (");
		}
		if (!data.isEmpty()) {
			for (int l : data.keySet()) {
				sb.append(l + ":" + data.get(l) + ", ");
			}
		}
		if (!sb.equals(""))
			sb.delete(sb.lastIndexOf(","), sb.length()).append(")");

		return sb.toString();
	}
}
