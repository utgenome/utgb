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
// TrackBase.java
// Since: Jun 12, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track;

import java.util.HashMap;

import org.utgenome.gwt.utgb.client.BrowserServiceAsync;
import org.utgenome.gwt.utgb.client.RPCServiceManager;
import org.utgenome.gwt.utgb.client.bio.Coordinate;
import org.utgenome.gwt.utgb.client.util.CanonicalProperties;
import org.utgenome.gwt.utgb.client.util.Properties;
import org.utgenome.gwt.utgb.client.util.xml.XMLAttribute;
import org.utgenome.gwt.utgb.client.util.xml.XMLUtil;
import org.utgenome.gwt.utgb.client.util.xml.XMLWriter;
import org.utgenome.gwt.utgb.client.view.TrackView;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;

/**
 * {@link TrackBase} is a base class that supports to implement your own {@link Track}s.
 * 
 * @author leo
 * 
 */
public abstract class TrackBase implements Track {
	protected TrackGroup _trackGroup = null;
	protected TrackFrame _frame = null;
	protected final TrackInfo _trackInfo;
	private boolean _isInitialized = false;
	private int defaultTrackHeight = TrackFrameState.DEFAULT_MIN_TRACKFRAME_HEIGHT;

	private TrackConfig __config;

	public TrackBase(String trackName) {
		this(new TrackInfo(trackName));
	}

	public TrackBase(TrackInfo trackInfo) {
		_trackInfo = trackInfo;
		__config = new TrackConfig(this);
	}

	public void setUp(TrackFrame trackFrame, TrackGroup group) {
		// do nothing
	}

	/**
	 * Substitute the query parameters in the given string with the actual values.
	 * 
	 * @param template
	 * @return
	 */
	public String resolvePropertyValues(String template) {
		// replace track group properties
		TrackWindow w = getTrackWindow();
		if (template.contains("%start"))
			template = template.replaceAll("%start", Integer.toString(w.getStartOnGenome()));
		if (template.contains("%end"))
			template = template.replaceAll("%end", Integer.toString(w.getEndOnGenome()));
		if (template.contains("%len"))
			template = template.replaceAll("%len", Integer.toString(w.getSequenceLength()));
		if (template.contains("%pixelwidth"))
			template = template.replaceAll("%pixelwidth", Integer.toString(w.getPixelWidth()));
		String chr = getTrackGroupProperty(UTGBProperty.TARGET);
		if (chr != null && template.contains("%chr"))
			template = template.replaceAll("%chr", chr);
		String ref = getTrackGroupProperty(UTGBProperty.REVISION);
		if (ref != null && template.contains("%ref"))
			template = template.replaceAll("%ref", ref);
		String species = getTrackGroupProperty(UTGBProperty.SPECIES);
		if (species != null && template.contains("%species"))
			template = template.replaceAll("%species", species);

		return template;
	}

	public TrackGroup getTrackGroup() {
		assert (_trackGroup != null);
		return _trackGroup;
	}

	public TrackWindow getTrackWindow() {
		return _trackGroup.getTrackWindow();
	}

	public TrackInfo getTrackInfo() {
		return _trackInfo;
	}

	public void setTrackGroup(TrackGroup trackGroup) {
		this._trackGroup = trackGroup;
		validateTrackEnvironment();
	}

	public TrackFrame getFrame() {
		return _frame;
	}

	public void setFrame(TrackFrame frame) {
		this._frame = frame;
		validateTrackEnvironment();
	}

	private void validateTrackEnvironment() {
		if (_trackGroup != null && _frame != null) {
			_trackGroup.setResizeNotification(false);
			if (frameConfig != null) {

				_frame.setPacked(frameConfig.pack);

				// restore trackFrame State
				if (frameConfig.height > TrackFrameState.DEFAULT_MIN_TRACKFRAME_HEIGHT) {
					defaultTrackHeight = frameConfig.height;
					_frame.resize(defaultTrackHeight);
				}

			}
			setUp(_frame, _trackGroup);
			_trackGroup.setResizeNotification(true);
			_isInitialized = true;
		}
	}

	public int getDefaultWindowHeight() {
		return defaultTrackHeight;
	}

	public int getMinimumWindowHeight() {
		return 0;
	}

	public void onChangeTrackGroupProperty(TrackGroupPropertyChange change) {
		// do nothing in default
	}

	public void beforeChangeTrackWindow(TrackWindow newWindow) {
		// do nothing in default 
	}

	public void onChangeTrackWindow(TrackWindow newWindow) {
		// do nothing in default
	}

	public void onChange(TrackGroupPropertyChange change, TrackWindow newWindow) {

		// invoke onChangeTrackGroupProperty for compatibility
		if (change != null)
			this.onChangeTrackGroupProperty(change);

		if (newWindow != null)
			this.onChangeTrackWindow(newWindow);

	}

	public void draw() {
		// do nothing on default
	}

