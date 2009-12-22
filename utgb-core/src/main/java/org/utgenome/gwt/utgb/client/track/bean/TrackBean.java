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
// AvailableTrackInfo.java
// Since: Aug 8, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.bean;

import java.util.Map;
import java.util.TreeMap;

import com.google.gwt.user.client.rpc.IsSerializable;

public class TrackBean implements IsSerializable, Comparable<TrackBean> {
	private String trackName = "";
	private String className;
	private int height;
	private Boolean pack = null;
	private String description = "";
	private String developer = "";

	/**
	 */
	private Map<String, String> property = new TreeMap<String, String>();

	/**
	 * @param trackName
	 * @param className
	 * @param height
	 * @param pack
	 * @param description
	 * @param developer
	 */
	public TrackBean(String trackName, String className, int height, boolean pack, String description, String developer) {
		this.trackName = trackName;
		this.className = className;
		this.height = height;
		this.pack = new Boolean(pack);
		this.description = description;
		this.developer = developer;
	}

	public TrackBean() {
	}

	public String getName() {
		return trackName;
	}

	public void setName(String trackName) {
		this.trackName = trackName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public Boolean getPack() {
		return pack;
	}

	public void setPack(Boolean pack) {
		this.pack = pack;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDeveloper() {
		return developer;
	}

	public void setDeveloper(String developer) {
		this.developer = developer;
	}

	public void putProperty(String key, String value) {
		this.property.put(key, value);
	}

	public Map<String, String> getProperty() {
		return this.property;
	}

	public int compareTo(TrackBean o) {
		if (o == null)
			return 1;
		return trackName.compareTo(o.trackName);
	}
}
