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
// GFF3Reader.java
// Since: Jul 7, 2008
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.format.gff3;

import java.io.Reader;

/**
 * GFF3 Format has
 * 
 * <pre>
 * Undefined fields are replaced with the &quot;.&quot; character, as described in
 *  the original GFF spec.
 * 
 *  Column 1: &quot;seqid&quot;
 * 
 *  The ID of the landmark used to establish the coordinate system for the
 *  current feature. IDs may contain any characters, but must escape any
 *  characters not in the set [a-zA-Z0-9.:&circ;*$@!+_?-|].  In particular, IDs
 *  may not contain unescaped whitespace and must not begin with an
 *  unescaped &quot;&gt;&quot;.
 * 
 *  Column 2: &quot;source&quot;
 * 
 *  The source is a free text qualifier intended to describe the algorithm
 *  or operating procedure that generated this feature.  Typically this is
 *  the name of a piece of software, such as &quot;Genescan&quot; or a database
 *  name, such as &quot;Genbank.&quot;  In effect, the source is used to extend the
 *  feature ontology by adding a qualifier to the type creating a new
 *  composite type that is a subclass of the type in the type column.
 * 
 *  Column 3: &quot;type&quot;
 * 
 *  The type of the feature (previously called the &quot;method&quot;).  This is
 *  constrained to be either: (a) a term from the &quot;lite&quot; sequence
 *  ontology, SOFA; or (b) a SOFA accession number.  The latter
 *  alternative is distinguished using the syntax SO:000000.
 * 
 *  Columns 4 &amp; 5: &quot;start&quot; and &quot;end&quot;
 * 
 *  The start and end of the feature, in 1-based integer coordinates,
 *  relative to the landmark given in column 1.  Start is always less than
 *  or equal to end.
 * 
 *  For zero-length features, such as insertion sites, start equals end
 *  and the implied site is to the right of the indicated base in the
 *  direction of the landmark.
 * 
 *  Column 6: &quot;score&quot;
 * 
 *  The score of the feature, a floating point number.  As in earlier
 *  versions of the format, the semantics of the score are ill-defined.
 *  It is strongly recommended that E-values be used for sequence
 *  similarity features, and that P-values be used for ab initio gene
 *  prediction features.
 * 
 *  Column 7: &quot;strand&quot;
 * 
 *  The strand of the feature.  + for positive strand (relative to the
 *  landmark), - for minus strand, and . for features that are not
 *  stranded.  In addition, ? can be used for features whose strandedness
 *  is relevant, but unknown.
 * 
 *  Column 8: &quot;phase&quot;
 * 
 *  For features of type &quot;CDS&quot;, the phase indicates where the feature
 *  begins with reference to the reading frame.  The phase is one of the
 *  integers 0, 1, or 2, indicating the number of bases that should be
 *  removed from the beginning of this feature to reach the first base of
 *  the next codon. In other words, a phase of &quot;0&quot; indicates that the next
 *  codon begins at the first base of the region described by the current
 *  line, a phase of &quot;1&quot; indicates that the next codon begins at the
 *  second base of this region, and a phase of &quot;2&quot; indicates that the
 *  codon begins at the third base of this region. This is NOT to be
 *  confused with the frame, which is simply start modulo 3.
 * 
 *  For forward strand features, phase is counted from the start
 *  field. For reverse strand features, phase is counted from the end
 *  field.
 * 
 *  The phase is REQUIRED for all CDS features.
 * 
 *  Column 9: &quot;attributes&quot;
 * 
 *  A list of feature attributes in the format tag=value.  Multiple
 *  tag=value pairs are separated by semicolons.  URL escaping rules are
 *  used for tags or values containing the following characters: &quot;,=;&quot;.
 *  Spaces are allowed in this field, but tabs must be replaced with the
 *  %09 URL escape.
 * 
 *  These tags have predefined meanings:
 * 
 *  ID	   Indicates the name of the feature.  IDs must be unique
 *  within the scope of the GFF file.
 * 
 *  Name   Display name for the feature.  This is the name to be
 *  displayed to the user.  Unlike IDs, there is no requirement
 *  that the Name be unique within the file.
 * 
 *  Alias  A secondary name for the feature.  It is suggested that
 *  this tag be used whenever a secondary identifier for the
 *  feature is needed, such as locus names and
 *  accession numbers.  Unlike ID, there is no requirement
 *  that Alias be unique within the file.
 * 
 *  Parent Indicates the parent of the feature.  A parent ID can be
 *  used to group exons into transcripts, transcripts into
 *  genes, an so forth.  A feature may have multiple parents.
 *  Parent can *only* be used to indicate a partof 
 *  relationship.
 * 
 *  Target Indicates the target of a nucleotide-to-nucleotide or
 *  protein-to-nucleotide alignment.  The format of the
 *  value is &quot;target_id start end [strand]&quot;, where strand
 *  is optional and may be &quot;+&quot; or &quot;-&quot;.  If the target_id 
 *  contains spaces, they must be escaped as hex escape %20.
 * 
 *  Gap   The alignment of the feature to the target if the two are
 *  not collinear (e.g. contain gaps).  The alignment format is
 *  taken from the CIGAR format described in the 
 *  Exonerate documentation.
 *  (http://cvsweb.sanger.ac.uk/cgi-bin/cvsweb.cgi/exonerate
 *  ?cvsroot=Ensembl).  See &quot;THE GAP ATTRIBUTE&quot; for a description
 *  of this format.
 * 
 *  Derives_from  
 *  Used to disambiguate the relationship between one
 *  feature and another when the relationship is a temporal
 *  one rather than a purely structural &quot;part of&quot; one.  This
 *  is needed for polycistronic genes.  See &quot;PATHOLOGICAL CASES&quot;
 *  for further discussion.
 * 
 *  Note   A free text note.
 * 
 *  Dbxref A database cross reference.  See the section
 *  &quot;Ontology Associations and Db Cross References&quot; for
 *  details on the format.
 * 
 *  Ontology_term  A cross reference to an ontology term.  See
 *  the section &quot;Ontology Associations and Db Cross References&quot;
 *  for details.
 * 
 *  Multiple attributes of the same type are indicated by separating the
 *  values with the comma &quot;,&quot; character, as in:
 * 
 *  Parent=AF2312,AB2812,abc-3
 * 
 *  Note that attribute names are case sensitive.  &quot;Parent&quot; is not the
 *  same as &quot;parent&quot;.
 * 
 *  All attributes that begin with an uppercase letter are reserved for
 *  later use.  Attributes that begin with a lowercase letter can be used
 *  freely by applications.
 * 
 * 
 * </pre>
 * 
 * 
 * 
 * @author leo
 * 
 */
public class GFF3Reader {

	public GFF3Reader(Reader input) {

	}

}
