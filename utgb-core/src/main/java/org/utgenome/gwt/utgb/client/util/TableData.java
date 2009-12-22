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
// TableData.java
// Since: Jun 25, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.util;

import java.util.HashMap;
import java.util.Set;

import org.utgenome.gwt.utgb.client.UTGBClientException;

import com.google.gwt.core.client.GWT;

/**
 * TableData holds table data, the key of which is a column with a name "id"
 * 
 * @author leo
 *
 */
public class TableData {
	
	private String[] _columnName;
	private HashMap<Object, Object[]> _rowTable = new HashMap<Object, Object[]>();
	
	public final static String DEFAULT_KEY_COLUMN_NAME = "id"; 
	private int _keyColumn = -1;
	
	public TableData(String[] columnName)
	{
		this._columnName = columnName;
		// search key column 
		for(int i=0; i<_columnName.length; i++)
		{
			if(_columnName[i].equals(DEFAULT_KEY_COLUMN_NAME))
				_keyColumn = i;
		}

		verifyKeyColumn();
	}
	
	public TableData(String[] columnName, int keyColumn)
	{
		this._columnName = columnName;
		_keyColumn = keyColumn;
		verifyKeyColumn();
	}
	
	private void verifyKeyColumn()
	{
		if(_keyColumn < 0 || _keyColumn >= _columnName.length)
		{
			GWT.log("key column is not found in " + _columnName, new UTGBClientException());
			_keyColumn = 0; // default
		}
	}
	
	
	
	/**
	 * @param row
	 * @return key
	 */
	public Object addRow(Object[] row)
	{
		Object key = row[_keyColumn];
		_rowTable.put(key, row);
		return key;
	}
	
	/**
	 * key set of Integer  
	 * @return
	 */
	public Set<Object> keySet()
	{
		return _rowTable.keySet();
	}
	
	public Object[] getRow(Object key)
	{
		return _rowTable.get(key);
	}
	
	public int getColumnCount()
	{
		return _columnName.length;
	}
	
	
	public String[] getColumnLabel()
	{
		return _columnName;
	}
	public String getColumnLabel(int column)
	{
		return _columnName[column];
	}

	public void clear() {
		_rowTable.clear();
	}

	public int getKeyColumn() {
		return _keyColumn;
	}
	
}




