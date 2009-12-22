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
// LinkOperation.java
// Since: 2007/06/14
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.operation;

import org.utgenome.gwt.utgb.client.util.Utilities;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;

/**
 * @author ssksn
 *
 */
public class LinkOperation implements Operation
{
    private final String url;
    private final String target;
    
    public LinkOperation(final Node linkOperationNode) {
        this.url    = Utilities.getAttributeValue(linkOperationNode, "url");
        final String _target = Utilities.getAttributeValue(linkOperationNode, "target");
        if ( _target == null ) this.target = "";
        else this.target = _target;
    }
    
    public void execute(Widget sender, int x, int y)
    {
        Window.open(url, target, "");
    }

}




