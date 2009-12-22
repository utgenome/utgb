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
// DefaultRequestMap.java
// Since: 2008/08/04
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.xerial.util.FileResource;
import org.xerial.util.ResourceFilter;
import org.xerial.util.io.VirtualFile;
import org.xerial.util.log.Logger;

/**
 * DefaultRequestMap provides a search function to find a request handler that matches the given web request.
 * 
 * When the request URL is "refseq/human/hg18/list.json", and there is an action handler, refseq.list,
 * 
 * <li>actionHandlername = "list" <li>actionSuffix = "json" <li>actionPrefix = "refseq/human/hg18"
 * 
 * @author leo
 * 
 */
public class DefaultRequestMap implements RequestMap {

	private static Logger _logger = Logger.getLogger(DefaultRequestMap.class);

	private static HashMap<String, Class<RequestHandler>> requestHandlerTable = new HashMap<String, Class<RequestHandler>>();

	@SuppressWarnings("unchecked")
	public static void loadActionPackage(ClassLoader classLoader, String packageName, String actionPrefix) {
		_logger.info("load action package: " + packageName + " alias=" + actionPrefix);

		// list up all classed in the package
		ResourceFilter filter = new ResourceFilter() {
			public boolean accept(String resourcePath) {
				return resourcePath.endsWith(".class");
			}
		};
		ArrayList<Class<?>> classList = new ArrayList<Class<?>>();
		List<VirtualFile> javaClassFileList = FileResource.listResources(classLoader, packageName, filter);
		for (VirtualFile vf : javaClassFileList) {
			Class<?> c = getJavaClass(classLoader, vf, packageName);
			if (c == null)
				continue;
			classList.add(c);
		}

		String prefix = (actionPrefix.length() > 0) ? actionPrefix + "." : "";
		// find request handlers
		for (Class<?> c : classList) {
			if (!c.isInterface() && RequestHandler.class.isAssignableFrom(c)) {
				String className = c.getName();
				_logger.trace("found a web action: " + className);

				if (className.startsWith(packageName)) {
					String shortHandlerName = prefix + className.substring(packageName.length() + 1);
					String handlerKey = shortHandlerName.toLowerCase();
					if (!requestHandlerTable.containsKey(handlerKey)) {
						_logger.info("added web action: " + handlerKey.replaceAll("\\.", "/"));
						requestHandlerTable.put(handlerKey, (Class<RequestHandler>) c);
					}
				}
			}
		}

	}

	public static Class<?> getJavaClass(ClassLoader classLoader, VirtualFile vf, String baseJavaPackage) {
		String logicalPath = vf.getLogicalPath();
		int dot = logicalPath.lastIndexOf(".");
		if (dot <= 0)
			return null;
		String className = baseJavaPackage + "." + logicalPath.substring(0, dot).replaceAll("/", ".");

		try {
			Class<?> c = Class.forName(className, false, classLoader);
			return c;
		}
		catch (ClassNotFoundException e) {
			_logger.error(e);
		}
		catch (UnsatisfiedLinkError e) {
			_logger.error(e);
		}
		catch (VerifyError e) {
			_logger.error(e);
		}
		catch (NoClassDefFoundError e) {
			_logger.error(e);
		}
		return null;
	}

	protected static Class<RequestHandler> findRequestHandlerClass(String handlerName) {
		_logger.trace("requested handler package: " + handlerName);
		Class<RequestHandler> requestHandlerClass = requestHandlerTable.get(handlerName.toLowerCase());
		return requestHandlerClass;
	}

	protected static RequestHandler findRequestHandler(String handlerName) {
		Class<RequestHandler> requestHandlerClass = findRequestHandlerClass(handlerName);
		if (requestHandlerClass == null)
			return null;
		try {
			return requestHandlerClass.newInstance();
		}
		catch (InstantiationException e) {
			_logger.error(e);
			return null;
		}
		catch (IllegalAccessException e) {
			_logger.error(e);
			return null;
		}
	}

	public RequestHandler map(RequestURI requestURI, HttpServletRequest request, HttpServletResponse response) {

		// Gets the RequestHandler of the specified name
		String handlerName = requestURI.getHandlerName();
		RequestHandler handler = findRequestHandler(handlerName);
		if (handler == null) {
			// try to find an action handler using the full handler name
			handler = findRequestHandler(requestURI.getFullName());
		}

		if (handler != null) {
			_logger.trace("handler name: " + requestURI);
			// set prefix & suffix
			request.setAttribute("actionPrefix", requestURI.getPrefix());
			request.setAttribute("actionSuffix", requestURI.getSuffix());
		}

		return handler;
	}

}
