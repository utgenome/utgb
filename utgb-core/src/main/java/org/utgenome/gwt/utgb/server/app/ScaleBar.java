//--------------------------------------
//
// ScaleBar.java
// Since: 2010/07/30
//
//--------------------------------------
package org.utgenome.gwt.utgb.server.app;

import java.awt.Color;
import java.awt.Font;
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
 * Web action: ScaleBar
 * 
 */
public class ScaleBar extends WebTrackBase {
	private static final long serialVersionUID = 1L;
	private static Logger _logger = Logger.getLogger(ScaleBar.class);

	/**
	 * Describe your web action parameters here. Public fields in this class will be set using the web request query
	 * parameters before calling handle().
	 */

	/**
	 * Predefined coordinate parameters for GenomeTrack. Uncomment the following lines if you want to receive these
	 * parameter values.
	 */
	// public String species;   /* human, mouse, etc. */
	// public String revision;  /* hg19, mm9 ... */
	// public String name;	    /* chr1, chr2, ... */
	public long start; /* start position on the genome */
	public long end; /* end position on the genome (inclusive) */
	public int width = 700; /* track pixel width */
	public int height = 20;

	private final int[] template = { 5, 2, 1 };
	private long range;
	private BufferedImage image;
	private Graphics2D g;

	/**
	 * Use dbGroup, dbName parameters to specify database contents to be accessed
	 */
	// public String dbGroup;   /* database group */
	// public String dbName;    /* database name in the group */ 

	public ScaleBar() {
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// write your own code to generate an web page here.
		reverse();
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		g = image.createGraphics();
		draw();

		response.setContentType("image/png");
		ImageIO.write(image, "png", response.getOutputStream());
		// Generate debug log messages. (log level: trace, debug, info, warn, error, fatal)
		// You can switch log level by specifying -Dloglevel=debug in the eclipse launch file, or 
		// use -l (log level) option in the utgb command.
		// _logger.debug("debug message");
	}

	private void reverse() {
		if (start > end) {
			long tmp = start;
			start = end;
			end = tmp;
		}
		range = end - start + 1;
	}

	private void draw() {
		long half_range = range / 2;
		int digit = Long.toString(half_range).length();
		long unit = (long) Math.pow(10, digit - 1);

		for (int i = 0; i < template.length; i++) {
			if (half_range >= template[i] * unit) {
				unit *= template[i];
				break;
			}
		}
		double ratio = (double) unit / (double) range;
		int x_left = (int) ((0.5 - ratio / 2.0) * width + 0.5);
		int x_right = (int) ((0.5 + ratio / 2.0) * width + 0.5);

		int unit_digit = Long.toString(unit).length();
		int unit_remainder = (unit_digit - 1) % 3;
		unit_remainder++;
		int unit_quotient = (unit_digit - 1) / 3;

		String suffix = "";
		if (unit_quotient == 0) {
		}
		else if (unit_quotient == 1) {
			suffix = "k";
		}
		else if (unit_quotient == 2) {
			suffix = "M";
		}
		else if (unit_quotient == 3) {
			suffix = "G";
		}
		else if (unit_quotient == 4) {
			suffix = "T";
		}

		g.setColor(Color.BLACK);
		g.drawLine(x_left, height / 2, x_right, height / 2);
		g.drawLine(x_left, height * 2 / 5, x_left, height * 3 / 5);
		g.drawLine(x_right, height * 2 / 5, x_right, height * 3 / 5);

		Font f = new Font("SansSerif", Font.PLAIN, 10);
		g.setFont(f);
		g.drawString(Long.toString(unit).substring(0, unit_remainder) + suffix + "b", x_right + 5, height / 2 + height / 15);
	}
}
