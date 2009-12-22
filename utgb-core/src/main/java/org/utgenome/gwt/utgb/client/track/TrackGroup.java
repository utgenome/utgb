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
// TrackGroup.java
// Since: Jun 18, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.utgenome.gwt.utgb.client.track.impl.TrackGroupPropertyImpl;
import org.utgenome.gwt.utgb.client.track.lib.NavigatorTrack;
import org.utgenome.gwt.utgb.client.util.Properties;
import org.utgenome.gwt.utgb.client.util.xml.XMLAttribute;
import org.utgenome.gwt.utgb.client.util.xml.XMLUtil;
import org.utgenome.gwt.utgb.client.util.xml.XMLWriter;

interface HasFactory {
	public static abstract class TrackGroupFactory {
		/**
		 * obtain a new factory instance.
		 * 
		 * @return new Factory instance.
		 */
		public abstract TrackGroup newInstance();

		/**
		 * set a property.
		 * 
		 * @param key
		 *            property name to be set.
		 * @param value
		 *            property value to be set.
		 */
		public void setProperty(final String key, final String value) {
		}

		/**
		 * get a property value.
		 * 
		 * @param key
		 *            property name you want to know.
		 * @return property value.
		 */
		public String getProperty(final String key) {
			return null;
		}

		public void clear() {
		}
	}
}

/**
 * <p>
 * A {@link TrackGroup} manages a set of tracks. You can create a track group with several tracks by using
 * {@link #addTrack(Track)} method. Every {@link TrackGroup} can have several child {@link TrackGroup}s. For example,
 * when a {@link TrackGroup} A has a set of tracks [T1, T2, T3], and another TrackGroup B consists of [T4, T5],
 * {@link #addTrackGroup(TrackGroup)} to the {@link TrackGroup} A yields a nested track group: [T1, T2, T3, [T4, T5]].
 * </p>
 * 
 * <p>
 * Broadcasts to this track group are send to all tracks in this group, including T1, T2, T3 and the track group B([T4,
 * T5]). However, T4 and T5 do not recieve this broadcast message directly, since it is subsumed in
 * {@link TrackGroup#onParentTrackGroupPropertyChange(TrackGroupPropertyChange)} and
 * {@link TrackGroup#onParentTrackWindowChange(TrackWindow)} method in the track group B. So, whether bypassing
 * broadcast messages into child tracks or not depends on your {@link TrackGroup} implementation. In the default
 * implementation of {@link TrackGroup} (i.e. {@link TrackGroupBase}),
 * {@link TrackGroup#onParentTrackGroupPropertyChange(TrackGroupPropertyChange)} and
 * {@link TrackGroup#onParentTrackWindowChange(TrackWindow)} methods just ignore such broadcast messages from the parent
 * group.
 * 
 * In order to recieve broadcast messages from the parent group, override
 * {@link TrackGroup#onParentTrackGroupPropertyChange(TrackGroupPropertyChange)} or
 * {@link TrackGroup#onParentTrackWindowChange(TrackWindow)} methods as follows: <code>
 * 
 * public void onParentTrackGroupPropertyChange(TrackGroupPropertyChange change)
 * {
 *     change.apply(getPropertyWriter());
 * }
 * 
 * public void onParentTrackWindowChange(TrackWindow newWindow)
 * {
 *     getPropertyWriter().setTrackWindow(newWindow);
 * }
 * 
 * </code>
 * 
 * Changes to the {@link TrackGroupPropertyWriter} are automatically broadcasted to tracks in the group.
 * </p>
 * 
 * <p>
 * {@link TrackGroup} has another important role to localize broadcast message within a {@link TrackGroup}. For example,
 * a broadcast message within the {@link TrackGroup} B is send to the track T4 and T5 only; tracks T1, T2, and T3 do not
 * recieve this localized broadcast message.
 * </p>
 * 
 * <p>
 * When you want to use common variables shared within a track group, extend {@link TrackGroup} class and put these
 * variables as field parameters of this extention.
 * </p>
 * 
 * @author leo
 * 
 */
public class TrackGroup implements TrackEntry, Comparable<TrackGroup>, HasFactory {
	public static TrackGroupFactory factory() {
		return new TrackGroupFactory() {
			String name = "";

			public TrackGroup newInstance() {
				return new TrackGroup(name);
			}

			public void setProperty(String key, String value) {
				if (key.equals("name"))
					name = value;
			}
		};
	}

