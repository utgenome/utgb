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
// TextLabel.java
// Since: Nov 30, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.server.app;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.utgenome.gwt.utgb.server.RequestHandlerBase;
import org.utgenome.gwt.utgb.server.util.graphic.GraphicUtil;

/**
 * Generates a TextLabel graphic
 * 
 * @author leo
 * 
 */
public class TextLabel extends RequestHandlerBase {
	private static final long serialVersionUID = 1L;

	private String text = "";
	private int width = 300;
	private int height = 200;
	private int size = 12;
	private String color = "000000";

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		if (size <= 0)
			size = 1;
		if (width <= 0)
			width = 50;
		if (height <= 0)
			height = 12;

		BufferedImage image = GraphicUtil.getTransparentBufferedImage(width, height);
		Graphics2D g = GraphicUtil.getGraphics(image);
		// g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		g.setFont(new Font("SansSerif", Font.PLAIN, size));
		FontMetrics fontMetrics = g.getFontMetrics();
		int fontHeight = fontMetrics.getHeight();
		int fontWidth = fontMetrics.stringWidth(text);
		g.setColor(GraphicUtil.parseColor(color));
		g.drawString(text, 0, fontHeight);

		BufferedImage clippedText = image.getSubimage(0, 0, fontWidth, fontHeight);
		GraphicUtil.writeImage(clippedText, "png", response);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

}
