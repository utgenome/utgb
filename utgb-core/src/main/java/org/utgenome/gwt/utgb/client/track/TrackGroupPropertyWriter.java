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
// TrackGroupPropertyWriter.java
// Since: Jun 12, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track;

import java.util.Map;

/**
 * {@link TrackGroupPropertyWriter} is an interface to write {@link TrackGroupProperty} values. After calling methods in
 * {@link TrackGroupPropertyWriter}, it is assumed that every track managed by a {@link TrackGroup} recieves a
 * {@link Track#onChangeTrackGroupProperty(TrackGroupPropertyChange)} or {@link Track#onChangeTrackWindow(TrackWindow)}
 * event.
 * 
 * @author leo
 * 
 */
public interface TrackGroupPropertyWriter {
	/**
	 * set a genome location displayed in the current TrackWindowImpl
	 * 
	 * @param startOnGenome
	 * @param endOnGenome
	 */
	public void setTrackWindow(long startOnGenome, long endOnGenome);

	/**
	 * @param windowSize
	 */
	public void setTrackWindowSize(int windowSize);

	public void setTrackWindow(TrackWindow newWindow);

	public void setProperyChangeNotifaction(boolean enable);

	public void scrollTrackWindow(double scrollPercentage);

	public void scaleUpTrackWindow();

	public void scaleDownTrackWindow();

	/**
	 * Set a property (key, value)
	 * 
	 * @param key
	 * @param value
	 */
	public void setProperty(String key, String value);

	/**
	 * Set a set of properties specified in the given Map of properties.
	 * 
	 * This method is useful when you have to set multiple property values at the same time before invoking
	 * onChangeTrackProperty events.
	 * 
	 * @param property
	 */
	public void setProperty(Map<String, String> property);

	public void setProperty(Map<String, String> properties, TrackWindow newWindow);

	/**
	 * Apply the property changes to this writer
	 * 
	 * @param change
	 * @param target
	 */
	public void apply(TrackGroupPropertyChange change);
}
