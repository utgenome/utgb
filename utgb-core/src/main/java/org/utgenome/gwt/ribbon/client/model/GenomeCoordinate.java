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
// GenomeCoordinate.java
// Since: 2010/01/26
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.ribbon.client.model;

import java.io.Serializable;

/**
 * Coordinate system
 * 
 * @author leo
 * 
 */
public class GenomeCoordinate implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * group name of this coordinate
	 */
	public String group = "utgb";
	/**
	 * sequence id of this coordinate. e.g., hg19, oryLat2, etc.
	 */
	public String sequence;

	/**
	 * chromosome/contig/scaffold name. e.g., chr1, contig1, scaffold1, etc.
	 */
	public String chr;

}
