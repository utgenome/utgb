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
// BStoreConfig.java
// Since: Apr 19, 2010
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.weaver.bstore;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * B-store configuration
 * 
 * @author leo
 * 
 */
public class BStoreConfig {

    /**
     * Search paths for storage
     * 
     * <ol>
     * <li>{CURRENT_DIR}/.utgb/b-store
     * <li>$HOME/.utgb/b-store
     * <li>(remote repository URLs specified in the config file)
     * </ol>
     * 
     * @author leo
     * 
     */
    public static class LocalRepo {
        public String path = defaultPath();

        private static String defaultPath() {
            return new File(System.getProperty("user.home"), ".utgb/b-store").getPath();
        }
    }

    public static class RemoteRepo {
        public String url;
    }

    public String version = "1.0";
    public String user;
    public LocalRepo localRepository = new LocalRepo();
    public List<RemoteRepo> remoteRepository = new ArrayList<RemoteRepo>();

}
