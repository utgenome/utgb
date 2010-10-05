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
// utgb-core Project
//
// UTGBErrorCode.java
// Since: Jan 7, 2008
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome;

/**
 * Error coe
 * 
 * @author leo
 * 
 */
public enum UTGBErrorCode {
	Unknown, InvalidRequestParameter,

	DatabaseError, JSONToObjectMapping, MaliciousSQLSyntax, FailedToLoadTrackConfig, UnknownDBType, FileNotFound, InvalidSQLSyntax, PARSE_ERROR, BIND_ERROR, InvalidSyntax, IO_ERROR, INVALID_INPUT, INHERITED, INVALID_BED_LINE, MISSING_FILES, MISSING_OPTION, NOT_AN_ACGT, INVALID_FORMAT,

}
