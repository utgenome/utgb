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
// OldUTGBPropertyTrack.java
// Since: 2007/06/20
//
// $URL$ 
// $Author$ ssksn
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.lib.old;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.utgenome.gwt.utgb.client.track.Track;
import org.utgenome.gwt.utgb.client.track.TrackBase;
import org.utgenome.gwt.utgb.client.track.TrackFrame;
import org.utgenome.gwt.utgb.client.track.TrackGroup;
import org.utgenome.gwt.utgb.client.track.TrackGroupProperty;
import org.utgenome.gwt.utgb.client.track.TrackGroupPropertyChange;
import org.utgenome.gwt.utgb.client.track.TrackGroupPropertyWriter;
import org.utgenome.gwt.utgb.client.track.TrackWindow;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author ssksn
 * 
 */
public class OldUTGBPropertyTrack extends TrackBase {

	public static TrackFactory factory() {
		return new TrackFactory() {
			public Track newInstance() {
				return new OldUTGBPropertyTrack();
			}
		};
	}

	private final DockPanel _panel = new DockPanel();

	private final Label _label = new Label();

	private final FlexTable _tablePanel = new FlexTable();

	private final ListBox _speciesListBox = new ListBox();
	private final ListBox _revisionListBox = new ListBox();

	private final TextBox _targetTextBox = new TextBox();

	private final TextBox _startTextBox = new TextBox();
	private final TextBox _endTextBox = new TextBox();

	private final TextBox _widthTextBox = new TextBox();

	private final List<String> _speciesList = new ArrayList<String>();
	private final List<List<String>> _species_revisionMapList = new ArrayList<List<String>>();

