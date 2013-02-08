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
// RequestHandlerTable.java
// Since: 2008/08/01
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.server;

import java.util.ArrayList;

/**
 * {@link RequestHandlerTable} holds a set of {@link RequestHandler}s and 
 * provides a search function to find a request handler that matches the given 
 * web request. 
 * 
 * When the request URL is "refseq/human/hg18/list.json", and 
 * there is an action handler, refseq.list, 
 * 
 * <li>actionHandlername = "refseq.list"
 * <li>actionSuffix = "json"
 * <li>actionPrefix = ""
 * <li>actionContext = "human/hg18"
 * 
 * @author leo
 *
 */
public class RequestHandlerTable {

	
	
	public RequestHandlerTable() {
	
	}

	/**
	 * 
	 * @param handlerName dot-separated handler name (e.g. admin.login, list, etc.). Dot is optional. 
	 * @param handlerClass 
	 */
	public void add(String handlerName, Class<RequestHandler> handlerClass)
	{
		ArrayList<String> list = new ArrayList<String>();
		list.add("");
		
	}
	
	public RequestHandler findHandler(String relativeActionURL)
	{
		return null;
	}
	
}




