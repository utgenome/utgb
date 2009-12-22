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
// utgb-shell Project
//
// WebBrowser.java
// Since: 2007/11/23
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.shell;

import java.lang.reflect.Method;

import org.xerial.util.log.Logger;

/**
 * Open a given URL via OS's default web browser.
 * 
 * This code is created by modifying the following public code: (BareBones library)
 * http://www.centerkey.com/java/browser/myapp/BareBonesBrowserLaunch.java
 * 
 * @author leo
 * 
 */
public class WebBrowser {

	private static Logger _logger = Logger.getLogger(WebBrowser.class);

	public static void openURL(String url) {
		try {
			OSType osType = OSType.getOSType();
			switch (osType) {
			case MacOS:
				Class fileMgr = Class.forName("com.apple.eio.FileManager");
				Method openURL = fileMgr.getDeclaredMethod("openURL", new Class[] { String.class });
				openURL.invoke(null, new Object[] { url });
				break;
			case Windows:
				Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
				break;
			default:
				// assume Unix or Linux
				String[] browsers = { "firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape" };
				String browser = null;
				for (int count = 0; count < browsers.length && browser == null; count++)
					if (Runtime.getRuntime().exec(new String[] { "which", browsers[count] }).waitFor() == 0)
						browser = browsers[count];
				if (browser == null)
					throw new Exception("Could not find web browser");
				else
					Runtime.getRuntime().exec(new String[] { browser, url });
				break;
			}
		}
		catch (Exception e) {
			_logger.error(e);
		}
	}

}
