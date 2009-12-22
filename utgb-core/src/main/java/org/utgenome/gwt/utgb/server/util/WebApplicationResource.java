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
// UTGBMedaka Project
//
// WebApplicationResource.java
// Since: Aug 9, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.server.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;

import org.xerial.util.FileResource;
import org.xerial.util.StringUtil;
import org.xerial.util.log.Logger;

/**
 * Helper class to retrieve web application resources contained in webapp folders (/), /WEB-INF, etc.
 * 
 * @author leo
 *
 */
public class WebApplicationResource {

	private static Logger _logger = Logger.getLogger(WebApplicationResource.class);
	
	private ServletContext _context; 
	
	
	public WebApplicationResource(ServletContext context) 
	{
		_context = context;
	}
	
	/**
	 * Retrieves web application resources from the ServletContext
	 * @param basePath the resource path, which must begin with "/". For example, "/WEB-INF", "/" etc.  
	 * @param fileNamePattern the file name pattern
	 * @param recursive when true, it recursively searches sub folders 
	 * @return the list of found resource paths
	 */
	public ArrayList<String> find(String basePath, String fileNamePattern, boolean recursive)
	{
		return find(_context, basePath, fileNamePattern, recursive);
	}
	
	/**
	 * Retrieves web application resources from the ServletContext
	 * @param context the servlet context
	 * @param basePath the resource path, which must begin with "/". For example, "/WEB-INF", "/" etc.  
	 * @param fileNamePattern the file name pattern
	 * @param recursive when true, it recursively searches sub folders 
	 * @return the list of found resource paths
	 */
	public static ArrayList<String> find(ServletContext context, String basePath, String fileNamePattern, boolean recursive)
	{
		ArrayList<String> foundPath = new ArrayList<String>();
        Set pathSet = context.getResourcePaths(basePath);
        if(pathSet == null)
            return foundPath;
        
        for(Iterator it = pathSet.iterator(); it.hasNext(); )
        {
            String path = it.next().toString();
            URL resource;
			try {
				resource = context.getResource(path);
			} catch (MalformedURLException e) {
				_logger.error(e);
				continue;
			}
			if(resource == null)
			    continue;
            if(resource.getPath().endsWith("/") && recursive)
                foundPath.addAll(find(context, path, fileNamePattern, recursive));
            else
            {
            	if(Pattern.matches(fileNamePattern, resource.getFile())){
            		foundPath.add(path);	
            	}
            }
        }
        return foundPath;
	}


	
	/**
	 * Gets a {@link BufferedReader} of the specified web application resource
	 * @param context the servlet context
	 * @param path the resource path
	 * @return a {@link BufferedReader} of the specified resource
	 * @throws FileNotFoundException when the specified resource is not found
	 */
	public static BufferedReader openResource(ServletContext context, String path) throws FileNotFoundException
	{
		InputStream in = context.getResourceAsStream(path);
		if(in == null)
			throw new FileNotFoundException(path);
		
		return new BufferedReader(new InputStreamReader(in));
		
	}
	
	/**
	 * @param context the servlet context
	 * @param path the resource path, which must begin with "/" 
	 * @return null if the specified resource is not found 
	 */
	public static File getResoruceFile(ServletContext context, String path)
	{
	    try
        {
            URL resourceURL = context.getResource(path);
            return new File(resourceURL.toURI());
        }
        catch (MalformedURLException e)
        {
            _logger.error(e);
        }
        catch (URISyntaxException e)
        {
            _logger.error(e);
        }
	    return null;
	}
	
	
	/**
	 * Gets a {@link BufferedReader} of the specified web application resource
	 * @param path the resource path
	 * @return a {@link BufferedReader} of the specified resource
	 * @throws FileNotFoundException when the specified resource is not found
	 */
	public BufferedReader openResource(String path) throws FileNotFoundException
	{
		return openResource(_context, path);
	}
	
	public static String getContent(ServletContext context, String path) throws IOException
	{
		BufferedReader reader = openResource(context, path);
		String line;
		StringWriter writer = new StringWriter();
		while((line = reader.readLine()) != null)
		{
			writer.append(line);
			writer.append(StringUtil.newline());
		}
		return writer.toString();
	}
}




