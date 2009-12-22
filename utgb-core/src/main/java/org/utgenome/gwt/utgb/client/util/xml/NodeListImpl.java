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
// GenomeBrowser Project
//
// NodeListImpl.java
// Since: 2007/06/14
//
// $URL$ 
// $Author$ ssksn
//--------------------------------------
package org.utgenome.gwt.utgb.client.util.xml;

import java.util.ArrayList;

import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

/**
 * @author ssksn
 *
 */
public class NodeListImpl implements NodeList {

    private final ArrayList<Node> list = new ArrayList<Node>();
    
    // @see com.google.gwt.xml.client.NodeList#getLength()
    public int getLength()
    {
        return list.size();
    }

    // @see com.google.gwt.xml.client.NodeList#item(int)
    public Node item(int index)
    {
        return (list.get(index));
    }

    public void add(final Node node) {
        list.add(node);
    }
}