	public void refresh() {
		TrackBase.this.draw();
		//TrackBase.this.draw();
		//getFrame().onUpdateTrackWidget();
		DeferredCommand.addCommand(new Command() {
			public void execute() {
				getFrame().onUpdateTrackWidget();

			}
		});
	}

	/**
	 * Get the current genome coordinate
	 * 
	 * @return
	 */
	public Coordinate getCoordinate() {
		TrackGroupProperty prop = getTrackGroup().getPropertyReader();
		String species = prop.getProperty(UTGBProperty.SPECIES);
		String revision = prop.getProperty(UTGBProperty.REVISION);
		String name = prop.getProperty(UTGBProperty.TARGET);
		String group = prop.getProperty(UTGBProperty.GROUP, "utgb");

		return new Coordinate(group, species, revision, name);

	}

	public boolean isInitialized() {
		return _isInitialized;
	}

	public void onChangeTrackConfig(TrackConfigChange change) {
		// do nothing on default
	}

	public TrackConfig getConfig() {
		return __config;
	}

	public void setConfig(TrackConfig config) {
		this.__config = config;
	}

	public String getName() {
		return getTrackInfo().getTrackName();
	}

	public boolean isTrack() {
		return true;
	}

	public boolean isTrackGroup() {
		return false;
	}

	public static class TrackFrameConfig {
		public int height = TrackFrameState.DEFAULT_MIN_TRACKFRAME_HEIGHT;
		public boolean pack = true;
	}

	public TrackFrameConfig frameConfig = null;

	public void loadView(TrackView.Track view) {
		if (view.name != null)
			getTrackInfo().setTrackName(view.name);

		//		Properties p = new Properties();
		//		p.putAll(view.property);
		//		p.put("height", Integer.toString(view.height));
		//		p.put("pack", Boolean.toString(view.pack));

		frameConfig = new TrackFrameConfig();
		frameConfig.height = view.height;
		frameConfig.pack = view.pack;

		restoreProperties(view.property);
	}

	public TrackView.Track toView() {
		TrackView.Track t = new TrackView.Track();
		t.name = getName();
		t.height = getWidget().getOffsetHeight();
		t.class_ = getClassName();
		t.pack = _frame.isPacked();
		saveProperties(t.property);
		return t;
	}

	public String toXML() {
		XMLWriter xmlWriter = new XMLWriter();
		toXML(xmlWriter);
		return xmlWriter.toString();
	}

	public void toXML(XMLWriter xmlWriter) {
		xmlWriter.start("track", new XMLAttribute().add("className", getClassName()).add("name", getName()).add("height", getWidget().getOffsetHeight()).add(
				"pack", _frame.isPacked()));

		CanonicalProperties trackProperties = new CanonicalProperties();
		saveProperties(trackProperties);
		XMLUtil.toCanonicalXML(trackProperties, xmlWriter);
		xmlWriter.end(); // track
	}

	public String getClassName() {
		return this.getClass().getName();
	}

	/**
	 * Override this method to save internal state of the track into a {@link Properties} object
	 * 
	 * @param xmlWriter
	 */
	public void saveProperties(CanonicalProperties saveData) {
		__config.saveProperties(saveData);
	}

	/**
	 * Override this method to restore internal state of the track
	 * 
	 * @param properties
	 */
	public void restoreProperties(CanonicalProperties properties) {
		__config.restoreProperties(properties);
	}

	public String getTrackGroupProperty(String key) {
		return getTrackGroup().getProperty(key);
	}

	public String getTrackGroupProperty(String key, String defaultValue) {
		return getTrackGroup().getProperty(key, defaultValue);
	}

	public void setTrackGroupProperty(String key, String value) {
		getTrackGroup().getPropertyWriter().setProperty(key, value);
	}

	public void setCenterOfTrackWindow(String chr, int start, int end) {

		TrackWindow win = getTrackGroup().getTrackWindow();
		int width = win.getEndOnGenome() - win.getStartOnGenome();
		int left = start;
		int right = end;
		if (width < 0) {
			width = -width;
		}

		// locate the new window so that the target region will be at 20% from the left side 
		int newLeft = left - (int) (width * 0.3);
		int newRight = right + (int) (width * 0.3);

		TrackGroupPropertyWriter writer = getTrackGroup().getPropertyWriter();
		HashMap<String, String> property = new HashMap<String, String>();
		property.put(UTGBProperty.TARGET, chr);

		try {
			writer.setProperyChangeNotifaction(false);
			if (!win.isReverseStrand())
				writer.setTrackWindow(newLeft, newRight);
			else
				writer.setTrackWindow(newRight, newLeft);
		}
		finally {
			writer.setProperyChangeNotifaction(true);
		}
		writer.setProperty(property);
	}

	/**
	 * Get the RPC service for communicating with the server
	 * 
	 * @return
	 */
	public BrowserServiceAsync getBrowserService() {
		return RPCServiceManager.getRPCService();
	}

}
