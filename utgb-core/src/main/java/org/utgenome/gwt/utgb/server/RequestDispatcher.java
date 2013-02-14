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
// RequestDispatcher.java
// Since: Oct 4, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.server;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.utgenome.UTGBException;
import org.xerial.core.XerialException;
import org.xerial.lens.Lens;
import org.xerial.util.bean.BeanUtil;
import org.xerial.util.log.Logger;

/**
 * {@link RequestDispatcher} dispatches HTTP GET/POST request to appropriate action handlers.
 * 
 * see also http://trac.utgenome.org/project/UTGB/wiki/UTGBCore/RequestDispatcher
 * 
 * <h1>UTGB Request Dispatcher Mechanism</h1>
 * 
 * <h2>web.xml setting</h2>
 * 
 * Add the following description into your web.xml.
 * 
 * <li>The lines param-name=base-package, param-value=(application base) specifies the location (base package name)
 * where the RequestDispatcher? searches recursively for RequestHandler? implementations.
 * 
 * <li>With the following setting, an HTTP request, e.g., http://localhost:8989/hello.action, is mapped to the request
 * handler org.utgenome.gwt.utgb.server.app.Hello class. The upper letters are converted into the lower letters when
 * mapping the request, e.g., Hello action can be accecced via hello.action URL.
 * 
 * <li>Another example using hierarchies of actions: http://localhost:8989/admin/login.action is mapped to
 * org.utgenome.gwt.utgb.server.Login class.
 * 
 * <pre>
 *  &lt;servlet&gt;
 *  &lt;servlet-name&gt;dispatcher&lt;/servlet-name&gt;
 *  &lt;servlet-class&gt;org.utgenome.gwt.utgb.server.RequestDispatcher&lt;/servlet-class&gt;
 *  &lt;init-param&gt;
 *  &lt;param-name&gt;base-package&lt;/param-name&gt;
 *  &lt;param-value&gt;(your web application request handler base package. e.g. org.utgenome.gwt.utgb.server.app)&lt;/param-value&gt;
 *  &lt;/init-param&gt;
 *  &lt;/servlet&gt;
 * 
 *  &lt;servlet-mapping&gt;
 *  &lt;servlet-name&gt;dispatcher&lt;/servlet-name&gt;
 *  &lt;url-pattern&gt;*.action&lt;/url-pattern&gt;
 *  &lt;/servlet-mapping&gt;
 * </pre>
 * 
 * <h2>Automatic Data Binding to Request Handler</h2>
 * 
 * Hello.java class has serveral parameter values name and year:
 * 
 * <code>
 <pre>
 * public class Hello implements RequestHandler {
 * 	private String name = &quot;&quot;;
 * 	private int year = 2000;
 * 
 * 	public void handle(HttpServletRequest request, HttpServletResponse response) {
 * 		PrintWriter out = response.getWriter().println(&quot;Hello &quot; + name + &quot;(&quot; + year + &quot;)&quot;);
 * 	}
 * 
 * 	public void setName(String name) {
 * 		this.name = name;
 * 	}
 * 
 * 	public void setYear(int year) {
 * 		this.year = year;
 * 	}
 * }
 * </pre>
 </code>
 * 
 * 
 * Our {@link RequestDispatcher} automatically set these parameter values from a given HTTP request. For example, an
 * HTTP request
 * 
 * http://localhost:8989/hello.action?name=leo&year=2007
 * 
 * will invoke setName("leo") and setYear(2007) methods.
 * 
 * Note that, although the query string in the HTTP request consists of string values, our {@link BeanUtil} library
 * detects the data type to which the string should be translated by analysing the class definition. In this example,
 * the string value
 * 
 * "2007" is translated into an integer, 2007.
 * 
 * The web page result of the avobe request looks like as this:
 * 
 * <pre>
 *  Hello leo(2007)
 * </pre>
 * 
 * 
 * Alternate access method: http://localhost:8989/dispathcer?actionClass=org.utgenome .gwt.utgb.server.app.hello&
 * 
 * @author leo
 * 
 */
public class RequestDispatcher implements Filter {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger _logger = Logger.getLogger(RequestDispatcher.class);

	private LinkedList<RequestMap> requestMapList = new LinkedList<RequestMap>();
	private boolean isInGWTHostedMode = false;
	private static HashMap<String, String> mimeTypes = new HashMap<String, String>();

