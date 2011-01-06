/*--------------------------------------------------------------------------
 *  Copyright 2010 utgenome.org
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
// SAMLens.java
// Since: 2010/12/09
//
//--------------------------------------
package org.utgenome.format.sam;

import java.io.File;

import net.sf.samtools.BrowseableBAMIndex;
import net.sf.samtools.SAMFileReader;

/**
 * Lens for SAM/BAM files
 * 
 * @author leo
 * 
 */
public class SAMLens {

	public static void split(File bamFile) {

		SAMFileReader sam = new SAMFileReader(bamFile, SAMReader.getBamIndexFile(bamFile), false);

		BrowseableBAMIndex index = sam.getBrowseableIndex();

	}

}
