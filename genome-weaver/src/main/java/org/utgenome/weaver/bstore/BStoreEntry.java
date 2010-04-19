/*--------------------------------------------------------------------------
 *  Copyright 2010 utgenome.org
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
// genome-weaver Project
//
// BStoreEntry.java
// Since: Apr 19, 2010
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.weaver.bstore;

import java.util.Date;

/**
 * B-Store's entry information
 * 
 * @author leo
 * 
 */
public class BStoreEntry
{
    public final String path;
    public final String user;
    public final Date   date;

    private BStoreEntry(String path, String user) {
        this.path = path;
        this.user = user;
        this.date = new Date();
    }

    private BStoreEntry(String path, String user, Date date) {
        this.path = path;
        this.user = user;
        this.date = date;
    }

    public static BStoreEntry createNewFile(String path, String user) {
        return new BStoreEntry(path, user);
    }

}
