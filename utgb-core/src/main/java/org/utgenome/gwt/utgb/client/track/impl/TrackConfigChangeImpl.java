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
// TrackConfigChangeImpl.java
// Since: Jun 20, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.impl;

import java.util.HashSet;

import org.utgenome.gwt.utgb.client.track.TrackConfig;
import org.utgenome.gwt.utgb.client.track.TrackConfigChange;
import org.utgenome.gwt.utgb.client.util.CanonicalProperties;

public class TrackConfigChangeImpl implements TrackConfigChange {

	TrackConfig _config;
	HashSet<String> changedParamSet = new HashSet<String>();

	public TrackConfigChangeImpl(TrackConfig config, String changedParam) {
		this._config = config;
		changedParamSet.add(changedParam);
	}

	public boolean contains(String configParameterName) {
		return changedParamSet.contains(CanonicalProperties.toCanonicalName(configParameterName));
	}

	public boolean containsOneOf(String[] configParameterName) {

		for (int i = 0; i < configParameterName.length; i++) {
			if (changedParamSet.contains(CanonicalProperties.toCanonicalName(configParameterName[i])))
				return true;
		}
		return false;
	}

	public String getValue(String configParamName) {
		return _config.getParameter(configParamName);
	}

	public int getIntValue(String configParamName) {
		String value = _config.getParameter(configParamName);
		return Integer.parseInt(value);
	}

	public float getFloatValue(String configParamName) {
		String value = _config.getParameter(configParamName);
		return Float.parseFloat(value);
	}

	public boolean getBoolValue(String configParamName) {
		String value = _config.getParameter(configParamName);
		return Boolean.parseBoolean(value);
	}

	public HashSet<String> getChangedParamSet() {
		return changedParamSet;
	}
}
