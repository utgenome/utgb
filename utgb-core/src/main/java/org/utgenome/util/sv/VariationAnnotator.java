/*--------------------------------------------------------------------------
 *  Copyright 2010 utgenome.org
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
// VariationAnnotator.java
// Since: 2010/10/13
//
//--------------------------------------
package org.utgenome.util.sv;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

import org.utgenome.format.fasta.CompactFASTA;
import org.utgenome.gwt.utgb.client.bio.BEDGene;
import org.utgenome.gwt.utgb.client.canvas.IntervalTree;
import org.xerial.lens.Lens;
import org.xerial.lens.ObjectHandler;
import org.xerial.util.log.Logger;
import org.xerial.util.opt.Argument;

/**
 * Annotating structural variation types (e.g., synonymous mutation, frame shift, etc.)
 * 
 * @author leo
 * 
 */
public class VariationAnnotator {

	private static Logger _logger = Logger.getLogger(VariationAnnotator.class);

	@Argument(index = 0)
	private String fastaFile;

	@Argument(index = 1)
	private String geneBED;

	@Argument(index = 2)
	private String variationPosFile;

	private CompactFASTA fasta;
	private IntervalTree<BEDGene> geneSet = new IntervalTree<BEDGene>();

	public VariationAnnotator() {

	}

	public void execute() throws Exception {

		// load FASTA (packed via utgb pack)
		_logger.info("loading FASTA pac file...");
		fasta = CompactFASTA.loadIntoMemory(fastaFile);

		// load BED
		Lens.findFromSilk(new BufferedReader(new FileReader(geneBED)), "gene", BEDGene.class, new ObjectHandler<BEDGene>() {
			public void finish() throws Exception {
				_logger.info("loading done.");
			}

			public void handle(BEDGene gene) throws Exception {
				geneSet.add(gene);
			}

			public void init() throws Exception {
				_logger.info("loading gene BED file...");
			}
		});

		// load variation position file
		Lens.findFromSilk(new BufferedReader(new FileReader(variationPosFile)), "variation", GeneticVariation.class, new ObjectHandler<GeneticVariation>() {
			public void finish() throws Exception {
				_logger.info("done");
			}

			public void handle(GeneticVariation v) throws Exception {
				annotate(v);
			}

			public void init() throws Exception {
				_logger.info("annotating variations...");
			}
		});

	}

	void output(GeneticVariation v) {
		_logger.info(v);
	}

	void annotate(GeneticVariation v) {

		List<BEDGene> overlappedGeneSet = geneSet.overlapQuery(v);
		for (BEDGene eachGene : overlappedGeneSet) {

		}
	}

}
