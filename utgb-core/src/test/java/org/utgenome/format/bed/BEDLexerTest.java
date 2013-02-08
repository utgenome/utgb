/*--------------------------------------------------------------------------
 *  Copyright 2009 utgenome.org
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
// BEDLexerTest.java
// Since: 2009/05/08
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.format.bed;

import org.junit.After;
import org.junit.Before;
import org.xerial.util.log.Logger;

import java.util.ArrayList;

public class BEDLexerTest
{

    private static Logger _logger = Logger.getLogger(BEDLexerTest.class);

    @Before
    public void setUp() throws Exception
    {}

    @After
    public void tearDown() throws Exception
    {}

    public static class BEDDescription
    {
        String name;
        ArrayList<BEDAttribute> attributes = new ArrayList<BEDAttribute>();

        public void setName(String name)
        {
            this.name = name;
        }

        public void addAttribute(BEDAttribute attribute)
        {
            attributes.add(attribute);
        }

        @Override
        public String toString()
        {
            return String.format("name=%s, attributes=%s", name, attributes.toString());
        }

    }

    public static class BEDAttribute
    {
        String name;
        String value;

        public void setName(String name)
        {
            this.name = name;
        }

        public void setValue(String value)
        {
            this.value = value;
        }

        @Override
        public String toString()
        {
            return String.format("{name=%s, value=%s}", name, value);
        }

    }



}
