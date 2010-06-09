/*--------------------------------------------------------------------------
 *  Copyright 2009 utgenome.org
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
// SAMTrack.java
// Since: Mar. 15, 2010
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.lib;

import java.util.List;

import org.utgenome.gwt.utgb.client.bio.SAMRead;
import org.utgenome.gwt.utgb.client.canvas.SAMCanvas;
import org.utgenome.gwt.utgb.client.db.Value;
import org.utgenome.gwt.utgb.client.db.ValueDomain;
import org.utgenome.gwt.utgb.client.db.datatype.StringType;
import org.utgenome.gwt.utgb.client.track.Track;
import org.utgenome.gwt.utgb.client.track.TrackBase;
import org.utgenome.gwt.utgb.client.track.TrackConfig;
import org.utgenome.gwt.utgb.client.track.TrackConfigChange;
import org.utgenome.gwt.utgb.client.track.TrackFrame;
import org.utgenome.gwt.utgb.client.track.TrackGroup;
import org.utgenome.gwt.utgb.client.track.TrackWindow;
import org.utgenome.gwt.utgb.client.util.Properties;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

/**
 * Track for displaying SAM/BAM query result
 * 
 * @author yoshimura
 * 
 */
public class SAMQueryTrack extends TrackBase {
	private final boolean isDebug = true;
	private boolean isC2T = false;

	protected String trackBaseURL;

	protected String target = "chr13";
	protected int start = 3000000;
	protected int end = 3100000;

	protected String bamFileName = null;
	protected String indexFileName = null;
	protected String refSeqFileName = null;
	protected String colorMode = "nucleotide";

	private final FlexTable layoutTable = new FlexTable();
	private final SAMCanvas samCanvas = new SAMCanvas();
	private final AbsolutePanel labelPanel = new AbsolutePanel();

	private int height = 500;
	private int leftMargin = 100;
	private int labelWidth = 100;

	private List<SAMRead> readDataList;
	private String refSeq;

	public static TrackFactory factory() {
		return new TrackFactory() {
			@Override
			public Track newInstance() {
				return new SAMQueryTrack();
			}
		};
	}

	public SAMQueryTrack() {
		super("SAM Query Viewer Track");

		// prepare the widgets
		layoutTable.setCellPadding(0);
		layoutTable.setCellSpacing(0);
		layoutTable.setBorderWidth(0);
		layoutTable.setWidth("100%");
		//		layoutTable.getCellFormatter().setWidth(0, 0, leftMargin + "px");
		layoutTable.setWidget(1, 0, labelPanel);
		layoutTable.setWidget(1, 1, samCanvas);

	}

	public Widget getWidget() {
		return layoutTable;
	}

	@Override
	public void setUp(TrackFrame trackFrame, TrackGroup group) {

		TrackConfig config = getConfig();
		config.addConfig("BAM File Name", new StringType("bamFileName"), bamFileName);
		//		config.addConfigParameter("Index File Name", new StringType("indexFileName"), indexFileName);
		indexFileName = bamFileName + ".bai";
		config.addConfig("Reference Sequence File Name", new StringType("refSeqFileName"), refSeqFileName);
		ValueDomain colorModeDomain = new ValueDomain();
		colorModeDomain.addValueList(new Value("nucleotide"));
		colorModeDomain.addValueList(new Value("mapping quality"));
		colorModeDomain.addValueList(new Value("base quality"));
		config.addConfig("Color Mode", new StringType("colorMode", colorModeDomain), colorMode);
		config.addConfig("Target Name", new StringType("target"), target);
		config.addConfig("Start", new StringType("start"), String.valueOf(start));
		config.addConfig("End", new StringType("end"), String.valueOf(end));
		samCanvas.setWindow(group.getTrackWindow(), leftMargin);

		update(group.getTrackWindow());
	}

	class UpdateCommand implements Command {
		private final List<SAMRead> readList;
		private final String refSeq;

		public UpdateCommand(List<SAMRead> readList, String refSeq) {
			this.readList = readList;
			this.refSeq = refSeq;
		}

		public void execute() {
			TrackWindow w = getTrackGroup().getTrackWindow();

			height = getDefaultWindowHeight();
			// get graph x-range
			int s = w.getStartOnGenome();
			int e = w.getEndOnGenome();
			int width = w.getPixelWidth() - leftMargin;

			labelPanel.clear();
			labelPanel.setPixelSize(leftMargin, height);

			samCanvas.clear();
			samCanvas.setWindow(new TrackWindow(width, s, e), leftMargin);
			samCanvas.setC2T(isC2T);
			samCanvas.setColorMode(colorMode);

			// draw data graph
			//	        int count = 0;
			//	        for(SAMRead read : readList){
			//	        	samCanvas.drawSAMRead(count, read);
			//	        	samCanvas.drawLabelPanel(count, read, labelPanel, leftMargin);
			//	        	count++;
			//	        }
			samCanvas.drawSAMRead(readList);
			samCanvas.drawLabelPanel(readList, labelPanel, leftMargin);

			refresh();
			getFrame().loadingDone();
		}
	}

