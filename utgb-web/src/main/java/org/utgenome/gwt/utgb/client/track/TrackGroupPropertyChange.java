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
// TrackGroupPropertyChange.java
// Since: Jun 12, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track;

import java.util.Set;

/**
 * Notification of TrackGroupProperty change
 * 
 * @author leo
 * 
 */
public interface TrackGroupPropertyChange extends TrackGroupProperty {
	/**
	 * Test whether the property of the given key has been changed
	 * 
	 * @param key
	 *            property key
	 * @return true if the property of the given key has been changed, otherwise false
	 */
	boolean contains(String key);

	/**
	 * Test at least one of properties among the given key set has changed
	 * 
	 * @param keySet
	 *            a set of keys to test
	 * @return true if keySet contains a key of changed properties, otherwise false
	 */
	boolean containsOneOf(String[] keySet);

	boolean containsOneOf(Iterable<String> keyList);

	/**
	 * Get the key set of the changed property keys
	 * 
	 * @return
	 */
	Set<String> changedKeySet();

}
