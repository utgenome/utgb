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
// KeywordDBTest.java
// Since: May 20, 2010
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.format.keyword;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;
import org.xerial.util.FileResource;
import org.xerial.util.FileUtil;
import org.xerial.util.log.Logger;

/**
 * @author leo
 * 
 */
public class KeywordDBTest {

	private static Logger _logger = Logger.getLogger(KeywordDBTest.class);
	private String tmpFolder = "target";

	@Test
	public void importBED() throws Exception {

		File tmpKeywordDB = FileUtil.createTempFile(new File(tmpFolder), "keyword-bed", ".sqlite");
		KeywordDB db = new KeywordDB(tmpKeywordDB);
		try {
			db.importFromBED("ce6", FileResource.open(KeywordDBTest.class, "wormbase-keyword.bed"));
			KeywordSearchResult query = db.query("ce6", "Y74C9A.4b", 1, 10);
			assertEquals(1, query.result.size());

			db.close();

			// reopen 
			db = new KeywordDB(tmpKeywordDB);
			query = db.query("ce6", "Y74C9A.2", 1, 10);
			assertEquals(7, query.result.size());
			db.close();

		}
		finally {
			tmpKeywordDB.delete();
		}

	}

	@Test
	public void keywordSplit() throws Exception {

		String[] keywords = new String[] { "Y74C9A.4b", "chrI NM_0" };
		for (String each : keywords) {
			String s = KeywordDB.splitKeyword(each);
			_logger.info(s);
		}
	}

}
