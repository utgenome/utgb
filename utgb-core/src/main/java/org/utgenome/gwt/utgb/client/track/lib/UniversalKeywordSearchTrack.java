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
// UniversalKeywordSearchTrack.java
// Since: Aug 15, 2008
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.lib;

import java.util.ArrayList;
import java.util.HashMap;

import org.utgenome.gwt.utgb.client.GenomeBrowser;
import org.utgenome.gwt.utgb.client.track.Track;
import org.utgenome.gwt.utgb.client.track.TrackBase;
import org.utgenome.gwt.utgb.client.track.TrackFrame;
import org.utgenome.gwt.utgb.client.track.TrackGroup;
import org.utgenome.gwt.utgb.client.track.TrackGroupPropertyWriter;
import org.utgenome.gwt.utgb.client.track.UTGBProperty;
import org.utgenome.gwt.utgb.client.track.bean.Result;
import org.utgenome.gwt.utgb.client.track.bean.SearchResult;
import org.utgenome.gwt.utgb.client.ui.FormLabel;
import org.utgenome.gwt.utgb.client.util.JSONUtil;
import org.utgenome.gwt.utgb.client.util.Properties;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * keyword search across species, revision
 * 
 * @author leo
 * 
 */
public class UniversalKeywordSearchTrack extends TrackBase {
	public static TrackFactory factory() {
		return new TrackFactory() {
			public Track newInstance() {
				return new KeywordSearchTrack();
			}
		};
	}

	private DockPanel basePanel = new DockPanel();
	private HorizontalPanel layoutPanel = new HorizontalPanel();
	private SearchInput keywordPanel;
	private VerticalPanel searchResultPanel = new VerticalPanel();
	private Pager pager = new Pager();
	private PopupPanel keywordHelpPopup = new PopupPanel(true);
	private ArrayList<String> keywordExampleList = new ArrayList<String>();
	final HTML keywordHelp = new HTML("");

	class SearchInput extends Composite {
		private Label _label;
		private Widget _form;
		private HorizontalPanel _layoutPanel = new HorizontalPanel();

		public SearchInput(String label, Widget form) {
			_label = new Label(label);
			_label.setStyleName("search-label");
			_form = form;
			_form.setStyleName("search-field");
			_layoutPanel.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
			_layoutPanel.add(_label);
			_layoutPanel.add(_form);
			initWidget(_layoutPanel);
		}

		public String getInput() {
			return ((TextBox) _form).getText();
		}
	}

	class Pager extends Composite {
		int page = 0;
		int maxPage = 0;
		HorizontalPanel panel = new HorizontalPanel();
		String keyword = "";

		public Pager() {
			panel.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
			panel.setStyleName("pager");
			initWidget(panel);
		}

		public void update(String keyword, int page, int maxPage) {
			this.keyword = keyword;
			this.page = page;
			this.maxPage = maxPage;
			panel.clear();
			panel.add(new FormLabel("page: "));
			int minPagerPage = (page / 10) * 10 >= 0 ? ((page / 10) * 10) : page;
			int maxPagerPage = (minPagerPage + 10) > maxPage ? maxPage : minPagerPage + 10;
			if (minPagerPage > 0) {
				panel.add(getPagerLink(minPagerPage - 1, "<<"));
				panel.add(getPagerLink(page - 1, "prev"));
			}
			for (int i = minPagerPage; i < maxPagerPage; i++) {
				final int pageNum = i;
				final String currentKeyword = keyword;
				String pageNumStr = Integer.toString(i + 1);
				if (i == page) {
					Label label = new Label(pageNumStr);
					label.setStyleName("current");
					panel.add(label);
				}
				else {
					panel.add(getPagerLink(i, pageNumStr));
				}
			}
			if (maxPagerPage < maxPage) {
				panel.add(getPagerLink(page + 1, "next"));
				panel.add(getPagerLink(maxPagerPage, ">>"));
			}
		}