	private void initMimeTypes() {
		if (!isInGWTHostedMode)
			return; // no need to initialize the mime type table other than the GWT Hosted mode

		// the following part is borrowed from GWTShellServlet
		mimeTypes.put("abs", "audio/x-mpeg");
		mimeTypes.put("ai", "application/postscript");
		mimeTypes.put("aif", "audio/x-aiff");
		mimeTypes.put("aifc", "audio/x-aiff");
		mimeTypes.put("aiff", "audio/x-aiff");
		mimeTypes.put("aim", "application/x-aim");
		mimeTypes.put("art", "image/x-jg");
		mimeTypes.put("asf", "video/x-ms-asf");
		mimeTypes.put("asx", "video/x-ms-asf");
		mimeTypes.put("au", "audio/basic");
		mimeTypes.put("avi", "video/x-msvideo");
		mimeTypes.put("avx", "video/x-rad-screenplay");
		mimeTypes.put("bcpio", "application/x-bcpio");
		mimeTypes.put("bin", "application/octet-stream");
		mimeTypes.put("bmp", "image/bmp");
		mimeTypes.put("body", "text/html");
		mimeTypes.put("cdf", "application/x-cdf");
		mimeTypes.put("cer", "application/x-x509-ca-cert");
		mimeTypes.put("class", "application/java");
		mimeTypes.put("cpio", "application/x-cpio");
		mimeTypes.put("csh", "application/x-csh");
		mimeTypes.put("css", "text/css");
		mimeTypes.put("dib", "image/bmp");
		mimeTypes.put("doc", "application/msword");
		mimeTypes.put("dtd", "text/plain");
		mimeTypes.put("dv", "video/x-dv");
		mimeTypes.put("dvi", "application/x-dvi");
		mimeTypes.put("eps", "application/postscript");
		mimeTypes.put("etx", "text/x-setext");
		mimeTypes.put("exe", "application/octet-stream");
		mimeTypes.put("gif", "image/gif");
		mimeTypes.put("gtar", "application/x-gtar");
		mimeTypes.put("gz", "application/x-gzip");
		mimeTypes.put("hdf", "application/x-hdf");
		mimeTypes.put("hqx", "application/mac-binhex40");
		mimeTypes.put("htc", "text/x-component");
		mimeTypes.put("htm", "text/html");
		mimeTypes.put("html", "text/html");
		mimeTypes.put("hqx", "application/mac-binhex40");
		mimeTypes.put("ief", "image/ief");
		mimeTypes.put("jad", "text/vnd.sun.j2me.app-descriptor");
		mimeTypes.put("jar", "application/java-archive");
		mimeTypes.put("java", "text/plain");
		mimeTypes.put("jnlp", "application/x-java-jnlp-file");
		mimeTypes.put("jpe", "image/jpeg");
		mimeTypes.put("jpeg", "image/jpeg");
		mimeTypes.put("jpg", "image/jpeg");
		mimeTypes.put("js", "text/javascript");
		mimeTypes.put("jsf", "text/plain");
		mimeTypes.put("jspf", "text/plain");
		mimeTypes.put("kar", "audio/x-midi");
		mimeTypes.put("latex", "application/x-latex");
		mimeTypes.put("m3u", "audio/x-mpegurl");
		mimeTypes.put("mac", "image/x-macpaint");
		mimeTypes.put("man", "application/x-troff-man");
		mimeTypes.put("me", "application/x-troff-me");
		mimeTypes.put("mid", "audio/x-midi");
		mimeTypes.put("midi", "audio/x-midi");
		mimeTypes.put("mif", "application/x-mif");
		mimeTypes.put("mov", "video/quicktime");
		mimeTypes.put("movie", "video/x-sgi-movie");
		mimeTypes.put("mp1", "audio/x-mpeg");
		mimeTypes.put("mp2", "audio/x-mpeg");
		mimeTypes.put("mp3", "audio/x-mpeg");
		mimeTypes.put("mpa", "audio/x-mpeg");
		mimeTypes.put("mpe", "video/mpeg");
		mimeTypes.put("mpeg", "video/mpeg");
		mimeTypes.put("mpega", "audio/x-mpeg");
		mimeTypes.put("mpg", "video/mpeg");
		mimeTypes.put("mpv2", "video/mpeg2");
		mimeTypes.put("ms", "application/x-wais-source");
		mimeTypes.put("nc", "application/x-netcdf");
		mimeTypes.put("oda", "application/oda");
		mimeTypes.put("pbm", "image/x-portable-bitmap");
		mimeTypes.put("pct", "image/pict");
		mimeTypes.put("pdf", "application/pdf");
		mimeTypes.put("pgm", "image/x-portable-graymap");
		mimeTypes.put("pic", "image/pict");
		mimeTypes.put("pict", "image/pict");
		mimeTypes.put("pls", "audio/x-scpls");
		mimeTypes.put("png", "image/png");
		mimeTypes.put("pnm", "image/x-portable-anymap");
		mimeTypes.put("pnt", "image/x-macpaint");
		mimeTypes.put("ppm", "image/x-portable-pixmap");
		mimeTypes.put("ppt", "application/powerpoint");
		mimeTypes.put("ps", "application/postscript");
		mimeTypes.put("psd", "image/x-photoshop");
		mimeTypes.put("qt", "video/quicktime");
		mimeTypes.put("qti", "image/x-quicktime");
		mimeTypes.put("qtif", "image/x-quicktime");
		mimeTypes.put("ras", "image/x-cmu-raster");
		mimeTypes.put("rgb", "image/x-rgb");
		mimeTypes.put("rm", "application/vnd.rn-realmedia");
		mimeTypes.put("roff", "application/x-troff");
		mimeTypes.put("rtf", "application/rtf");
		mimeTypes.put("rtx", "text/richtext");
		mimeTypes.put("sh", "application/x-sh");
		mimeTypes.put("shar", "application/x-shar");
		mimeTypes.put("smf", "audio/x-midi");
		mimeTypes.put("sit", "application/x-stuffit");
		mimeTypes.put("snd", "audio/basic");
		mimeTypes.put("src", "application/x-wais-source");
		mimeTypes.put("sv4cpio", "application/x-sv4cpio");
		mimeTypes.put("sv4crc", "application/x-sv4crc");
		mimeTypes.put("swf", "application/x-shockwave-flash");
		mimeTypes.put("t", "application/x-troff");
		mimeTypes.put("tar", "application/x-tar");
		mimeTypes.put("tcl", "application/x-tcl");
		mimeTypes.put("tex", "application/x-tex");
		mimeTypes.put("texi", "application/x-texinfo");
		mimeTypes.put("texinfo", "application/x-texinfo");
		mimeTypes.put("tif", "image/tiff");
		mimeTypes.put("tiff", "image/tiff");
		mimeTypes.put("tr", "application/x-troff");
		mimeTypes.put("tsv", "text/tab-separated-values");
		mimeTypes.put("txt", "text/plain");
		mimeTypes.put("ulw", "audio/basic");
		mimeTypes.put("ustar", "application/x-ustar");
		mimeTypes.put("xbm", "image/x-xbitmap");
		mimeTypes.put("xht", "application/xhtml+xml");
		mimeTypes.put("xhtml", "application/xhtml+xml");
		mimeTypes.put("xml", "text/xml");
		mimeTypes.put("xpm", "image/x-xpixmap");
		mimeTypes.put("xsl", "text/xml");
		mimeTypes.put("xwd", "image/x-xwindowdump");
		mimeTypes.put("wav", "audio/x-wav");
		mimeTypes.put("svg", "image/svg+xml");
		mimeTypes.put("svgz", "image/svg+xml");
		mimeTypes.put("vsd", "application/x-visio");
		mimeTypes.put("wbmp", "image/vnd.wap.wbmp");
		mimeTypes.put("wml", "text/vnd.wap.wml");
		mimeTypes.put("wmlc", "application/vnd.wap.wmlc");
		mimeTypes.put("wmls", "text/vnd.wap.wmlscript");
		mimeTypes.put("wmlscriptc", "application/vnd.wap.wmlscriptc");
		mimeTypes.put("wrl", "x-world/x-vrml");
		mimeTypes.put("Z", "application/x-compress");
		mimeTypes.put("z", "application/x-compress");
		mimeTypes.put("zip", "application/zip");
	}

