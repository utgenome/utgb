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
// UTGBClientErrorCode.java
// Since: May 3, 2010
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client;

import java.io.Serializable;

/**
 * Error codes
 * 
 * @author leo
 * 
 */
public enum UTGBClientErrorCode implements Serializable {
	UNKNOWN, PARSE_ERROR, MISSING_FILES, NOT_IN_PROJECT_ROOT, UNKNOWN_TRACK, UNKNOWN_TRACK_GROUP;

}
