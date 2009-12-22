//--------------------------------------
//
// BarGraph.java
// Since: 2009/01/15
//
//--------------------------------------
package org.utgenome.gwt.utgb.server.app;

import java.awt.Color;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.utgenome.graphics.BarGraphCanvas;
import org.utgenome.graphics.GenomeWindow;
import org.utgenome.gwt.utgb.server.WebTrackBase;
import org.xerial.util.log.Logger;

/**
 * Request handler
 * 
 */
public class BarGraph extends WebTrackBase {
	private static final long serialVersionUID = 1L;
	private static Logger _logger = Logger.getLogger(BarGraph.class);

	private String xLabel;
	private String yLabel;
	private int yMin;
	private int yMax;

	// x position on genome sequence
	private long xMin;
	private long xMax;
	private boolean logScaleY = false;

	public BarGraph() {
	}

	private long start = 1;
	private long end = 100000;
	private int width = 800;

	public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		BarGraphCanvas canvas = new BarGraphCanvas(width, 100, new GenomeWindow(start, end));
		canvas.drawXLabel(xLabel);
		Random r = new Random();
		for (long i = start; i < end; i++) {
			int c = (int) i % 255;
			canvas.plot(i, r.nextInt(100), new Color(c, c, c));
		}

		//		BufferedImage buffer = new BufferedImage(800, 100, BufferedImage.TYPE_INT_ARGB);
		//		Graphics2D g = (Graphics2D) buffer.getGraphics();
		//
		//		//g.drawLine(x1, y1, x2, y2)
		//
		//		g.setColor(new Color(200, 150, 150));
		//		g.drawRect(1, 1, 700, 90);
		//		g.drawLine(5, 10, 400, 50);
		//		g.drawLine(10, 10, 410, 50);
		//
		//		g.drawString(xLabel, 300, 90);

		response.setContentType("image/png");
		ImageIO.write(canvas.getBufferedImage(), "png", response.getOutputStream());
	}

	public void setXLabel(String label) {
		xLabel = label;
	}

	public void setYLabel(String label) {
		yLabel = label;
	}

	public void setYMin(int min) {
		yMin = min;
	}

	public void setYMax(int max) {
		yMax = max;
	}

	public void setXMin(long min) {
		xMin = min;
	}

	public void setXMax(long max) {
		xMax = max;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public void setEnd(long end) {
		this.end = end;
	}

	public void setWidth(int width) {
		this.width = width;
	}

}
