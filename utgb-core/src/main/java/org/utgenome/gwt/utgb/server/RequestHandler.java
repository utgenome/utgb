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
// RequestHandler.java
// Since: Oct 4, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.utgenome.UTGBException;

/**
 * An interface to handler web request
 * @author leo
 *
 */
public interface RequestHandler
{

    /**
     * Handles the HTTP request  
     * 
     * @param request the http servlet request
     * @param response the http servlet response
     */
    public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

    /**
     * Validate request parameters
     * @return true when request parameter values are valid, otherwise false 
     * @throws ServletException
     * @throws UTGBException
     * 
     */
    public void validate(HttpServletRequest request, HttpServletResponse response) throws ServletException, UTGBException;
}




