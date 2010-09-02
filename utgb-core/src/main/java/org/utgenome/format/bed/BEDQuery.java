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
// BEDQuery.java
// Since: May 20, 2010
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.format.bed;

/**
 * An interface for extracting BED entries using {@link BED2SilkReader}.
 * 
 * <h3>usage:</h3>
 * 
 * <pre>
 *  BEDQuery q = new BEDQuery() { 
 *   public void addTrack(BEDTrack track) {
 *      // called when a track entry is found 
 *    } 
 *   public void addGene(BEDGene gene) {
 *      // called when a gene entry is found    
 *    } 
 *  };
 *  Lens.loadSilk(q, new BufferedReader(new FileReader(...)));
 * </pre>
 * 
 * @author leo
 * 
 */
public interface BEDQuery {

	public void addTrack(BEDTrack track);

	public void addGene(BEDEntry gene);

	public void reportError(Exception e);
}
