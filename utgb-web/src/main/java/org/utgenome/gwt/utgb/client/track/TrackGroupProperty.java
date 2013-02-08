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
// TrackGroupProperty.java
// Since: Jun 12, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track;

import java.util.Set;

import org.utgenome.gwt.utgb.client.util.xml.XMLWriter;

/**
 * {@link TrackGroupProperty} is an interface to read property values defined in a {@link TrackGroup}
 * 
 * @author leo
 * 
 */
public interface TrackGroupProperty {
	/**
	 * get a property value of a given key
	 * 
	 * @param key
	 * @return a property value of a given key
	 */
	public String getProperty(String key);

	/**
	 * @return the key set of properties
	 */
	public Set<String> keySet();

	/**
	 * get a property value of a given key. If the corresponding property is not found, return the default value
	 * 
	 * @param key
	 * @param defaultValue
	 * @return the property value of the key if it exists, otherwise return the defaultValue
	 */
	public String getProperty(String key, String defaultValue);

	public TrackWindow getTrackWindow();

	public void toXML(XMLWriter xmlWriter);
}
