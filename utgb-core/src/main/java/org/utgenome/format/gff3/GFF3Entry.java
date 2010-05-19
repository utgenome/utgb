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
// GFF3Entry.java
// Since: Jul 7, 2008
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.format.gff3;

import java.util.HashMap;

/**
 * GFF3 entry
 * 
 * @author leo
 * 
 */
public class GFF3Entry {

	public String seqId;
	public String soruce;
	public String type;
	public long start;
	public long end;
	public double score;
	public String strand;
	public String phase;
	public HashMap<String, String> attributes = new HashMap<String, String>();

}
