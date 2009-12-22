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
// utgb-core Project
//
// FileResourceTest.java
// Since: 2007/11/24
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.util;

import java.util.List;

import org.junit.Test;
import org.xerial.util.FileResource;
import org.xerial.util.ResourceFilter;
import org.xerial.util.io.VirtualFile;
import org.xerial.util.log.Logger;

public class FileResourceTest
{

    private static Logger _logger = Logger.getLogger(FileResourceTest.class);

    // @Test
    // public void findContextXMLTemplate()
    // {
    // URL templateURL = FileResource.find(UTGBServer.class,
    // "context.xml.template");
    // assertNotNull(templateURL);
    // }
    //	
    @Test
    public void findActionClasses()
    {
        List<VirtualFile> fileList = FileResource.listResources(new ResourceFilter() {
            public boolean accept(String resourcePath)
            {
                return (resourcePath.endsWith(".class") || resourcePath.endsWith("java"))
                        && !resourcePath.contains("$");
            }
        });

        _logger.debug("class files: " + fileList.size());
        for (VirtualFile vf : fileList)
        {
            _logger.trace("resource: " + vf.getURL());
        }
    }

}
