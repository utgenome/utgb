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
// DatabaseCatalog.java
// Since: Jun 15, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.db;

import java.util.ArrayList;

import org.utgenome.gwt.utgb.client.UTGBClientException;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

/**
 * 
 * read a JSON description of database catalog, a list of database relations.
 * 
 * JSON format example (json object consisting of pairs of table_name:relation)
 * <code>
 * { 
 *   "table_1":{"relation":[["id", "integer"], ["name", "string"], ...]}, 
 *   "table_2":{"relation":[["p_id", "integer"], ["phone", "string"], ...]}
 * }
 * </code>
 * 
 * @author leo
 * 
 */
public class DatabaseCatalog
{

    /**
     * "table_1", "table_2"
     */
    private ArrayList<String> _tableNameList = new ArrayList<String>();
    /**
     * a list of relations, in the order of "table_1", "table_2", ...
     */
    private ArrayList<Relation> _relationList = new ArrayList<Relation>();

    public DatabaseCatalog(String jsonData) throws UTGBClientException
    {
        load(jsonData);
    }

    public DatabaseCatalog()
    {

    }

    /**
     * parsing the given JSONData representing database schema
     * 
     * @param jsonData
     * @throws UTGBClientException
     */
    public void load(String jsonData) throws UTGBClientException
    {
        _relationList.clear();
        _tableNameList.clear();

        JSONValue json = JSONParser.parse(jsonData);
        JSONObject jsonObj = json.isObject();
        if (jsonObj == null)
            throw new UTGBClientException("invalid json data:" + jsonData);

        // read relations
        for (String tableName : jsonObj.keySet())
        {
            JSONValue relationValue = jsonObj.get(tableName);
            Relation r = new Relation(relationValue.isObject());

            // update
            _tableNameList.add(tableName);
            _relationList.add(r);
        }
    }

    public ArrayList<String> getTableNameList()
    {
        return _tableNameList;
    }

    public ArrayList<Relation> getRelationList()
    {
        return _relationList;
    }

    public Relation getRelation(String tableName)
    {
        for (int i = 0; i < _tableNameList.size(); i++)
        {
            String t = (String) _tableNameList.get(i);
            if (t.equals(tableName))
                return (Relation) _relationList.get(i);
        }
        return null;
    }
}
