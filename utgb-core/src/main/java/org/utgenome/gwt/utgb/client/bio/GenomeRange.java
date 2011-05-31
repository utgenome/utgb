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
// OnGenome.java
// Since: May 16, 2010
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.bio;

import java.io.Serializable;

/**
 * A common interface for interval data mapped onto a genome sequence (e.g., Read, Gene, SAMRead, WigGraphData, etc.)
 * 
 * @author leo
 * 
 */
public interface GenomeRange extends Serializable {

	/**
	 * return 1-based start position of the data. The interval represents [start, end). ([inclusive, exclusive))
	 * 
	 * @return
	 */
	public int getStart();

	/**
	 * return 1-based end position of the data. The interval represents [start, end). ([inclusive, exclusive))
	 * 
	 * @return
	 */
	public int getEnd();

	public boolean isSense();

	public boolean isAntiSense();

	/**
	 * return the length of the data
	 * 
	 * @return
	 */
	public int length();

	public String getName();

	public void accept(GenomeRangeVisitor visitor);
}
