/*--------------------------------------------------------------------------
 *  Copyright 2008 utgenome.org
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
// utgb-shell Project
//
// Read.java
// Since: Apr 10, 2009
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.format.silk.read;

import java.util.ArrayList;
import java.util.Properties;

public class Read
{
    // -read(name, view_start, view_end, start, end, strand, sequence, QV*, _[json])|
    public String name;
    public long viewstart;
    public long viewend;
    public long start;
    public long end;
    public String strand;
    public String sequence;
    public ArrayList<Integer> qv = new ArrayList<Integer>();
    private Properties prop = new Properties();

    @Override
    public String toString()
    {
        return String.format("name:%s, start:%s, end:%s, strand:%s, sequence:%s", name, start, end, strand, sequence);
    }

    public void put(String key, String value)
    {
        prop.put(key, value);
    }
}
