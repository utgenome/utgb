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
// OldUTGBTrack.java
// Since: 2007/06/19
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.lib.old;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.utgenome.gwt.utgb.client.db.datatype.StringType;
import org.utgenome.gwt.utgb.client.track.Design;
import org.utgenome.gwt.utgb.client.track.Track;
import org.utgenome.gwt.utgb.client.track.TrackBase;
import org.utgenome.gwt.utgb.client.track.TrackConfig;
import org.utgenome.gwt.utgb.client.track.TrackConfigChange;
import org.utgenome.gwt.utgb.client.track.TrackFrame;
import org.utgenome.gwt.utgb.client.track.TrackGroup;
import org.utgenome.gwt.utgb.client.track.TrackGroupProperty;
import org.utgenome.gwt.utgb.client.track.TrackGroupPropertyChange;
import org.utgenome.gwt.utgb.client.track.TrackWindow;
import org.utgenome.gwt.utgb.client.track.operation.OperationParser;
import org.utgenome.gwt.utgb.client.util.GETMethodURL;
import org.utgenome.gwt.utgb.client.util.JSONUtil;
import org.utgenome.gwt.utgb.client.util.Properties;
import org.utgenome.gwt.widget.client.Style;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

/**
 * @author ssksn
 * 
 */
public class OldUTGBTrack extends TrackBase {
	public static TrackFactory factory() {
		return new TrackFactory() {
			Map<String, List<String>> properties = new HashMap<String, List<String>>();

			@Override
			public Track newInstance() {
				OldUTGBTrack track = new OldUTGBTrack();
				if (properties.containsKey("descriptionXMLURL")) {
					final List<String> values = properties.get("descriptionXMLURL");
					for (int i = 0; i < values.size(); i++) {
						final String descriptionXMLURL = (values.get(i));
						track.setDescriptionXML(descriptionXMLURL);
					}
				}
				return track;
			}

			@Override
			public void setProperty(String key, String value) {
				if (properties.containsKey(key)) {
					final List<String> values = properties.get(key);
					values.add(value);
				}
				else {
					final List<String> values = new ArrayList<String>();
					properties.put(key, values);
					values.add(value);
				}
			}

			@Override
			public void clear() {
				properties.clear();
			}
		};
	}

	private class DescriptionURLInfo {
		protected String descriptionXMLURL;
		protected GETMethodURL graphicLayerURL = null;
		protected GETMethodURL indexLayerURL = null;
		protected GETMethodURL operationLayerURL = null;
		protected GETMethodURL indexOperationLayerURL = null;
		protected String scalingStability = null;
		protected String scrollingStability = null;

		public DescriptionURLInfo(final String descriptionXMLURL) {
			setDescriptionXMLURL(descriptionXMLURL);
		}

		final String getDescriptionXMLURL() {
			return descriptionXMLURL;
		}

		private final void setDescriptionXMLURL(final String descriptionXMLURL) {
			this.descriptionXMLURL = descriptionXMLURL;
		}

		final void setGraphicLayerURL(final String graphicLayerURL) {
			this.graphicLayerURL = GETMethodURL.newInstance(graphicLayerURL);
		}

		final GETMethodURL getGraphicLayerURL() {
			return graphicLayerURL;
		}

		final void setIndexLayerURL(final String indexLayerURL) {
			this.indexLayerURL = GETMethodURL.newInstance(indexLayerURL);
		}

		final GETMethodURL getIndexLayerURL() {
			return indexLayerURL;
		}

		final void setOperationLayerURL(final String operationLayerURL) {
			this.operationLayerURL = GETMethodURL.newInstance(operationLayerURL);
		}

		final GETMethodURL getOperationLayerURL() {
			return operationLayerURL;
		}

		final void setIndexOperationLayerURL(final String indexOperationLayerURL) {
			this.indexOperationLayerURL = GETMethodURL.newInstance(indexOperationLayerURL);
		}

		final GETMethodURL getIndexOperationLayerURL() {
			return indexOperationLayerURL;
		}

		final void setScalingStability(final String scalingStability) {
			this.scalingStability = scalingStability;
		}

		final String getScalingStability() {
			return scalingStability;
		}

		final void setScrollingStability(final String scrollingStability) {
			this.scrollingStability = scrollingStability;
		}

		final String getScrollingStability() {
			return scrollingStability;
		}
	}

