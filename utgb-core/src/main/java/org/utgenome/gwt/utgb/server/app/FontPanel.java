//--------------------------------------
//
// Font.java
// Since: 2009/07/15
//
//--------------------------------------
package org.utgenome.gwt.utgb.server.app;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.utgenome.gwt.utgb.server.WebTrackBase;
import org.xerial.util.FileResource;
import org.xerial.util.Pair;
import org.xerial.util.log.Logger;

/**
 * Font panel
 * 
 */
public class FontPanel extends WebTrackBase {
	private static final long serialVersionUID = 1L;
	private static Logger _logger = Logger.getLogger(FontPanel.class);

	public FontPanel() {

	}

	public float fontSize = 10;
	public String color = "#003366";

	public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Pair<FontInfo, Font> fontInfo = getFontInfo(fontSize);
		FontInfo fi = fontInfo.getFirst();

		BufferedImage bf = new BufferedImage(fi.width * 256, fi.height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) bf.getGraphics();
		g.setColor(new Color(0, 0, 0, 0));
		g.fillRect(0, 0, bf.getWidth(), bf.getHeight());

		Color col;
		try {
			col = Color.decode(color);
		}
		catch (NumberFormatException e) {
			col = Color.BLACK;
		}

		g.setColor(col);
		g.setFont(fontInfo.getSecond());
		//g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		for (char c = 0; c < 256; c++)
			g.drawString(String.valueOf(c), c * fi.width, fi.baseLine);

		response.setContentType("image/png");
		ImageIO.write(bf, "png", response.getOutputStream());

	}

	public static class FontInfo {
		public float size;
		public int height;
		public int width;
		public int baseLine;
	}

	public static Pair<FontInfo, Font> getFontInfo(float fontSize) throws IOException {
		BufferedImage b = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		Graphics2D gEnv = (Graphics2D) b.getGraphics();

		Font onePixelFont;
		try {
			URL fontFile = FileResource.find(FontPanel.class, "Anonymous.ttf");
			if (fontFile == null)
				onePixelFont = new Font("Monaco", Font.TRUETYPE_FONT, 1);
			else
				onePixelFont = Font.createFont(Font.TRUETYPE_FONT, fontFile.openStream());
		}
		catch (FontFormatException e) {
			throw new IOException(e.getMessage());
		}
		Font font = onePixelFont.deriveFont(fontSize);
		FontMetrics fm = gEnv.getFontMetrics(font);

		FontInfo fi = new FontInfo();
		fi.size = fontSize;
		fi.height = fm.getHeight() + fm.getMaxDescent();
		fi.width = fm.stringWidth("A");
		fi.baseLine = fm.getHeight() - fm.getMaxDescent();

		return new Pair<FontInfo, Font>(fi, font);
	}

}
