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
// OperationTrack.java
// Since: 2007/06/11
//
// This track supports operation xml.
//
// $URL$ 
// $Author$ ssksn
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.lib;

import java.util.HashMap;
import java.util.Map;

import org.utgenome.gwt.utgb.client.GenomeBrowser;
import org.utgenome.gwt.utgb.client.operation.OperationParser;
import org.utgenome.gwt.utgb.client.track.Design;
import org.utgenome.gwt.utgb.client.track.Track;
import org.utgenome.gwt.utgb.client.track.TrackBase;
import org.utgenome.gwt.utgb.client.track.TrackFrame;
import org.utgenome.gwt.utgb.client.track.TrackGroup;
import org.utgenome.gwt.utgb.client.track.TrackGroupPropertyChange;
import org.utgenome.gwt.utgb.client.track.TrackGroupPropertyWriter;
import org.utgenome.gwt.utgb.client.track.TrackInfo;
import org.utgenome.gwt.utgb.client.track.TrackWindow;
import org.utgenome.gwt.utgb.client.util.Utilities;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

public class OperationTrack extends TrackBase {
	public static TrackFactory factory() {
		return new TrackFactory() {
			public Track newInstance() {
				return new OperationTrack();
			}
		};
	}

	public static TrackFactory factory(final String descriptionXML) {
		return new TrackFactory() {
			public Track newInstance() {
				final OperationTrack track = new OperationTrack();
				track.setDescriptionXML(descriptionXML);
				return track;
			}
		};
	}

	final VerticalPanel _panel = new VerticalPanel();

	String _descriptionXMLURL;

	String species = "medaka";
	String build = "version1.0";
	String target = "scaffold1";
	String startIndex = "1";
	String endIndex = "1000000";

	String graphicLayerURL = "";
	String indexLayerURL = "";
	String operationLayerURL = "";

	final AbsolutePanel _absolutePanel = new AbsolutePanel();
	final Image graphicPanel = new Image();

	final Hyperlink parameterButton = new Hyperlink();
	final Label _label = new Label();

