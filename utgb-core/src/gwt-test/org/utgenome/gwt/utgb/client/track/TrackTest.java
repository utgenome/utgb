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
// TrackTest.java
// Since: Jul 18, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track;

import org.utgenome.gwt.utgb.client.track.lib.AnnotationTrack;

import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;

public class TrackTest extends GWTTestCase {

	public String getModuleName() {
		return "org.utgenome.gwt.utgb.UTGB";
	}

	protected void setUp() throws Exception {
	}

	protected void tearDown() throws Exception {
	}


	public void testLoad()
	{
		System.out.println(GWT.getTypeName(new AnnotationTrack()));
		
	}
	
}




