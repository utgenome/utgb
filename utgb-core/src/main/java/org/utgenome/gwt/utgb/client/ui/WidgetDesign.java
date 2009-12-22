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
// WidgetDesign.java
// Since: 2007/11/20
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.ui;

import java.util.HashMap;

import com.google.gwt.user.client.ui.Image;

/**
 * WidgetDesign provides mappings from image types to the corresponding image files
 * @author leo
 *
 */
public class WidgetDesign
{
    public static int TitleBar_L = 101;
    public static int TitleBar_Base = 102; 
    public static int TitleBar_R = 103;
    
    private static HashMap<Integer, String> imageTable = new HashMap<Integer, String>();
    private static String imageFolder = "image/gray/";
    
    private static void setImage(int imageType, String filePath)
    {
        imageTable.put(new Integer(imageType), filePath);
    }
    static 
    {
        setImage(TitleBar_L, "window/wframe-l.gif");
        setImage(TitleBar_Base, "window/wframe-c.gif");
        setImage(TitleBar_R, "window/wframe-r.gif");
    }

    public static Image getImage(int imageType)
    {
        String filePath = imageFolder + imageTable.get(new Integer(imageType));
        return new Image(filePath);
    }

    public static String getImageURL(int imageType)
    {
        return imageFolder + imageTable.get(new Integer(imageType));
    }

    /**
     * Non-constructable
     */
    private WidgetDesign()
    {
        
    }
}




