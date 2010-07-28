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

		final int letterSize = 9; // ACGTacgtN

		GenomeCanvas canvas = new GenomeCanvas(fontWidth * letterSize, height, new GenomeWindow(1, letterSize + 1));
		final Color textColor = new Color(255, 255, 255);
		long offset = 1;
		canvas.drawGeneRect(offset, offset + 1L, 0, height, GraphicUtil.parseColor(colorA));
		canvas.drawBase("A", offset, offset + 1L, height - 2, fontSize, textColor);
		offset++;
		canvas.drawGeneRect(offset, offset + 1L, 0, height, GraphicUtil.parseColor(colorC));
		canvas.drawBase("C", offset, offset + 1L, height - 2, fontSize, textColor);
		offset++;
		canvas.drawGeneRect(offset, offset + 1L, 0, height, GraphicUtil.parseColor(colorG));
		canvas.drawBase("G", offset, offset + 1L, height - 2, fontSize, textColor);
		offset++;
		canvas.drawGeneRect(offset, offset + 1L, 0, height, GraphicUtil.parseColor(colorT));
		canvas.drawBase("T", offset, offset + 1L, height - 2, fontSize, textColor);

		final int repeatColorAlpha = 70;
		final Color repeatTextColor = new Color(140, 140, 140);
		offset++;
		canvas.drawGeneRect(offset, offset + 1L, 0, height, GraphicUtil.parseColor(colorA, repeatColorAlpha));
		canvas.drawBase("a", offset, offset + 1L, height - 2, fontSize, repeatTextColor);
		offset++;
		canvas.drawGeneRect(offset, offset + 1L, 0, height, GraphicUtil.parseColor(colorC, repeatColorAlpha));
		canvas.drawBase("c", offset, offset + 1L, height - 2, fontSize, repeatTextColor);
		offset++;
		canvas.drawGeneRect(offset, offset + 1L, 0, height, GraphicUtil.parseColor(colorG, repeatColorAlpha));
		canvas.drawBase("g", offset, offset + 1L, height - 2, fontSize, repeatTextColor);
		offset++;
		canvas.drawGeneRect(offset, offset + 1L, 0, height, GraphicUtil.parseColor(colorT, repeatColorAlpha));
		canvas.drawBase("t", offset, offset + 1L, height - 2, fontSize, repeatTextColor);

		offset++;
		canvas.drawGeneRect(offset, offset + 1L, 0, height, GraphicUtil.parseColor(colorN));
		canvas.drawBase("N", offset, offset + 1L, height - 2, fontSize, repeatTextColor);

		canvas.outputImage(response, "png");
	}

}
