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
// RevisionSelectTrack.java
// Since: Jun 13, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.lib;

import org.utgenome.gwt.utgb.client.track.Track;
import org.utgenome.gwt.utgb.client.track.TrackGroupPropertyChange;
import org.utgenome.gwt.utgb.client.track.UTGBProperty;

/**
 * A track for selecting a genome sequence revision
 * 
 * @author leo
 *
 */
public class RevisionSelectTrack extends ValueSelectorTrack
{
    public static TrackFactory factory()
    {
        return new TrackFactory()
        {
            public Track newInstance()
            {
                return new RevisionSelectTrack();
            }
        };
    }
    
    
    public RevisionSelectTrack()
    {
        super("Revision", UTGBProperty.REVISION);
        
    }

    public void onChangeTrackGroupProperty(TrackGroupPropertyChange change)
    {
        if(change.contains(UTGBProperty.SPECIES))
        {
        	setRevision(change.getProperty(UTGBProperty.SPECIES));
        	refresh();
        	updateSelection();
        }
        
        super.onChangeTrackGroupProperty(change);
    }
    
    private void update()
    {
    	setRevision(getTrackGroup().getPropertyReader().getProperty(UTGBProperty.SPECIES));
    }

	public void draw() {
		update();
		super.draw();
	}


	private void setRevision(String species)
    {
    	if(species==null)
    		return;
    	if(species.equals("human"))
    	{
    		clearValues();
            addValue("hg17");
            addValue("hg18");
    	}
    	else if(species.equals("drosophila"))
    	{
    		clearValues();
    		addValue("R4.1");
    		addValue("R5.1");
    	}
    	else if(species.equals("monosigaovata"))
    	{
    		clearValues();
    		addValue("build2");
    	}
    	else if(species.equals("medaka"))
    	{
    		clearValues();
    		addValue("version1.0");
    		addValue("version0.9");
    		addValue("200506");
    		addValue("200406");
    		
    	}
    	
    }
     

}

