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
// TrackGroupPropertyImpl.java
// Since: Jun 12, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.utgenome.gwt.utgb.client.track.Track;
import org.utgenome.gwt.utgb.client.track.TrackGroup;
import org.utgenome.gwt.utgb.client.track.TrackGroupProperty;
import org.utgenome.gwt.utgb.client.track.TrackGroupPropertyChange;
import org.utgenome.gwt.utgb.client.track.TrackGroupPropertyChangeListener;
import org.utgenome.gwt.utgb.client.track.TrackGroupPropertyWriter;
import org.utgenome.gwt.utgb.client.track.TrackWindow;
import org.utgenome.gwt.utgb.client.util.xml.XMLAttribute;
import org.utgenome.gwt.utgb.client.util.xml.XMLUtil;
import org.utgenome.gwt.utgb.client.util.xml.XMLWriter;

/**
 * {@link TrackGroupPropertyImpl} is a set of (key, value) pairs shared within a {@link TrackGroup} Instead of directly
 * access {@link TrackGroupPropertyImpl}, you must read and write values through the {@link TrackGroupProperty} and
 * {@link TrackGroupPropertyWriter}, respectively. This indirection is necessary for broadcasting
 * {@link Track#onChangeTrackGroupProperty(TrackGroupPropertyChange)} and {@link Track#onChangeTrackWindow(TrackWindow)}
 * events to all tracks in a group.
 * 
 * @author leo
 * 
 */
public class TrackGroupPropertyImpl implements TrackGroupProperty, TrackGroupPropertyWriter {
	private final TrackGroup _trackGroup;
	private TrackWindow _trackWindow = new TrackWindowImpl(700, 1, 100);
	private HashMap<String, String> _properties = new HashMap<String, String>();
	private ArrayList<TrackGroupPropertyChangeListener> _changeListener = new ArrayList<TrackGroupPropertyChangeListener>();
	private boolean enableNotifiaction = true;

	public TrackGroupPropertyImpl(TrackGroup trackGroup) {
		this._trackGroup = trackGroup;
	}

	public void setProperyChangeNotifaction(boolean enable) {
		this.enableNotifiaction = enable;
	}

	public String getProperty(String key) {
		Object value = _properties.get(key);
		if (value != null)
			return value.toString();
		else
			return null;
	}

	public String getProperty(String key, String defaultValue) {
		Object value = _properties.get(key);
		return (value != null) ? value.toString() : defaultValue;
	}

	public TrackWindow getTrackWindow() {
		return _trackWindow;
	}

	public void clear() {
		_properties.clear();
		_changeListener.clear();
	}

	/**
	 * Notify the change to the track group and listeners
	 * 
	 * @param change
	 */
	protected void notifyTheChange(TrackGroupPropertyChange change, TrackWindow newWindow) {
		if (!enableNotifiaction)
			return;

		_trackGroup.setResizeNotification(false);

		_trackGroup.broadcastChange(change, newWindow);
		for (TrackGroupPropertyChangeListener listener : _changeListener) {
			listener.onChange(change, newWindow);
		}

		_trackGroup.setResizeNotification(true);
		_trackGroup.notifyResize();
	}

	public void setProperty(String key, String value) {
		_properties.put(key, value);

		// notify the change
		notifyTheChange(new TrackPropertyChangeImpl(this, key), null);
	}

	public void setProperty(Map<String, String> property) {
		if (property == null || property.size() == 0)
			return;
		_properties.putAll(property);
		notifyTheChange(new TrackPropertyChangeImpl(this, property.keySet()), null);
	}

	public void setProperty(Map<String, String> properties, TrackWindow newWindow) {

		boolean notifyPropChange = false;
		if (properties != null && properties.size() > 0) {
			_properties.putAll(properties);
			notifyPropChange = true;
		}

		_trackWindow = newWindow;
		notifyTheChange(notifyPropChange ? new TrackPropertyChangeImpl(this, properties.keySet()) : null, newWindow);
	}

	public void setTrackWindow(int startOnGenome, int endOnGenome) {
		if (_trackWindow != null)
			_trackWindow = _trackWindow.newWindow(startOnGenome, endOnGenome);
		else
			_trackWindow = new TrackWindowImpl(700, startOnGenome, endOnGenome);
		notifyTheChange(null, _trackWindow);
	}

	public void setTrackWindowSize(int windowWidth) {
		_trackWindow = _trackWindow.newPixelWidthWindow(windowWidth);
		notifyTheChange(null, _trackWindow);
	}

	public Set<String> keySet() {
		return _properties.keySet();
	}

	public void setTrackWindow(TrackWindow newWindow) {
		if (newWindow == null)
			throw new NullPointerException("window is null");
		_trackWindow = newWindow;
		notifyTheChange(null, _trackWindow);
	}

	public void apply(TrackGroupPropertyChange change) {
		HashMap<String, String> map = new HashMap<String, String>();
		for (Iterator<String> it = change.changedKeySet().iterator(); it.hasNext();) {
			String key = it.next();
			String value = change.getProperty(key);
			map.put(key, value);
		}
		this.setProperty(map);
	}

	public void addTrackGroupPropertyChangeListener(TrackGroupPropertyChangeListener listener) {
		_changeListener.add(listener);
	}

	public void removeTrackGroupPropertyChangeListener(TrackGroupPropertyChangeListener listener) {
		_changeListener.remove(listener);
	}

	public void toXML(XMLWriter xmlWriter) {
		xmlWriter.start("groupProperties");
		XMLUtil.toXML(_properties, xmlWriter);
		toXML(_trackWindow, xmlWriter);
		xmlWriter.end(); // group-properties
	}

	public static XMLWriter toXML(TrackWindow w, XMLWriter writer) {
		writer.element("trackWindow", new XMLAttribute().add("start", w.getStartOnGenome()).add("end", w.getEndOnGenome()).add("width", w.getPixelWidth()));
		return writer;
	}

	public void scrollTrackWindow(double scrollPercentage) {
		_trackGroup.broadCastScrollTrackWindow(scrollPercentage);
	}

	public void scaleDownTrackWindow() {
		_trackGroup.broadCastWindowSizeChange(-1);
	}

	public void scaleUpTrackWindow() {
		_trackGroup.broadCastWindowSizeChange(1);
	}

}
