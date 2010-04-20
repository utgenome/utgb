/*--------------------------------------------------------------------------
 *  Copyright 2009 utgenome.org
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
// utgb-core Project
//
// GenomeTrack.java
// Since: Feb 17, 2009
//
// $URL: http://svn.utgenome.org/utgb/trunk/utgb/utgb-core/src/main/java/org/utgenome/gwt/utgb/client/track/lib/GenomeTrack.java $ 
// $Author: yoshimura $
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.lib;

import org.utgenome.gwt.utgb.client.bio.Coordinate;
import org.utgenome.gwt.utgb.client.db.datatype.StringType;
import org.utgenome.gwt.utgb.client.track.Track;
import org.utgenome.gwt.utgb.client.track.TrackConfigChange;
import org.utgenome.gwt.utgb.client.track.TrackFrame;
import org.utgenome.gwt.utgb.client.track.TrackGroup;
import org.utgenome.gwt.utgb.client.track.TrackWindow;
import org.utgenome.gwt.utgb.client.util.Properties;

/**
 * BEDTrack is for visualizing data that can be located on BDE Format Data.
 * 
 * @author yoshimura
 * 
 */
public class BEDTrack extends GenomeTrack {

	protected String fileName = null;

	public static TrackFactory factory() {
		return new TrackFactory() {
			@Override
			public Track newInstance() {
				return new BEDTrack();
			}
		};
	}

	public BEDTrack() {
		super("BED Track");
	}

	@Override
	protected String getTrackURL() {
		Coordinate c = getCoordinate();

		Properties p = new Properties();
		TrackWindow w = getTrackGroup().getTrackWindow();
		p.add("start", w.getStartOnGenome());
		p.add("end", w.getEndOnGenome());
		p.add("width", w.getWindowWidth() - leftMargin);
		p.add("fileName", fileName);

		return c.getTrackURL(trackBaseURL, p);
	}

	@Override
	public void setUp(TrackFrame trackFrame, TrackGroup group) {
		super.setUp(trackFrame, group);

		config.addConfigParameter("File Name", new StringType("fileName"), fileName);
	}

	@Override
	public void onChangeTrackConfig(TrackConfigChange change) {
		super.onChangeTrackConfig(change);

		if (change.contains("fileName")) {
			fileName = change.getValue("fileName");
			draw();
		}
	}

	@Override
	public void saveProperties(Properties saveData) {
		super.saveProperties(saveData);
		saveData.add("fileName", fileName);
	}

	@Override
	public void restoreProperties(Properties properties) {
		super.restoreProperties(properties);
		fileName = properties.get("fileName", fileName);
	}

}
