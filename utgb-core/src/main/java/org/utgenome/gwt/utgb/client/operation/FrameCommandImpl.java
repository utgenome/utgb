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
// FrameCommandImpl.java
// Since: 2007/06/18
//
// $URL$ 
// $Author$ ssksn
//--------------------------------------
package org.utgenome.gwt.utgb.client.operation;

import org.utgenome.gwt.utgb.client.track.Track;

import com.google.gwt.xml.client.Node;

/**
 * @author ssksn
 *
 */
public class FrameCommandImpl implements FrameCommand
{
    public FrameCommandImpl(final Node frameCommandNode) {
        
    }
    
    public static FrameCommand newInstance(final Node frameCommandNode) {
        return null;
    }
    
    public void execute(Track track)
    {

    }
}




