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
// CompactWIGData.java
// Since: 2010/05/29
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.bio;

import java.util.ArrayList;
import java.util.HashMap;

public class CompactWIGData implements GraphData {
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
	float[] data;
	int start;
	int span = 1;

	public void setSpan(int span) {
		this.span = span;
	}

	public int getSpan() {
		return span;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getStart() {
		return start;
	}

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

	public float[] getData() {
		return data;
	}

	public void setData(float[] data) {
		this.data = data;
	}

	@Override
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
		if (data != null) {
			sb.append(data + ", ");
		}
		if (!sb.equals(""))
			sb.delete(sb.lastIndexOf(","), sb.length()).append(")");

		return sb.toString();
	}

	public void accept(OnGenomeDataVisitor visitor) {
		visitor.visitGraph(this);
	}

	public int getEnd() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	public int length() {
		return 0;
	}

}
