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
// UTGB Gallery Project
//
// LegacyTrackInfo.java
// Since: Apr 2, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.lib;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * LegacyTrackInfo contains all information (URL query values), which is necessary to produce a CGI request to track
 * image servers.
 * 
 * @author leo
 * 
 */
public class LegacyTrackInfo implements IsSerializable {
	String species = "human";
	String revision = "hg18";
	String name = "Legacy Track";
	String target = "chr1";
	String baseURL = "http://dev.utgenome.org/~utgb/human/genome_browser/tracks/easytrack/easygenetrack.php";
	String style;
	String indexBaseURL = null;

	long start;
	long end;
	int width = 800;
	int height;

	public LegacyTrackInfo() {
	}

	public LegacyTrackInfo(LegacyTrackInfo info) {
		this.species = info.species;
		this.revision = info.revision;
		this.name = info.name;
		this.target = info.target;
		this.baseURL = info.baseURL;
		this.style = info.style;
		this.indexBaseURL = info.indexBaseURL;
		this.start = info.start;
		this.end = info.end;
		this.width = info.width;
		this.height = info.height;
	}

	/**
	 * @param speciesInfo
	 * @param name
	 * @param chr
	 * @param baseURL
	 * @param start
	 * @param end
	 */
	public LegacyTrackInfo(String species, String revision, String name, String baseURL) {
		this.species = species;
		this.revision = revision;
		this.name = name;
		this.baseURL = baseURL;
	}

	public LegacyTrackInfo(String species, String revision, String name, String target, String baseURL, int start, int end, String indexBaseURL) {
		this.species = species;
		this.revision = revision;
		this.name = name;
		this.target = target;
		this.baseURL = baseURL;
		this.start = start;
		this.end = end;
		this.indexBaseURL = indexBaseURL;
	}

	public String getImageURL() {
		String imageURL = httpQueryString(baseURL);
		// GWT.log(imageURL, null);
		return imageURL;
	}

	public String getImageURL(int offset) {
		String imageURL = httpQueryString(baseURL, offset);
		// GWT.log(imageURL, null);
		return imageURL;
	}

	private String httpQueryString(String baseURL) {
		String queryPart = join(new String[] { "species", "revision", "target", "start", "end", "width" }, new String[] { species, revision, target,
				Long.toString(start), Long.toString(end), Integer.toString(width - LegacyTrack.INDEX_FRAME_WIDTH) });

		return concatinate(baseURL, queryPart);
	}

	private String concatinate(String baseURL, String queryPart) {
		if (baseURL.indexOf('?') > 0)
			return baseURL + "&" + queryPart;
		else
			return baseURL + "?" + queryPart;
	}

	private String httpQueryString(String baseURL, int offset) {
		return concatinate(baseURL, join(new String[] { "species", "revision", "target", "start", "end", "width" }, new String[] { species, revision, target,
				Long.toString(start + offset), Long.toString(end + offset), Integer.toString(width - LegacyTrack.INDEX_FRAME_WIDTH) }));
	}

	public String getIndexImageURL() {
		return httpQueryString(indexBaseURL);
	}

	public static String join(String[] args, String[] value) {
		String result = new String();
		for (int i = 0; i < args.length; i++) {
			result += args[i] + "=" + value[i];
			if (i != (args.length - 1))
				result += "&";
		}
		return result;
	}

	public String getBaseURL() {
		return baseURL;
	}

	public void setBaseURL(String baseURL) {
		this.baseURL = baseURL;
	}

	public long getEnd() {
		return end;
	}

	public void setEnd(long end) {
		this.end = end;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
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

	public String getRevision() {
		return revision;
	}

	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public String getIndexBaseURL() {
		return indexBaseURL;
	}

	public void setIndexBaseURL(String indexBaseURL) {
		this.indexBaseURL = indexBaseURL;
	}

	public boolean hasIndex() {
		return indexBaseURL != null;
	}

	public void setSpecies(String species) {
		this.species = species;
	}

	public void setRevision(String revision) {
		this.revision = revision;

	}

}
