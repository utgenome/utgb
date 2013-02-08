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
// UTGBMedaka Project
//
// Result.java
// Since: Aug 10, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.bean;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Result implements IsSerializable {
	private int id = -1;
	private String species;
	private String revision;
	private String target;
	private int start;
	private int end;

	/**
	 */
	private ArrayList<String> keywords = new ArrayList<String>();

	/**
	 * 
	 */
	public Result() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getTarget() {
		return target;
	}

	public void setTarget(String scaffold) {
		this.target = scaffold;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int startpos) {
		this.start = startpos;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int endpos) {
		this.end = endpos;
	}

	public ArrayList<String> getKeywordList() {
		return keywords;
	}

	public void addKeyword(String keyword) {
		keywords.add(keyword);
	}

	public void addName(String keyword) {
		keywords.add(keyword);
	}

	public void clearKeyword() {
		keywords.clear();
	}

}
