/*--------------------------------------------------------------------------
 *  Copyright 2007 utgenome.org
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
// GenomeBrowser Project
//
// GeneTableTrack.java
// Since: 2007/06/05
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.lib.debug;

import org.utgenome.gwt.utgb.client.db.datatype.StringType;
import org.utgenome.gwt.utgb.client.track.Track;
import org.utgenome.gwt.utgb.client.track.TrackBase;
import org.utgenome.gwt.utgb.client.track.TrackConfig;
import org.utgenome.gwt.utgb.client.track.TrackConfigChange;
import org.utgenome.gwt.utgb.client.track.TrackFrame;
import org.utgenome.gwt.utgb.client.track.TrackGroup;
import org.utgenome.gwt.utgb.client.ui.EditableTable;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A demo track, which shows how to display table data on the browser screen. {@link SampleGeneTableTrack} shares the
 * same {@link GeneList} data with {@link SampleGeneTrack}.
 * 
 * 
 * @author ssksn
 * 
 */
public class SampleGeneTableTrack extends TrackBase implements GeneListObserver {

	public static TrackFactory factory() {
		return new TrackFactory() {
			public Track newInstance() {
				return new SampleGeneTableTrack();
			}
		};
	}

	private DockPanel _panel = new DockPanel();
	private Button _addButton = new Button("add random gene");
	private Button _clearButton = new Button("clear");

	private EditableTable _table = new EditableTable(new String[] { "id", "gene name", "start", "end" });
	private GeneList _geneList = null;
	private TrackConfig _config = new TrackConfig(this);

	private String GENE_PREFIX = "gene";

	public SampleGeneTableTrack() {
		super("Sample Gene Table");

		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.add(_addButton);
		buttonPanel.add(_clearButton);
		_panel.add(buttonPanel, DockPanel.NORTH);
		_panel.add(_table, DockPanel.CENTER);

		_addButton.addClickHandler(new ClickHandler() {
			int count = 1;

			public void onClick(ClickEvent sender) {
				int start = Random.nextInt(1000000);
				int size = Random.nextInt(10000);
				_geneList.add(new GeneData(GENE_PREFIX + count++, start, start + size));
			}
		});

		_clearButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent sender) {
				_geneList.clear();
			}
		});

	}

	void drawGene(GeneData gene) {
		if (_geneList == null)
			return;

		int geneStart = gene.getStart();
		int geneEnd = gene.getEnd();
		String name = gene.getName();

		_table.addRow(new String[] { "0", name, Integer.toString(geneStart), Integer.toString(geneEnd) });

	}

	public void draw() {
		if (_geneList == null)
			return;
		_table.removeAllRows();
		for (GeneData gene : _geneList) {
			drawGene(gene);
		}
	}

	public Widget getWidget() {
		return _panel;
	}

	public void onNewGeneAdded(GeneData gene) {
		drawGene(gene);
		getFrame().onUpdateTrackWidget();
	}

	public void onGeneListIsCleared() {
		draw();
		getFrame().onUpdateTrackWidget();
	}

	public int getDefaultWindowHeight() {
		return 100;
	}

	public void setUp(TrackFrame trackFrame, TrackGroup group) {
		SampleGeneTrackGroup geneGroup = (SampleGeneTrackGroup) group;
		_geneList = geneGroup.getGeneList();
		_geneList.addObserver(this);

		_config.addConfigParameter("randome gene prefix", new StringType("prefix"), GENE_PREFIX);
	}

	public TrackConfig getConfig() {
		return _config;
	}

	public void onChange(TrackConfigChange change) {
		if (change.contains("prefix")) {
			GENE_PREFIX = change.getValue("prefix");
			draw();
		}
	}

}
