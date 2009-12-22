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
// ConfigInputForm.java
// Since: 2007/07/17
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.lib.old.datatype;

import org.utgenome.gwt.utgb.client.db.datatype.InputForm;

import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

public abstract class ConfigInputForm extends InputForm
{

    protected ScrollPanel scrollPanel = new ScrollPanel();

    public ConfigInputForm()
    {
        scrollPanel.setStyleName("form-frame");
        initWidget(scrollPanel);
    }

    protected ScrollPanel getScrollPanel()
    {
        return scrollPanel;
    }

    public void addWidget(final Widget widget)
    {
        scrollPanel.add(widget);
    }

    protected void onAttach()
    {
        super.onAttach();
        final int height = scrollPanel.getOffsetHeight();

        scrollPanel.setHeight((height > 30 ? height : 30) + "px");
    }

}
