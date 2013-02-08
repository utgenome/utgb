/*--------------------------------------------------------------------------
 *  Copyright 2010 utgenome.org
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
// ReadList.java
// Since: 2010/09/02
//
//--------------------------------------
package org.utgenome.gwt.utgb.client.bio;

import java.util.ArrayList;
import java.util.List;

/**
 * ReadList is a list of read objects, and used to draw a sequence of blocks on genome.
 * 
 * @author leo
 * 
 */
public class ReadList extends Interval {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ArrayList<GenomeRange> read = new ArrayList<GenomeRange>();

	private String name;

	public ReadList() {
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addRead(GenomeRange read) {
		this.read.add(read);
	}

	public List<GenomeRange> getRead() {
		return this.read;
	}

	@Override
	public void accept(GenomeRangeVisitor visitor) {
		visitor.visitReadList(this);
	}
}
