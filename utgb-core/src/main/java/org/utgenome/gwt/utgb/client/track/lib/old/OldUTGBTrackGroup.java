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
// OldUTGBTrackGroup.java
// Since: 2007/06/19
//
// $URL$ 
// $Author$ ssksn
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.lib.old;

import java.util.HashMap;
import java.util.Map;

import org.utgenome.gwt.utgb.client.RPCServiceManager;
import org.utgenome.gwt.utgb.client.track.TrackGroup;
import org.utgenome.gwt.utgb.client.track.TrackGroupPropertyChange;
import org.utgenome.gwt.utgb.client.track.TrackGroupPropertyWriter;
import org.utgenome.gwt.utgb.client.track.TrackWindow;
import org.utgenome.gwt.utgb.client.track.impl.TrackGroupPropertyImpl;
import org.utgenome.gwt.utgb.client.track.impl.TrackWindowImpl;
import org.utgenome.gwt.utgb.client.track.lib.TrackTreeTrack;
import org.utgenome.gwt.utgb.client.track.lib.ValueSelectorTrack;
import org.utgenome.gwt.utgb.client.util.GETMethodURL;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author ssksn
 * 
 */
public class OldUTGBTrackGroup extends TrackGroup {
	public static TrackGroupFactory factory() {
		return new TrackGroupFactory() {
			public TrackGroup newInstance() {
				final OldUTGBTrackGroup trackGroup = new OldUTGBTrackGroup();
				final TrackGroupPropertyWriter propertyWriter = trackGroup.getPropertyWriter();

				propertyWriter.setProperty(OldUTGBProperty.REVISION, (String) (properties.get(OldUTGBProperty.REVISION)));
				propertyWriter.setProperty(OldUTGBProperty.TARGET, (String) (properties.get(OldUTGBProperty.TARGET)));
				propertyWriter.setProperty(OldUTGBProperty.SPECIES, (String) (properties.get(OldUTGBProperty.SPECIES)));

				return trackGroup;
			}

			Map<String, String> properties = new HashMap<String, String>();

			public void setProperty(String key, String value) {
				properties.put(key, value);
			}

		};
	}

	public OldUTGBTrackGroup() {
		super("OldUTGBTrackGroup");
	}

