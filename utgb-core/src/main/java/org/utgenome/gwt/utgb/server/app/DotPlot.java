//--------------------------------------
//
// DotPlot.java
// Since: 2010/10/28
//
//--------------------------------------
package org.utgenome.gwt.utgb.server.app;

//--------------------------------------
//
// DotPlot.java
// Since: 2010/10/26
//
//--------------------------------------

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.utgenome.gwt.utgb.server.WebTrackBase;


/**
 * Web action: DotPlot
 *
 */
public class DotPlot extends WebTrackBase
{
    private static final long	serialVersionUID	= 1L;
    private static ArrayList<String> list = new ArrayList<String>();
    private static int counter =0;

    public DotPlot()
    {}
    	
    public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
	    // Read text file
    	try
        {
    	  File file = new File("c:/test/test.txt");
          String line = null;
          BufferedReader br = new BufferedReader(
                  new FileReader(file));
          while ((line = br.readLine()) != null)
            {
        	  String[] data = line.split("\t");
        	  list.add(data[0]);
        	  list.add(data[1]);
        	  list.add(data[2]);
        	  list.add(data[3]);
              counter++;
            }
          br.close();
        
        }
      catch (Exception e)
        {
          e.printStackTrace();
        }
      
	    // set the content type to be generated to PNG image
	    response.setContentType("image/png");

	    // prepare a buffer for drawing grpahics  
	    BufferedImage buffer = new BufferedImage(2000, 2000, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g = (Graphics2D) buffer.getGraphics();

		// draw lines
		for (int i=0; i < counter; i++) {
			float x1 = Float.parseFloat((String) list.get(4*i));
			float y1 = Float.parseFloat((String) list.get(4*i+1));
			float x2 = Float.parseFloat((String) list.get(4*i+2));
			float y2 = Float.parseFloat((String) list.get(4*i+3));
			
			Color z = new Color(25*i%255, 85*i%255, 200*i%255);  
		    g.setColor(z);
			g.draw(new Line2D.Float(x1/100, y1/100, x2/100, y2/100));
		// output the buffered image as PNG image 

		}
	ImageIO.write(buffer, "png", response.getOutputStream());
    }
    
}

