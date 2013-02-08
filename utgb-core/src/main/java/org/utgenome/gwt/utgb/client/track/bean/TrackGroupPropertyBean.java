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
// GroupProperties.java
// Since: Aug 9, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.bean;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.IsSerializable;

public class TrackGroupPropertyBean implements IsSerializable {
	/**
	 */
	private HashMap<String, String> property = new HashMap<String, String>();
	private TrackWindowBean window;

	public TrackGroupPropertyBean() {
	}

	public void putProperty(String key, String value) {
		property.put(key, value);
	}

	public HashMap<String, String> getProperty() {
		return property;
	}

	public void setTrackWindow(TrackWindowBean window) {
		this.window = window;
	}

	public TrackWindowBean getTrackWindow() {
		return window;
	}

}
