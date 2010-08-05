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
// Coordinate.java
// Since: Jan 16, 2009
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.bio;

import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Coordinate system
 * 
 * @author leo
 * 
 */
public class Coordinate implements IsSerializable {

	/**
	 * Name of the group who defines genetic maps . e.g, utgb, ensembl, ucsc
	 */
	private String group = "utgb";

	/**
	 * Species name. e.g., human, medaka, etc.
	 */
	private String species;

	/**
	 * Reference genome sequence revision. e.g. hg18 (for human), version1.0 (for medaka)
	 */
	private String revision;
	/**
	 * name of the coordinate. e.g. chr1, scaffold10
	 */
	private String name;

	public Coordinate() {
	}

	public Coordinate(String group, String species, String revision, String name) {
		this.group = group;
		this.species = species;
		this.revision = revision;
		this.name = name;
	}

	public String getTrackURL(String baseURL, Map additionalProperties) {

		String url = getTrackURL(baseURL);
		StringBuilder buf = new StringBuilder();
		buf.append(url);
		for (Object eachKey : additionalProperties.keySet()) {
			buf.append("&");
			buf.append(eachKey.toString());
			buf.append("=");
			buf.append(additionalProperties.get(eachKey).toString());
		}
		return buf.toString();
	}

	public String getTrackURL(String baseURL) {

		if (baseURL.contains("%q"))
			baseURL = baseURL.replace("%q", "group=%group&species=%species&revision=%ref&name=%chr&start=%start&end=%end&width=%pixelwidth");

		baseURL = baseURL.replaceAll("%group", group);
		baseURL = baseURL.replaceAll("%species", species);
		baseURL = baseURL.replaceAll("%ref", revision);
		baseURL = baseURL.replaceAll("%chr", name);

		return baseURL.trim();

	}

	/**
	 * Create a new coordinate for UTGB
	 * 
	 * @param species
	 * @param revision
	 * @param coordinateName
	 * @return
	 */
	public static Coordinate newUTGBCoordinate(String species, String revision, String coordinateName) {
		return new Coordinate("utgb", species, revision, coordinateName);
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSpecies() {
		return species;
	}

	public void setSpecies(String species) {
		this.species = species;
	}

	public String getRevision() {
		return revision;
	}

	public void setRevision(String revision) {
		this.revision = revision;
	}

}
