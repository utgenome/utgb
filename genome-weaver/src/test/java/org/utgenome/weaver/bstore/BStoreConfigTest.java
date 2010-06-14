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
// BStoreConfigTest.java
// Since: Apr 19, 2010
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.weaver.bstore;

import org.junit.Test;
import org.xerial.lens.Lens;
import org.xerial.util.log.Logger;

public class BStoreConfigTest {
    private static Logger _logger = Logger.getLogger(BStoreConfigTest.class);

    @Test
    public void config() throws Exception {

        BStoreConfig config = new BStoreConfig();
        _logger.info(Lens.toSilk(config));
    }
}
