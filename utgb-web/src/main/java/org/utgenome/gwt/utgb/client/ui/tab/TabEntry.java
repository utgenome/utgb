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
// Aqua Project
//
// TabEntry.java
// Since: 2007/03/24
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.ui.tab;

import com.google.gwt.user.client.ui.Composite;

/**
 * A TabEntry is a single panel of the TabViewer. They are meant to be lazily
 * instantiated so that the viewer doesn't load all of the tabbed panels.
 * 
 * Each TabEntry is expected to have a static <code>factory()</code> method that
 * will be called by the TabViewer on startup.
 * 
 * <code>createInstance()</code> must be defined in eath TabEntryFactory.
 * 
 * Example: <code>
 * clsss MyTab extends TabEntry
 * {
 *    pubic static TabEntryFactory factory()
 *    {
 *        return new TabEntryFactory("my tab", "a description of the tab") {
 * 		 	 public TabEntry createInstance() {
 * 			 	return new MyTab();
 * 			 }
 * 		  };
 *    }
 * }
 * </code>
 * 
 * @author leo
 * 
 */
public abstract class TabEntry extends Composite implements TabEventListener
{

    /**
     * Encapsulated information about a TabEntry.
     * 
     * @author leo
     */
    public abstract static class TabEntryFactory
    {
        private TabEntry _instance = null;

        private String _tabName, _description;

        public TabEntryFactory(String name, String description)
        {
            _tabName = name;
            _description = description;
        }

        public String getDescription()
        {
            return _description;
        }

        public String getTabName()
        {
            return _tabName;
        }

        public abstract TabEntry createInstance();

        public boolean isInstanciated()
        {
            return _instance != null;
        }

        public final TabEntry getInstance()
        {
            if (_instance != null)
                return _instance;

            return (_instance = createInstance());
        }
    }

    public void onFocus()
    {
    // do nothing
    }

    public void onLostFocus()
    {
    // do nothing
    }

}
