/*--------------------------------------------------------------------------
 *  Copyright 2008 utgenome.org
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
// TrackTest.java
// Since: Feb 6, 2008
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.utgenome.gwt.utgb.client.bean.track.Content;
import org.utgenome.gwt.utgb.client.bean.track.Frame;
import org.utgenome.gwt.utgb.client.bean.track.HiddenParameter;
import org.utgenome.gwt.utgb.client.bean.track.Layer;
import org.utgenome.gwt.utgb.client.bean.track.Option;
import org.utgenome.gwt.utgb.client.bean.track.Parameter;
import org.utgenome.gwt.utgb.client.bean.track.Species;
import org.utgenome.gwt.utgb.client.bean.track.TrackDescription;
import org.xerial.util.FileResource;
import org.xerial.util.bean.BeanUtil;

public class TrackDescriptionTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void beanTest() throws Exception {
		// create a Track instance from the XML file
		TrackDescription trackDescription = BeanUtil.createXMLBean(TrackDescription.class, FileResource.open(TrackDescriptionTest.class, "sampletrack.xml"));
		assertEquals("1.0", trackDescription.getRevision());
		assertEquals("org.utgenome.medaka", trackDescription.getGroup());
		assertEquals("1.1", trackDescription.getVersion());
		assertEquals("QualityValue", trackDescription.getName());
		assertTrue(trackDescription.getDescription().length() > 0);
		assertTrue(trackDescription.getDescription().contains("This track presents the quality"));

		assertEquals("http://trac.utgenome.org/project/UTGB/wiki/UTGBMedaka/Track#QualityValueTrack", trackDescription.getWebsite());
		assertEquals(1, trackDescription.getAuthorList().size());
		assertEquals("Taro L. Saito", String.class.cast(trackDescription.getAuthorList().get(0)));

		Content content = trackDescription.getContent();
		assertNotNull(content);
		assertEquals("utgb", content.getType());

		assertEquals(2, content.getSpeciesList().size());
		Species species1 = Species.class.cast(content.getSpeciesList().get(0));
		assertEquals("medaka", species1.getName());
		assertEquals("hg17", species1.getRevision());
		Species species2 = Species.class.cast(content.getSpeciesList().get(1));
		assertEquals("medaka", species2.getName());
		assertEquals("hg18", species2.getRevision());

		{
			assertEquals(1, content.getHiddenParameterList().size());
			HiddenParameter hidden = HiddenParameter.class.cast(content.getHiddenParameterList().get(0));
		}

		{
			assertEquals(3, content.getParameterList().size());
			Parameter p1 = Parameter.class.cast(content.getParameterList().get(0));
			assertEquals("revision", p1.getName());
			assertEquals("Revision", p1.getDisplayName());
			assertEquals(true, p1.isRequired());
			assertEquals("string", p1.getType()); // default

			Parameter p2 = Parameter.class.cast(content.getParameterList().get(1));
			assertEquals("start", p2.getName());
			assertEquals("Start", p2.getDisplayName());
			assertEquals(true, p2.isRequired());
			assertEquals("integer", p2.getType());

			Parameter p3 = Parameter.class.cast(content.getParameterList().get(2));
			assertEquals("end", p3.getName());
			assertEquals("End", p3.getDisplayName());
			assertEquals(true, p3.isRequired());
			assertEquals("integer", p3.getType());
		}

		assertEquals(5, content.getLayerList().size());
		{
			Layer l1 = Layer.class.cast(content.getLayerList().get(0));
			assertEquals("graphic", l1.getType());
			assertEquals("QV", l1.getName());
			assertEquals("http://somewhere.org/graphic.cgi", l1.getBaseURL());
			Frame f1 = l1.getFrame();
			assertEquals("horizontal", f1.getStyle());
			assertEquals(150, f1.getHeight());
			assertEquals(false, f1.isAutoAdjust());

			assertEquals(5, l1.getParamList().size());
			{
				Parameter p = Parameter.class.cast(l1.getParamList().get(0));
				assertEquals("width", p.getName());
				assertEquals("Track Width", p.getDisplayName());
				assertEquals("integer", p.getType());
				assertEquals("800", p.getDefault());
			}
			{
				Parameter p = Parameter.class.cast(l1.getParamList().get(1));
				assertEquals("color", p.getName());
				assertEquals("Graph Color", p.getDisplayName());
				assertEquals("list", p.getStyle());
				assertEquals("red", p.getDefault());

				assertEquals(3, p.getOptionList().size());
				{
					Option opt = Option.class.cast(p.getOptionList().get(0));
					assertEquals("Red", opt.getDisplayName());
					assertEquals("red", opt.getValue());
				}
				{
					Option opt = Option.class.cast(p.getOptionList().get(1));
					assertEquals("Green", opt.getDisplayName());
					assertEquals("green", opt.getValue());
				}
				{
					Option opt = Option.class.cast(p.getOptionList().get(2));
					assertEquals("Blue", opt.getDisplayName());
					assertEquals("blue", opt.getValue());
				}
			}
			{
				Parameter p = Parameter.class.cast(l1.getParamList().get(2));
				assertEquals("view", p.getName());
				assertEquals("Display Style", p.getDisplayName());
				assertEquals("radio", p.getStyle());
				assertEquals("full", p.getDefault());

				assertEquals(3, p.getOptionList().size());
				{
					Option opt = Option.class.cast(p.getOptionList().get(0));
					assertEquals("full", opt.getDisplayName());
					assertEquals("full", opt.getValue());
				}
				{
					Option opt = Option.class.cast(p.getOptionList().get(1));
					assertEquals("pack", opt.getDisplayName());
					assertEquals("pack", opt.getValue());
				}
				{
					Option opt = Option.class.cast(p.getOptionList().get(2));
					assertEquals("dense", opt.getDisplayName());
					assertEquals("dense", opt.getValue());
				}
			}
		}

	}

}
