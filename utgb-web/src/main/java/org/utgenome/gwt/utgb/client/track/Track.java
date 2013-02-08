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
// Track.java
// Since: Jun 5, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track;

import org.utgenome.gwt.utgb.client.track.lib.ToolBoxTrack;
import org.utgenome.gwt.utgb.client.util.CanonicalProperties;
import org.utgenome.gwt.utgb.client.util.Properties;
import org.utgenome.gwt.utgb.client.util.xml.XMLWriter;
import org.utgenome.gwt.utgb.client.view.TrackView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;

/**
 * Track is an interface for implementing your own track.
 * 
 * Rather than directly implements this interface, it might be better to start with extending {@link TrackBase} class,
 * which already implements several naive methods such as {@link #setTrackGroup(TrackGroup)} , {@link #getTrackGroup()},
 * etc.
 * 
 * 
 * <p>
 * Tips to implement your own track:
 * </p>
 * <ul>
 * <li>A {@link Track} must have a {@link Widget} that can be retrieved via {@link #getWidget()} method. This returned
 * widget is used to display your track.</li>
 * <li>Initialize your track in the {@link #setUp(TrackFrame, TrackGroup)} method, which is automatically called by the
 * track group to which your track belongs.</li>
 * <li>Use {@link #getFrame()}, to interact with {@link TrackFrame} surrounding your track.</li>
 * <li>Use {@link #getTrackGroup()} to retrieve shared properties (e.g., {@link TrackGroupProperty}, {@link TrackWindow}
 * , etc.) among tracks within the currnt track group.</li>
 * <li>Override {@link #onChangeTrackGroupProperty(TrackGroupPropertyChange)} and
 * {@link #onChangeTrackWindow(TrackWindow)} to recieve changes on properties of the track group. These methods become
 * active only after {@link #setUp(TrackFrame, TrackGroup)} is called.</li>
 * </ul>
 * 
 * <p>
 * The initialization process of tracks is as follows:
 * </p>
 * <ol>
 * <li>Create an instance of your track using a public consutructor of your track or {@link TrackFactory#newInstance()}
 * method.</li>
 * <li>Call the {@link TrackGroup#addTrack(Track)} method to add your tracks.</li>
 * <li>{@link TrackGroup} automatically calls your {@link Track#setUp(TrackFrame, TrackGroup)} method on appropricate
 * timing, that is, when {@link TrackFrame} and {@link TrackGroup} are ready.</li>
 * <li>After the setup, {@link #onChangeTrackGroupProperty(TrackGroupPropertyChange)} and
 * {@link #onChangeTrackWindow(TrackWindow)} are activated.</li>
 * <li>{@link Track#refresh()} is called in the {@link TrackQueue}</li>
 * </ol>
 * 
 * @author leo
 * 
 */
public interface Track extends TrackEntry, TrackGroupPropertyChangeListener {
	/**
	 * TrackFactory helps to create new Track instance, which can be used to defer its instantiation.
	 * 
	 * Since GWT has no {@link Class.newInstance} method with configurable parameters, you have to implement your own
	 * factory() method for each track to support deferred instantiation.
	 * 
	 * <p>
	 * Note that, {@link GWT#create(Class)} supports deferred instantiation, but you cannot configure any parameter
	 * values, when instanticate classes.
	 * </p>
	 * 
	 * <p>
	 * How to use:
	 * </p>
	 * <code> 
	 * class YourOwnTrack extends TrackBase 
	 * {
	 *     public static abstract TrackFactory factory() 
	 *     {
	 *        return new TrackFactory() {
	 *            Map properties = new HashMap();
	 *        
	 *            public Track newInstance() {]
	 *                Track track = new YourOwnTrack();
	 *                
	 *                // You should set properties to a Track instance.
	 *                // for example
	 *                // 
	 *                // Set propertySet = properties.entrySet();
	 *                // Iterator it = propertySet.iterator();
	 *                // while ( it.hasNext() ) {
	 *                //     Map.Entry entry = (Map.Entry)(it.next());
	 *                //     String key      = entry.getKey();
	 *                //     String value    = entry.getValue();
	 *                //
	 *                //     track.setProperty(key, value);  // YourOwnTrack#setProperty(String, String)
	 *                // }
	 *                
	 *                return track;
	 *            }
	 *            
	 *            public void setProperty(String key, String value) {
	 *                properties.put(key, value);
	 *            }
	 *        }
	 *     }
	 * }
	 * 
	 * </code>
	 * 
	 * See also the usage examles in {@link ToolBoxTrack}.
	 * 
	 * @author leo
	 * 
	 */
	public static abstract class TrackFactory {
		/**
		 * obtain a new factory instance.
		 * 
		 * @return new Factory instance.
		 */
		public abstract Track newInstance();

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

