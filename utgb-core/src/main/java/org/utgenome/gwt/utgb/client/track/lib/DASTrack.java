/*--------------------------------------------------------------------------
 *  Copyright 2009 utgenome.org
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
// GenomeTrack.java
// Since: Feb 17, 2009
//
// $URL: http://svn.utgenome.org/utgb/trunk/utgb/utgb-core/src/main/java/org/utgenome/gwt/utgb/client/track/lib/GenomeTrack.java $ 
// $Author: yoshimura $
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.lib;

import org.utgenome.gwt.utgb.client.bio.Coordinate;
import org.utgenome.gwt.utgb.client.db.datatype.StringType;
import org.utgenome.gwt.utgb.client.track.Track;
import org.utgenome.gwt.utgb.client.track.TrackConfigChange;
import org.utgenome.gwt.utgb.client.track.TrackFrame;
import org.utgenome.gwt.utgb.client.track.TrackGroup;
import org.utgenome.gwt.utgb.client.track.TrackWindow;
import org.utgenome.gwt.utgb.client.util.Properties;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Frame;

/**
 * DASTrack is for visualizing data that can be located on das data.
 * 
 * @author yoshimura
 * 
 */
public class DASTrack extends GenomeTrack
{

    private String dasBaseURL = null;
    private String dasType = null;

    public static TrackFactory factory()
    {
        return new TrackFactory() {
            public Track newInstance()
            {
                return new DASTrack();
            }
        };
    }

    private class ContentFrame extends Frame
    {
        @Override
        protected void onLoad()
        {}

        @Override
        public void onBrowserEvent(Event event)
        {

            if (event.getTypeInt() == Event.ONCHANGE)
            {
                TrackFrame frame = getFrame();
                if (frame != null)
                {
                    frame.loadingDone();
                }
            }
        }
    }

    public DASTrack()
    {
        super("DAS Track");
    }

    protected String getTrackURL()
    {
        Coordinate c = getCoordinate();

        Properties p = new Properties();
        TrackWindow w = getTrackGroup().getTrackWindow();
        p.add("start", w.getStartOnGenome());
        p.add("end", w.getEndOnGenome());
        p.add("width", w.getWindowWidth() - leftMargin);
        p.add("dasBaseURL", dasBaseURL);
        if (dasType != null)
        {
            p.add("dasType", dasType);
        }

        return c.getTrackURL(trackBaseURL, p);
    }

    @Override
    public void setUp(TrackFrame trackFrame, TrackGroup group)
    {

        super.setUp(trackFrame, group);

        config.addConfigParameter("DAS Base URL", new StringType("dasBaseURL"), dasBaseURL);
        config.addConfigParameter("DAS Data Type", new StringType("dasType"), dasType);
    }

    @Override
    public void onChangeTrackConfig(TrackConfigChange change)
    {
        super.onChangeTrackConfig(change);

        if (change.containsOneOf(new String[] { "dasBaseURL", "dasType" }))
        {
            if (change.contains("dasBaseURL"))
            {
                dasBaseURL = change.getValue("dasBaseURL");
            }
            if (change.contains("dasType"))
            {
                dasType = change.getValue("dasType");
            }
            draw();
        }
    }

    @Override
    public void saveProperties(Properties saveData)
    {
        super.saveProperties(saveData);
        saveData.add("dasBaseURL", dasBaseURL);
        if (dasType != null)
        {
            saveData.add("dasType", dasType);
        }
    }

    @Override
    public void restoreProperties(Properties properties)
    {
        super.restoreProperties(properties);
        dasBaseURL = properties.get("dasBaseURL", dasBaseURL);
        dasType = properties.get("dasType", dasType);
    }
}
