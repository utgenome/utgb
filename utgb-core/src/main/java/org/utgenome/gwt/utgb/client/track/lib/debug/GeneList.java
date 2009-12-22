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
// GenomeBrowser Project
//
// GeneList.java
// Since: Jun 6, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.lib.debug;

import java.util.ArrayList;
import java.util.Iterator;

public class GeneList extends ArrayList<GeneData> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8230490455308026154L;
	private ArrayList<GeneListObserver> _geneListObserver = new ArrayList<GeneListObserver>();

	public void addObserver(GeneListObserver observer) {
		_geneListObserver.add(observer);
	}

	public boolean add(GeneData gene) {
		super.add(gene);
		for (Iterator<GeneListObserver> it = _geneListObserver.iterator(); it.hasNext();) {
			GeneListObserver observer = (GeneListObserver) it.next();
			observer.onNewGeneAdded(gene);
		}
		return true;
	}

	public void clear() {
		super.clear();
		for (Iterator<GeneListObserver> it = _geneListObserver.iterator(); it.hasNext();) {
			GeneListObserver observer = (GeneListObserver) it.next();
			observer.onGeneListIsCleared();
		}
	}
}
