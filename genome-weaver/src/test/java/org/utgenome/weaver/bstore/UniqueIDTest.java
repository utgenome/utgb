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
// UniqueIDTest.java
// Since: Apr 19, 2010
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.weaver.bstore;

import static org.junit.Assert.*;

import org.junit.Test;
import org.xerial.lens.Lens;
import org.xerial.util.log.Logger;

public class UniqueIDTest
{
    private static Logger _logger = Logger.getLogger(UniqueIDTest.class);

    @Test
    public void str() throws Exception {
        Repository r = new Repository("my repo", "Taro L. Saito <leo@xerial.org>");
        UniqueID u = UniqueID.createID(Lens.toSilk(r));

        assertEquals(UniqueID.ID_PREFIX_LENGTH, u.getPrefix().length() / 2);
        assertEquals(UniqueID.ID_LENGTH, u.getFullID().length() / 2);
        assertTrue(u.getFullID().startsWith(u.getPrefix()));

        UniqueID u2 = UniqueID.createID(Lens.toSilk(r));
        assertEquals(u.getPrefix(), u2.getPrefix());
        assertEquals(u.getFullID(), u2.getFullID());
    }
}
