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
// OldUTGBRulerTrack.java
// Since: 2007/07/18
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.lib.old;

import java.util.HashMap;
import java.util.Map;

import org.utgenome.gwt.utgb.client.track.Track;

public class OldUTGBRulerTrack extends OldUTGBTrack {

	public static TrackFactory factory() {
		return new TrackFactory() {
			Map<String, String> properties = new HashMap<String, String>();

			public Track newInstance() {
				OldUTGBRulerTrack track = new OldUTGBRulerTrack();
				final String descriptionXMLURL = (String) (properties.get("descriptionXMLURL"));
				if (descriptionXMLURL != null) {
					track.setDescriptionXML(descriptionXMLURL);
				}
				return track;
			}

			public void setProperty(String key, String value) {
				properties.put(key, value);
			}
		};
	}

	public OldUTGBRulerTrack() {
	}

	public int getDefaultWindowHeight() {
		return 10;
	}

	public int getMinimumWindowHeight() {
		return 10;
	}

}
