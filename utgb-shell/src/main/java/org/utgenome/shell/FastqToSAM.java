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
// utgb-shell Project
//
// FastqToSAM.java
// Since: Jul 12, 2010
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.shell;

import org.utgenome.format.fastq.FastqToBAM;

public class FastqToSAM extends UTGBShellCommand {

	@Override
	public void execute(String[] args) throws Exception {

		FastqToBAM.execute(args);
	}

	@Override
	public String getOneLinerDescription() {
		return "convert fastq files into SAM/BAM format";
	}

	@Override
	public String name() {
		return "fastq2sam";
	}

}
