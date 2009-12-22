/*--------------------------------------------------------------------------
 *  Copyright 2008 utgenome.org
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
// SequenceInfo.java
// Since: May 11, 2008
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.bean;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Information of the genoem sequence (species name and sequence revision)
 * 
 * @author leo
 * 
 */
public class SequenceInfo {

	private String species = "";
	private ArrayList<String> revisionList = new ArrayList<String>();

	public SequenceInfo() {
	}

	public SequenceInfo(String species) {
		this.species = species;
	}

	public void addRevision(String revision) {
		this.revisionList.add(revision);

	}

	public String getSpecies() {
		return species;
	}

	public void setSpecies(String species) {
		this.species = species;
	}

	public ArrayList<String> getRevisionList() {
		return revisionList;
	}

	public String toJSON() {
		StringBuffer buf = new StringBuffer();
		buf.append("{\"species\":\"");
		buf.append(species);
		buf.append("\", \"revision\":[");
		int count = 0;
		for (Iterator<String> it = revisionList.iterator(); it.hasNext(); count++) {
			if (count > 0)
				buf.append(",");
			String revision = it.next();
			buf.append("\"");
			buf.append(revision);
			buf.append("\"");
		}
		buf.append("]}");
		return buf.toString();
	}

}
