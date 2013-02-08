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
// BugReport Project
//
// Relation.java
// Since: 2007/03/28
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.db;

import java.util.ArrayList;
import java.util.List;

import org.utgenome.gwt.utgb.client.UTGBClientException;
import org.utgenome.gwt.utgb.client.db.datatype.BooleanType;
import org.utgenome.gwt.utgb.client.db.datatype.DataType;
import org.utgenome.gwt.utgb.client.db.datatype.DoubleType;
import org.utgenome.gwt.utgb.client.db.datatype.IntegerType;
import org.utgenome.gwt.utgb.client.db.datatype.PasswordType;
import org.utgenome.gwt.utgb.client.db.datatype.StringType;
import org.utgenome.gwt.utgb.client.db.datatype.TextType;
import org.utgenome.gwt.utgb.client.util.JSONUtil;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * A Relation holds one or more DataTypes and denotes their relationships. For
 * example, in relational databases, a table has a structure, e.g. (pid:integer,
 * name:string, address:string)
 * 
 * You can construct this structure as follows: <code>
 * Relation r = new Relation();
 * r.add("pid", new IntegerType());
 * r.add("name", new StringType());
 * r.add("address", new StringType());
 * </code>
 * 
 * JSON format example. <code>
 * {"relation":[["id", "integer"], ["name", "string"], ...]}
 * </code>
 * 
 * 
 * @author leo
 * 
 */
public class Relation implements IsSerializable
{
    /**
     * The following line specifies the content of the List (dataType). This
     * information is requried in the GWT compiler.
     * 
     */
    private List<DataType> dataTypeList = new ArrayList<DataType>();

    public Relation()
    {}

    public Relation(String jsonStr) throws UTGBClientException
    {
        JSONValue json = JSONParser.parse(jsonStr);
        parse(json.isObject());
    }

    public Relation(JSONObject jsonObj) throws UTGBClientException
    {
        parse(jsonObj);
    }

    private void parse(JSONObject jsonObj) throws UTGBClientException
    {
        if (jsonObj == null)
            throw new UTGBClientException("null json object");

        if (!jsonObj.containsKey("relation"))
            throw new UTGBClientException("no relation key found");
        JSONArray relationList = jsonObj.get("relation").isArray();
        for (int i = 0; i < relationList.size(); i++)
        {
            JSONArray dataTypePair = relationList.get(i).isArray();
            if (dataTypePair == null || dataTypePair.size() != 2)
                throw new UTGBClientException("data type must be json array with two elements: "
                        + relationList.toString());

            String parameterName = JSONUtil.toStringValue(dataTypePair.get(0));
            String typeName = JSONUtil.toStringValue(dataTypePair.get(1));

            add(getDataType(parameterName, typeName));
        }
    }

    public static DataType getDataType(String parameterName, String typeName) throws UTGBClientException
    {
        if (typeName.equals("boolean"))
            return new BooleanType(parameterName);
        else if (typeName.equals("double"))
            return new DoubleType(parameterName);
        else if (typeName.equals("string"))
            return new StringType(parameterName);
        else if (typeName.equals("password"))
            return new PasswordType(parameterName);
        else if (typeName.equals("text"))
            return new TextType(parameterName);
        else if (typeName.equals("integer"))
            return new IntegerType(parameterName);
        else
            throw new UTGBClientException("unknown data type: " + typeName);
    }

    public void add(DataType dataType)
    {
        dataTypeList.add(dataType);
    }

    public DataType getDataType(int index)
    {
        return (DataType) dataTypeList.get(index);
    }

    public List<DataType> getDataTypeList()
    {
        return dataTypeList;
    }

    public String toString()
    {
        StringBuffer s = new StringBuffer();
        s.append("(");
        for (DataType dt : dataTypeList)
        {
            s.append(dt.toString());
            s.append(" ");
        }
        s.append(")");
        return s.toString();
    }

}
