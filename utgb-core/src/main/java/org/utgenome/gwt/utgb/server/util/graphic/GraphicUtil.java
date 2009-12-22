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
// GraphicUtil.java
// Since: Nov 30, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.server.util.graphic;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.xerial.util.log.Logger;

/**
 * Utilities for manipulating Graphics in Java
 * 
 * @author leo
 * 
 */
public class GraphicUtil {

	private static Logger _logger = Logger.getLogger(GraphicUtil.class);

	/**
	 * Generates a BufferedImage instance whose background is transparent.
	 * 
	 * @param width
	 * @param height
	 * @return a BufferedImage
	 */
	public static BufferedImage getTransparentBufferedImage(int width, int height) {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) image.createGraphics();

		g.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f));
		g.fill(new Rectangle2D.Double(0, 0, width, height));
		g.setComposite(AlphaComposite.SrcOver);
		return image;
	}

	public static Graphics2D getGraphics(BufferedImage bufferedImage) {
		return (Graphics2D) bufferedImage.getGraphics();
	}

	/**
	 * Output the given BufferedImage to the servlet response.
	 * 
	 * @param image
	 * @param imageType
	 * @param response
	 * @throws IOException
	 */
	public static void writeImage(BufferedImage image, String imageType, HttpServletResponse response) throws IOException {
		response.setContentType("image/" + imageType);
		ImageIO.write(image, imageType, response.getOutputStream());
	}

	public static Color parseColor(String colorCodeStr) {
		if (colorCodeStr.startsWith("#"))
			colorCodeStr = colorCodeStr.substring(1);

		int colorCode = 0x666666;
		try {
			colorCode = Integer.parseInt(colorCodeStr, 16);
		}
		catch (NumberFormatException e) {
			_logger.error("invalid color code:" + e);
		}
		return new Color((colorCode >> 16) & 0xFF, (colorCode >> 8) & 0xFF, (colorCode) & 0xFF);
	}

	public static Color parseColor(String colorCodeStr, int alpha) {
		if (colorCodeStr.startsWith("#"))
			colorCodeStr = colorCodeStr.substring(1);

		int colorCode = 0x666666;
		try {
			colorCode = Integer.parseInt(colorCodeStr, 16);
		}
		catch (NumberFormatException e) {
			_logger.error("invalid color code:" + e);
		}
		return new Color((colorCode >> 16) & 0xFF, (colorCode >> 8) & 0xFF, (colorCode) & 0xFF, alpha);
	}

}
