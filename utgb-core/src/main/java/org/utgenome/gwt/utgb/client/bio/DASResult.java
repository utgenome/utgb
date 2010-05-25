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
// DASResult.java
// Since: May 25, 2010
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.bio;

import java.io.Serializable;
import java.util.List;

/**
 * DAS Result
 * 
 * @author leo
 * 
 */
public class DASResult implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static class DASGFF implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public String version;
		public String href;
	}

	public static class Segment implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public String id;
		public long start;
		public long stop;
		public List<DASFeature> feature;

		public static class DASFeature extends Read {
			private static final long serialVersionUID = 1L;

			public String id;

			public String label;
			public String score;
			public String orientation;
			public String phase;

			public Method method;
			public FeatureType type;
			public Group group;
			public Target target;

			public void setId(String id) {
				setName(id);
			}

			public static class Target implements Serializable {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
				public String id;
				public long start;
				public long stop;
				public String value;
			}

			public static class FeatureType implements Serializable {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
				public String id;
				public String category;
				public String reference;
				public String value;
			}

			public static class Group implements Serializable {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
				public String id;
				public String type;
				public String label;
				public Link link;
				public Target target;

				public static class Link implements Serializable {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;
					public String href;
					public String value;
				}
			}

			public static class Method implements Serializable {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
				public String id;
				public String value;
			}
		}
	}

	public DASGFF gff;
	public Segment segment;

}