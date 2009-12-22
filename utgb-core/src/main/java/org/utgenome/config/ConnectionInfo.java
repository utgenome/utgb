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
// ConnectionInfo.java
// Since: Jan 9, 2008
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.config;

/**
 * The connection tag in track-config.xml
 * 
 * @author leo
 * 
 */
public class ConnectionInfo
{
    private String scope;
    private String dbms;
    private String address;
    private String user;
    private String pass;

    public ConnectionInfo()
    {}

    public String getScope()
    {
        return scope;
    }

    public void setScope(String scope)
    {
        this.scope = scope;
    }

    public String getDbms()
    {
        return dbms;
    }

    public void setDbms(String dbms)
    {
        this.dbms = dbms;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getUser()
    {
        return user;
    }

    public void setUser(String user)
    {
        this.user = user;
    }

    public String getPass()
    {
        return pass;
    }

    public void setPass(String pass)
    {
        this.pass = pass;
    }

}
