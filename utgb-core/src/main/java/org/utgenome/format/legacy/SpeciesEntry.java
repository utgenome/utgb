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
// utgb-core Project
//
// SpeciesEntry.java
// Since: Oct 10, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.format.legacy;

public class SpeciesEntry
{
    private String species = "";
    private String revision = "";
    
    public SpeciesEntry()
    {}

    public String getSpecies()
    {
        return species;
    }

    public void setSpecies(String species)
    {
        this.species = species;
    }

    public String getRevision()
    {
        return revision;
    }

    public void setRevision(String revision)
    {
        this.revision = revision;
    }
    
    
    
}




