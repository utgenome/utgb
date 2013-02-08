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
// UTGBMedaka Project
//
// ViewLoaderTrack.java
// Since: Aug 13, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.lib;

import org.utgenome.gwt.utgb.client.UTGBClientException;
import org.utgenome.gwt.utgb.client.UTGBEntryPointBase;
import org.utgenome.gwt.utgb.client.track.Track;
import org.utgenome.gwt.utgb.client.track.TrackBase;
import org.utgenome.gwt.utgb.client.track.TrackGroup;
import org.utgenome.gwt.utgb.client.ui.FormLabel;
import org.utgenome.gwt.utgb.client.view.TrackView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;

/**
 * Track for loading view files in Silk format.
 * 
 * @author leo
 * 
 */
public class ViewLoaderTrack extends TrackBase {
	public static TrackFactory factory() {
		return new TrackFactory() {
			public Track newInstance() {
				return new ViewLoaderTrack();
			}
		};
	}

	VerticalPanel panel = new VerticalPanel();
	TextBox urlBox = new TextBox();

	public ViewLoaderTrack() {
		super("View Loader");
		// load view via HTTP
		HorizontalPanel hp = new HorizontalPanel();
		hp.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
		hp.add(new FormLabel("View Silk URL: "));
		urlBox.setWidth("400px");
		urlBox.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent e) {
				if (e.getCharCode() == KeyCodes.KEY_ENTER) {
					downloadView(urlBox.getText());
				}
			}
		});
		Button loadButton = new Button("load");
		loadButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent e) {
				downloadView(urlBox.getText());
			}
		});
		hp.add(urlBox);
		hp.add(loadButton);
		// load view from a file
		final FormPanel fileUploadForm = new FormPanel();
		fileUploadForm.setAction(GWT.getModuleBaseURL() + "utgb-core/loadview");
		fileUploadForm.setEncoding(FormPanel.ENCODING_MULTIPART);
		fileUploadForm.setMethod(FormPanel.METHOD_POST);
		HorizontalPanel formButtonPanel = new HorizontalPanel();
		FileUpload fileBox = new FileUpload();
		fileBox.setName("file");
		fileBox.setWidth("300px");
		Button uploadButton = new Button("submit");
		uploadButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent e) {
				fileUploadForm.submit();
			}
		});
		formButtonPanel.add(new FormLabel("View Silk File:"));
		formButtonPanel.add(fileBox);
		formButtonPanel.add(uploadButton);
		fileUploadForm.add(formButtonPanel);
		DOM.setStyleAttribute(fileUploadForm.getElement(), "margin", "0");
		fileUploadForm.addSubmitCompleteHandler(new SubmitCompleteHandler() {
			public void onSubmitComplete(SubmitCompleteEvent e) {

				getFrame().setNowLoading();
				String viewXML = extractEmbeddedSilkInComment(e.getResults());
				setViewSilk(viewXML);
			}
		});
		// set panes
		panel.setStyleName("toolbox");
		panel.add(hp);
		panel.add(fileUploadForm);
	}

	private static String extractEmbeddedSilkInComment(String html) {
		html = html.replaceFirst("<!--", "");
		html = html.replaceFirst("-->", "");
		return html;
	}

	private void downloadView(final String url) {
		getFrame().setNowLoading();
		getBrowserService().getHTTPContent(url, new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				GWT.log("failed to load " + url, caught);
				getFrame().loadingDone();
			}

			public void onSuccess(String viewXML) {

				setViewSilk(viewXML);
				getFrame().loadingDone();
			}
		});
	}

	private void setViewSilk(String viewSilk) {
		if (viewSilk == null)
			return;

		UTGBEntryPointBase.showLoadingMessage();
		getBrowserService().createTrackView(viewSilk, new AsyncCallback<TrackView>() {
			public void onFailure(Throwable e) {
				GWT.log(e.getMessage(), e);
				DialogBox dialog = new DialogBox();
				dialog.setText(e.getMessage());
				dialog.show();
				UTGBEntryPointBase.hideLoadingMessage();
			}

			public void onSuccess(TrackView v) {
				TrackGroup newGroup;
				try {
					newGroup = TrackGroup.createTrackGroup(v);
					TrackGroup rootTrackGroup = getTrackGroup().getRootTrackGroup();
					rootTrackGroup.clear();
					rootTrackGroup.addTrackGroup(newGroup);
				}
				catch (UTGBClientException e) {
					GWT.log(e.getMessage(), e);
					DialogBox dialog = new DialogBox();
					dialog.setText(e.getMessage());
					dialog.show();
				}

				UTGBEntryPointBase.hideLoadingMessage();
			}
		});

	}

	public Widget getWidget() {
		return panel;
	}
}
