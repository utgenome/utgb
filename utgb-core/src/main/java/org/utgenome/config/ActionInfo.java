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
// utgb-core Project
//
// ActionInfo.java
// Since: Jan 9, 2008
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.config;

/**
 * action tag in track-config.xml
 * 
 * @author leo
 * 
 */
public class ActionInfo
{
    private String prefix = "";
    private String basePackage = null;

    public ActionInfo()
    {}

    public String getPrefix()
    {
        return prefix;
    }

    public void setPrefix(String prefix)
    {
        this.prefix = prefix;
    }

    public String getBasePackage()
    {
        return basePackage;
    }

    public void setBasePackage(String basePackage)
    {
        this.basePackage = basePackage;
    }

}
