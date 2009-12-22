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
// UTGBMedaka Project
//
// AvailableTrackInfoTest.java
// Since: Aug 8, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.bean;

import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.utgenome.gwt.utgb.client.track.bean.TrackBean;
import org.xerial.json.InvalidJSONDataException;
import org.xerial.util.bean.BeanUtil;
import org.xerial.util.bean.InvalidBeanException;
import org.xerial.util.log.LogLevel;
import org.xerial.util.log.Logger;
import org.xerial.util.xml.InvalidXMLException;
import org.xerial.util.xml.XMLException;

public class TrackBeanTest
{
    static Logger _logger = Logger.getLogger(TrackBeanTest.class);
    
    @Before
    public void setUp() throws Exception
    {
        _logger.setLogLevel(LogLevel.DEBUG);
    }

    @After
    public void tearDown() throws Exception
    {}

    
    @Test
    public void json() throws InvalidBeanException, InvalidJSONDataException, InvalidXMLException
    {
        TrackBean trackInfo = new TrackBean("sample track", "org.utgenome.SampleTrack", 50, false, "description", "leo");
        trackInfo.putProperty("species", "human");
        trackInfo.putProperty("target", "scaffold1");
        String json = BeanUtil.toJSON(trackInfo);
        _logger.debug(json);
        
        TrackBean trackInfo2 = (TrackBean) BeanUtil.createBean(TrackBean.class, json);
        
        assertEquals(trackInfo.getName(), trackInfo2.getName());
        assertEquals(trackInfo.getClassName(), trackInfo2.getClassName());
        assertEquals(trackInfo.getPack(), trackInfo2.getPack());
        assertEquals(trackInfo.getProperty().size(), trackInfo2.getProperty().size());
        assertEquals(trackInfo.getProperty().get("species"), trackInfo2.getProperty().get("species"));
        assertEquals(trackInfo.getProperty().get("target"), trackInfo2.getProperty().get("target"));
        
    }
    
    @Test 
    public void xml() throws InvalidBeanException, XMLException 
    {
        TrackBean trackInfo = new TrackBean("sample track", "org.utgenome.SampleTrack", 50, false, "description", "leo");
        trackInfo.putProperty("species", "human");
        trackInfo.putProperty("target", "scaffold1");
        
        String xml = BeanUtil.toXML("track", trackInfo);
        _logger.debug(xml);
        
        TrackBean trackInfo2 = (TrackBean) BeanUtil.createXMLBean(TrackBean.class, xml);
        _logger.debug(BeanUtil.toXML("track", trackInfo2));
        
        assertEquals(trackInfo.getName(), trackInfo2.getName());
        assertEquals(trackInfo.getClassName(), trackInfo2.getClassName());
        assertEquals(trackInfo.getPack(), trackInfo2.getPack());
        assertEquals(trackInfo.getProperty().size(), trackInfo2.getProperty().size());
        assertEquals(trackInfo.getProperty().get("species"), trackInfo2.getProperty().get("species"));
        assertEquals(trackInfo.getProperty().get("target"), trackInfo2.getProperty().get("target"));

        
    }
}




