//--------------------------------------
//
// ACGT.java
// Since: Jul 28, 2010
//
//--------------------------------------
package org.utgenome.gwt.utgb.server.app;

import java.awt.Color;
import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.utgenome.graphics.GenomeCanvas;
import org.utgenome.graphics.GenomeWindow;
import org.utgenome.gwt.utgb.server.WebTrackBase;
import org.utgenome.gwt.utgb.server.util.graphic.GraphicUtil;
import org.xerial.util.log.Logger;

/**
 * Web action: ACGT
 * 
 */
public class ACGT extends WebTrackBase {
	private static final long serialVersionUID = 1L;
	private static Logger _logger = Logger.getLogger(ACGT.class);

	/**
	 * Describe your web action parameters here. Public fields in this class will be set using the web request query
	 * parameters before calling handle().
	 */
	private static final int DEFAULT_HEIGHT = 12;

	private static final String DEFAULT_COLOR_A = "50B6E8";
	private static final String DEFAULT_COLOR_C = "E7846E";
	private static final String DEFAULT_COLOR_G = "84AB51";
	private static final String DEFAULT_COLOR_T = "FFA930";
	private static final String DEFAULT_COLOR_N = "FFFFFF";

	public String colorA = DEFAULT_COLOR_A;
	public String colorC = DEFAULT_COLOR_C;
	public String colorG = DEFAULT_COLOR_G;
	public String colorT = DEFAULT_COLOR_T;
	public String colorN = DEFAULT_COLOR_N;

	public int fontWidth = 12;
	public int height = DEFAULT_HEIGHT;
	private static float fontSize = 10.5f;

	private HashMap<Character, Color> colorTable = new HashMap<Character, Color>();

	public ACGT() {
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		if (fontWidth < 1 || height < 1) {
			return;
		}

		final int letterSize = 9; // ACGTacgtN

		boolean drawLetter = height >= DEFAULT_HEIGHT - 2;

		final HashMap<Character, String> colorTable = new HashMap<Character, String>();
		colorTable.put('A', colorA);
		colorTable.put('C', colorC);
		colorTable.put('G', colorG);
		colorTable.put('T', colorT);
		colorTable.put('N', colorN);
		GenomeCanvas canvas = new GenomeCanvas(fontWidth * letterSize, height * 2, new GenomeWindow(1, letterSize));

		final String letters = "ACGTacgtN";

		final int repeatColorAlpha = 70;
		final Color defaultTextColor = new Color(255, 255, 255);
		final Color repeatTextColor = new Color(140, 140, 140);

		for (int y = 0; y < 2; y++) {
			int baseYPos = height * (y + 1) - 2;
			long offset = 1;
			for (int i = 0; i < letters.length(); ++i) {
				char ch = letters.charAt(i);
				boolean isRepeatChar = Character.isLowerCase(ch);
				if (isRepeatChar)
					ch = Character.toUpperCase(ch);

				Color textColor = isRepeatChar ? repeatTextColor : defaultTextColor;
				if (y == 0) {
					String colorStr = colorTable.containsKey(ch) ? colorTable.get(ch) : "E0E0E0";
					Color color = isRepeatChar ? GraphicUtil.parseColor(colorStr, repeatColorAlpha) : GraphicUtil.parseColor(colorStr);
					canvas.drawGeneRect(offset, offset + 1L, 0, height, color);
				}
				//				else {
				//					textColor = defaultTextColor;
				//				}

				if (drawLetter)
					canvas.drawBase(letters.substring(i, i + 1), offset, offset + 1L, baseYPos, fontSize, textColor);

				offset++;
			}
		}
		canvas.outputImage(response, "png");
	}

}
