/*--------------------------------------------------------------------------
 *  Copyright 2008 utgenome.org
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
// AlignmentViewTrack.java
// Since: Sep 5, 2008
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.lib;

import org.utgenome.gwt.utgb.client.bio.AlignmentResult;
import org.utgenome.gwt.utgb.client.canvas.AlignmentCanvas;
import org.utgenome.gwt.utgb.client.track.Track;
import org.utgenome.gwt.utgb.client.track.TrackBase;
import org.utgenome.gwt.utgb.client.track.UTGBProperty;
import org.utgenome.gwt.utgb.client.ui.FormLabel;
import org.utgenome.gwt.utgb.client.ui.Message;
import org.utgenome.gwt.utgb.client.util.Properties;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Alignment Result Viewer
 * 
 * @author leo
 * 
 */
public class AlignmentViewTrack extends TrackBase {

	public static TrackFactory factory() {
		return new TrackFactory() {
			public Track newInstance() {
				return new AlignmentViewTrack();
			}
		};
	}

	/**
	 * sample URL
	 * http://svn.utgenome.org/utgb/trunk/utgb/utgb-core/src/test/java/org/utgenome/gwt/utgb/server/alignment.json
	 * "http://svn.utgenome.org/utgb/trunk/utgb/utgb-core/src/test/java/org/utgenome/gwt/utgb/server/alignment.json"
	 */
	private String serviceURL = "http://157.82.238.56/webapp/onlineMapping/JsonAlignment?id=20080910134319_23651";

	private static final String PROP_SERVICE_URL = "serviceURL";

	private FlexTable layoutTable = new FlexTable();
	private AlignmentCanvas canvas;
	private SequenceInputForm sequenceInputForm = new SequenceInputForm();

	public AlignmentViewTrack() {
		this("Alignment View");
	}

	public AlignmentViewTrack(String trackName) {
		super(trackName);

		canvas = new AlignmentCanvas(this);

		FormLabel label = new FormLabel("Sequence:");
		layoutTable.setWidget(0, 0, label);
		layoutTable.setWidget(0, 1, sequenceInputForm);
		layoutTable.setWidget(2, 0, new FormLabel("Alignment:"));
		layoutTable.setWidget(2, 1, canvas);

	}

	class SequenceInputForm extends Composite implements ClickHandler {

		private TextArea sequenceArea = new TextArea();
		private Button submitButon = new Button("submit");
		private Message message = new Message();

		private VerticalPanel panel = new VerticalPanel();

		public SequenceInputForm() {
			sequenceArea.setWidth("500px");
			sequenceArea.setVisibleLines(5);
			HorizontalPanel hp = new HorizontalPanel();
			hp.add(submitButon);
			hp.add(message);

			panel.add(sequenceArea);
			panel.add(hp);
			initWidget(panel);

			submitButon.addClickHandler(this);
			sequenceArea.addKeyUpHandler(new KeyUpHandler() {
				public void onKeyUp(KeyUpEvent e) {
					submitButon.setEnabled(true);
				}
			});
		}

		public void onClick(ClickEvent e) {
			submitButon.setEnabled(false); // avoid double submission of the query
			String inputSequence = sequenceArea.getText();
			doAlignment(inputSequence);
		}

	}

	public void doAlignment(String sequence) {

		getFrame().setNowLoading();
		String target = getTrackGroupProperty(UTGBProperty.TARGET);
		getBrowserService().getAlignment(serviceURL, target, sequence, new AsyncCallback<AlignmentResult>() {

			public void onFailure(Throwable e) {
				GWT.log("error", e);
				getFrame().loadingDone();
			}

			public void onSuccess(AlignmentResult result) {
				canvas.drawAlignment(result);
				getFrame().loadingDone();
			}

		});

	}

	public Widget getWidget() {
		return layoutTable;
	}

	@Override
	public void saveProperties(Properties saveData) {
		saveData.put(PROP_SERVICE_URL, serviceURL);
	}

	@Override
	public void restoreProperties(Properties properties) {
		serviceURL = properties.get(PROP_SERVICE_URL, serviceURL);
	}
}
