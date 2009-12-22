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
// BED2SilkReaderTest.java
// Since: 2009/05/27
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.format.wig;

import java.io.File;
import java.io.FileReader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.utgenome.format.bed.BED2SilkReader;
import org.utgenome.format.wig.WIG2SilkReader;

public class WIG2SilkReaderTest
{

    @Before
    public void setUp() throws Exception
    {}

    @After
    public void tearDown() throws Exception
    {}

    @Test
    public void testGen() throws Exception
    {
		File input = new File("db/sample.wig");
		WIG2SilkReader in = new WIG2SilkReader(new FileReader(input));
		char[] cbuf = new char[1000];
		int nCbufs;
		while((nCbufs = in.read(cbuf, 0, 1000))!=-1){
			System.out.print(String.valueOf(cbuf, 0, nCbufs));
		}
    }
}
