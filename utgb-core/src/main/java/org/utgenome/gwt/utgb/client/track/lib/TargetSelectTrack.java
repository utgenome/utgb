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
// TargetSelectTrack.java
// Since: Jun 21, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.lib;

import org.utgenome.gwt.utgb.client.track.Track;
import org.utgenome.gwt.utgb.client.track.TrackGroupPropertyChange;
import org.utgenome.gwt.utgb.client.track.UTGBProperty;

public class TargetSelectTrack extends ValueSelectorTrack {

	public static TrackFactory factory() {
        return new TrackFactory()
        {
            public Track newInstance()
            {
                return new TargetSelectTrack();
            }
        };
    }
	    
	    
    public TargetSelectTrack()
    {
        super("Target", UTGBProperty.TARGET);
    }

    
    
    public void draw() {
    	update();
    	super.draw();
	}


	public void onChangeTrackGroupProperty(TrackGroupPropertyChange change)
    {
        if(change.contains(UTGBProperty.SPECIES))
        {
        	setTarget(change.getProperty(UTGBProperty.SPECIES));
        	refresh();
        	updateSelection();
        }
        
    	super.onChangeTrackGroupProperty(change);
    }
	
	private void update()
	{
		setTarget(getTrackGroup().getPropertyReader().getProperty(UTGBProperty.SPECIES)); 
	}

    private void setTarget(String species)
    {
    	if(species == null)
    		return;
    	if(species.equals("human"))
    	{
    		setTargetList(new String[] 
    		                         {
    				"chr1",
    				"chr2",
    				"chr3",
    				"chr4",
    				"chr5",
    				"chr6",
    				"chr7",
    				"chr8",
    				"chr9",
    				"chr10",
    				"chr11",
    				"chr12",
    				"chr13",
    				"chr14",
    				"chr15",
    				"chr16",
    				"chr17",
    				"chr18",
    				"chr19",
    				"chr20",
    				"chr21",
    				"chr22",
    				"chrX",
    				"chrY"
    		                         });
    	}
    	else if(species.equals("drosophila"))
    	{
    		setTargetList(new String[] {
    			"2L",
    			"2R",
    			"3L",
    			"3R",
    			"4",
    			"X",
    			"U"
    		});
    	}

    	
    }
    
    private void setTargetList(String[] target)
    {
    	clearValues();
    	for(int i=0; i<target.length; i++)
    		addValue(target[i]);
    }
     
}

