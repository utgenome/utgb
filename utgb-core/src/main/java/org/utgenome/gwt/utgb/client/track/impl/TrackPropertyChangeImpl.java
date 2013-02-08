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
// TrackPropertyChangeImpl.java
// Since: Jun 25, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.impl;

import java.util.HashSet;
import java.util.Set;

import org.utgenome.gwt.utgb.client.track.TrackGroupProperty;
import org.utgenome.gwt.utgb.client.track.TrackGroupPropertyChange;
import org.utgenome.gwt.utgb.client.track.TrackWindow;
import org.utgenome.gwt.utgb.client.util.xml.XMLWriter;

/**
 * An implementation of the {@link TrackGroupPropertyChange}
 * 
 * @author leo
 * 
 */
class TrackPropertyChangeImpl implements TrackGroupPropertyChange {
	private HashSet<String> _keySetOfChangedProperties = new HashSet<String>();

	private TrackGroupProperty _reader;

	public TrackPropertyChangeImpl(TrackGroupProperty reader, String keyOfChangedProperty) {
		this._reader = reader;
		addChangedProperty(keyOfChangedProperty);
	}

	public TrackPropertyChangeImpl(TrackGroupProperty reader, Set<String> keySet) {
		this._reader = reader;
		addChangedProperty(keySet);
	}

	public void addChangedProperty(String key) {
		_keySetOfChangedProperties.add(key);
	}

	public void addChangedProperty(Set<String> keySet) {
		for (String key : keySet)
			addChangedProperty(key);
	}

	public boolean contains(String key) {
		return _keySetOfChangedProperties.contains(key);
	}

	public boolean containsOneOf(String[] keySet) {
		for (int i = 0; i < keySet.length; i++) {
			if (_keySetOfChangedProperties.contains(keySet[i]))
				return true;
		}
		return false;
	}

	public boolean containsOneOf(Iterable<String> keyList) {
		for (String key : keyList) {
			if (_keySetOfChangedProperties.contains(key))
				return true;
		}
		return false;
	}

	public String getProperty(String key) {
		return _reader.getProperty(key);
	}

	public String getProperty(String key, String defaultValue) {
		return _reader.getProperty(key, defaultValue);
	}

	public TrackWindow getTrackWindow() {
		return _reader.getTrackWindow();
	}

	public Set<String> keySet() {
		return _reader.keySet();
	}

	public Set<String> changedKeySet() {
		return _keySetOfChangedProperties;
	}

	public void toXML(XMLWriter xmlWriter) {
		_reader.toXML(xmlWriter);
	}

}
