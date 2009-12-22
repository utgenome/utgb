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
// LocusBindingTest.java
// Since: May 7, 2009
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.server.bio;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.utgenome.gwt.utgb.client.bio.Gene;
import org.xerial.silk.SilkUtil;
import org.xerial.util.FileResource;
import org.xerial.util.log.Logger;

public class LocusBindingTest {

	private static Logger _logger = Logger.getLogger(LocusBindingTest.class);

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	public static class LocusDataLoader {
		public void addCoordinate(CoordinateLoader coordinate) {
			_logger.info("coordinate: " + coordinate);
		}

	}

	public static class CoordinateLoader {

		String name;
		String species;
		String revision;
		String group;

		public void addLocus(Gene locus) {
			_logger.info(String.format("locus: %s, start=%s, end=%s, cds=%s", locus.getName(), locus.getStart(), locus.getEnd(), locus.getCDS().toString()));

		}

		public void setName(String name) {
			this.name = name;
		}

		public void setSpecies(String species) {
			this.species = species;
		}

		public void setRevision(String revision) {
			this.revision = revision;
		}

		public void setGroup(String group) {
			this.group = group;
		}

		@Override
		public String toString() {
			return String.format("name=%s, species=%s, revision=%s, group=%s", name, species, revision, group);
		}

	}

	@Test
	public void loadLocusSilk() throws Exception {

		CoordinateLoader loader = SilkUtil.createBean(CoordinateLoader.class, FileResource.find(LocusBindingTest.class, "locus.silk"));
		_logger.info(loader.toString());

	}

}
