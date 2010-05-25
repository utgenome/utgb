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
// BrowserInfo.java
// Since: Jun 18, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.util;

import java.util.HashMap;

/**
 * URL query string reader. For example, .../UTGB.html?species=human&revision=hg18
 * 
 * @author leo
 * 
 */
public class BrowserInfo {
	public static HashMap<String, String> getURLQueryRequestParameters() {
		HashMap<String, String> properties = new HashMap<String, String>();
		String query = getQueryString();
		if (query == null || query.length() < 1)
			return properties;

		String queryPart = query.substring(1);
		String[] keyAndValue = queryPart.split("&");
		for (int i = 0; i < keyAndValue.length; i++) {
			String[] kv = keyAndValue[i].split("=");
			if (kv.length > 1)
				properties.put(kv[0], unescape(kv[1]));
			else
				properties.put(kv[0], "");
		}

		return properties;
	}

	public native static String unescape(String s) /*-{
	      return unescape(s);
	   }-*/;

	public static native void setQueryString(String qs) /*-{
	   	$wnd.location.search = qs;
	   }-*/;

	public static native String getQueryString() /*-{
	    return $wnd.location.search;
	    }-*/;

	public static native String getProtocol() /*-{
	    return $wnd.location.protocol;
	    }-*/;

	public static native String getPort() /*-{
	    return $wnd.location.port;
	    }-*/;

	public static native String getPath() /*-{
	    return $wnd.location.pathname;
	    }-*/;

	public static native String getHref() /*-{
	    return $wnd.location.href;
	    }-*/;

	public static native String getHostName() /*-{
	    return $wnd.location.hostname;
	    }-*/;

	public static native String getHost() /*-{
	    return $wnd.location.host;
	    }-*/;

	public static native String getHash() /*-{
	    return $wnd.location.hash;
	    }-*/;

	public static native boolean isGoogleGearsInstalled() /*-{
	       return ($wnd.google && google.gears);
	   }-*/;
	
	public static native String getUserAgent() /*-{
			return navigator.userAgent;
	}-*/;
	
	public static native boolean isCanvasSupported() /*-{
		return !!document.createElement('canvas').getContext;
	}-*/;
}
