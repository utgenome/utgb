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
// SCMDProject
// 
// HTMLFilter.java 
// Since: 2004/08/07
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package org.utgenome.gwt.utgb.server.util.xml;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Removes illegal characters that cannot be used as XML text contents into entity references. 
 * 
 * @author leo
 *  
 */
public class HTMLContentFilter implements TextContentFilter
{
    Pattern cdataPattern = Pattern.compile("<!\\[CDATA\\[([^\\]]*)\\]\\]>");
    /**
     *  
     */
    public HTMLContentFilter()
    {
    }

    /**
     * replaces &, <, >, ", ' characters into corresponding entity references
     * 
     * @param content the conversion target string
     *          
     * @return
     */
    public String filter(String content) {
        
        Matcher m = cdataPattern.matcher(content);
        if(m.matches())
        {
            // returns the content of the CDATA section
            return m.group(1);
        }
        
        StringBuffer substituedStringBuffer = new StringBuffer(content.length());
        for (int i = 0; i < content.length(); i++)
        {
            char c = content.charAt(i);
            switch (c)
            {
            case '<':
                substituedStringBuffer.append("&lt;");
                break;
            case '>':
                substituedStringBuffer.append("&gt;");
                break;
            case '"':
                substituedStringBuffer.append("&quot;");
                break;
            case '\'':
                substituedStringBuffer.append("&apos;");
                break;
            case '&':
                substituedStringBuffer.append("&amp;");
                break;
            default:
                substituedStringBuffer.append(c);
            }
        }
        return substituedStringBuffer.toString();
    }

}

