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
// BugReport Project
//
// DynamicForm.java
// Since: 2007/03/28
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.db;

import java.util.HashMap;
import java.util.Map.Entry;

import org.utgenome.gwt.utgb.client.UTGBClientException;
import org.utgenome.gwt.utgb.client.db.datatype.DataType;
import org.utgenome.gwt.utgb.client.db.datatype.InputForm;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

/**
 * A DynamicForm class creates a data input form organized according to a given
 * reltaion
 * 
 * @author leo
 * 
 */
public class DynamicForm extends Composite
{
    FlexTable _panel = new FlexTable();
    Relation _relation = null;
    HashMap<String, InputForm> _parameterNameToInputForm = new HashMap<String, InputForm>();

    public DynamicForm()
    {
        _panel.setStyleName("form");
        initWidget(_panel);
    }

    private boolean isValidateParameterName(String parameterName)
    {
        if (_parameterNameToInputForm.get(parameterName) == null)
        {
            GWT.log("no input form for the given parameter name, " + parameterName + ", found",
                    new UTGBClientException());
            return false;
        }
        return true;
    }

    public void setValue(String parameterName, String value)
    {
        if (!isValidateParameterName(parameterName))
            return;

        InputForm inputForm = (InputForm) _parameterNameToInputForm.get(parameterName);
        inputForm.setValue(value);
    }

    public String getValue(String parameterName)
    {
        if (!isValidateParameterName(parameterName))
            return "";

        InputForm inputForm = (InputForm) _parameterNameToInputForm.get(parameterName);
        return inputForm.getUserInput();
    }

    public void setRelataion(Relation relation)
    {
        this._relation = relation;
        reloadRelation();
    }

    public void reloadRelation()
    {
        // update the panel
        _panel.clear();
        _parameterNameToInputForm.clear();
        for (DataType dt : _relation.getDataTypeList())
        {
            HorizontalPanel fieldPanel = new HorizontalPanel();

            InputForm inputForm = dt.getInputForm();

            Label label = new Label(dt.getName() + ":");
            label.setStyleName("form-label");
            inputForm.setStyleName("form-field");

            int row = _panel.getRowCount();
            _panel.setWidget(row, 0, label);
            _panel.setWidget(row, 1, inputForm);
            _parameterNameToInputForm.put(dt.getName(), inputForm);
        }
    }

    public JSONObject getInputData()
    {
        JSONObject json = new JSONObject();
        for (Entry<String, InputForm> entry : _parameterNameToInputForm.entrySet())
        {
            String parameterName = (String) entry.getKey();
            InputForm inputField = (InputForm) entry.getValue();
            json.put(parameterName, inputField.getJSONValue());
        }

        return json;
    }

}