	final FlexTable _panel = new FlexTable();
	final Label _label = new Label();
	final AbsolutePanel indexAbsolutePanel = new AbsolutePanel();
	final Image indexGraphicPanel = new Image();
	final AbsolutePanel mainAbsolutePanel = new AbsolutePanel();
	final Image mainGraphicPanel = new Image();
	private boolean isUptodate = false;
	private boolean isLoading = false;
	private int remainingDescriptionXMLCount = 0;
	private Stack<String> unparsedDescriptionXML = new Stack<String>();
	protected final List<DescriptionURLInfo> descriptionURLList = new ArrayList<DescriptionURLInfo>();
	protected DescriptionURLInfo currentDescriptionURLInfo = null;
	protected final List<AcceptSpeciesEntry> acceptSpeciesEntries = new ArrayList<AcceptSpeciesEntry>();
	private static final int INDEX_WINDOW_WIDTH = 100;
	protected final List<OldUTGBOptionAttribute> optionAttributes = new ArrayList<OldUTGBOptionAttribute>();

	public OldUTGBTrack() {
		super("Operation Track");
		_panel.setWidth("100%");
		_label.setStyleName("operation-track-label");
		_label.setWidth("100%");
		_label.setHorizontalAlignment(Label.ALIGN_CENTER);
		indexGraphicPanel.setWidth(INDEX_WINDOW_WIDTH + "px");
		// indexGraphicPanel.setStyleName("utgbtrack-indeximage");
		indexAbsolutePanel.add(indexGraphicPanel, 0, 0);
		indexAbsolutePanel.setWidth(INDEX_WINDOW_WIDTH + "px");

		_panel.setBorderWidth(0);
		_panel.setCellPadding(0);
		_panel.setCellSpacing(0);

		_panel.getCellFormatter().setWidth(0, 0, INDEX_WINDOW_WIDTH + "px");
		_panel.setWidget(0, 0, indexAbsolutePanel);
		mainAbsolutePanel.add(mainGraphicPanel);
		_panel.setWidget(0, 1, mainAbsolutePanel);
		Style.hideHorizontalScrollBar(_panel);
	}

	@Override
	public int getDefaultWindowHeight() {
		return 50;
	}

	public Widget getWidget() {
		return _panel;
	}

	public void setDescriptionXML(final String descriptionXMLURL) {
		unparsedDescriptionXML.push(descriptionXMLURL);
	}

	@Override
	public void draw() {

		if (!unparsedDescriptionXML.empty()) {
			updateDescriptionXML();
			return;
		}

		if (isUptodate)
			return;

		getFrame().setNowLoading();

		{ // remove an old image panel and operationAreas.
			{ // main
				//mainAbsolutePanel.clear();
				//mainAbsolutePanel.add(mainGraphicPanel); // re-add image panel
			}
			{ // index
				//indexAbsolutePanel.clear();
			}
		}
		{
			// acceptability check
			final TrackGroupProperty propertyReader = _trackGroup.getPropertyReader();
			final String species = propertyReader.getProperty(OldUTGBProperty.SPECIES);
			final String revision = propertyReader.getProperty(OldUTGBProperty.REVISION);
			boolean isAccept = isAccepted(species, revision);
			if (!isAccept) {
				mainGraphicPanel.setUrl("theme/image/na.png");
				GWT.log("This track does not accept this species/revision setting.", null);
				isUptodate = true;
				return;
			}
			else {
				_frame.setNowLoading();
				eraseMessage();
				indexAbsolutePanel.add(indexGraphicPanel); // re-add image panel
			}
		}
		{ // update image panel
			{ // main
				final String mainGraphicURL = getMainGraphicURL();
				if (mainGraphicURL != null) {
					Image.prefetch(mainGraphicURL);
					mainGraphicPanel.setUrl(mainGraphicURL);
					getConfig().setParameter("URL", mainGraphicURL);
				}
			}
			{ // index
				final String indexGraphicURL = getIndexGraphicURL();
				if (indexGraphicURL != null) {
					Image.prefetch(indexGraphicURL);
					indexGraphicPanel.setUrl(indexGraphicURL);
				}
				else {
					indexGraphicPanel.setUrl(Design.IMAGE_TRANSPARENT);
					indexGraphicPanel.setWidth(INDEX_WINDOW_WIDTH + "px");
				}
			}
		}
		{ // process operation xml
			parseOperationXML();
		}
		isUptodate = true;
	}

