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
// RoundCircle.java
// Since: 2007/11/26
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.server.app;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.utgenome.gwt.utgb.server.RequestHandlerBase;
import org.utgenome.gwt.utgb.server.util.graphic.GraphicUtil;
import org.xerial.util.log.Logger;

/**
 * Generates a circle image
 * 
 * @author leo
 * 
 */
public class RoundCircle extends RequestHandlerBase {
	private static final long serialVersionUID = 19443895944286732L;

	private static Logger _logger = Logger.getLogger(RoundCircle.class);

	private String color = "666666";
	private int size = 4; // radius of the circle

	public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (size <= 0)
			size = 1;
		int canvasSize = size * 2;

		BufferedImage circleImage = GraphicUtil.getTransparentBufferedImage(canvasSize, canvasSize);
		Graphics2D g = GraphicUtil.getGraphics(circleImage);

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Ellipse2D circle = new Ellipse2D.Double(0, 0, canvasSize, canvasSize);

		g.setColor(GraphicUtil.parseColor(color));
		g.fill(circle);

		GraphicUtil.writeImage(circleImage, "png", response);
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

}