	protected ArrayList<Track> _trackList = new ArrayList<Track>();
	protected ArrayList<TrackGroup> _trackGroupList = new ArrayList<TrackGroup>();
	protected TrackGroupPropertyImpl _trackGroupProperty = new TrackGroupPropertyImpl(this);
	protected ArrayList<TrackUpdateListener> _trackEventListenerList = new ArrayList<TrackUpdateListener>();
	protected TrackLayoutManager _layoutManager;
	protected TrackGroup _parentTrackGroup = null;
	protected String _trackGroupName = "";
	protected boolean _notifyResize = true;

	public void clear() {
		// take a coy of the track list
		ArrayList<Track> trackList = new ArrayList<Track>();
		trackList.addAll(_trackList);
		for (Iterator<Track> it = trackList.iterator(); it.hasNext();)
			removeTrack(it.next());
		// take a copy of the track group list
		ArrayList<TrackGroup> trackGroupList = new ArrayList<TrackGroup>();
		trackGroupList.addAll(_trackGroupList);
		for (Iterator<TrackGroup> it = trackGroupList.iterator(); it.hasNext();)
			removeTrackGroup(it.next());
	}

	public TrackGroup(String trackGroupName) {
		_trackGroupName = trackGroupName;
	}

	/**
	 * Add a new track to this group.
	 * 
	 * @param track
	 * @return a frame for the inserted track
	 */
	public void addTrack(Track track) {
		addTrackInternal(track);
		for (Iterator<TrackUpdateListener> it = _trackEventListenerList.iterator(); it.hasNext();) {
			TrackUpdateListener listener = it.next();
			listener.onInsertTrack(track);
		}
		// notifyResize();
	}

	private void addTrackInternal(Track track) {
		_trackList.add(track);
		track.setTrackGroup(this);
	}

	/**
	 * Insert a new track before the specified index
	 * 
	 * @param track
	 * @param beforeIndex
	 */
	public void insertTrack(Track track, int beforeIndex) {
		addTrackInternal(track);
		for (Iterator<TrackUpdateListener> it = _trackEventListenerList.iterator(); it.hasNext();) {
			TrackUpdateListener listener = it.next();
			listener.onInsertTrack(track, beforeIndex);
		}
		// notifyResize();
	}

	/**
	 * Add a track group as a child of this track group. Broadcasts to the parent track group will be notified to its
	 * child track groups.
	 * 
	 * Note: {@link TrackUpdateListener}s added to the parent track group are propagated to the inserted trackGroup.
	 * 
	 * @param trackGroup
	 */
	public void addTrackGroup(TrackGroup trackGroup) {
		_trackGroupList.add(trackGroup);
		trackGroup.setParentTrackGroup(this);
		this.setResizeNotification(false);
		// notify to the listners
		for (Iterator<TrackUpdateListener> it = _trackEventListenerList.iterator(); it.hasNext();) {
			TrackUpdateListener listener = it.next();
			listener.onAddTrackGroup(trackGroup);
			// propagates the listners of this group
			// trackGroup.addTrackUpdateListener(listener);
		}
		this.setResizeNotification(true);
		notifyResize();
	}

	/**
	 * Broadcast the property change to all of the tracks in this track group.
	 * 
	 * @param change
	 *            changes of track properties
	 */
	public void broadcastChange(TrackGroupPropertyChange change, TrackWindow newWindow) {
		// broadcast for all tracks in this group
		for (Iterator<Track> it = _trackList.iterator(); it.hasNext();) {
			Track track = it.next();
			if (track.isInitialized()) {
				track.onChange(change, newWindow);
			}
		}
		// bypass the broadcast message to the other track groups
		for (Iterator<TrackGroup> it = _trackGroupList.iterator(); it.hasNext();) {
			TrackGroup group = it.next();
			group.onParentChange(change, newWindow);
		}

	}

	public void broadCastScrollTrackWindow(double scrollPercentage) {
		// bypass the broadcast message to the other track groups
		for (Iterator<TrackGroup> it = _trackGroupList.iterator(); it.hasNext();) {
			TrackGroup group = it.next();
			NavigatorTrack.scroll(group, scrollPercentage);
		}
	}

