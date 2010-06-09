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
import org.utgenome.gwt.utgb.client.ui.FormLabel;
import org.utgenome.gwt.utgb.client.util.Properties;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Track for displaying SAM information
 * 
 * @author yoshimura
 * 
 */
public class SAMTrack extends TrackBase {
	private final boolean isDebug = true;
	private boolean isC2T = false;
	protected String readFileName = null;
	protected String refSeqFileName = null;
	protected String colorMode = "nucleotide";

	private final FlexTable layoutTable = new FlexTable();
	private final SAMCanvas samCanvas = new SAMCanvas();
	private final AbsolutePanel labelPanel = new AbsolutePanel();
	private final ListBox readListBox = new ListBox();

	private int height = 500;
	private int leftMargin = 100;
	private int labelWidth = 100;

	private List<SAMRead> readDataList;
	private String choosedReadName = new String();

	public static TrackFactory factory() {
		return new TrackFactory() {
			@Override
			public Track newInstance() {
				return new SAMTrack();
			}
		};
	}

	public SAMTrack() {
		super("SAM Viewer Track");

		// prepare the widgets
		layoutTable.setCellPadding(0);
		layoutTable.setCellSpacing(0);
		layoutTable.setBorderWidth(0);
		layoutTable.setWidth("100%");
		//		layoutTable.getCellFormatter().setWidth(0, 0, leftMargin + "px");
		layoutTable.setWidget(1, 0, labelPanel);
		//		layoutTable.setWidget(0, 1, readListBox);
		layoutTable.setWidget(1, 1, samCanvas);

		// Set the value in the text box when the user selects a date
		readListBox.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent e) {
				choosedReadName = readDataList.get(readListBox.getSelectedIndex()).qname;
				getFrame().setNowLoading();
				DeferredCommand.addCommand(new UpdateCommand(readDataList));
			}
		});
	}

	public Widget getWidget() {
		return layoutTable;
	}

	@Override
	public void setUp(TrackFrame trackFrame, TrackGroup group) {
		TrackConfig config = getConfig();
		config.addConfig("Read File Name", new StringType("readFileName"), readFileName);
		config.addConfig("Reference Sequence File Name", new StringType("refSeqFileName"), refSeqFileName);
		ValueDomain colorModeDomain = new ValueDomain();
		colorModeDomain.addValueList(new Value("nucleotide"));
		colorModeDomain.addValueList(new Value("mapping quality"));
		colorModeDomain.addValueList(new Value("base quality"));
		config.addConfig("Color Mode", new StringType("colorMode", colorModeDomain), colorMode);
		samCanvas.setWindow(group.getTrackWindow(), leftMargin);

		update(group.getTrackWindow());
	}

	class UpdateCommand implements Command {
		private final List<SAMRead> readList;

		public UpdateCommand(List<SAMRead> readList) {
			this.readList = readList;
		}

		public void execute() {
			TrackWindow w = getTrackGroup().getTrackWindow();

			height = getDefaultWindowHeight();
			// get graph x-range
			int s = w.getStartOnGenome();
			int e = w.getEndOnGenome();
			int width = w.getPixelWidth() - leftMargin;

			labelPanel.clear();
			for (SAMRead temp : readDataList) {
				FormLabel tempLabel = new FormLabel(temp.qname);
				labelPanel.add(tempLabel, 0, 0);
				labelWidth = tempLabel.getOffsetWidth() > labelWidth ? tempLabel.getOffsetWidth() : labelWidth;
				labelPanel.remove(tempLabel);

				tempLabel = new FormLabel(temp.rname);
				labelPanel.add(tempLabel, 0, 0);
				labelWidth = tempLabel.getOffsetWidth() > labelWidth ? tempLabel.getOffsetWidth() : labelWidth;
				labelPanel.remove(tempLabel);

				if (samCanvas.getReadWidth(temp.cigar) > width)
					width = samCanvas.getReadWidth(temp.cigar);
			}
			labelPanel.setPixelSize(labelWidth, height);

			samCanvas.clear();
			samCanvas.setWindow(new TrackWindow(width, s, e), leftMargin);
			samCanvas.setC2T(isC2T);
			samCanvas.setColorMode(colorMode);

			//	        if(isDebug)GWT.log("choosed : " + choosedReadName, null);

			// draw data graph
			int count = 0;
			for (SAMRead read : readList) {
				//	        	if(read.qname.equals(choosedReadName)){
				samCanvas.drawSAMRead(count, read);
				samCanvas.drawLabelPanel(count, read, labelPanel, leftMargin);
				count++;
				//	        	}
			}
			//			refresh();
			getFrame().loadingDone();
		}
	}

	public void update(TrackWindow newWindow) {

		getFrame().setNowLoading();

		getBrowserService().getSAMReadList(readFileName, refSeqFileName, new AsyncCallback<List<SAMRead>>() {

			public void onFailure(Throwable e) {
				GWT.log("failed to retrieve sam data", e);
				getFrame().loadingDone();
			}

			public void onSuccess(List<SAMRead> dataList) {
				readDataList = dataList;
				readListBox.clear();

				for (SAMRead read : dataList) {
					if (isDebug)
						GWT.log("read : " + read.qname, null);

					readListBox.addItem(read.qname);
					if (choosedReadName.isEmpty())
						choosedReadName = read.qname;
				}
				readListBox.setVisibleItemCount(1);
				DeferredCommand.addCommand(new UpdateCommand(readDataList));
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

		if (change.contains("readFileName")) {
			readFileName = change.getValue("readFileName");
			isUpdate = true;
		}
		if (change.contains("refSeqFileName")) {
			refSeqFileName = change.getValue("refSeqFileName");
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
			DeferredCommand.addCommand(new UpdateCommand(readDataList));
		}
	}

	//	public void saveProperties(Properties saveData) {
	//		saveData.add("readFileName", readFileName);
	//		saveData.add("redSeqFileName", refSeqFileName);
	//		saveData.add("colorMode", colorMode);
	//		saveData.add("isC2T", isC2T);
	//		saveData.add("leftMargin", leftMargin);
	//	}

	@Override
	public void restoreProperties(Properties properties) {
		super.restoreProperties(properties);

		readFileName = properties.get("readFileName", readFileName);
		refSeqFileName = properties.get("refSeqFileName", refSeqFileName);
		colorMode = properties.get("colorMode", colorMode);
		isC2T = properties.getBoolean("isC2T", isC2T);
		leftMargin = properties.getInt("leftMargin", leftMargin);

		String p = properties.get("changeParamOnClick");
		if (p != null) {
			// set canvas action

		}
	}

}
