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
// LegacyTrack.java
// Since: Jun 7, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.lib;

import org.utgenome.gwt.utgb.client.db.datatype.IntegerType;
import org.utgenome.gwt.utgb.client.db.datatype.StringType;
import org.utgenome.gwt.utgb.client.track.Design;
import org.utgenome.gwt.utgb.client.track.Track;
import org.utgenome.gwt.utgb.client.track.TrackBase;
import org.utgenome.gwt.utgb.client.track.TrackConfig;
import org.utgenome.gwt.utgb.client.track.TrackConfigChange;
import org.utgenome.gwt.utgb.client.track.TrackFrame;
import org.utgenome.gwt.utgb.client.track.TrackGroup;
import org.utgenome.gwt.utgb.client.track.TrackGroupPropertyChange;
import org.utgenome.gwt.utgb.client.track.TrackWindow;
import org.utgenome.gwt.utgb.client.ui.CSS;
import org.utgenome.gwt.utgb.client.util.Properties;

import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

/**
 * A simple wrapper for old UTGB tracks
 * 
 * @author leo
 * 
 */
public class LegacyTrack extends TrackBase {
	public static int INDEX_FRAME_WIDTH = 100;

	private FlexTable _panel = new FlexTable();
	private Image trackImage = new Image();
	private LegacyTrackInfo _legacyTrackInfo;
	private TrackConfig _config = new TrackConfig(this);
	private int offset = 0;

	public static TrackFactory factory() {
		return new TrackFactory() {
			public Track newInstance() {
				return new LegacyTrack();
			}
		};
	}

	public LegacyTrack() {
		this(new LegacyTrackInfo());
	}

	public LegacyTrack(LegacyTrackInfo trackInfo) {
		super(trackInfo.getName());
		this._legacyTrackInfo = trackInfo;

		_panel.setCellPadding(0);
		_panel.setCellSpacing(0);
		CSS.fontSize(_panel, 0);

		_panel.getCellFormatter().setWidth(0, 0, INDEX_FRAME_WIDTH + "px");
		_panel.setWidget(0, 1, trackImage);
	}

	public int getDefaultWindowHeight() {
		return 100;
	}

	public Widget getWidget() {
		return _panel;
	}

	public void onChangeTrackWindow(TrackWindow newWindow) {
		_legacyTrackInfo.setStart(newWindow.getStartOnGenome());
		_legacyTrackInfo.setEnd(newWindow.getEndOnGenome());

		draw();
	}

	public void draw() {

		int windowSize = getTrackGroup().getTrackWindow().getWindowWidth();
		_legacyTrackInfo.setWidth(windowSize);

		String imageUrl = _legacyTrackInfo.getImageURL(offset);
		trackImage.setUrl(imageUrl);
		getFrame().setNowLoading();
	}

	public void onChangeTrackGroupProperty(TrackGroupPropertyChange change) {
		String[] relatedProperty = { "species", "revision", "target" };

		if (!change.containsOneOf(relatedProperty))
			return; // no related change

		String species = change.getProperty("species", _legacyTrackInfo.getSpecies());
		String revision = change.getProperty("revision", _legacyTrackInfo.getRevision());
		String target = change.getProperty("target", _legacyTrackInfo.getTarget());

		_legacyTrackInfo.setSpecies(species);
		_legacyTrackInfo.setRevision(revision);
		_legacyTrackInfo.setTarget(target);

		// sync with the configuration panel
		_config.setParameter("species", species);
		_config.setParameter("revision", revision);
		_config.setParameter("target", target);

		draw();
	}

	public void setUp(TrackFrame trackFrame, TrackGroup group) {
		trackFrame.pack();

		TrackWindow w = group.getTrackWindow();
		_legacyTrackInfo.setStart(w.getStartOnGenome());
		_legacyTrackInfo.setEnd(w.getEndOnGenome());

		trackImage.addLoadHandler(new LoadHandler() {
			public void onLoad(LoadEvent e) {
				getFrame().onUpdateTrackWidget();
				getFrame().loadingDone();
			}
		});
		trackImage.addErrorHandler(new ErrorHandler() {
			public void onError(ErrorEvent arg0) {
				trackImage.setUrl(Design.IMAGE_NOT_AVAILABLE);
				getFrame().loadingDone();
			}
		});

		// set up the configuration panel
		_config.addConfigParameter("Track Name", new StringType("name"), _legacyTrackInfo.getName());
		_config.addConfigParameter("Track Image URL", new StringType("baseURL"), _legacyTrackInfo.getBaseURL());
		_config.addConfigParameter("Species", new StringType("species"), _legacyTrackInfo.getSpecies());
		_config.addConfigParameter("Revision", new StringType("revision"), _legacyTrackInfo.getRevision());
		_config.addConfigParameter("Target", new StringType("target"), _legacyTrackInfo.getTarget());
		_config.addConfigParameter("Offset", new IntegerType("offset"), Integer.toString(offset));

	}

	public TrackConfig getConfig() {
		return _config;
	}

	public void restoreProperties(Properties properties) {
		_legacyTrackInfo.setBaseURL(properties.get("baseURL", _legacyTrackInfo.getBaseURL()));
		_legacyTrackInfo.setSpecies(properties.get("species", _legacyTrackInfo.getSpecies()));
		_legacyTrackInfo.setRevision(properties.get("revision", _legacyTrackInfo.getRevision()));
		_legacyTrackInfo.setTarget(properties.get("target", _legacyTrackInfo.getTarget()));
		offset = properties.getInt("offset", offset);
	}

	public void saveProperties(Properties saveData) {
		saveData.add("baseURL", _legacyTrackInfo.getBaseURL());
		saveData.add("species", _legacyTrackInfo.getSpecies());
		saveData.add("revision", _legacyTrackInfo.getRevision());
		saveData.add("target", _legacyTrackInfo.getTarget());
		saveData.add("offset", offset);
	}

	public void onChangeTrackConfig(TrackConfigChange change) {
		if (change.contains("name"))
			getTrackInfo().setTrackName(change.getValue("name"));

		boolean drawFlag = false;
		if (change.contains("offset")) {
			offset = change.getIntValue("offset");
			drawFlag = true;
		}

		String[] param = { "baseURL", "species", "revision", "target" };
		if (change.containsOneOf(param)) {
			_legacyTrackInfo.setBaseURL(change.getValue("baseURL"));
			_legacyTrackInfo.setSpecies(change.getValue("species"));
			_legacyTrackInfo.setRevision(change.getValue("revision"));
			_legacyTrackInfo.setTarget(change.getValue("target"));
			drawFlag = true;
		}

		if (drawFlag)
			draw();

	}

}