		private Anchor getPagerLink(final int pageNum, String label) {
			Anchor link = new Anchor(label);
			link.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent e) {
					performSearch(keyword, pageNum, 10);
				}
			});
			return link;
		}
	}

	private void performSearch(final String keyword, int numPage, int entriesPerPage) {
		getFrame().setNowLoading();
		String species = getTrackGroup().getPropertyReader().getProperty(UTGBProperty.SPECIES, "");
		String revision = getTrackGroup().getPropertyReader().getProperty(UTGBProperty.REVISION, "");
		GenomeBrowser.getService().keywordSearch(species, revision, keyword, entriesPerPage, numPage, new AsyncCallback<SearchResult>() {
			public void onFailure(Throwable caught) {
				getFrame().loadingDone();
				GWT.log("search failed:", caught);
			}

			public void onSuccess(SearchResult foundEntryList) {
				if (foundEntryList == null) {
					getFrame().loadingDone();
					return;
				}
				searchResultPanel.clear();
				if (foundEntryList.getCount() <= 0) {
					searchResultPanel.add(new FormLabel("no entry is found"));
				}
				else {
					pager.update(keyword, foundEntryList.getPage(), foundEntryList.getMaxpage());
					searchResultPanel.add(pager);
					for (final Result e : foundEntryList.getResult()) {
						HorizontalPanel hp = new HorizontalPanel();
						hp.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
						hp.add(new Image("image/item.gif"));
						String label = e.getSpecies() + "/" + e.getRevision() + "/" + e.getTarget() + ":" + e.getStart() + "-" + e.getEnd() + "";
						Anchor link = new Anchor(label);
						link.setStyleName("searchresult");
						link.addClickHandler(new ClickHandler() {
							public void onClick(ClickEvent event) {
								TrackGroupPropertyWriter writer = getTrackGroup().getPropertyWriter();
								HashMap<String, String> property = new HashMap<String, String>();
								property.put(UTGBProperty.SPECIES, e.getSpecies());
								property.put(UTGBProperty.REVISION, e.getRevision());
								property.put(UTGBProperty.TARGET, e.getTarget());
								writer.setProperty(property);
								writer.setTrackWindow(e.getStart(), e.getEnd());
							}
						});
						hp.add(link);
						searchResultPanel.add(hp);
						FlowPanel tagPanel = new FlowPanel();
						for (String tag : e.getKeywordList()) {
							HorizontalPanel h = new HorizontalPanel();
							h.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
							// Label key = new Label(tag.getKey());
							// key.setStyleName("foundkey");
							// Label colon = new Label(":");
							FormLabel value = new FormLabel(tag);
							// h.add(key);
							// h.add(colon);
							h.add(value);
							tagPanel.add(h);
						}
						searchResultPanel.add(tagPanel);
					}
				}
				refresh();
				getFrame().loadingDone();
			}
		});
	}

	public UniversalKeywordSearchTrack() {
		super("Keyword Search");
		basePanel.setStyleName("form");
		searchResultPanel.setStyleName("searchresult");
		final TextBox keywordBox = new TextBox();
		keywordBox.addKeyUpHandler(new KeyUpHandler() {
			public void onKeyUp(KeyUpEvent e) {
				if (e.getNativeKeyCode() == KeyCodes.KEY_ENTER)
					performSearch(keywordPanel.getInput(), 0, 10);
			}
		});
		keywordBox.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent e) {
				keywordBox.setFocus(true);
			}
		});
		keywordPanel = new SearchInput("Keyword", keywordBox);
		final HorizontalPanel hp = new HorizontalPanel();
		hp.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
		hp.add(keywordPanel);
		Button button = new Button("Search");
		button.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent e) {
				performSearch(keywordPanel.getInput(), 0, 10);
			}
		});
		hp.add(button);

		// keywordHelp.setStyleName("");
		keywordHelpPopup.add(keywordHelp);
		keywordHelpPopup.setStyleName("helpPopup");
		final Label helpLabel = new Label("keyword help");
		helpLabel.setStyleName("help");
		helpLabel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent e) {
				keywordHelpPopup.setPopupPosition(helpLabel.getAbsoluteLeft() + 5, helpLabel.getAbsoluteTop() + 10);
				keywordHelpPopup.show();
			}
		});
		hp.add(helpLabel);
		layoutPanel.add(hp);

		basePanel.add(layoutPanel, DockPanel.CENTER);
		basePanel.add(searchResultPanel, DockPanel.SOUTH);
	}

	public Widget getWidget() {
		return basePanel;
	}

	public void setUp(TrackFrame trackFrame, TrackGroup group) {
		trackFrame.pack();
		// trackFrame.disableClose();
	}

	@Override
	public void draw() {
		StringBuilder htmlBuf = new StringBuilder();
		htmlBuf.append("<b>Keyword Examples:</b>");
		htmlBuf.append("<ul>");
		for (String e : keywordExampleList) {
			htmlBuf.append("<li>");
			htmlBuf.append(e);
			htmlBuf.append("</li>");
		}
		htmlBuf.append("</ul>");

		keywordHelp.setHTML(htmlBuf.toString());
	}

	@Override
	public void restoreProperties(Properties properties) {
		keywordExampleList.clear();
		keywordExampleList.addAll(JSONUtil.parseJSONArray(properties.get("keyword.examples", "[]")));

	}

	@Override
	public void saveProperties(Properties saveData) {
		saveData.add("keyword.examples", JSONUtil.toJSONArray(keywordExampleList));
	}

}
