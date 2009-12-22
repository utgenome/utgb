//--------------------------------------
//
// LocusTrack.java
// Since: 2009/02/24
//
//--------------------------------------
package org.utgenome.gwt.utgb.server.app;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.utgenome.gwt.utgb.server.WebTrackBase;
import org.xerial.util.log.Logger;

/**
 * Request handler
 * 
 */
public class LocusTrack extends WebTrackBase {
	private static final long serialVersionUID = 1L;
	private static Logger _logger = Logger.getLogger(LocusTrack.class);

	int width = 800;

	public LocusTrack() {
	}

	public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		BufferedImage buffer = new BufferedImage(width, 300, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) buffer.getGraphics();

		g.setColor(Color.BLUE);
		g.drawRect(10, 50, 100, 30);

		response.setContentType("image/png");
		ImageIO.write(buffer, "png", response.getOutputStream());

	}

	public void setWidth(int width) {
		this.width = width;
	}

}