	public void broadCastWindowSizeChange(int scaleDiff) {
		// bypass the broadcast message to the other track groups
		for (Iterator<TrackGroup> it = _trackGroupList.iterator(); it.hasNext();) {
			TrackGroup group = it.next();
			NavigatorTrack.zoom(group, scaleDiff);
		}
	}

	/**
	 * Get the {@link TrackGroupProperty} of this track group
	 * 
	 * @return
	 */
	public TrackGroupProperty getPropertyReader() {
		return _trackGroupProperty;
	}

	public String getProperty(String key, String defaultValue) {
		return getPropertyReader().getProperty(key, defaultValue);
	}

	public String getProperty(String key) {
		return getPropertyReader().getProperty(key);
	}

	/**
	 * Get the {@link TrackGroupPropertyWriter} of this track group. Any modification via the setter methods in the
	 * {@link TrackGroupPropertyWriter} will be broadcasted to this group via
	 * {@link #onParentTrackGroupPropertyChange(TrackGroupProperty)}.
	 * 
	 * @return
	 */
	public TrackGroupPropertyWriter getPropertyWriter() {
		return _trackGroupProperty;
	}

	/**
	 * Get the list of track groups containd in this group, <strong>excluding</strong> track groups within the sub
	 * groups.
	 * 
	 * @return
	 */
	public List<TrackGroup> getTrackGroupList() {
		return _trackGroupList;
	}

	/**
	 * Get the list of tracks containd in this group, <strong>excluding</strong> tracks within the sub groups.
	 * 
	 * @return
	 */
	public List<Track> getTrackList() {
		return _trackList;
	}

	/**
	 * Get the list of tracks containd in this group, <strong>including </strong> tracks within the sub groups.
	 * 
	 * @return
	 */
	public List<Track> getAllTrackList() {
		ArrayList<Track> trackList = new ArrayList<Track>();
		trackList.addAll(_trackList);
		for (Iterator<TrackGroup> it = _trackGroupList.iterator(); it.hasNext();) {
			TrackGroup group = it.next();
			trackList.addAll(group.getTrackList());
		}
		return trackList;
	}

	/**
	 * Get the {@link TrackWindow} of this track group
	 * 
	 * @return
	 */
	public TrackWindow getTrackWindow() {
		return _trackGroupProperty.getTrackWindow();
	}

	public void onParentChange(TrackGroupPropertyChange change, TrackWindow newWindow) {
		//getPropertyWriter().setProperty(change, newWindow);
	}

	/**
	 * An event handler when the {@link TrackWindow} of this group has changed. Override this method to implement your
	 * own event handler for this track group.
	 * 
	 * @param newWindow
	 */
	public void onParentTrackWindowChange(TrackWindow newWindow) {
		// do nothing
	}

	/**
	 * An event handler when the {@link TrackGroupProperty} of the parent group has changed. Override this method to
	 * implement your own event handler for this track group.
	 * 
	 * @param change
	 */
	public void onParentTrackGroupPropertyChange(TrackGroupPropertyChange change) {
		// do nothing
	}

	/**
	 * Redraw all child tracks, including tracks within the sub groups.
	 */
	public void redrawAll() {
		// draw all tracks in this group
		for (Iterator<Track> it = _trackList.iterator(); it.hasNext();) {
			Track track = it.next();
			track.refresh();
		}
		// draw other track groups
		for (Iterator<TrackGroup> it = _trackGroupList.iterator(); it.hasNext();) {
			TrackGroup group = it.next();
			group.redrawAll();
		}
	}

	/**
	 * Remove the specified track from this group.
	 * 
	 * @param track
	 */
	public void removeTrack(Track track) {
		_trackList.remove(track);
		for (Iterator<TrackUpdateListener> it = _trackEventListenerList.iterator(); it.hasNext();) {
			TrackUpdateListener listener = it.next();
			listener.onRemoveTrack(track);
		}
		// notifyResize();
	}

