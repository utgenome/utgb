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
// AGPPullParse.java
// Since: Jun 5, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.format.agp;

import java.io.BufferedReader;
import java.io.Reader;
public class AGPPullParser 
{
	
	BufferedReader _reader;
	
	public AGPPullParser(Reader input)
	{
		_reader = new BufferedReader(input);
	}
	
	
	
}




