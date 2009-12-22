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
// AnnotationTrack.java
// Since: Jun 15, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.lib;

import org.utgenome.gwt.utgb.client.track.Track;
import org.utgenome.gwt.utgb.client.track.TrackBase;

import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.Widget;

/**
 * A simple text editor
 * 
 * @author leo
 *
 */
public class AnnotationTrack extends TrackBase
{
    private final DockPanel _panel = new DockPanel();
    private final RichTextArea _textEditor = new RichTextArea();

    public static TrackFactory factory()
    {
        return new TrackFactory()
        {
            public Track newInstance()
            {
                return new AnnotationTrack();
            }
        };
    }
    
    public AnnotationTrack()
    {
        super("Annotation");

        _textEditor.setSize("100%", "50px");
        _panel.add(_textEditor, DockPanel.CENTER);
    }

    public Widget getWidget()
    {
        return _panel;
    }

    public int getDefaultWindowHeight()
    {
        return 100;
    }
    
}

