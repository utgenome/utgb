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
// TrackGroupWithGeneList.java
// Since: Jun 18, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.lib.debug;

import org.utgenome.gwt.utgb.client.track.TrackGroup;
import org.utgenome.gwt.utgb.client.track.TrackWindow;

public class SampleGeneTrackGroup extends TrackGroup
{
	public static TrackGroupFactory factory()
	{
		return new TrackGroupFactory() {
			public TrackGroup newInstance() {
				return new SampleGeneTrackGroup();
			}
		};
	}

	
    private final GeneList _geneList = new GeneList();
    
    public SampleGeneTrackGroup()
    {
    	super("Gene List Group");
        
        _geneList.add(new GeneData("sample gene", 1000, 2000));
        _geneList.add(new GeneData("sample gene2", 3000, 5000));
    }

    public void onParentTrackWindowChange(TrackWindow newWindow)
    {
        // propergate the parent window change
        getPropertyWriter().setTrackWindow(newWindow);
    }

    public GeneList getGeneList()
    {
    	return _geneList;
    }
}