	private String removeFirstSlash(String requestURI) {
		if (requestURI.startsWith("/"))
			return requestURI.substring(1);
		else
			return requestURI;
	}

	public static String removeGWTModuleNamePart(String requestURI) {
		return requestURI.replaceFirst("\\/\\w+(\\.\\w+)*\\/", "");
	}

	private static Pattern invalidCharInParamKey = Pattern.compile("[.-]");

	public static void setRequestParametersToHandler(RequestHandler handler, HttpServletRequest req) {
		assert (handler != null);

		// bind the parameter values to the RequestHandler
		try {
			Properties prop = new Properties();
			for (Enumeration e = req.getParameterNames(); e.hasMoreElements();) {
				String key = e.nextElement().toString();
				Matcher m = invalidCharInParamKey.matcher(key);
				String sanitizedKey = m.replaceAll("");
				prop.put(sanitizedKey, req.getParameter(key));
			}
			if (_logger.isTraceEnabled())
				_logger.trace(prop.toString());
			Lens.loadMap(handler, prop);
		}
		catch (XerialException e) {
			_logger.error(e);
		}

	}

	public void destroy() {
		_logger.info("destroy RequestDispatcher");
	}

	public static String getExtension(String path) {
		int dotIndex = path.lastIndexOf(".");
		if (dotIndex > 0) {
			return path.substring(dotIndex + 1);
		}
		else
			return "";
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;

		// resolve request URI
		String requestURI = null;
		if (isInGWTHostedMode) {
			// in GWT-shell, we have to remove the module name prefix in the
			// request URI.
			requestURI = req.getRequestURI();
			requestURI = removeGWTModuleNamePart(requestURI);
		}
		else
			requestURI = removeFirstSlash(req.getServletPath());
		if (_logger.isTraceEnabled())
			_logger.trace("request URI: " + requestURI);

		dispatchRequest(new RequestURI(requestURI), req, resp, filterChain);
	}

