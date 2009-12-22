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
// UTGB Common Project
//
// FASTATest.java
// Since: Jun 4, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.format.fasta;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.utgenome.UTGBException;
import org.xerial.util.FileResource;

public class FASTATest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	
	@Test
	public void fasta() throws IOException, UTGBException
	{
		URL url = FileResource.find(FASTATest.class, "sample.fasta");
		FASTA f = new FASTA(url.openStream());
		
		ArrayList<FASTASequence> list = f.getSequenceList();
		assertEquals(2, list.size());
		
		FASTASequence s1 = list.get(0);
		assertEquals(2, s1.getDescriptionSize());
		assertEquals("sample data", s1.getDescription(0));
		assertEquals("9123", s1.getDescription(1));
		assertEquals("ACTGDTGCCGGTAA", s1.getSequence());
		
		FASTASequence s2 = list.get(1);
		assertEquals(2, s2.getDescriptionSize());
		assertEquals("second data", s2.getDescription(0));
		assertEquals("1", s2.getDescription(1));
		assertEquals("ACCCGG", s2.getSequence());

				
	}
}




