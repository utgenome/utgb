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
// MouseClickListener.java
// Since: 2007/06/14
//
// $URL$ 
// $Author$ ssksn
//--------------------------------------
package org.utgenome.gwt.utgb.client.operation;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author ssksn
 * 
 */
public class MouseClickEventImpl extends EventImpl implements ClickHandler {

	public void onClick(ClickEvent e) {
		final int SIZE = operations.size();
		for (int i = 0; i < SIZE; i++) {
			final Operation operation = (Operation) (operations.get(i));
			operation.execute((Widget) e.getSource(), -1, -1);
		}
	}

}
