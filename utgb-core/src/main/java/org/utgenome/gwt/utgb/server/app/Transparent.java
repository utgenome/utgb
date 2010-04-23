//--------------------------------------
//
// Transparent.java
// Since: Apr 23, 2010
//
//--------------------------------------
package org.utgenome.gwt.utgb.server.app;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.utgenome.gwt.utgb.server.WebTrackBase;
import org.utgenome.gwt.utgb.server.util.graphic.GraphicUtil;
import org.xerial.util.log.Logger;

/**
 * Web action: Transparent renders a transparent PNG
 * 
 */
public class Transparent extends WebTrackBase {
	private static final long serialVersionUID = 1L;
	private static Logger _logger = Logger.getLogger(Transparent.class);

	public String color = "000000";
	public float opacity = 0.1f;

	public Transparent() {
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		final int w = 1;
		BufferedImage b = new BufferedImage(w, w, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = b.createGraphics();

		g.setColor(GraphicUtil.parseColor(color, (int) (255 * opacity)));
		g.fillRect(0, 0, w, w);
		response.setContentType("image/png");
		ImageIO.write(b, "png", response.getOutputStream());

	}

}
