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
// UTGB Common Project
//
// TreePanel.java
// Since: 2007/05/28
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.ui.treeview;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * TreePanel
 * 
 * @author leo
 * 
 */
public class TreePanel extends Composite {
	private VerticalPanel _basePanel = new VerticalPanel();
	private Widget _content = null;
	private VerticalPanel _childPanel = new VerticalPanel();

	private TreePanel _nextSibling = null;
	private TreePanel _firstChild = null;

	public TreePanel() {
		init();
	}

	public TreePanel(Widget content) {
		this._content = content;
		init();
	}

	private void init() {
		_basePanel.setStyleName("tree-panel");
		initWidget(_basePanel);
	}

	public TreePanel getNextSibling() {
		return _nextSibling;
	}

	public TreePanel getFirstChild() {
		return _firstChild;
	}

	public TreePanel addChild(Widget w) {
		return addChild(new TreePanel(w));
	}

	public TreePanel addSiblign(Widget w) {
		return addSibling(new TreePanel(w));
	}

	public TreePanel addChild(TreePanel child) {
		if (_firstChild == null)
			return _firstChild = child;
		else
			return _firstChild.addSibling(child);
	}

	public TreePanel addSibling(TreePanel sibling) {
		if (_nextSibling == null)
			return _nextSibling = sibling;
		else
			return _nextSibling.addSibling(sibling);
	}

	public int numChildren() {
		if (_firstChild == null)
			return 0;
		else
			return _firstChild.numSiblings();
	}

	public int numSiblings() {
		TreePanel cursor = _nextSibling;
		int count = 0;
		while (cursor != null) {
			count++;
			cursor = cursor._nextSibling;
		}
		return count;
	}

	public void layout(Panel panel) {

		// setup the base panel
		_basePanel.clear();
		_basePanel.add(new TreePanelSwitch());
		if (_content != null)
			_basePanel.add(_content);
		_basePanel.add(_childPanel);

		panel.add(this);
		if (_nextSibling != null) {
			HorizontalPanel siblingPanel = new HorizontalPanel();
			panel.add(siblingPanel);
			_nextSibling.layout(siblingPanel);
		}

		if (_firstChild != null) {
			_firstChild.layout(_childPanel);
		}
	}

}

class TreePanelSwitch extends Image implements ClickHandler {
	private boolean _isOpen = true;

	private static String OPEN_IMAGE = "image/tree_open.gif";
	private static String CLOSED_IMAGE = "image/tree_closed.gif";
	private static String WHITE_IMAGE = "image/tree_white.gif";

	public TreePanelSwitch() {
		super(OPEN_IMAGE);
	}

	public void onClick(ClickEvent e) {
		_isOpen = !_isOpen;
		this.setUrl(CLOSED_IMAGE);
	}

}