	public void update(TrackWindow newWindow) {
		getFrame().setNowLoading();

		getBrowserService().querySAMReadList(bamFileName, indexFileName, refSeqFileName, target, start, end, new AsyncCallback<List<SAMRead>>() {

			public void onFailure(Throwable e) {
				GWT.log("failed to retrieve sam data", e);
				getFrame().loadingDone();
			}

			public void onSuccess(List<SAMRead> dataList) {
				GWT.log("read sam", null);
				readDataList = dataList;

				if (isDebug)
					for (SAMRead read : dataList) {
						GWT.log("read : " + read.qname, null);
					}

				//				GenomeBrowser.getService().getRefSeq(refSeqFileName, target, start, end, new AsyncCallback<String>() {
				//
				//					public void onFailure(Throwable e) {
				//						GWT.log("failed to retrieve sam data", e);
				//						getFrame().loadingDone();
				//					}
				//
				//					public void onSuccess(String sequence) {
				//						GWT.log("read refSeq", null);
				//						refSeq = sequence;
				//						
				//						if (isDebug) 
				//							GWT.log("refSeq : " + refSeq , null);

				DeferredCommand.addCommand(new UpdateCommand(readDataList, refSeq));
				//					}
				//				});
			}
		});

	}

	@Override
	public void onChangeTrackWindow(TrackWindow newWindow) {
		//		samCanvas.setWindow(newWindow, leftMargin);
	}

	@Override
	public void onChangeTrackConfig(TrackConfigChange change) {
		boolean isUpdate = false;

		if (isDebug) {
			for (String key : change.getChangedParamSet()) {
				GWT.log("Change : " + key + " : " + change.getValue(key), null);
			}
		}

		if (change.contains("bamFileName")) {
			bamFileName = change.getValue("bamFileName");
			indexFileName = bamFileName + ".bai";
			isUpdate = true;
		}
		//		if (change.contains("indexFileName")) {
		//			indexFileName = change.getValue("indexFileName");
		//			isUpdate = true;
		//		}
		if (change.contains("refSeqFileName")) {
			refSeqFileName = change.getValue("refSeqFileName");
			isUpdate = true;
		}
		if (change.contains("target")) {
			target = change.getValue("target");
			isUpdate = true;
		}
		if (change.contains("start")) {
			start = change.getIntValue("start");
			isUpdate = true;
		}
		if (change.contains("end")) {
			end = change.getIntValue("end");
			isUpdate = true;
		}
		if (change.contains("colorMode")) {
			colorMode = change.getValue("colorMode");
		}
		if (change.contains("isC2T")) {
			isC2T = change.getBoolValue("isC2T");
		}

		if (isUpdate) {
			update(getTrackWindow());
		}
		else {
			getFrame().setNowLoading();
			DeferredCommand.addCommand(new UpdateCommand(readDataList, refSeq));
		}
	}

	//	public void saveProperties(Properties saveData) {
	//		saveData.add("bamFileName", bamFileName);
	//		//		saveData.add("indexFileName", indexFileName);
	//		saveData.add("redSeqFileName", refSeqFileName);
	//		saveData.add("colorMode", colorMode);
	//		saveData.add("isC2T", isC2T);
	//		saveData.add("leftMargin", leftMargin);
	//		saveData.add("target", target);
	//		saveData.add("start", start);
	//		saveData.add("end", end);
	//	}

	@Override
	public void restoreProperties(Properties properties) {
		super.restoreProperties(properties);

		bamFileName = properties.get("bamFileName", bamFileName);
		//		indexFileName = properties.get("indexFileName", indexFileName);
		refSeqFileName = properties.get("refSeqFileName", refSeqFileName);
		colorMode = properties.get("colorMode", colorMode);
		isC2T = properties.getBoolean("isC2T", isC2T);
		leftMargin = properties.getInt("leftMargin", leftMargin);

		target = properties.get("target", target);
		start = properties.getInt("start", start);
		end = properties.getInt("end", end);

		String p = properties.get("changeParamOnClick");
		if (p != null) {
			// set canvas action

		}
	}

}
