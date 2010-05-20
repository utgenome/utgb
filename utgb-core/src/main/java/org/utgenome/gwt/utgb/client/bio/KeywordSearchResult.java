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
// utgb-medaka Project
//
// KeywordSearchResult.java
// Since: Jun 18, 2008
//
// $URL: http://svn.utgenome.org/utgb/trunk/utgb/utgb-keyword/src/main/java/org/utgenome/keyword/app/KeywordSearchResult.java $ 
// $Author: leo $
//--------------------------------------
package org.utgenome.gwt.utgb.client.bio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * keyword search result
 * 
 * @author leo
 * 
 */
public class KeywordSearchResult implements Serializable {

	private static final long serialVersionUID = 1L;

	public int page;
	public int count;
	public int maxPage;
	public List<Entry> result = new ArrayList<Entry>();

	public static class Entry implements Serializable {
		private static final long serialVersionUID = 1L;

		public String name = "";
		public String chr = "";
		public int start;
		public int end;
		public String ref = "";
		public String offsets = null;

		public String getHit() {
			if (offsets != null) {
				String[] offsetData = offsets.split(" ");
				if (offsetData == null || offsetData.length < 4)
					return name;

				StringBuilder nameBuf = new StringBuilder();
				for (int i = 0; i < offsetData.length; i += 4) {
					if (i > 0)
						nameBuf.append(" ");

					int startIndex = Integer.parseInt(offsetData[i + 2]);
					int byteLength = Integer.parseInt(offsetData[i + 3]);
					nameBuf.append(name.substring(startIndex, startIndex + byteLength));
				}
				return nameBuf.toString();
			}

			return name;
		}
	}

}
