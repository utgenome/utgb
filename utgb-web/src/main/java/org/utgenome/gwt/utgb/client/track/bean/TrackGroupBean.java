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
// UTGBMedaka Project
//
// TrackGroup.java
// Since: Aug 9, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.bean;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.IsSerializable;

public class TrackGroupBean implements IsSerializable {
	private String className;
	/**
	 */
	private ArrayList<TrackGroupBean> trackGroup = new ArrayList<TrackGroupBean>();
	/**
	 */
	private ArrayList<TrackBean> track = new ArrayList<TrackBean>();

	/**
	 */
	private HashMap<String, String> property = new HashMap<String, String>();
	private TrackGroupPropertyBean groupProperty = new TrackGroupPropertyBean();

	public TrackGroupBean() {
	}

	public void addTrackGroup(TrackGroupBean trackGroup) {
		this.trackGroup.add(trackGroup);
	}

	public ArrayList<TrackGroupBean> getTrackGroup() {
		return trackGroup;
	}

	public void addTrack(TrackBean track) {
		this.track.add(track);
	}

	public ArrayList<TrackBean> getTrack() {
		return track;
	}

	public void putProperty(String key, String value) {
		property.put(key, value);
	}

	public HashMap<String, String> getProperty() {
		return property;
	}

	public void setGroupProperties(TrackGroupPropertyBean groupProperty) {
		this.groupProperty = groupProperty;
	}

	public TrackGroupPropertyBean getGroupProperties() {
		return this.groupProperty;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

}