	/**
	 * Removed the specified track group and its belonging tracks from this group.
	 * 
	 * Note: {@link TrackUpdateListener}s propagated to the removed track group will be removed.
	 * 
	 * @param trackGroup
	 */
	public void removeTrackGroup(TrackGroup trackGroup) {
		_trackGroupList.remove(trackGroup);
		trackGroup.removeParentTrackGroup();
		this.setResizeNotification(false);
		for (Iterator<TrackUpdateListener> it = _trackEventListenerList.iterator(); it.hasNext();) {
			TrackUpdateListener listener = it.next();
			listener.onRemoveTrackGroup(trackGroup);
			// propagates the listners of this group
			// trackGroup.removeTrackUpdateListener(listener);
		}
		this.setResizeNotification(true);
		notifyResize();
	}

	/**
	 * Set the window location on the genome. The change will be reported to all of the tracks in this group via the
	 * {@link #onParentTrackWindowChange(TrackWindow)} method.
	 * 
	 * @param startOnGenome
	 * @param endOnGenome
	 */
	public void setTrackWindowLocation(long startOnGenome, long endOnGenome) {
		_trackGroupProperty.setTrackWindow(startOnGenome, endOnGenome);
	}

	/**
	 * Set the width of the track window of this group. The change will be notified to all of the tracks in this group
	 * via the {@link #onParentTrackWindowChange(TrackWindow)}.
	 * 
	 * @param windowWidth
	 */
	public void setTrackWindowWidth(int windowWidth) {
		_trackGroupProperty.setTrackWindowSize(windowWidth);
	}

	/**
	 * Add a {@link TrackUpdateListener}
	 * 
	 * @param listener
	 */
	public void addTrackUpdateListener(TrackUpdateListener listener) {
		_trackEventListenerList.add(listener);
		for (Iterator<TrackGroup> it = _trackGroupList.iterator(); it.hasNext();) {
			TrackGroup group = it.next();
			group.addTrackUpdateListener(listener);
		}
	}

	/**
	 * Remove the specified {@link TrackUpdateListener}
	 * 
	 * @param listener
	 */
	public void removeTrackUpdateListener(TrackUpdateListener listener) {
		_trackEventListenerList.remove(listener);
		for (Iterator<TrackGroup> it = _trackGroupList.iterator(); it.hasNext();) {
			TrackGroup group = it.next();
			group.removeTrackUpdateListener(listener);
		}
	}

	/**
	 * Return the index of the given track in the group.
	 * 
	 * @param track
	 * @return track index in the group
	 */
	public int getTrackIndex(Track track) {
		return _layoutManager.getTrackIndex(track);
	}

	/**
	 * Set the track window (window size, start on genome, end on genome) at once
	 * 
	 * @param newWindow
	 */
	public void setTrackWindow(TrackWindow newWindow) {
		_trackGroupProperty.setTrackWindow(newWindow);
	}

	/**
	 * Return the current parent trackgroup.
	 * 
	 * @return currentParentTrackGroup
	 */
	public TrackGroup getParentTrackGroup() {
		return _parentTrackGroup;
	}

	protected void setParentTrackGroup(TrackGroup parentTrackGroup) {
		this._parentTrackGroup = parentTrackGroup;
	}

	/**
	 * Notify the change of the inner track widget size to this group
	 */
	public void notifyResize() {
		if (!_notifyResize)
			return;
		for (Iterator<TrackUpdateListener> it = _trackEventListenerList.iterator(); it.hasNext();) {
			TrackUpdateListener listener = it.next();
			listener.onResizeTrack();
			listener.onResizeTrackWindow(_trackGroupProperty.getTrackWindow().getWindowWidth());
		}
	}

	/**
	 * enable/disable notification of track frame resizes
	 * 
	 * @param notify
	 *            true to enable, false to disable
	 */
	public void setResizeNotification(boolean enable) {
		_notifyResize = enable;
		// apply the same satting for the child track groups
		for (Iterator<TrackGroup> it = _trackGroupList.iterator(); it.hasNext();) {
			TrackGroup g = it.next();
			g.setResizeNotification(enable);
		}
	}

	/**
	 * @return get the total height of the track group widget
	 */
	public int getHeight() {
		int height = 0;
		for (Iterator<Track> it = _trackList.iterator(); it.hasNext();) {
			Track track = it.next();
			if (track.isInitialized()) {
				TrackFrame frame = track.getFrame();
				int frameHeight = frame.getOffsetHeight();
				if (frameHeight < TrackFrameState.DEFAULT_MIN_TRACKFRAME_HEIGHT)
					frameHeight = TrackFrameState.DEFAULT_MIN_TRACKFRAME_HEIGHT;
				height += frameHeight;
			}
		}
		for (Iterator<TrackGroup> it = _trackGroupList.iterator(); it.hasNext();) {
			TrackGroup group = it.next();
			height += group.getHeight();
		}
		return height;
	}

