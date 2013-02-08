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
// ServletUtil.java
// Since: Oct 2, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.server.util;

import javax.servlet.http.HttpServletRequest;

/**
 * Utilities to handle servlet context
 * @author leo
 *
 */
public class ServletUtil
{
    /**
     * Gets the http request parameter
     * @param request the servlet request
     * @param key the parameter key
     * @param defalutValue the default value to be returned when the requested parameter is not found
     * @return the parameter value of the specified key. 
     */
    public static String getParameter(HttpServletRequest request, String key, String defalutValue)
    {
        String value = request.getParameter(key);
        return value != null ? value : defalutValue;
    }
}




