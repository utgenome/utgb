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
// SpeciesSelectTrack.java
// Since: Jun 13, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.lib;

import org.utgenome.gwt.utgb.client.track.Track;

/**
 * Species selection
 * @author leo
 *
 */
public class SpeciesSelectTrack extends ValueSelectorTrack
{
    public static final String SPECIES_PROPERTY_NAME = "species";

    public static TrackFactory factory() 
    {
        return new TrackFactory()
        {
            public Track newInstance()
            {
                return new SpeciesSelectTrack();
            }
        };
    }
    
    public SpeciesSelectTrack()
    {
        super("Species", "species");
        
        addValue("human");
        addValue("drosophila");
        addValue("medaka");
        addValue("monoshigeobata");
    }

}

