//--------------------------------------
//
// DASViewer.java
// Since: 2009/05/21
//
//--------------------------------------
package org.utgenome.gwt.utgb.server.app;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.utgenome.UTGBErrorCode;
import org.utgenome.UTGBException;
import org.utgenome.graphics.GeneCanvas;
import org.utgenome.graphics.GenomeWindow;
import org.utgenome.gwt.utgb.client.bio.ChrLoc;
import org.utgenome.gwt.utgb.client.bio.DASResult;
import org.utgenome.gwt.utgb.server.WebTrackBase;
import org.xerial.core.XerialException;
import org.xerial.lens.Lens;
import org.xerial.util.log.Logger;

/**
 * Request handler
 * 
 */
public class DASViewer extends WebTrackBase {

	private static final long serialVersionUID = 1L;
	private static Logger _logger = Logger.getLogger(DASViewer.class);

	public int start = 1;
	public int end = 1;
	public int width = 800;
	public String name = "";
	public String dasBaseURL = "";
	public String dasType = null;

	public DASViewer() {
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//		String baseurl = "http://www.ensembl.org/das/Homo_sapiens.NCBI36.transcript";

		try {
			DASResult result = queryDAS(dasBaseURL, dasType, new ChrLoc(name, start, end));

			GeneCanvas geneCanvas = new GeneCanvas(width, 300, new GenomeWindow(start, end));
			if (result.segment.feature != null)
				geneCanvas.draw(result.segment.feature);

			response.setContentType("image/png");
			geneCanvas.toPNG(response.getOutputStream());
		}
		catch (UTGBException e) {
			e.printStackTrace();
		}
	}

	public static DASResult queryDAS(String baseURL, String dasType, ChrLoc chrLoc) throws UTGBException {
		if (!baseURL.endsWith("/"))
			baseURL += "/";

		String format = "%sfeatures?segment=%s:%d,%d";
		if (dasType != null && !dasType.matches(""))
			format += ";type=" + dasType;

		String chr = chrLoc.chr;
		if (chr == null)
			chr = "1";
		else
			chr = chr.replace("chr", "");

		int start = chrLoc.start;
		int end = chrLoc.end;

		String url = String.format(format, baseURL, chr, start <= end ? start : end, start <= end ? end : start);

		if (_logger.isDebugEnabled())
			_logger.debug(String.format("accessing DAS: %s", url));

		try {
			return Lens.loadXML(DASResult.class, new URL(url));
		}
		catch (MalformedURLException e) {
			throw new UTGBException(UTGBErrorCode.INVALID_INPUT, e);
		}
		catch (IOException e) {
			throw new UTGBException(UTGBErrorCode.IO_ERROR, e);
		}
		catch (XerialException e) {
			throw new UTGBException(UTGBErrorCode.PARSE_ERROR, e);
		}
	}

	/*
	 * 
	 <pre>
	 <GFF version="1.01" href="http://www.ensembl.org/das/Homo_sapiens.NCBI36.transcript/features?segment=13:31787617,31871806"> 
	<SEGMENT id="13" start="31787617" stop="31871806"> 
	<FEATURE id="ENSE00001543192"> 
	<START>31856797</START> 
	<END>31856935</END> 
	<TYPE id="exon:non_coding:ensembl" category="transcription">exon:non_coding:ensembl</TYPE> 
	<METHOD id="ensembl">ensembl</METHOD> 
	<SCORE>-</SCORE> 
	<ORIENTATION>+</ORIENTATION> 
	<GROUP id="ENST00000400497" type="transcript:ensembl" label="ENST00000400497 (AL445212.9-201)"> 
	  <LINK href="http://www.ensembl.org/Homo_sapiens/Transcript/Summary?t=ENST00000400497;db=core">TransView ENST00000400497</LINK> 
	</GROUP> 
	<TARGET id="ENST00000400497" start="1" stop="139" /> 
	</FEATURE> 
	</pre>
	 */

}
