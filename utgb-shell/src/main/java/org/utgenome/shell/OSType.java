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
// utgb-core Project
//
// OSInfo.java
// Since: 2007/11/23
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.shell;

import java.lang.reflect.Method;

/**
 * This class provides information of current OS type.
 * 
 * @author leo
 * 
 */
public enum OSType {
	Windows,
	MacOS,
	Other;

	/**
	 * Gets the current OS type
	 * @return the current OS type
	 */
	public static OSType getOSType()
	{
		String osName = System.getProperty("os.name");
		if(osName.startsWith("Mac OS"))
			return MacOS;
		else if(osName.startsWith("Windows"))
			return Windows;
		else
			return Other; 
	}
		
	
	
}




																																																																																																																																											// Unix
																																																																																																																																											// or
																																																																																																																																											// Linux
																																																																																																																																											// String[]
																																																																																																																																											// browsers
																																																																																																																																											// = {
																																																																																																																																											// "firefox",
																																																																																																																																											// "opera",
																																																																																																																																											// "konqueror",
																																																																																																																																											// "epiphany",
																																																																																																																																											// "mozilla",
																																																																																																																																											// "netscape"
																																																																																																																																											// };
																																																																																																																																											// String
																																																																																																																																											// browser
																																																																																																																																											// =
																																																																																																																																											// null;
																																																																																																																																											// for
																																																																																																																																											// (int
																																																																																																																																											// count
																																																																																																																																											// = 0;
																																																																																																																																											// count
																																																																																																																																											// <
																																																																																																																																											// browsers.length
																																																																																																																																											// &&
																																																																																																																																											// browser
																																																																																																																																											// ==
																																																																																																																																											// null;
																																																																																																																																											// count++)
																																																																																																																																											// if
																																																																																																																																											// (Runtime.getRuntime().exec(
																																																																																																																																											// new
																																																																																																																																											// String[]
																																																																																																																																											// {"which",
																																																																																																																																											// browsers[count]}).waitFor()
																																																																																																																																											// ==
																																																																																																																																											// 0)
																																																																																																																																											// browser
																																																																																																																																											// =
																																																																																																																																											// browsers[count];
																																																																																																																																											// if
																																																																																																																																											// (browser
																																																																																																																																											// ==
																																																																																																																																											// null)
																																																																																																																																											// throw
																																																																																																																																											// new
																																																																																																																																											// Exception("Could
																																																																																																																																											// not
																																																																																																																																											// find
																																																																																																																																											// web
																																																																																																																																											// browser");
																																																																																																																																											// else
																																																																																																																																											// Runtime.getRuntime().exec(new
																																																																																																																																											// String[]
																																																																																																																																											// {browser,
																																																																																																																																											// url});
																																																																																																																																											// } }
																																																																																																																																											// catch
																																																																																																																																											// (Exception
																																																																																																																																											// e) {
																																																																																																																																											// JOptionPane.showMessageDialog(null,
																																																																																																																																											// errMsg
																																																																																																																																											// +
																																																																																																																																											// ":\n"
																																																																																																																																											// +
																																																																																																																																											// e.getLocalizedMessage());
																																																																																																																																											// } }
																																																																																																																																											// }