	@Override
	public void onChangeTrackWindow(TrackWindow newWindow) {
		isUptodate = false;
		refresh();
	}

	@Override
	public void onChangeTrackGroupProperty(TrackGroupPropertyChange change) {
		final String[] propertyNameArray = OldUTGBProperty.getPropertyNameArray();
		if (change.containsOneOf(propertyNameArray)) {
			isUptodate = false;
			refresh();
		}
	}

	@Override
	public void onChangeTrackConfig(TrackConfigChange change) {
		isUptodate = false;
		refresh();
	}

	@Override
	public void setUp(TrackFrame trackFrame, TrackGroup group) {
		trackFrame.pack();
		trackFrame.enableConfig();

		mainGraphicPanel.addLoadHandler(new LoadHandler() {
			public void onLoad(LoadEvent e) {
				getFrame().onUpdateTrackWidget();
				_frame.loadingDone();
			}
		});
		mainGraphicPanel.addErrorHandler(new ErrorHandler() {
			public void onError(ErrorEvent e) {
				GWT.log("failed to load " + mainGraphicPanel.getUrl(), null);
				mainGraphicPanel.setUrl("theme/image/na.png");
				_frame.loadingDone();
			}
		});

		indexGraphicPanel.addLoadHandler(new LoadHandler() {
			public void onLoad(LoadEvent e) {
				getFrame().onUpdateTrackWidget();
			}
		});

		indexGraphicPanel.addErrorHandler(new ErrorHandler() {
			public void onError(ErrorEvent e) {
				GWT.log("failed to load " + indexGraphicPanel.getUrl(), null);
				indexGraphicPanel.setUrl(Design.IMAGE_TRANSPARENT);
			}
		});

		// updateDescriptionXML();
	}

	protected void updateDescriptionXML() {
		// parse description XMLs
		ArrayList<String> parseList = new ArrayList<String>();
		parseList.addAll(unparsedDescriptionXML);
		remainingDescriptionXMLCount = unparsedDescriptionXML.size();
		getFrame().setNowLoading();
		for (String url : parseList) {
			parseDescriptionXML(url);
		}
		unparsedDescriptionXML.clear();
	}

