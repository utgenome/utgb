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
// ReadSweeperTest.java
// Since: 2010/10/13
//
//--------------------------------------
package org.utgenome.format.sam;

import java.io.File;
import java.util.Collection;

import org.junit.Test;
import org.utgenome.format.sam.ReadSweeper.ReadSetHandler;
import org.utgenome.gwt.utgb.client.bio.SAMRead;
import org.xerial.util.FileResource;
import org.xerial.util.FileUtil;
import org.xerial.util.log.Logger;

public class ReadSweeperTest {

	private static Logger _logger = Logger.getLogger(ReadSweeperTest.class);

	@Test
	public void sweep() throws Exception {

		File bam = FileUtil.createTempFile(new File("target"), "sample", ".bam");
		File bai = new File(bam.getAbsolutePath() + ".bai");
		FileUtil.copy(FileResource.openByteStream(ReadSweeperTest.class, "coverage.bam"), bam);
		FileUtil.copy(FileResource.openByteStream(ReadSweeperTest.class, "coverage.bam.bai"), bai);

		ReadSweeper<SAMRead> s = new ReadSweeper<SAMRead>();

		s.sweep(new SAMReadIterator(bam, "seq1"), new ReadSetHandler<SAMRead>() {

			public void handle(int sweepLine, Collection<SAMRead> readSet) {
				_logger.info(String.format("%-3d:%d", sweepLine, readSet.size()));
			}
		});
	}

}
