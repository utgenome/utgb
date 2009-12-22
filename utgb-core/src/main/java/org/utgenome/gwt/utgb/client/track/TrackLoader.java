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
// TrackLoader.java
// Since: 2007/07/18
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track;

import java.util.HashMap;
import java.util.Map;

import org.utgenome.gwt.utgb.client.track.HasFactory.TrackGroupFactory;
import org.utgenome.gwt.utgb.client.track.Track.TrackFactory;
import org.utgenome.gwt.utgb.client.track.bean.TrackBean;
import org.utgenome.gwt.utgb.client.track.impl.TrackWindowImpl;
import org.utgenome.gwt.utgb.client.util.Utilities;
import org.utgenome.gwt.utgb.client.util.xml.DOMUtil;

import com.google.gwt.core.client.GWT;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

public class TrackLoader {
 
	private TrackLoader() {}
	
	public static TrackGroup createTrackGroupFromXML(String xml)
	{
		final Document document = XMLParser.parse(xml);
		final Node topLevelNode = document.getFirstChild();
		return loadTrackGroupFromXML(topLevelNode);
	}
	
	
	public static TrackGroup loadTrackGroupFromXML(final Node trackGroupNode) {
		final String trackGroupClassName = Utilities.getAttributeValue(trackGroupNode, "className");
		
		try {
			final TrackGroupFactory trackGroupFactory = TrackFactoryHolder.getTrackGroupFactory(trackGroupClassName);
			if ( trackGroupFactory == null ) return null;
			trackGroupFactory.clear();
			{ // STEP 1: parsing TrackGroup object properties
				final NodeList propertiesNodeList = getPropertiesNodeList(trackGroupNode);
		        final int SIZE = propertiesNodeList.getLength();
		        for ( int i = 0; i < SIZE; i++ ) {
		            final Node propertyNode = propertiesNodeList.item(i);

		            final Node valueNode = propertyNode.getFirstChild();
		
		            final String key   = Utilities.getAttributeValue(propertyNode, "key");
		            final String value = valueNode != null ? valueNode.getNodeValue() : "";

		            trackGroupFactory.setProperty(key, value);
		        }
			}
			
			// STEP 2: construct TrackGroup instance
	        final TrackGroup trackGroup = trackGroupFactory.newInstance();

	        { // STEP 2-1: parsing TrackGroupProperty
	            final NodeList groupPropertiesNodeList = getGroupPropertiesNodeList(trackGroupNode);
	            
	            final Map<String, String> propertiesMap = new HashMap<String, String>();
	            
	            final int SIZE = groupPropertiesNodeList.getLength();
	            for ( int i = 0; i < SIZE; i++ ) {
	                final Node groupPropertyNode = groupPropertiesNodeList.item(i);
	                
	                final NodeList propertiesNodeList = getPropertiesNodeList(groupPropertyNode);
	                final int _SIZE = propertiesNodeList.getLength();
	                for ( int j = 0; j < _SIZE; j++ ) {
	                    final Node propertyNode = propertiesNodeList.item(j);
	                    
	                    final Node valueNode = propertyNode.getFirstChild();
	                    
	                    final String key   = Utilities.getAttributeValue(propertyNode, "key");
	                    final String value = valueNode.getNodeValue();
	                    
	                    propertiesMap.put(key, value);
	                }
	                
	                { // STEP 4-2: parsing TrackWindow
	                    final Node trackWindowNode = getTrackWindowNode(groupPropertyNode);
	                    if ( trackWindowNode != null ) {
	                        final int startIndex = Integer.parseInt(Utilities.getAttributeValue(trackWindowNode, "start"));
	                        final int endIndex   = Integer.parseInt(Utilities.getAttributeValue(trackWindowNode, "end"));
	                        final int width      = Integer.parseInt(Utilities.getAttributeValue(trackWindowNode, "width"));
	                        
	                        trackGroup.setTrackWindow(new TrackWindowImpl(width, startIndex, endIndex));
	                    }
	                }
	            }
	            
	            final TrackGroupPropertyWriter propertyWriter = trackGroup.getPropertyWriter();
	            propertyWriter.setProperty(propertiesMap);
	        }
	        
	        
	        { // STEP 3-1: parsing sub trackGroups
	        	final NodeList trackGroupNodeList = getTrackGroupNodeList(trackGroupNode);
	            final int SIZE = trackGroupNodeList.getLength();
	            for ( int i = 0; i < SIZE; i++ ) {
	                final Node _trackGroupNode = trackGroupNodeList.item(i);
	
	                final TrackGroup _trackGroup = loadTrackGroupFromXML(_trackGroupNode);
	                if ( _trackGroup != null ) trackGroup.addTrackGroup(_trackGroup);
	            }
	        }
	        { // STEP 3-2: parsing child tracks
	        	final NodeList trackNodeList = getTrackNodeList(trackGroupNode);
	            final int SIZE = trackNodeList.getLength();
	            for ( int i = 0; i < SIZE; i++ ) {
	                final Node _trackNode = trackNodeList.item(i);
	
	                final Track _track = loadTrackFromXML(_trackNode);
	                if ( _track != null ) trackGroup.addTrack(_track);
	            }
	        }

	        
			return trackGroup;
		} catch ( UnknownTrackException ute ) {
			GWT.log(ute.getMessage(), ute);
			return null;
		}
	}
	
	public static Track loadTrackFromXML(final Node trackNode) {
		final String trackClassName = DOMUtil.getAttributeValue(trackNode, "className");
		
		try {
			final TrackFactory trackFactory = TrackFactoryHolder.getTrackFactory(trackClassName);
			if ( trackFactory == null ) return null;
			trackFactory.clear();
			
	        Track track = trackFactory.newInstance();
	        track.loadXML(trackNode);
	        return track;
		} catch ( UnknownTrackException ute ) {
			GWT.log(ute.getMessage(), ute);
			return null;
		}
	}
	
	public static Track createTrack(TrackBean trackInfo)
	{
	    try
        {
            TrackFactory trackFactory = TrackFactoryHolder.getTrackFactory(trackInfo.getClassName());
            if(trackFactory == null)
                return null;
            Track track = trackFactory.newInstance();
            track.load(trackInfo);
            return track;
        }
        catch (UnknownTrackException e)
        {
            GWT.log(e.getMessage(), e);
            return null;
        }
	    
	}
	
	public static Node getTrackWindowNode(final Node trackGroupNode) {
		final NodeList childNodeList = trackGroupNode.getChildNodes();
        final int SIZE = childNodeList.getLength();
        for ( int i = 0; i < SIZE; i++ ) {
            final Node childNode = childNodeList.item(i);

            final short nodeType = childNode.getNodeType();
            if ( nodeType != Node.TEXT_NODE && childNode.getNodeName().equalsIgnoreCase("trackWindow") ) {
            	return childNode;
            }
        }
		return null;
	}
	
	public static NodeList getGroupPropertiesNodeList(final Node node) {
		return DOMUtil.getChildNodeList(node, "groupProperties");
	}
	
	public static NodeList getPropertiesNodeList(final Node node) {
		return DOMUtil.getChildNodeList(node, "property");
	}

	public static NodeList getTrackNodeList(final Node node) {
		return DOMUtil.getChildNodeList(node, "track");
	}

	public  static NodeList getTrackGroupNodeList(final Node node) {
		return DOMUtil.getChildNodeList(node, "trackGroup");
	}

}
