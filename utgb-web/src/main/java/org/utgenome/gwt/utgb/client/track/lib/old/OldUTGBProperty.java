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
// OldUTGBProperty.java
// Since: 2007/06/19
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.lib.old;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 
 * @author ssksn
 */
public final class OldUTGBProperty {
	/**
	 * Uncallable constructor.
	 * 
	 * @throws AssertionError
	 *             if this constructor is called.
	 */
	private OldUTGBProperty() {
		throw new AssertionError();
	}

	public static final String SPECIES = "species";
	public static final String REVISION = "revision";
	public static final String TARGET = "target";

	private static final String[] propertyNameArray = { SPECIES, REVISION, TARGET };
	private static final Set<String> propertyNameSet = new HashSet<String>();
	private static final List<String> propertyNameList = new ArrayList<String>();

	static {
		{
			propertyNameSet.add(SPECIES);
			propertyNameSet.add(REVISION);
			propertyNameSet.add(TARGET);
		}
		{
			propertyNameList.add(SPECIES);
			propertyNameList.add(REVISION);
			propertyNameList.add(TARGET);
		}
	}

	public static final String[] getPropertyNameArray() {
		return propertyNameArray;
	}

	public static final Set<String> getPropertyNameSet() {
		return propertyNameSet;
	}

	public static final List<String> getPropertyNameList() {
		return propertyNameList;
	}
}
