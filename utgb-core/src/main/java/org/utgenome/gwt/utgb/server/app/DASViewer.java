//--------------------------------------
//
// DASViewer.java
// Since: 2009/05/21
//
//--------------------------------------
package org.utgenome.gwt.utgb.server.app;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.utgenome.graphics.GeneCanvas;
import org.utgenome.graphics.GenomeWindow;
import org.utgenome.gwt.utgb.client.bio.Read;
import org.utgenome.gwt.utgb.server.WebTrackBase;
import org.xerial.core.XerialException;
import org.xerial.lens.Lens;
import org.xerial.lens.ObjectLens;
import org.xerial.util.log.Logger;

/**
 * Request handler
 * 
 */
public class DASViewer extends WebTrackBase {

	public void setStart(long start) {
		this.start = start;
	}

	public void setEnd(long end) {
		this.end = end;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	private static final long serialVersionUID = 1L;
	private static Logger _logger = Logger.getLogger(DASViewer.class);

	private long start = 1;
	private long end = 1;
	private int width = 800;
	private String name = "";
	private String baseurl = "";
	private String dasType = null;

	public DASViewer() {
	}

	public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//		String baseurl = "http://www.ensembl.org/das/Homo_sapiens.NCBI36.transcript";
		if (!baseurl.endsWith("/"))
			baseurl += "/";

		String format = "%sfeatures?segment=%s:%d,%d";
		if (dasType != null && !dasType.matches(""))
			format += ";type=" + dasType;

		String url = String.format(format, baseurl, name.substring(3), start <= end ? start : end, start <= end ? end : start);
		try {
			_logger.info(url);
			DASFeature feature = Lens.loadXML(DASFeature.class, new URL(url));

			if (_logger.isTraceEnabled())
				_logger.trace(ObjectLens.toJSON(feature));

			GeneCanvas geneCanvas = new GeneCanvas(width, 300, new GenomeWindow(start, end));
			if (feature.segment.feature != null)
				geneCanvas.draw(feature.segment.feature);

			response.setContentType("image/png");
			geneCanvas.toPNG(response.getOutputStream());

		}
		catch (XerialException e) {
			_logger.error(e);
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

	public static class DASFeature {
		public DASGFF gff;
		public Segment segment;

		@Override
		public String toString() {
			return ObjectLens.toJSON(this);
		}
	}

	public static class DASGFF {
		public String version;
		public String href;

	}

	public static class Segment {
		public String id;
		public long start;
		public long stop;
		public List<Feature> feature;
	}

	public static class Feature extends Read {
		public String id;

		public String label;
		public String score;
		public String orientation;
		public String phase;

		public Method method;
		public FeatureType type;
		public Group group;
		public Target target;

		public void setId(String id) {
			setName(id);
		}
	}

	public static class Target {
		public String id;
		public long start;
		public long stop;
		public String value;
	}

	public static class FeatureType {
		public String id;
		public String category;
		public String reference;
		public String value;
	}

	public static class Group {
		public String id;
		public String type;
		public String label;
		public Link link;
		public Target target;
	}

	public static class Link {
		public String href;
		public String value;
	}

	public static class Method {
		public String id;
		public String value;
	}

	public void setDasBaseURL(String dasBaseURL) {
		this.baseurl = dasBaseURL;
	}

	public void setDasType(String dasType) {
		this.dasType = dasType;
	}
}