	/**
	 * This method is invoked when {@link TrackFrame} and {@link TrackGroup} for this track become ready, that is, the
	 * track is drawable. Override this method to write initialization codes for your tracks.
	 * 
	 * @param tracKFrame
	 *            the {@link TrackFrame} of this track
	 * @param group
	 *            the {@link TrackGroup} of this track
	 */
	public void setUp(TrackFrame trackFrame, TrackGroup group);

	/**
	 * @return true if {@link #setUp(TrackFrame, TrackGroup)} has already done.
	 */
	public boolean isInitialized();

	/**
	 * @return the information of the track
	 */
	public TrackInfo getTrackInfo();

	/**
	 * draw the track widget
	 */
	public void draw();

	/**
	 * draw and resize the frame
	 */
	public void refresh();

	/**
	 * @return the track widget
	 */
	public Widget getWidget();

	/**
	 * 
	 * @return default height of the track window
	 */
	public int getDefaultWindowHeight();

	/**
	 * 
	 * @return minimum height of the track window
	 */
	public int getMinimumWindowHeight();

	/**
	 * An event handler when some cofigurations of the track changes
	 * 
	 * @param config
	 */
	public void onChangeTrackConfig(TrackConfigChange change);

	/**
	 * set a mediator of this track
	 * 
	 * @param trackGroup
	 *            mediator of this track
	 */
	public void setTrackGroup(TrackGroup trackGroup);

	/**
	 * @return the mediator of this track
	 */
	public TrackGroup getTrackGroup();

	public String getTrackGroupProperty(String key, String defaultValue);

	public String getTrackGroupProperty(String key);

	public void setTrackGroupProperty(String key, String value);

	/**
	 * Set the frame that wraps this track
	 * 
	 * @param frame
	 *            a frame for this track
	 */
	public void setFrame(TrackFrame frame);

	/**
	 * Get the frame of this track
	 * 
	 * @return the frame of this track
	 */
	public TrackFrame getFrame();

	/**
	 * Get the {@link TrackConfig} panel. Override this method to return your own configuration panel
	 * 
	 * @return
	 */
	public TrackConfig getConfig();

	public void setConfig(TrackConfig config);

	public String toXML();

	public void toXML(XMLWriter xmlWriter);

	/**
	 * load the track parameters from the view definition
	 * 
	 * @param view
	 */
	public void loadView(TrackView.Track view);

	/**
	 * Override this method to save internal state of this track into a {@link Properties} object
	 * 
	 * @param xmlWriter
	 */
	public void saveProperties(CanonicalProperties saveData);

	/**
	 * Override this method to restore internal state of this track
	 * 
	 * @param properties
	 */
	public void restoreProperties(CanonicalProperties properties);

	/**
	 * An event handler when some properties shared within the track group has changed
	 * 
	 * @param change
	 */
	public void onChangeTrackGroupProperty(TrackGroupPropertyChange change);

	public void onChangeTrackHeight(int newHeight);

	/**
	 * An event handler that will be invoked before the track window location change
	 * 
	 * @param newWindow
	 */
	public void beforeChangeTrackWindow(TrackWindow newWindow);

	/**
	 * An event handler when the track window location has changed
	 * 
	 * @param newWindow
	 */
	public void onChangeTrackWindow(TrackWindow newWindow);

	// @see org.utgenome.gwt.utgb.client.track.TrackGroupPropertyChangeListener#onChange(org.utgenome.gwt.utgb.client.track.TrackGroupPropertyChange, org.utgenome.gwt.utgb.client.track.TrackWindow)
	public void onChange(TrackGroupPropertyChange change, TrackWindow newWindow);

	/**
	 * Report an error
	 * 
	 * @param string
	 */
	public void error(String string);

}