	public void dispatchRequest(RequestURI requestURI, HttpServletRequest req, HttpServletResponse resp, FilterChain filterChain) throws IOException,
			ServletException {

		for (RequestMap map : requestMapList) {
			RequestHandler handler = map.map(requestURI, req, resp);
			if (handler != null) {
				req.setAttribute("actionPrefix", requestURI.getPrefix());
				req.setAttribute("actionSuffix", requestURI.getSuffix());
				setRequestParametersToHandler(handler, req);

				// validate the request parameters
				try {
					handler.validate(req, resp);
				}
				catch (UTGBException e) {
					_logger.error(e);
					return;
				}

				// passes the request to the handler
				handler.handle(req, resp);

				// returns without chains the request
				return;
			}
		}

		// when no handler is found

		// search src/main/webapp folder (in GWT hosted mode)
		if (doGetFromSrcMainWebApp(requestURI.getURI(), req, resp))
			return;

		// when no handler is found for the requested URI, forward this
		// request to the next chain filter
		filterChain.doFilter(req, resp);
	}

	private static DateFormat sHttpDateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.US);

	/**
	 * Converts a file-style date/time into a string form that is compatible with HTTP.
	 */
	public static String toInternetDateFormat(long time) {
		Date date = dateToGMT(new Date(time));
		String dateGmt;
		synchronized (sHttpDateFormat) {
			dateGmt = sHttpDateFormat.format(date) + " GMT";
		}
		return dateGmt;
	}

	/**
	 * Converts a local date to GMT.
	 * 
	 * @param date
	 *            the date in the local time zone
	 * @return the GMT version
	 */
	private static Date dateToGMT(Date date) {
		Calendar cal = Calendar.getInstance();
		long tzMillis = cal.get(Calendar.ZONE_OFFSET) + cal.get(Calendar.DST_OFFSET);
		return new Date(date.getTime() - tzMillis);
	}

	private boolean doGetFromSrcMainWebApp(String requestURI, ServletRequest request, ServletResponse response) throws IOException {

		if (!isInGWTHostedMode)
			return false;

		// search the resource
        File resourceFile;
        try {
            resourceFile = new File(new File(UTGBMaster.getProjectRootFolder(), "src/main/webapp"), requestURI);
            if (!resourceFile.exists()) {
                //_logger.debug("resource file: " + resourceFile +" is not found");
                return false;
            }
        }
        catch(UTGBException e) {
            _logger.error(e);
            return false;
        }

		// Get the MIME type.
		String mimeType = mimeTypes.get(getExtension(requestURI));
		if (mimeType == null) {
			mimeType = "application/octet-stream";
		}

		HttpServletResponse resp = (HttpServletResponse) response;
		resp.setStatus(HttpServletResponse.SC_OK);

		// set cache time
		long cacheTime = 5;
		long expires = 24 * 1000;
		resp.setHeader("Cache-Control", "max-age=" + cacheTime);
		String expiresString = toInternetDateFormat(expires);
		resp.setHeader("Expires", expiresString);

		// set current date
		long now = new Date().getTime();
		resp.setHeader("Date", toInternetDateFormat(now));

		// content type
		resp.setContentType(mimeType);

		// last modified time
		long lastModified = resourceFile.lastModified();
		String lastModifiedStr = toInternetDateFormat(lastModified);
		resp.setHeader("Last-Modified", lastModifiedStr);

		// content length
		long contentLength = resourceFile.length();
		if (contentLength >= 0) {
			resp.setHeader("Content-Length", Long.toString(contentLength));
		}

		// Send the bytes.
		OutputStream out = resp.getOutputStream();
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(resourceFile));
		byte[] buf = new byte[1024];
		int bytesRead = 0;
		while ((bytesRead = in.read(buf)) >= 0) {
			out.write(buf, 0, bytesRead);
		}
		in.close();
		return true;
	}

	public static boolean isGWTHostedMode() {
		return Boolean.parseBoolean(System.getProperty("gwt-hosted-mode"));
	}

	public void init(FilterConfig config) throws ServletException {
		_logger.info("initializing RequestDispatcher");

		isInGWTHostedMode = isGWTHostedMode();

		if (isInGWTHostedMode) {
			_logger.info("GWT Hosted Mode");
			initMimeTypes();
		}

		initRequestMap();
	}

	public void initRequestMap() {
		addRequestMap(new DefaultRequestMap());
	}

	public void addRequestMap(RequestMap map) {
		requestMapList.add(map);
	}

}