	public OldUTGBPropertyTrack() {
		super("Property Viewer");

		_panel.add(_tablePanel, DockPanel.CENTER);
		_panel.setWidth("100%");

		_label.setStyleName("operation-track-label");
		_label.setWidth("100%");
		_label.setHorizontalAlignment(Label.ALIGN_CENTER);

		_tablePanel.setStyleName("parameter-track");
		_tablePanel.setWidth("100%");

		{
			_speciesList.add("medaka");
			_speciesList.add("medaka-HNI");

			{
				final List<String> medakaRevisionList = new ArrayList<String>();
				medakaRevisionList.add("version1.0");
				medakaRevisionList.add("version0.9");
				medakaRevisionList.add("200506");
				medakaRevisionList.add("200406");

				_species_revisionMapList.add(medakaRevisionList);
			}
			{
				final List<String> medakaHNIRevisionList = new ArrayList<String>();
				medakaHNIRevisionList.add("version1.0");

				_species_revisionMapList.add(medakaHNIRevisionList);
			}
		}

		_tablePanel.setText(0, 0, OldUTGBProperty.SPECIES.toUpperCase());
		_tablePanel.setWidget(0, 1, _speciesListBox);
		{
			setItems(_speciesListBox, _speciesList);

			_speciesListBox.setVisibleItemCount(1);

			_speciesListBox.addChangeHandler(new ChangeHandler() {
				public void onChange(ChangeEvent e) {
					final TrackGroupPropertyWriter propertyWriter = _trackGroup.getPropertyWriter();

					final int selectedSpeciesIndex = _speciesListBox.getSelectedIndex();
					final String selectedValue = _speciesListBox.getValue(selectedSpeciesIndex);

					if (selectedValue.length() > 0) {
						propertyWriter.setProperty(OldUTGBProperty.SPECIES, selectedValue);

						removeAllItems(_revisionListBox);

						final List<String> revisionList = _species_revisionMapList.get(selectedSpeciesIndex);
						setItems(_revisionListBox, revisionList);
					}
				}

			});
		}

		_tablePanel.setText(0, 2, OldUTGBProperty.REVISION.toUpperCase());
		_tablePanel.setWidget(0, 3, _revisionListBox);
		{
			final List<String> revisionList = _species_revisionMapList.get(0);
			setItems(_revisionListBox, revisionList);

			_revisionListBox.setVisibleItemCount(1);
			_revisionListBox.setSelectedIndex(1);

			_revisionListBox.addChangeHandler(new ChangeHandler() {
				public void onChange(ChangeEvent e) {
					final TrackGroupPropertyWriter propertyWriter = _trackGroup.getPropertyWriter();

					final int selectedSpeciesIndex = _revisionListBox.getSelectedIndex();
					final String selectedValue = _revisionListBox.getValue(selectedSpeciesIndex);

					if (selectedValue.length() > 0)
						propertyWriter.setProperty(OldUTGBProperty.REVISION, _revisionListBox.getValue(selectedSpeciesIndex));
				}
			});
		}

		_tablePanel.setText(0, 4, OldUTGBProperty.TARGET.toUpperCase());
		_tablePanel.setWidget(0, 5, _targetTextBox);
		{
			_targetTextBox.addKeyPressHandler(new KeyPressHandler() {

				public void onKeyPress(KeyPressEvent e) {
					char keyCode = e.getCharCode();
					if (keyCode == KeyCodes.KEY_ENTER || keyCode == KeyCodes.KEY_TAB) {
						final TrackGroupPropertyWriter propertyWriter = _trackGroup.getPropertyWriter();
						propertyWriter.setProperty(OldUTGBProperty.TARGET, _targetTextBox.getText());
					}

				}
			});
		}

		_tablePanel.setText(1, 0, "START");
		_tablePanel.setWidget(1, 1, _startTextBox);
		{
			_startTextBox.addKeyPressHandler(new KeyPressHandler() {
				public void onKeyPress(KeyPressEvent e) {
					char keyCode = e.getCharCode();
					if (keyCode == KeyCodes.KEY_ENTER || keyCode == KeyCodes.KEY_TAB) {
						final TrackWindow trackWindow = _trackGroup.getTrackWindow();
						_trackGroup.setTrackWindowLocation(Integer.parseInt(_startTextBox.getText()), trackWindow.getEndOnGenome());
					}

				}
			});
		}

		_tablePanel.setText(1, 2, "END");
		_tablePanel.setWidget(1, 3, _endTextBox);
		{
			_endTextBox.addKeyPressHandler(new KeyPressHandler() {
				public void onKeyPress(KeyPressEvent e) {
					char keyCode = e.getCharCode();
					if (keyCode == KeyCodes.KEY_ENTER || keyCode == KeyCodes.KEY_TAB) {
						final TrackWindow trackWindow = _trackGroup.getTrackWindow();
						_trackGroup.setTrackWindowLocation(trackWindow.getStartOnGenome(), Integer.parseInt(_endTextBox.getText()));
					}

				}
			});
		}

		_tablePanel.setText(1, 4, "WIDTH");
		_tablePanel.setWidget(1, 5, _widthTextBox);
		{
			_widthTextBox.setEnabled(false);
		}
	}

	private static final void setItems(final ListBox listBox, final List<String> itemList) {
		final Iterator<String> speciesIterator = itemList.iterator();
		while (speciesIterator.hasNext()) {
			final String str = speciesIterator.next();
			listBox.addItem(str, str);
		}
	}

	private static final void removeAllItems(final ListBox listBox) {
		while (listBox.getItemCount() > 0) {
			listBox.removeItem(0);
		}
	}

	public Widget getWidget() {
		return _panel;
	}

