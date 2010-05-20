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
// BEDTrack.java
// Since: May 20, 2010
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.format.bed;

import java.io.Serializable;

/**
 * BEDTrack represents a track line in the BED file.
 * 
 * @author leo
 * 
 */
public class BEDTrack implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String name;
	public String description;
	public int visibility;
	public String color;
	public String itemRgb;
	public int useScore;
	public String group;
	public String priority;
	public String db;
	public long offset;
	public String url;
	public String htmlUrl;

	@Override
	public String toString() {
		return String
				.format(
						"track:name=%s, description=%s, visibility=%d, color=%s, itemRgb=%s, useScore=%d, group=%s, priority=%s, db=%s, offset=%d, url=%s, htmlUrl=%s\n",
						name, description, visibility, color, itemRgb, useScore, group, priority, db, offset, url, htmlUrl);
	}
}
