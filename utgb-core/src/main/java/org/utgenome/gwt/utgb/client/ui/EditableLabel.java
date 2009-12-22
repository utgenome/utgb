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
// EditableLabel.java
// Since: Jun 18, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.user.client.ui.TextBox;

/**
 * This class is used to implement {@link EditableTable}.
 * 
 * @author leo
 * 
 */
public class EditableLabel extends TextBox {
	private final CellReference _cellReference;

	public EditableLabel(CellReference cellReference, String text) {
		_cellReference = cellReference;
		setText(text);

		CSS.set(this, CSS.CSS_BORDER, "0");
		CSS.backgroundColor(this, "#9999FF");
		setSize("100%", "100%");

		addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent arg0) {
				setSelectionRange(0, getText().length());
			}
		});
		addFocusHandler(new FocusHandler() {

			public void onFocus(FocusEvent arg0) {
				//CSS.backgroundColor(_textBox, "#9999FF");				
			}
		});

	}

	public CellReference getCellReference() {
		return _cellReference;
	}

}
