/*--------------------------------------------------------------------------
 *  Copyright 2011 utgenome.org
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
// TrackDisplayTest.java
// Since: 2011/02/28
//
//--------------------------------------
package org.utgenome.gwt.utgb.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.utgenome.gwt.utgb.client.view.TrackDisplay;
import org.utgenome.gwt.utgb.client.view.TrackDisplay.DB;
import org.utgenome.gwt.utgb.client.view.TrackDisplay.Track;
import org.xerial.lens.SilkLens;
import org.xerial.util.FileResource;

public class TrackDisplayTest {

	@Test
	public void loadSilk() throws Exception {

		TrackDisplay display = SilkLens.loadSilk(TrackDisplay.class, FileResource.find(TrackDisplayTest.class, "display.silk"));

		assertNotNull(display.track);
		assertEquals(2, display.track.size());
		Track t = display.track.get(0);
		assertEquals("Reference Sequence", t.name);
		DB db = t.db;
		assertEquals("db/screenshot/hg19.fa", db.path);

		t = display.track.get(1);
		db = t.db;
		assertEquals("db/screenshot/sample.%chr.bam", db.path);
	}

}
