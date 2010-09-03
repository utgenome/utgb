/*--------------------------------------------------------------------------
 *  Copyright 2009 utgenome.org
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
// BEDGene.java
// Since: 2010/04/29
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.format.bed;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.utgenome.gwt.utgb.client.bio.BEDGene;
import org.utgenome.gwt.utgb.client.bio.CDS;
import org.utgenome.gwt.utgb.client.bio.Exon;
import org.utgenome.gwt.utgb.client.bio.Gene;
import org.utgenome.gwt.utgb.client.bio.OnGenome;

/**
 * Representing each gene line of BED format
 * 
 * @author leo
 * 
 */
public class BEDEntry extends BEDGene {

	private static final long serialVersionUID = 1L;

	public static BEDEntry createFromResultSet(String chr, ResultSet rs) throws SQLException {

		BEDEntry gene = new BEDEntry();
		gene.coordinate = chr;
		gene.setStart(rs.getInt(1));
		gene.setEnd(rs.getInt(2));

		gene.setName(rs.getString(3));
		gene.score = rs.getInt(4);
		gene.setStrand(rs.getString(5));

		ArrayList<int[]> regionList = readRegions(rs.getString(6));
		for (int[] region : regionList) {
			CDS cds = new CDS(region[0], region[1]);
			gene.addCDS(cds);
		}

		regionList = readRegions(rs.getString(7));
		for (int[] region : regionList) {
			Exon exon = new Exon(region[0], region[1]);
			gene.addExon(exon);
		}

		gene.setColor(rs.getString(8));

		return gene;
	}

	private static ArrayList<int[]> readRegions(String string) {
		ArrayList<int[]> res = new ArrayList<int[]>();

		StringTokenizer st = new StringTokenizer(string, "[] ,");
		while (st.hasMoreTokens()) {
			String str = st.nextToken();

			// get start of region
			if (str.startsWith("(")) {
				int[] region = new int[2];
				region[0] = Integer.valueOf(str.substring(1)).intValue();

				// get end of region
				while (st.hasMoreTokens()) {
					str = st.nextToken();
					if (str.endsWith(")")) {
						region[1] = Integer.valueOf(str.substring(0, str.length() - 1)).intValue();
						res.add(region);
						break;
					}
				}
			}
		}
		return res;
	}

	@Override
	public String toString() {
		return String.format("%s: %s:%d-%d\t%s\t%s\t%s\t%s", getName(), coordinate, getStart(), getEnd(), getStrand(), getCDS(), getExon(), getColor());

	}

}