	public void setTrackLayoutManager(TrackLayoutManager layout) {
		_layoutManager = layout;
	}

	public String getTrackGroupName() {
		return _trackGroupName;
	}

	public void setTrackGroupName(String newTrackGroupName) {
		this._trackGroupName = newTrackGroupName;
	}

	protected void setTrackGroupProperty(final TrackGroupPropertyImpl trackGroupProperty) {
		if (trackGroupProperty != null)
			_trackGroupProperty = trackGroupProperty;
	}

	public String getName() {
		return _trackGroupName;
	}

	public boolean isTrack() {
		return false;
	}

	public boolean isTrackGroup() {
		return true;
	}

	/**
	 * Get the list of {@link TrackEntry}s, including both of {@link Track} and {@link TrackGroup}.
	 * 
	 * @return
	 */
	public List<TrackEntry> getTrackEntryList() {
		ArrayList<TrackEntry> list = new ArrayList<TrackEntry>();
		list.addAll(_trackList);
		list.addAll(_trackGroupList);
		return list;
	}

	/**
	 * Add a {@link TrackGroupPropertyChangeListener}
	 * 
	 * @param listener
	 */
	public void addTrackGroupPropertyChangeListener(TrackGroupPropertyChangeListener listener) {
		_trackGroupProperty.addTrackGroupPropertyChangeListener(listener);
	}

	/**
	 * Remove the specified {@link TrackGroupPropertyChangeListener}
	 * 
	 * @param listener
	 */
	public void removeTrackGroupPropertyChangeListener(TrackGroupPropertyChangeListener listener) {
		_trackGroupProperty.removeTrackGroupPropertyChangeListener(listener);
	}

	/**
	 * Get the root {@link TrackGroup}
	 * 
	 * @return
	 */
	public TrackGroup getRootTrackGroup() {
		TrackGroup rootGroup = null;
		TrackGroup groupCursor = this;
		while (groupCursor != null) {
			rootGroup = groupCursor;
			groupCursor = groupCursor.getParentTrackGroup();
		}
		return rootGroup;
	}

	protected String getClassName() {
		return this.getClass().getName();
	}

	protected void removeParentTrackGroup() {
		setParentTrackGroup(null);
	}

	/**
	 * 
	 * @see Comparable#compareTo(Object)
	 * @return
	 */
	public int compareTo(TrackGroup o) {
		if (o instanceof TrackGroup) {
			final TrackGroup _o = (TrackGroup) o;
			return getTrackGroupName().compareTo(_o.getTrackGroupName());
		}
		else if (o instanceof Track) {
			return 1;
		}
		else {
			throw new ClassCastException("The specified object is neither TrackGroup nor Track");
		}
	}

	protected void setTrackGroupProperty(final TrackGroupProperty trackGroupProperty) {
	}

	/**
	 * Output the state of this track group into {@link XMLWriter}
	 * 
	 * @param xmlWriter
	 */
	public void toXML(XMLWriter xmlWriter) {
		xmlWriter.start("trackGroup", new XMLAttribute("className", getClassName()).add("name", _trackGroupName));
		Properties trackGroupInternalProperties = new Properties();
		storeInternalProperties(trackGroupInternalProperties);
		XMLUtil.toXML(trackGroupInternalProperties, xmlWriter);
		getPropertyReader().toXML(xmlWriter);
		final List<TrackGroup> trackGroupList = getTrackGroupList();
		for (int i = 0; i < trackGroupList.size(); i++) {
			final TrackGroup trackGroup = (trackGroupList.get(i));
			trackGroup.toXML(xmlWriter);
		}
		final List<Track> trackList = getTrackList();
		for (int i = 0; i < trackList.size(); i++) {
			final Track track = (trackList.get(i));
			track.toXML(xmlWriter);
		}
		xmlWriter.end(); // track-group
	}

	/**
	 * Save (store) the internal properties of this track group into the given {@link Properties}
	 * 
	 * @param saveData
	 */
	protected void storeInternalProperties(Properties saveData) {
		saveData.add("name", _trackGroupName);
	}

}