	public OperationTrack() {
		super("Operation Track");
		_panel.setWidth("100%");

		final DockPanel infoPanel = new DockPanel();
		infoPanel.setStyleName("operation-track-label");
		infoPanel.setWidth("100%");

		parameterButton.setText("[show parameters]");
		parameterButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent e) {
				final int frameIndex = getTrackGroup().getTrackIndex(OperationTrack.this);
				final ParameterTrack _parameterTrack = getParameterTrack();
				getTrackGroup().insertTrack(_parameterTrack, frameIndex);
			}

		});
		infoPanel.add(parameterButton, DockPanel.WEST);
		infoPanel.add(_label, DockPanel.EAST);

		_panel.add(infoPanel);
		_panel.add(_absolutePanel);
		_absolutePanel.add(graphicPanel);

	}

	public void setStartIndex(int start) {
		startIndex = Integer.toString(start);
	}

	public void setBuild(String build) {
		this.build = build;
	}

	private void setParameters() {

		if (getTrackGroup() == null)
			return;

		final TrackGroupPropertyWriter propertyWriter = getTrackGroup().getPropertyWriter();

		final Map<String, String> properties = new HashMap<String, String>();

		properties.put("descURL", _descriptionXMLURL);
		properties.put("species", species);
		properties.put("build", build);
		properties.put("target", target);
		properties.put("startIndex", startIndex);
		properties.put("endIndex", endIndex);

		properties.put("graphicURL", graphicLayerURL);
		properties.put("indexURL", indexLayerURL);
		properties.put("operationURL", operationLayerURL);

		propertyWriter.setProperty(properties);
	}

	public ParameterTrack getParameterTrack() {
		final ParameterTrack parameterTrack = new ParameterTrack();

		parameterTrack.addParameter("descURL", "description XML URL");
		parameterTrack.addParameter("species", "species");
		parameterTrack.addParameter("build", "build");
		parameterTrack.addParameter("target", "target");
		parameterTrack.addParameter("startIndex", "startIndex");
		parameterTrack.addParameter("endIndex", "endIndex");

		parameterTrack.addParameter("graphicURL", "graphic layer URL");
		parameterTrack.addParameter("indexURL", "index layer URL");
		parameterTrack.addParameter("operationURL", "operation layer URL");

		return parameterTrack;
	}

	public void setDescriptionXML(final String descriptionXMLURL) {
		this._descriptionXMLURL = descriptionXMLURL;

		parseDescriptionXML(_descriptionXMLURL);
	}

	private void parseDescriptionXML(final String _descriptionXMLURL) {
		GenomeBrowser.getService().getHTTPContent(_descriptionXMLURL, new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				GWT.log("cannot retrieve: " + _descriptionXMLURL, caught);
			}

			public void onSuccess(String text) {
				final Document dom = XMLParser.parse(text);

				{
					final NodeList topLevelTrackNodeList = dom.getElementsByTagName("track");
					if (topLevelTrackNodeList.getLength() != 1) {
						throw new AssertionError();
					}

					final Node topLevelTrackNode = topLevelTrackNodeList.item(0);

					TrackInfo info = getTrackInfo();
					info.setTrackName(Utilities.getAttributeValue(topLevelTrackNode, "name"));
					info.setDescription(Utilities.getAttributeValue(topLevelTrackNode, "comment", ""));
					final String trackDescriptionURL = Utilities.getAttributeValue(topLevelTrackNode, "description_url");
					if (trackDescriptionURL != null) {
						info.setLinkURL(trackDescriptionURL);
					}
				}

				final NodeList layerNodes = dom.getElementsByTagName("layer");
				for (int i = 0; i < layerNodes.getLength(); i++) {
					final Node layerNode = layerNodes.item(i);

					final NamedNodeMap attributes = layerNode.getAttributes();
					final Node kindNode = attributes.getNamedItem("kind");
					final Node urlNode = attributes.getNamedItem("url");

					final String kindValue = kindNode.getNodeValue();
					final String urlValue = urlNode.getNodeValue();

					if (kindValue.equals("graphic")) {
						graphicLayerURL = urlValue;
					}
					if (kindValue.equals("index")) {
						indexLayerURL = urlValue;
					}
					if (kindValue.equals("operation")) {
						operationLayerURL = urlValue;
					}
				}
				{
					if (getTrackGroup() != null) {
						final TrackGroupPropertyWriter propertyWriter = getTrackGroup().getPropertyWriter();

						HashMap<String, String> newProperty = new HashMap<String, String>();
						newProperty.put("graphicURL", graphicLayerURL);
						newProperty.put("indexURL", indexLayerURL);
						newProperty.put("operationURL", operationLayerURL);
						propertyWriter.setProperty(newProperty);
					}
				}
				// draw();
			}
		});
	}

	public int getDefaultWindowHeight() {
		return 200;
	}

	public Widget getWidget() {
		return _panel;
	}

	public void onChangeTrackWindow(final TrackWindow newWindow) {
		// draw();
	}

	public void draw() {

		{ // remove an old image panel and operationAreas.
			while (_absolutePanel.getWidgetCount() > 0) {
				_absolutePanel.remove(0);
			}
			_absolutePanel.add(graphicPanel); // re-add image panel
		}
		{ // update image panel
			final String graphicURL = getGraphicURL();
			if (graphicURL != null) {
				Image.prefetch(graphicURL);
				graphicPanel.setUrl(graphicURL);
			}
		}
		{ // process operation xml
			parseOperationXML();
		}

	}

	private String getOperationURL() {
		if (operationLayerURL == null)
			return null;
		String url = operationLayerURL;
		if (url.indexOf('?') == -1)
			url += '?';
		else if ((url.indexOf('?') != (url.length() - 1)) && (url.indexOf('&') != (url.length() - 1)))
			url += '&';

		final int windowWidth = getTrackGroup().getPropertyReader().getTrackWindow().getWindowWidth();

		return url
				+ join(new String[] { "species", "revision", "target", "start", "end", "width" }, new String[] { species, build, target, startIndex, endIndex,
						Integer.toString(windowWidth) });
	}

	private String getGraphicURL() {
		if (graphicLayerURL == null)
			return null;
		String url = graphicLayerURL;
		if (url.indexOf('?') == -1)
			url += '?';
		else if ((url.indexOf('?') != (url.length() - 1)) && (url.indexOf('&') != (url.length() - 1)))
			url += '&';

		final int windowWidth = getTrackGroup().getPropertyReader().getTrackWindow().getWindowWidth();

		return url
				+ join(new String[] { "species", "revision", "target", "start", "end", "width" }, new String[] { species, build, target, startIndex, endIndex,
						Integer.toString(windowWidth) });
	}

	public static String join(String[] keys, String[] value) {
		String result = new String();
		for (int i = 0; i < keys.length; i++) {
			result += keys[i] + "=" + value[i];
			if (i != (keys.length - 1))
				result += "&";
		}
		return result;
	}

	public final void parseOperationXML() {
		final String operationURL = getOperationURL();

		if (operationURL == null)
			return;
		if (!operationLayerURL.startsWith("http"))
			return;

		_label.setText("Now Operation XML parsing ...");

		GenomeBrowser.getService().getHTTPContent(operationURL, new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
			}

			public void onSuccess(String text) {
				if (text.length() <= 0) {
					_label.setText("");
					return;
				}
				{
					final Document dom = XMLParser.parse(text);
					final OperationParser parser = OperationParser.getParser();
					parser.parse(dom, _absolutePanel, OperationTrack.this);
					_label.setText("");
				}
			}
		});
	}

	public void onChangeTrackGroupProperty(TrackGroupPropertyChange change) {
		boolean drawFlag = false;

		{
			{
				final String key = "descURL";
				if (change.contains(key)) {
					_descriptionXMLURL = change.getProperty(key);
					drawFlag = true;
				}
			}
			{
				final String key = "species";
				if (change.contains(key)) {
					species = change.getProperty(key);
					drawFlag = true;
				}
			}
			{
				final String key = "build";
				if (change.contains(key)) {
					build = change.getProperty(key);
					drawFlag = true;
				}
			}
			{
				final String key = "target";
				if (change.contains(key)) {
					target = change.getProperty(key);
					drawFlag = true;
				}
			}
			{
				final String key = "startIndex";
				if (change.contains(key)) {
					startIndex = change.getProperty(key);
					drawFlag = true;
				}
			}
			{
				final String key = "endIndex";
				if (change.contains(key)) {
					endIndex = change.getProperty(key);
					drawFlag = true;
				}
			}

			{
				final String key = "graphicURL";
				if (change.contains(key)) {
					graphicLayerURL = change.getProperty(key);
					drawFlag = true;
				}
			}
			{
				final String key = "indexURL";
				if (change.contains(key)) {
					indexLayerURL = change.getProperty(key);
					drawFlag = true;
				}
			}
			{
				final String key = "operationURL";
				if (change.contains(key)) {
					operationLayerURL = change.getProperty(key);
					drawFlag = true;
				}
			}
		}
		if (drawFlag)
			draw();
	}

	public void setUp(TrackFrame trackFrame, TrackGroup group) {
		trackFrame.pack();
		setParameters();
		graphicPanel.addLoadHandler(new LoadHandler() {

			public void onLoad(LoadEvent arg0) {
				getFrame().onUpdateTrackWidget();

			}
		});

		graphicPanel.addErrorHandler(new ErrorHandler() {

			public void onError(ErrorEvent e) {
				graphicPanel.setUrl(Design.IMAGE_NOT_AVAILABLE);
			}
		});

	}

}