	public void defaultSetUp() {
		setTrackGroupProperty(new TrackGroupPropertyImpl(this) {
			private int currentTargetLength = 0;
			private GETMethodURL GET_TARGET_LENGTH_URL = GETMethodURL
					.newInstance("http://medaka3.gi.k.u-tokyo.ac.jp/~ssksn/browser_web_api/getTargetLength.php");

			private final int correctIndex(final int inputIndex) {
				int _index = Math.max(inputIndex, 1);
				_index = Math.min(_index, currentTargetLength);

				return _index;
			}

			public void setTrackWindow(int startOnGenome, int endOnGenome) {
				super.setTrackWindow(correctIndex(startOnGenome), correctIndex(endOnGenome));
			}

			public void setTrackWindow(TrackWindow newWindow) {
				final TrackWindowImpl _window = new TrackWindowImpl();
				_window.set(newWindow);
				_window.setStartOnGenome(correctIndex(newWindow.getStartOnGenome()));
				_window.setEndOnGenome(correctIndex(newWindow.getEndOnGenome()));

				super.setTrackWindow(_window);
			}

			public void setProperty(Map<String, String> property) {
				boolean updateFlag = false;
				String species = getProperty(OldUTGBProperty.SPECIES);
				String revision = getProperty(OldUTGBProperty.REVISION);
				String target = getProperty(OldUTGBProperty.TARGET);

				if (property.containsKey(OldUTGBProperty.SPECIES)) {
					species = (String) property.get(OldUTGBProperty.SPECIES);
					updateFlag = true;
				}
				if (property.containsKey(OldUTGBProperty.REVISION)) {
					revision = (String) property.get(OldUTGBProperty.REVISION);
					updateFlag = true;
				}
				if (property.containsKey(OldUTGBProperty.TARGET)) {
					target = (String) property.get(OldUTGBProperty.TARGET);
					updateFlag = true;
				}

				if (updateFlag) {
					final Map<String, String> parameterMap = new HashMap<String, String>();
					parameterMap.put("species", species);
					parameterMap.put("revision", revision);
					parameterMap.put("target", target);
					final String url = GET_TARGET_LENGTH_URL.getURL(parameterMap);

					RPCServiceManager.getRPCService().getHTTPContent(url, new AsyncCallback<String>() {
						public void onFailure(Throwable caught) {
							GWT.log("cannot retrieve: " + url, caught);
						}

						public void onSuccess(String result) {
							final String targetLengthStr = result.trim();

							final int targetLength = Integer.parseInt(targetLengthStr);

							if (targetLength != -1) {
								currentTargetLength = targetLength;

								setTrackWindow(1, currentTargetLength);
							}
						}
					});
				}
				super.setProperty(property);
			}

			public void setProperty(String key, String value) {
				boolean updateFlag = false;
				String species = null;
				String revision = null;
				String target = null;

				if (key.equals(OldUTGBProperty.SPECIES)) {
					species = value;
					revision = getProperty(OldUTGBProperty.REVISION);
					target = getProperty(OldUTGBProperty.TARGET);

					updateFlag = true;
				}
				else if (key.equals(OldUTGBProperty.REVISION)) {
					species = getProperty(OldUTGBProperty.SPECIES);
					revision = value;
					target = getProperty(OldUTGBProperty.TARGET);

					updateFlag = true;
				}
				else if (key.equals(OldUTGBProperty.TARGET)) {
					species = getProperty(OldUTGBProperty.SPECIES);
					revision = getProperty(OldUTGBProperty.REVISION);
					target = value;

					updateFlag = true;
				}

				if (updateFlag) {
					final Map<String, String> parameterMap = new HashMap<String, String>();
					parameterMap.put("species", species);
					parameterMap.put("revision", revision);
					parameterMap.put("target", target);
					final String url = GET_TARGET_LENGTH_URL.getURL(parameterMap);

					getBrowserService().getHTTPContent(url, new AsyncCallback<String>() {
						public void onFailure(Throwable caught) {
							GWT.log("cannot retrieve: " + url, caught);
						}

						public void onSuccess(String result) {
							final String targetLengthStr = result.trim();

							final int targetLength = Integer.parseInt(targetLengthStr);

							if (targetLength != -1) {
								currentTargetLength = targetLength;

								setTrackWindow(1, currentTargetLength);
							}
						}
					});
				}
				super.setProperty(key, value);
			}
		});

		final TrackGroupPropertyWriter propertyWriter = getPropertyWriter();
		propertyWriter.setTrackWindowSize(800);
		final Map<String, String> defaultPropertyMap = new HashMap<String, String>();
		defaultPropertyMap.put(OldUTGBProperty.SPECIES, "medaka");
		defaultPropertyMap.put(OldUTGBProperty.REVISION, "200506");
		defaultPropertyMap.put(OldUTGBProperty.TARGET, "scaffold1");
		propertyWriter.setProperty(defaultPropertyMap);

		//addTrack(new LoadAndStoreTrack());
		final TrackTreeTrack ttTrack = new TrackTreeTrack();
		addTrackUpdateListener(ttTrack);
		addTrack(ttTrack);

		addTrack(new OldUTGBPropertyTrack());
		addTrack(new OldUTGBAddTrackTrack());

		// {
		// final SequenceRulerTrack sequenceRulerTrack = new SequenceRulerTrack();
		// addTrack(sequenceRulerTrack);
		// sequenceRulerTrack.setSequenceSize(10000000);
		// addTrack(new RulerTrack());
		// }

		{
			final ValueSelectorTrack speciesTrack = new ValueSelectorTrack("Species", OldUTGBProperty.SPECIES);
			speciesTrack.addValue("medaka");
			speciesTrack.addValue("medaka-HNI");
			addTrack(speciesTrack);
		}

		{
			final ValueSelectorTrack revisionTrack = new ValueSelectorTrack("Revision", OldUTGBProperty.REVISION) {
				public void onChangeTrackGroupProperty(TrackGroupPropertyChange change) {
					if (change.contains(OldUTGBProperty.SPECIES)) {
						setRevision(change.getProperty(OldUTGBProperty.SPECIES));
						draw();
						updateSelection();
					}

				}

				private void setRevision(String species) {

					if (species.equals("medaka")) {
						clearValues();
						addValue("200406");
						addValue("200506");
						addValue("version0.9");
						addValue("version1.0");
					}
					else if (species.equals("medaka-HNI")) {
						clearValues();
						addValue("version1.0");
					}

				}
			};
			revisionTrack.addValue("200406");
			revisionTrack.addValue("200506");
			revisionTrack.addValue("version0.9");
			revisionTrack.addValue("version1.0");
			addTrack(revisionTrack);
		}

		{
			final OldUTGBTrack rulerTrack = new OldUTGBRulerTrack();
			addTrack(rulerTrack);
			rulerTrack.setDescriptionXML("http://medaka3.gi.k.u-tokyo.ac.jp/~ssksn/descriptions/rulertrack.xml");

			final OldUTGBTrack zoomerTrack = new OldUTGBRulerTrack();
			addTrack(zoomerTrack);
			zoomerTrack.setDescriptionXML("http://medaka3.gi.k.u-tokyo.ac.jp/~ssksn/descriptions/zoomertrack.xml");

			final OldUTGBTrack basecolorTrack = new OldUTGBRulerTrack();
			addTrack(basecolorTrack);
			basecolorTrack.setDescriptionXML("http://medaka3.gi.k.u-tokyo.ac.jp/~ssksn/descriptions/basecolortrack.xml");
		}

		final String[] descriptionXMLs = {
				// "http://medaka3.gi.k.u-tokyo.ac.jp/~ssksn/descriptions/rulertrack.xml",
				// "http://medaka3.gi.k.u-tokyo.ac.jp/~ssksn/descriptions/zoomertrack.xml",
				// "http://medaka3.gi.k.u-tokyo.ac.jp/~ssksn/descriptions/basecolortrack.xml",
				"http://medaka3.gi.k.u-tokyo.ac.jp/~ssksn/descriptions/qv.xml", "http://medaka3.gi.k.u-tokyo.ac.jp/~ssksn/descriptions/coverage.xml",
				"http://medaka3.gi.k.u-tokyo.ac.jp/~ssksn/descriptions/mappedGene.xml", "http://medaka3.gi.k.u-tokyo.ac.jp/~ssksn/descriptions/5sage.xml",
				"http://medaka3.gi.k.u-tokyo.ac.jp/~yamada/repeat/test.xml",
				"http://medaka.utgenome.org/~ssksn/clonelink_test_track/clonelink_test_description.xml",
				// "http://medaka3.gi.k.u-tokyo.ac.jp/~ssksn/bac_end_track/bac_end_description.xml",
				"http://medaka3.gi.k.u-tokyo.ac.jp/~ssksn/geneticMarker_track/geneticMarker_description.xml" };

		for (int i = 0; i < descriptionXMLs.length; i++) {
			final String descriptionXML = descriptionXMLs[i];

			final OldUTGBTrack track = new OldUTGBTrack();

			addTrack(track);
			track.setDescriptionXML(descriptionXML);
		}

		/*
		 * final String descriptionXML =
		 * "http://medaka3.gi.k.u-tokyo.ac.jp/~ssksn/fosmid_end_track/fosmid_end_description.xml";
		 * 
		 * final OldUTGBTrack track = new OldUTGBTrack(); track.setDescriptionXML(descriptionXML);
		 * 
		 * addTrack(track);
		 */

		setTrackWindowLocation(1, 1000000);
	}

}
