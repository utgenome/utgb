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
// UGTB Project
// 
// TextContentFilter.java 
// Since: 2004/08/07
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package org.utgenome.gwt.utgb.server.util.xml;

/**
 * An interface to convert a text string 
 * @author leo
 *
 */
public interface TextContentFilter
{
    public String filter(String textContent);
}


