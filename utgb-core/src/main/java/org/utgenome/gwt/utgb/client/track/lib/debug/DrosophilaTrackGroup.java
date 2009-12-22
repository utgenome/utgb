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
// DrosophilaTrackGroup.java
// Since: Jun 21, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.lib.debug;

import java.util.HashMap;

import org.utgenome.gwt.utgb.client.track.TrackGroup;
import org.utgenome.gwt.utgb.client.track.TrackGroupPropertyChange;
import org.utgenome.gwt.utgb.client.track.TrackWindow;
import org.utgenome.gwt.utgb.client.track.lib.LegacyTrack;
import org.utgenome.gwt.utgb.client.track.lib.LegacyTrackInfo;

public class DrosophilaTrackGroup extends TrackGroup {

	public static TrackGroupFactory factory()
	{
		return new TrackGroupFactory() {
			HashMap<String, String> property = new HashMap<String, String>();
			
			public TrackGroup newInstance() {
				TrackGroup group = new DrosophilaTrackGroup();
				group.getPropertyWriter().setProperty(property);
				return group;
			}

			public String getProperty(String key) {
				if(property.containsKey(key))
					return property.get(key);
				else
					return null;
			}

			public void setProperty(String key, String value) {
				property.put(key, value);
			}
		};
	}
	
	
	public DrosophilaTrackGroup()
	{
		super("Drosopihla Track Group");
		addTrack(new LegacyTrack(new LegacyTrackInfo("drosophila", "R4.1","5'SAGE Dme", "http://dev.utgenome.org/~utgb/api/browserview/default/easytrack/easygenetrack.php/dbtype=dbaccess/trackname=5pSageDme/tracktype=gene")));
		addTrack(new LegacyTrack(new LegacyTrackInfo("drosophila", "R4.1","5'SAGE Dmw", "http://dev.utgenome.org/~utgb/api/browserview/default/easytrack/easygenetrack.php/dbtype=dbaccess/trackname=5pSageDmw/tracktype=gene")));
		addTrack(new LegacyTrack(new LegacyTrackInfo("drosophila", "R4.1","FlyBase CDS", "http://dev.utgenome.org/~utgb/api/browserview/default/easytrack/easygenetrack.php/dbtype=dbaccess/trackname=FlyBaseCDS/tracktype=gene")));
		addTrack(new LegacyTrack(new LegacyTrackInfo("drosophila", "R4.1","FlyBase Transcript", "http://dev.utgenome.org/~utgb/api/browserview/default/easytrack/easygenetrack.php/dbtype=dbaccess/trackname=FlyBaseTranscript/tracktype=gene")));
		addTrack(new LegacyTrack(new LegacyTrackInfo("drosophila", "R4.1","FlyBase GSVector", "http://dev.utgenome.org/~utgb/api/browserview/default/easytrack/easygenetrack.php/dbtype=dbaccess/trackname=GSvector/tracktype=gene")));
		//addTrack(new LegacyTrack(new LegacyTrackInfo("drosophila", "R4.1","-5'SAGE Dme", "http://dev.utgenome.org/~utgb/api/browserview/default/easytrack/easygenetrack.php/dbtype=dbaccess/trackname=minus5pSageDme/tracktype=gene")));

	}

	public void onParentTrackGroupPropertyChange(TrackGroupPropertyChange change) {
		getPropertyWriter().apply(change);
	}


	public void onParentTrackWindowChange(TrackWindow newWindow) {
		getPropertyWriter().setTrackWindow(newWindow);
	}
	
}

