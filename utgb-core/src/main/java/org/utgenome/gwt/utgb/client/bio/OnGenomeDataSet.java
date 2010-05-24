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
// OnGenomeDataSet.java
// Since: May 16, 2010
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.bio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A set of on-genome data used for transferring genome data between server and client (GWT)
 * 
 * @author leo
 * 
 */
public class OnGenomeDataSet implements Serializable {

	private static final long serialVersionUID = 1L;

	public ChrLoc location;
	public List<OnGenome> read = new ArrayList<OnGenome>();

}
