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
// OldUTGBOptionAttribute.java
// Since: 2007/07/11
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.lib.old;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.utgenome.gwt.utgb.client.track.TrackConfig;
import org.utgenome.gwt.utgb.client.track.lib.old.datatype.ColorType;
import org.utgenome.gwt.utgb.client.track.lib.old.datatype.DispType;
import org.utgenome.gwt.utgb.client.track.lib.old.datatype.GradationType;
import org.utgenome.gwt.utgb.client.track.lib.old.datatype.LowerBoundType;
import org.utgenome.gwt.utgb.client.track.lib.old.datatype.SelectType;
import org.utgenome.gwt.utgb.client.track.lib.old.datatype.UpperBoundType;

/**
 * @author ssksn
 * 
 */
public abstract class OldUTGBOptionAttribute {

	public abstract void setConfig(final TrackConfig trackConfig);

	public abstract void setParameters(final Map<String, String> parameterMap);

	private static class SelectOptionAttribute extends OldUTGBOptionAttribute {
		private DispType dispType = null;
		private ColorType colorType = null;
		private SelectType selectType = null;

		SelectOptionAttribute(final String parameterName, final String[] values, final String[] operations, final String prefix) {
			for (int i = 0; i < operations.length; i++) {
				if (operations[i].equalsIgnoreCase("disp")) {
					dispType = new DispType(parameterName, values);
				}
				if (operations[i].equalsIgnoreCase("color")) {
					colorType = new ColorType(parameterName, values, prefix);
				}
				if (operations[i].equalsIgnoreCase("select")) {
					selectType = new SelectType(parameterName, values);
				}
			}
		}

		public void setConfig(TrackConfig trackConfig) {
			if (dispType != null)
				trackConfig.addConfig(dispType, "");
			if (colorType != null)
				trackConfig.addConfig(colorType, "");
			if (selectType != null)
				trackConfig.addConfig(selectType, "");
		}

		public void setParameters(Map<String, String> parameterMap) {
			if (dispType != null)
				dispType.setParameters(parameterMap);
			if (colorType != null)
				colorType.setParameters(parameterMap);
			if (selectType != null)
				selectType.setParameters(parameterMap);
		}

	}

	private static class RealOptionAttribute extends OldUTGBOptionAttribute {
		private GradationType gradationType = null;
		private UpperBoundType uboundType = null;
		private LowerBoundType lboundType = null;

		RealOptionAttribute(final String parameterName, final double min, final double max, final String[] operations, final String prefix) {
			for (int i = 0; i < operations.length; i++) {
				if (operations[i].equalsIgnoreCase("gradation")) {
					gradationType = new GradationType(parameterName, min, max, prefix);
				}
				if (operations[i].equalsIgnoreCase("ubound")) {
					uboundType = new UpperBoundType(parameterName, min, max);
				}
				if (operations[i].equalsIgnoreCase("lbound")) {
					lboundType = new LowerBoundType(parameterName, min, max);
				}
			}
		}

		public void setConfig(TrackConfig trackConfig) {
			if (gradationType != null)
				trackConfig.addConfig(gradationType, "");
			if (uboundType != null)
				trackConfig.addConfig(uboundType, "");
			if (lboundType != null)
				trackConfig.addConfig(lboundType, "");
		}

		public void setParameters(Map<String, String> parameterMap) {
			if (gradationType != null)
				gradationType.setParameters(parameterMap);
			if (uboundType != null)
				uboundType.setParameters(parameterMap);
			if (lboundType != null)
				lboundType.setParameters(parameterMap);
		}

	}

	public static OldUTGBOptionAttribute getSelectInstance(final String parameterName, final String[] values, final String[] operations, final String prefix) {
		final String[] uniqueValues = getUniques(values);
		final String[] uniqueOperations = getUniques(operations);

		final SelectOptionAttribute selectOptionAttribute = new SelectOptionAttribute(parameterName, uniqueValues, uniqueOperations, prefix);

		return selectOptionAttribute;
	}

	public static OldUTGBOptionAttribute getRealInstance(final String parameterName, final String minValue, final String maxValue, final String[] operations,
			final String prefix) {
		try {
			final String[] uniqueOperations = getUniques(operations);

			final double min = Double.parseDouble(minValue);
			final double max = Double.parseDouble(maxValue);

			final RealOptionAttribute realOptionAttribute = new RealOptionAttribute(parameterName, min, max, uniqueOperations, prefix);

			return realOptionAttribute;
		}
		catch (NumberFormatException ne) {
			return null;
		}
	}

	private static final String[] getUniques(final String[] inputArray) {
		final Set<String> set = new HashSet<String>();

		for (int i = 0; i < inputArray.length; i++) {
			final String value = inputArray[i];

			if (!set.contains(value))
				set.add(value);
		}

		final String[] outputArray = new String[set.size()];

		int j = 0;
		for (int i = 0; i < inputArray.length; i++) {
			final String value = inputArray[i];

			if (set.contains(value))
				outputArray[j++] = value;
		}

		return outputArray;
	}
}