	public void onChangeTrackGroupProperty(TrackGroupPropertyChange change) {
		String currentSelectedSpecies = _speciesListBox.getItemText(_speciesListBox.getSelectedIndex());
		final String newSpecies = change.getProperty(OldUTGBProperty.SPECIES);
		if (change.contains(OldUTGBProperty.SPECIES) && !(newSpecies.equals(currentSelectedSpecies))) {
			final int _index = _speciesList.indexOf(newSpecies);
			if (_index == -1) {
				removeAllItems(_speciesListBox);
				setItems(_speciesListBox, _speciesList);
				_speciesListBox.addItem("(" + newSpecies + ")", "");
				_speciesListBox.setSelectedIndex(_speciesList.size());

				removeAllItems(_revisionListBox);
				_revisionListBox.setEnabled(false);
				_targetTextBox.setEnabled(false);
				_startTextBox.setEnabled(false);
				_endTextBox.setEnabled(false);

				writeMessage("This species/revision setting is not valid for the track. Please select a valid setting.");
				_frame.onUpdateTrackWidget();
			}
			else {
				removeAllItems(_speciesListBox);
				setItems(_speciesListBox, _speciesList);
				_speciesListBox.setSelectedIndex(_index);

				final List<String> _revisionList = _species_revisionMapList.get(_index);
				removeAllItems(_revisionListBox);
				setItems(_revisionListBox, _revisionList);

				_revisionListBox.setEnabled(true);
				_targetTextBox.setEnabled(true);
				_startTextBox.setEnabled(true);
				_endTextBox.setEnabled(true);

				eraseMassege();
				_frame.onUpdateTrackWidget();
			}
		}

		if (change.contains(OldUTGBProperty.REVISION) && _revisionListBox.isEnabled()) {
			final String newRevision = change.getProperty(OldUTGBProperty.REVISION);
			final int selectedIndex = _speciesListBox.getSelectedIndex();

			final List<String> _revisionList = _species_revisionMapList.get(selectedIndex);

			final int _index = _revisionList.indexOf(newRevision);
			if (_index == -1) {
				removeAllItems(_revisionListBox);
				setItems(_revisionListBox, _revisionList);
				_revisionListBox.addItem("(" + newRevision + ")", "");
				_revisionListBox.setSelectedIndex(_revisionList.size());

				_revisionListBox.setEnabled(false);
				_targetTextBox.setEnabled(false);
				_startTextBox.setEnabled(false);
				_endTextBox.setEnabled(false);

				writeMessage("This species/revision setting is not valid for the track. Please select a valid setting.");
				_frame.onUpdateTrackWidget();
			}
			else {
				removeAllItems(_revisionListBox);
				setItems(_revisionListBox, _revisionList);
				_revisionListBox.setSelectedIndex(_index);

				_revisionListBox.setEnabled(true);
				_targetTextBox.setEnabled(true);
				_startTextBox.setEnabled(true);
				_endTextBox.setEnabled(true);

				eraseMassege();
				_frame.onUpdateTrackWidget();
			}
		}

		if (change.contains(OldUTGBProperty.TARGET)) {
			_targetTextBox.setText(change.getProperty(OldUTGBProperty.TARGET));
		}
	}

	public void onChangeTrackWindow(TrackWindow newWindow) {
		_startTextBox.setText(Long.toString(newWindow.getStartOnGenome()));
		_endTextBox.setText(Long.toString(newWindow.getEndOnGenome()));
		_widthTextBox.setText(Integer.toString(newWindow.getWindowWidth()));
	}

	public void setUp(TrackFrame trackFrame, TrackGroup group) {
		trackFrame.pack();
		final TrackWindow trackWindow = group.getTrackWindow();

		_widthTextBox.setText(Integer.toString(trackWindow.getWindowWidth()));
		_startTextBox.setText(Long.toString(trackWindow.getStartOnGenome()));
		_endTextBox.setText(Long.toString(trackWindow.getEndOnGenome()));

		final TrackGroupProperty propertyReader = group.getPropertyReader();

		_targetTextBox.setText(propertyReader.getProperty(OldUTGBProperty.TARGET));
	}

	private final void writeMessage(final String message) {
		eraseMassege();

		_panel.add(_label, DockPanel.NORTH);
		_label.setText(message);
	}

	private final void eraseMassege() {
		final int index = _panel.getWidgetIndex(_label);
		if (index != -1) {
			_panel.remove(_label);
		}
	}

}