	protected void parseDescriptionXML(String descriptionXMLURL) {
		final String resolvedDescriptionXMLURL = (descriptionXMLURL.startsWith("http:")) ? descriptionXMLURL : GWT.getModuleBaseURL() + descriptionXMLURL;

		getBrowserService().getHTTPContent(resolvedDescriptionXMLURL, new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				GWT.log("cannot retrieve: " + resolvedDescriptionXMLURL, caught);
				remainingDescriptionXMLCount--;
				if (remainingDescriptionXMLCount <= 0)
					draw();
			}

			public void onSuccess(String text) {
				try {
					final Document dom = XMLParser.parse(text);
					final DescriptionURLInfo descriptionURLInfo = new DescriptionURLInfo(resolvedDescriptionXMLURL);
					{
						// set up the configuration panel
						final NodeList topLevelTrackNodeList = dom.getElementsByTagName("track");
						if (topLevelTrackNodeList.getLength() != 1) {
							throw new AssertionError();
						}
						final Node topLevelTrackNode = topLevelTrackNodeList.item(0);
						final String trackName = Utilities.getAttributeValue(topLevelTrackNode, "name");
						_trackInfo.setTrackName(trackName);
						final String trackComment = Utilities.getAttributeValue(topLevelTrackNode, "comment");
						if (trackComment != null) {
							_trackInfo.setDescription(trackComment);
						}
						final String trackDescriptionURL = Utilities.getAttributeValue(topLevelTrackNode, "description_url");
						if (trackDescriptionURL != null) {
							_trackInfo.setLinkURL(trackDescriptionURL);
						}
						final String optAttrStr = Utilities.getAttributeValue(topLevelTrackNode, "optattr");
						if (optAttrStr != null) {
							final String prefix = Integer.toString(Random.nextInt());
							parseOptionAttribute(optAttrStr.trim(), prefix);
							setConfig(new TrackConfig(OldUTGBTrack.this));
							for (int i = 0; i < optionAttributes.size(); i++) {
								final OldUTGBOptionAttribute optionAttribute = (optionAttributes.get(i));
								optionAttribute.setConfig(getConfig());
							}
						}
						getConfig().addConfig(new StringType("URL"), "");
					}
					final NodeList layerNodes = dom.getElementsByTagName("layer");
					for (int i = 0; i < layerNodes.getLength(); i++) {
						final Node layerNode = layerNodes.item(i);
						final String kindValue = Utilities.getAttributeValue(layerNode, "kind");
						final String urlValue = Utilities.getAttributeValue(layerNode, "url");
						if (kindValue.equals("graphic")) {
							descriptionURLInfo.setGraphicLayerURL(urlValue);
						}
						if (kindValue.equals("index")) {
							descriptionURLInfo.setIndexLayerURL(urlValue);
						}
						if (kindValue.equals("operation")) {
							descriptionURLInfo.setOperationLayerURL(urlValue);
						}
						if (kindValue.equals("indexoperation")) {
							descriptionURLInfo.setIndexOperationLayerURL(urlValue);
						}
					}
					{
						final NodeList scalingStabilityNodes = dom.getElementsByTagName("scaling_stability");
						if (scalingStabilityNodes.getLength() >= 1) {
							final Node scalingStabilityNode = scalingStabilityNodes.item(0);
							if (scalingStabilityNode != null)
								currentDescriptionURLInfo.setScalingStability(Utilities.getAttributeValue(scalingStabilityNode, "kind", null));
						}
					}
					{
						final NodeList scrollingStabilityNodes = dom.getElementsByTagName("scrolling_stability");
						if (scrollingStabilityNodes.getLength() >= 1) {
							final Node scrollingStabilityNode = scrollingStabilityNodes.item(0);
							currentDescriptionURLInfo.setScrollingStability(Utilities.getAttributeValue(scrollingStabilityNode, "kind", null));
						}
					}
					{
						final NodeList acceptSpeciesNodeList = dom.getElementsByTagName("accept_species");
						if (acceptSpeciesNodeList.getLength() >= 1) {
							final Node acceptSpeciesNode = acceptSpeciesNodeList.item(0);
							final NodeList acceptSpeciesEntries = Utilities.getTagChildNodes(acceptSpeciesNode);
							for (int i = 0; i < acceptSpeciesEntries.getLength(); i++) {
								final Node acceptSpeciesEntry = acceptSpeciesEntries.item(i);
								final String acceptSpecies = Utilities.getAttributeValue(acceptSpeciesEntry, "species", "any");
								final String acceptRevision = Utilities.getAttributeValue(acceptSpeciesEntry, "revision", "any");
								final AcceptSpeciesEntry entry = new AcceptSpeciesEntry(acceptSpecies, acceptRevision, descriptionURLInfo);
								addAcceptSpeciesEntry(entry);
							}
						}
					}
					descriptionURLList.add(descriptionURLInfo);
					isUptodate = false;
				}
				finally {
					remainingDescriptionXMLCount--;
					if (remainingDescriptionXMLCount <= 0)
						draw();
				}
			}
		});

	}

	/*
	 * class ParseDescriptionXMLCommand implements IncrementalCommand { private String descriptionXMLURL; private
	 * boolean nowLoading = false; private boolean loadingDone = false; public ParseDescriptionXMLCommand(String
	 * descrptionXMLURL) { this.descriptionXMLURL = descrptionXMLURL; }
	 * 
	 * public boolean execute() { if(nowLoading) return !loadingDone; // continue the command until the loading complete
	 * else { nowLoading = true; }
	 * 
	 * 
	 * return !loadingDone; } }
	 */

	public void writeMessage(final String message) {
		// _label.setText(message);
		// _frame.writeMessage(message);
	}

	public void eraseMessage() {
		// _label.setText("");
		// _frame.eraseMessage(true);
	}

	private final void parseOptionAttribute(final String optAttrStr, final String prefix) {
		final int slashIndex = optAttrStr.indexOf('/');
		if (slashIndex < 0)
			return;
		final String parameterName = optAttrStr.substring(0, slashIndex).trim();
		final int leftBlockBracketIndex = optAttrStr.indexOf('[', slashIndex + 1);
		if (leftBlockBracketIndex < 0)
			return;
		final String type = optAttrStr.substring(slashIndex + 1, leftBlockBracketIndex).trim();
		final int rightBlockBracketIndex = optAttrStr.indexOf(']', leftBlockBracketIndex + 1);
		if (rightBlockBracketIndex < 0)
			return;
		final String valueStr = optAttrStr.substring(leftBlockBracketIndex + 1, rightBlockBracketIndex).trim();
		if (valueStr.length() <= 0)
			return;
		final String[] values = valueStr.split(",");
		final int leftBracketIndex = optAttrStr.indexOf('(', rightBlockBracketIndex + 1);
		if (leftBracketIndex < 0)
			return;
		final int rightBracketIndex = optAttrStr.indexOf(')', leftBracketIndex + 1);
		if (rightBracketIndex < 0)
			return;
		final String operationStr = optAttrStr.substring(leftBracketIndex + 1, rightBracketIndex).trim();
		if (operationStr.length() <= 0)
			return;
		final String[] operations = operationStr.split(",");
		if (type.equalsIgnoreCase("select")) {
			// operation check
			for (int i = 0; i < operations.length; i++) {
				final String operation = operations[i];
				if (!operation.equalsIgnoreCase("disp") && !operation.equalsIgnoreCase("color") && !operation.equalsIgnoreCase("select"))
					return;
			}
			final OldUTGBOptionAttribute optionAttribute = OldUTGBOptionAttribute.getSelectInstance(parameterName, values, operations, prefix);
			if (optionAttribute != null)
				optionAttributes.add(optionAttribute);
		}
		else if (type.equalsIgnoreCase("real")) {
			// operation check
			for (int i = 0; i < operations.length; i++) {
				final String operation = operations[i];
				if (!operation.equalsIgnoreCase("gradation") && !operation.equalsIgnoreCase("ubound") && !operation.equalsIgnoreCase("lbound"))
					return;
			}
			if (values.length != 2)
				return;
			final String minValue = values[0];
			final String maxValue = values[1];
			final OldUTGBOptionAttribute optionAttribute = OldUTGBOptionAttribute.getRealInstance(parameterName, minValue, maxValue, operations, prefix);
			if (optionAttribute != null)
				optionAttributes.add(optionAttribute);
		}
		final int nextCommaIndex = optAttrStr.indexOf(',', rightBracketIndex + 1);
		if (nextCommaIndex < 0)
			return;
		final String nextOptAttrStr = optAttrStr.substring(nextCommaIndex + 1).trim();
		parseOptionAttribute(nextOptAttrStr, prefix);
	}

	final int getMainPanelWidth() {
		final int windowWidth = getTrackGroup().getPropertyReader().getTrackWindow().getPixelWidth();
		return windowWidth - INDEX_WINDOW_WIDTH;
	}

	private final String getMainGraphicURL() {
		if (currentDescriptionURLInfo != null) {
			final GETMethodURL graphicLayerURL = currentDescriptionURLInfo.getGraphicLayerURL();
			if (graphicLayerURL != null)
				return getURL(graphicLayerURL, getMainPanelWidth());
		}
		return null;
	}

	private final String getIndexGraphicURL() {
		if (currentDescriptionURLInfo != null) {
			final GETMethodURL indexLayerURL = currentDescriptionURLInfo.getIndexLayerURL();
			if (indexLayerURL != null)
				return getURL(indexLayerURL, getMainPanelWidth());
		}
		return null;
	}

	private final String getMainOperationURL() {
		if (currentDescriptionURLInfo != null) {
			final GETMethodURL operationLayerURL = currentDescriptionURLInfo.getOperationLayerURL();
			if (operationLayerURL != null)
				return getURL(operationLayerURL, getMainPanelWidth());
		}
		return null;
	}

	private final String getIndexOperationURL() {
		if (currentDescriptionURLInfo != null) {
			final GETMethodURL indexOperationLayerURL = currentDescriptionURLInfo.getIndexOperationLayerURL();
			if (indexOperationLayerURL != null)
				return getURL(indexOperationLayerURL, getMainPanelWidth());
		}
		return null;
	}

	private final String getURL(final GETMethodURL url, final int width) {
		if (url == null)
			return null;
		final Map<String, String> parameterMap = new HashMap<String, String>();
		final String[] propertyNameArray = OldUTGBProperty.getPropertyNameArray();
		final TrackGroupProperty propertyReader = _trackGroup.getPropertyReader();
		for (int i = 0; i < propertyNameArray.length; i++) {
			final String key = propertyNameArray[i];
			final String value = propertyReader.getProperty(key);
			parameterMap.put(key, value);
		}
		parameterMap.put("width", Integer.toString(width));
		final TrackWindow trackWindow = _trackGroup.getTrackWindow();
		final long startIndex = trackWindow.getStartOnGenome();
		final long endIndex = trackWindow.getEndOnGenome();
		parameterMap.put("start", Long.toString(startIndex));
		parameterMap.put("end", Long.toString(endIndex));
		for (int i = 0; i < optionAttributes.size(); i++) {
			final OldUTGBOptionAttribute optionAttribute = (optionAttributes.get(i));
			optionAttribute.setParameters(parameterMap);
		}
		final String returnURL = url.getURL(parameterMap);
		if (returnURL.startsWith("http"))
			return returnURL;
		else
			return null;
	}

	private final void parseOperationXML() {
		// main
		final String mainOperationURL = getMainOperationURL();
		parseOperationXML(mainOperationURL, mainAbsolutePanel, "Now Operation XML Parsing ...");
		// index
		final String indexOperationURL = getIndexOperationURL();
		parseOperationXML(indexOperationURL, indexAbsolutePanel, null);
	}

	private final void parseOperationXML(final String operationURL, final AbsolutePanel _absolutePanel, final String loadingMessage) {
		if (operationURL == null)
			return;
		if (loadingMessage != null)
			writeMessage(loadingMessage);
		getBrowserService().getHTTPContent(operationURL, new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
			}

			public void onSuccess(String text) {
				if (text.length() <= 0) {
					if (loadingMessage != null)
						eraseMessage();
					return;
				}
				{
					final Document dom = XMLParser.parse(text);
					final OperationParser parser = OperationParser.getParser();
					parser.parse(dom, _absolutePanel, OldUTGBTrack.this);
					if (loadingMessage != null)
						eraseMessage();
					getFrame().onUpdateTrackWidget();
				}
			}
		});
	}

	private boolean isAccepted(final String species, final String revision) {
		final Iterator<AcceptSpeciesEntry> it = acceptSpeciesEntries.iterator();
		while (it.hasNext()) {
			final AcceptSpeciesEntry acceptSpeciesEntry = it.next();
			final boolean result = acceptSpeciesEntry.isAccepted(species, revision);
			if (result) {
				currentDescriptionURLInfo = acceptSpeciesEntry.getDescriptionURLInfo();
				return true;
			}
		}
		return false;
	}

	private void addAcceptSpeciesEntry(final AcceptSpeciesEntry entry) {
		acceptSpeciesEntries.add(entry);
	}

	class AcceptSpeciesEntry {
		private String species = null;
		private String revision = null;
		private DescriptionURLInfo descriptionURLInfo = null;

		public AcceptSpeciesEntry(final String species, final String revision, final DescriptionURLInfo descriptionURLInfo) {
			if (species.equals("any"))
				this.species = null;
			else
				this.species = species;
			if (revision.equals("any"))
				this.revision = null;
			else
				this.revision = revision;
			this.descriptionURLInfo = descriptionURLInfo;
		}

		public boolean isAccepted(final String species, final String revision) {
			if (this.species == null)
				return true;
			else {
				if (!this.species.equals(species))
					return false;
				else {
					if (this.revision == null)
						return true;
					else {
						if (!this.revision.equals(revision))
							return false;
						else
							return true;
					}
				}
			}
		}

		public final DescriptionURLInfo getDescriptionURLInfo() {
			return descriptionURLInfo;
		}
	}

	@Override
	public void saveProperties(Properties saveData) {
		final ArrayList<String> descriptionURLs = new ArrayList<String>();
		for (int i = 0; i < descriptionURLList.size(); i++) {
			final DescriptionURLInfo descriptionURLInfo = (descriptionURLList.get(i));
			final String descriptionXMLURL = descriptionURLInfo.getDescriptionXMLURL();
			descriptionURLs.add(descriptionXMLURL);
		}
		saveData.add("descriptionXMLURL", JSONUtil.toJSONArray(descriptionURLs));
	}

	@Override
	public void restoreProperties(Properties properties) {
		ArrayList<String> valueList = JSONUtil.parseJSONArray(properties.get("descriptionXMLURL", "[]"));
		for (Iterator<String> it = valueList.iterator(); it.hasNext();) {
			String value = it.next();
			unparsedDescriptionXML.push(value);
		}
		isUptodate = false;
	}
}
