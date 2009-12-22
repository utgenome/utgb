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
// OldDescriptionXML.java
// Since: Oct 10, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.format.legacy;

public class OldDescriptionXML
{
    private String name = "";
    private String optattr = "";
    private AcceptSpecies acceptSpecies = new AcceptSpecies();
    
    public OldDescriptionXML()
    {}
    
    public void setAccept_species(AcceptSpecies acceptSpecies)
    {
        this.acceptSpecies = acceptSpecies;
    }
    
    public AcceptSpecies getAccept_species()
    {
        return acceptSpecies;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getOptattr()
    {
        return optattr;
    }

    public void setOptattr(String optattr)
    {
        this.optattr = optattr;
    }
    
}




