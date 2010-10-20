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
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.utgenome.UTGBException;
import org.utgenome.format.bed.BED2SilkReader;
import org.utgenome.format.fasta.CompactACGT;
import org.utgenome.format.fasta.CompactFASTA;
import org.utgenome.gwt.utgb.client.bio.AminoAcid;
import org.utgenome.gwt.utgb.client.bio.BEDGene;
import org.utgenome.gwt.utgb.client.bio.CDS;
import org.utgenome.gwt.utgb.client.bio.CodonTable;
import org.utgenome.gwt.utgb.client.bio.Exon;
import org.utgenome.gwt.utgb.client.bio.Interval;
import org.utgenome.gwt.utgb.client.canvas.IntervalTree;
import org.utgenome.util.StandardOutputStream;
import org.utgenome.util.sv.EnhancedGeneticVariation.MutationPosition;
import org.utgenome.util.sv.EnhancedGeneticVariation.MutationType;
import org.xerial.lens.Lens;
import org.xerial.lens.ObjectHandler;
import org.xerial.silk.SilkWriter;
import org.xerial.util.log.Logger;
import org.xerial.util.opt.Argument;
import org.xerial.util.opt.OptionParser;
import org.xerial.util.opt.OptionParserException;

/**
 * Annotating structural variation types (e.g., synonymous mutation, frame shift, etc.). This code is based on
 * Higasa-san's Perl script.
 * 
 * @author leo
 * @author higasa
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

	public static void main(String[] args) {
		VariationAnnotator va = new VariationAnnotator();
		OptionParser opt = new OptionParser(va);

		try {
			opt.parse(args);
			va.execute();
		}
		catch (OptionParserException e) {
			_logger.error(e);
		}
		catch (Exception e) {
			_logger.error(e);
			e.printStackTrace(System.err);
		}

	}

	public void execute() throws Exception {

		// load FASTA (packed via utgb pack)
		_logger.info("loading FASTA pac file: " + fastaFile);
		fasta = CompactFASTA.loadIntoMemory(fastaFile);

		// load BED
		_logger.info("loading gene information: " + geneBED);
		Reader bedReader = new BED2SilkReader(new BufferedReader(new FileReader(geneBED)));
		try {
			Lens.findFromSilk(bedReader, "gene", BEDGene.class, new ObjectHandler<BEDGene>() {
				public void finish() throws Exception {
					_logger.info(String.format("loaded %d genes", geneSet.size()));
				}

				public void handle(BEDGene gene) throws Exception {
					geneSet.add(gene);
				}

				public void init() throws Exception {
					_logger.info("loading gene BED file...");
				}
			});
		}
		finally {
			bedReader.close();
		}

		// data output
		final SilkWriter silk = new SilkWriter(new StandardOutputStream());

		// load variation position file
		_logger.info("loading variation data...");
		Lens.findFromSilk(new BufferedReader(new FileReader(variationPosFile)), "variation", GeneticVariation.class, new ObjectHandler<GeneticVariation>() {
			int count = 0;

			public void finish() throws Exception {
				_logger.info("done");
			}

			public void handle(GeneticVariation v) throws Exception {
				count++;
				List<EnhancedGeneticVariation> annotation = annotate(v);
				for (Object each : annotation) {
					silk.leafObject("snv", each);
				}

				if ((count % 10000) == 0) {
					_logger.info(String.format("processed %,d mutations", count));
				}
			}

			public void init() throws Exception {
				_logger.info("annotating variations...");
			}
		});

		silk.endDocument();
		silk.close();

		_logger.info("done.");
	}

	void output(GeneticVariation v) {
		_logger.info(v);
	}

	/**
	 * Annotate the given genetic variation using the reference sequence and gene set.
	 * 
	 * @param v
	 * @return
	 * @throws UTGBException
	 * @throws IOException
	 */
	List<EnhancedGeneticVariation> annotate(GeneticVariation v) throws IOException, UTGBException {

		List<EnhancedGeneticVariation> result = new ArrayList<EnhancedGeneticVariation>();

		// Find genes containing the variation
		List<BEDGene> overlappedGeneSet = geneSet.overlapQuery(v);

		if (overlappedGeneSet.isEmpty()) { // The variation is in an inter-genic region		
			EnhancedGeneticVariation annot = new EnhancedGeneticVariation(v);
			annot.mutationPosition = MutationPosition.InterGenic;
			annot.mutationType = MutationType.NA;

			result.add(annot);
			return result;
		}

		// The variation is in an exon/intron region
		for (BEDGene eachGene : overlappedGeneSet) {
			final int numExon = eachGene.getExon().size();
			final CDS cds = eachGene.getCDSRange();

			// Is in UTR?
			if (!cds.contains(v.getStart())) {
				EnhancedGeneticVariation annot = new EnhancedGeneticVariation(v);
				annot.geneName = eachGene.getName();
				boolean is5pUTR = v.precedes(cds) && eachGene.isSense();
				annot.mutationPosition = is5pUTR ? MutationPosition.UTR5 : MutationPosition.UTR3;
				result.add(annot);
				continue;
			}

			boolean hasOverlapWithExon = false;
			for (int exonIndex = 0; exonIndex < numExon; exonIndex++) {
				final Exon e = eachGene.getExon(eachGene.isSense() ? exonIndex : numExon - exonIndex - 1);
				final int exonStart = e.getStart();
				final int exonEnd = e.getEnd();

				if (!e.hasOverlap(v)) {
					// Is in splice site? 
					final Interval spliceSite5p = new Interval(exonStart - 2, exonStart);
					if (spliceSite5p.contains(v)) {
						EnhancedGeneticVariation annot = new EnhancedGeneticVariation(v);
						annot.geneName = eachGene.getName();
						annot.mutationPosition = MutationPosition.SS5;
						result.add(annot);
						break;
					}
					final Interval spliceSite3p = new Interval(exonEnd, exonEnd + 2);
					if (spliceSite3p.contains(v)) {
						EnhancedGeneticVariation annot = new EnhancedGeneticVariation(v);
						annot.geneName = eachGene.getName();
						annot.mutationPosition = MutationPosition.SS5;
						result.add(annot);
						break;
					}
					continue;
				}

				hasOverlapWithExon = true;

				// check frame
				int cdsStart = cds.contains(exonStart) ? exonStart : cds.getStart();
				int cdsEnd = cds.contains(exonEnd) ? exonEnd : cds.getEnd();

				int distFromBoundary = (eachGene.isSense() ? v.getStart() - cdsStart : cdsEnd - v.getStart() - 1);
				int frameIndex = distFromBoundary / 3;
				int frameOffset = distFromBoundary % 3;
				int frameStart = eachGene.isSense() ? cdsStart + 3 * frameIndex : cdsEnd - 3 * (frameIndex + 1);

				// check codon
				CompactACGT refCodon = fasta.getSequence(v.chr, frameStart, frameStart + 3);
				AminoAcid refAA = CodonTable.toAminoAcid(e.isSense() ? refCodon.toString() : refCodon.reverseComplement().toString());

				// TODO check alternative AminoAcid

				EnhancedGeneticVariation annot = new EnhancedGeneticVariation(v);
				annot.geneName = eachGene.getName();
				annot.mutationPosition = getExonPos(exonIndex, numExon);
				annot.aRef = refAA;
				result.add(annot);
				break;
			}

			if (!hasOverlapWithExon) { // no overlap with exons
				// intron
				EnhancedGeneticVariation annot = new EnhancedGeneticVariation(v);
				annot.geneName = eachGene.getName();
				annot.mutationType = MutationType.NC;
				annot.mutationPosition = MutationPosition.Intron;
				result.add(annot);
			}
		}

		return result;

	}

	private MutationPosition getExonPos(int exonPos, int numExon) {
		return (exonPos == 0) ? MutationPosition.FirstExon : (exonPos == numExon - 1) ? MutationPosition.LastExon : MutationPosition.Exon;
	}

}
