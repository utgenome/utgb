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
// TrackFactoryHolder.java
// Since: Aug 7, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track;

import java.util.HashMap;

import org.utgenome.gwt.utgb.client.UTGBClientErrorCode;
import org.utgenome.gwt.utgb.client.UTGBClientException;
import org.utgenome.gwt.utgb.client.track.HasFactory.TrackGroupFactory;
import org.utgenome.gwt.utgb.client.track.Track.TrackFactory;

/**
 * 
 * @author leo
 */
public class TrackFactoryHolder {

	/**
	 * Non-constractable
	 */
	private TrackFactoryHolder() {
	}

	private static HashMap<String, TrackFactory> trackFactoryTable = new HashMap<String, TrackFactory>();
	private static HashMap<String, TrackGroupFactory> trackGroupFactoryTable = new HashMap<String, TrackGroupFactory>();

	static {
		TrackFactorySetup.initialize();
	}

	public static void addTrackFactory(String absoluteTrackClassName, TrackFactory factory) {
		trackFactoryTable.put(absoluteTrackClassName, factory);
	}

	public static void addTrackGroupFactory(String absoluteTrackGroupClassName, TrackGroupFactory factory) {
		trackGroupFactoryTable.put(absoluteTrackGroupClassName, factory);
	}

	public static TrackFactory getTrackFactory(String name) throws UTGBClientException {
		TrackFactory factory = trackFactoryTable.get(name);
		if (factory == null)
			throw new UTGBClientException(UTGBClientErrorCode.UNKNOWN_TRACK, "unknown track name: " + name);
		else
			return factory;
	}

	public static TrackGroupFactory getTrackGroupFactory(String name) throws UTGBClientException {
		TrackGroupFactory factory = trackGroupFactoryTable.get(name);
		if (factory == null)
			throw new UTGBClientException(UTGBClientErrorCode.UNKNOWN_TRACK_GROUP, "unknown track group: " + name);
		else
			return factory;
	}

}
