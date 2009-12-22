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
// DescriptionXMLReader.java
// Since: Oct 10, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.format.legacy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.List;

import org.utgenome.gwt.utgb.client.track.bean.LegacyTrackInformation;
import org.xerial.core.XerialException;
import org.xerial.db.DBException;
import org.xerial.db.sql.sqlite.SQLiteAccess;
import org.xerial.util.bean.BeanUtil;

public class OldDescriptionXMLReader
{
    public static void main(String[] args)
    {
        try
        {
            SQLiteAccess sqlite = new SQLiteAccess("resource/legacy-track.db");
            
            List<LegacyTrackInformation> trackInfoList = sqlite.query("select * from tracks", LegacyTrackInformation.class);
            for(LegacyTrackInformation lt : trackInfoList)
            {
                boolean isValid = validate(lt);
                if(!isValid)
                {
                    System.out.println(lt.getName() + ": " + lt.getDescriptionXML() + " is not valid");
                }
                else
                {
                    System.out.println(lt.getName() + ": " + lt.getDescriptionXML() + " is valid");

                }
            }
        }
        catch (DBException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
    public static boolean speciesIsValid(String species, LegacyTrackInformation lt)
    {
        if(species.equals("any"))
            return true;
        else
            return species.equals(lt.getSpecies());
    }
    
    public static boolean revisioIsValid(String revision, LegacyTrackInformation lt)
    {
        if(revision.equals("any"))
            return true;
        else
            return revision.equals(lt.getRevision());
    }
    
    public static boolean validate(LegacyTrackInformation lt)
    {

        try
        {
            //System.out.println(lt.getName() + ": " + lt.getDescriptionXML());

            URL descriptionXMLURL = new URL(lt.getDescriptionXML().trim());
            Reader xmlReader = new BufferedReader(new InputStreamReader(descriptionXMLURL.openStream()));
            OldDescriptionXML descriptionXMLData = BeanUtil.createXMLBean(OldDescriptionXML.class, xmlReader);
            
            for(SpeciesEntry se: descriptionXMLData.getAccept_species().getEntry())
            {
                if(speciesIsValid(se.getSpecies(), lt) && revisioIsValid(se.getRevision(), lt))
                    return true;
            }
        }
        catch (XerialException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }
}




