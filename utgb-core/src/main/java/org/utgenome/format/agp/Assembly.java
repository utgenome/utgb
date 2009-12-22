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
// Assembly.java
// Since: Jun 5, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.format.agp;

/**
 * File Format: One feature of the AGP file is that column definitions change depending on whether the line is a component line or a gap line. There is a single column definition up to column 5, then
 * each column will have two definitions, depending on the value in column 5. column content description <table summary="AGP File format" border="1" cellpadding="1" cellspacing="1" width="700">
 * <tbody>
 * <tr>
 * <td width="47"><strong>column</strong></td>
 * <td width="142"><strong>content</strong></td>
 * <td width="493"><strong>description</strong></td>
 * 
 * </tr>
 * <tr>
 * <td>1</td>
 * <td>object</td>
 * <td> This is the identifier for the object being assembled. This can be a chromosome, scaffold or contig. If the object is a chromosome and an accession.version identifier is not used to describe
 * the object, then the naming convention is to precede the chromosome number with gchrc (if a chromosome) or gLGh (if a linkage group). For example: chr1. If the object is a contig or scaffold, then
 * the identifier needs to be unique within the assembly. </td>
 * </tr>
 * <tr>
 * 
 * <td>2</td>
 * <td>object_beg</td>
 * <td> The starting coordinates of the component/gap on the object in column 1. These are the location in the objectfs coordinate system, not the component’s. </td>
 * </tr>
 * <tr>
 * <td>3</td>
 * 
 * <td>object_end</td>
 * <td> The ending coordinates of the component/gap on the object in column 1. These are the location in the objectfs coordinate system, not the component’s. </td>
 * </tr>
 * <tr>
 * <td>4</td>
 * <td>part_number</td>
 * 
 * <td> The line count for the components/gaps that make up the object described in column 1. </td>
 * </tr>
 * <tr>
 * <td>5</td>
 * <td>component_type</td>
 * <td> The sequencing status of the component. These typically correspond to keywords in the International Sequence Database (GenBank/EMBL/DDBJ) submission. Current acceptable values are:<br>
 * 
 * <strong> &nbsp;&nbsp;A</strong>=Active Finishing<br>
 * <strong> &nbsp;&nbsp;D</strong>=Draft HTG (often phase1 and phase2 are called Draft, whether or not they have the draft keyword).<br>
 * <strong> &nbsp;&nbsp;F</strong>=Finished HTG (phase 3)<br>
 * <strong> &nbsp;&nbsp;G</strong>=Whole Genome Finishing<br>
 * 
 * <strong> &nbsp;&nbsp;N</strong>=gap with specified size<br>
 * <strong> &nbsp;&nbsp;O</strong>=Other sequence (typically means no HTG keyword)<br>
 * <strong> &nbsp;&nbsp;P</strong>=Pre Draft<br>
 * <strong>&nbsp;&nbsp;U</strong>= gap of unknown size, typically defaulting to predefined values.<br>
 * 
 * <strong> &nbsp;&nbsp;W</strong>=WGS contig </td>
 * </tr>
 * <tr>
 * <td>6a</td>
 * <td>component_id</td>
 * <td> If column 5 not equal to N: This is a unique identifier for the sequence component contributing to the object described in column 1. Ideally this will be a valid accession.version identifier
 * assigned by GenBank/EMBL/DDBJ. If the sequence has not been submitted to a public repository yet, a local identifier should be used. </td>
 * 
 * </tr>
 * <tr>
 * <td>6b</td>
 * <td>gap_length</td>
 * <td> If column 5 equal to N: This column represents the length of the gap. </td>
 * </tr>
 * <tr>
 * 
 * <td>7a</td>
 * <td>component_beg</td>
 * <td> If column 5 not equal to N: This column specifies the beginning of the part of the component sequence that contributes to the object in column 1 (in component coordinates). </td>
 * </tr>
 * <tr>
 * <td>7b</td>
 * 
 * <td>gap_type</td>
 * <td>
 * <p>
 * If column 5 equal to N: This column specifies the gap type. The combination of gap type and linkage (column 8b) indicates whether the gap is captured or uncaptured. In some cases, the gap types are
 * assigned a biological value (e.g. centromere).<br>
 * <br>
 * Accepted values: <strong> <br>
 * &nbsp;&nbsp; fragment:</strong> gap between two sequence contigs (also called a sequence gap). <strong> <br>
 * 
 * &nbsp;&nbsp;clone:</strong> a gap between two clones that do not overlap. <strong> <br>
 * &nbsp;&nbsp;contig:</strong> a gap between clone contigs (also called a "layout gap"). <strong><br>
 * &nbsp;&nbsp;centromere:</strong> a gap inserted for the centromere. <strong> <br>
 * &nbsp; short_arm:</strong> a gap inserted at the start of an acrocentric chromosome. <strong> <br>
 * 
 * &nbsp;&nbsp;heterochromatin:</strong> a gap inserted for an especially large region of heterochromatic sequence (may also include the centromere). <strong> <br>
 * &nbsp;&nbsp;telomere:</strong> a gap inserted for the telomere. <strong> <br>
 * &nbsp;&nbsp;repeat:</strong> an unresolvable repeat.
 * </p>
 * </td>
 * </tr>
 * 
 * <tr>
 * <td>8a</td>
 * <td>component_end</td>
 * <td> If column 5 not equal to N: This column specifies the end of the part of the component that contributes to the object in column 1 (in component coordinates). </td>
 * </tr>
 * <tr>
 * <td>8b</td>
 * 
 * <td>linkage</td>
 * <td>If column 5 equal to N: This column indicates if there is evidence of linkage between the adjacent lines. <br>
 * Values: <strong><br>
 * &nbsp;&nbsp; yes </strong> <strong><br>
 * &nbsp;&nbsp; no</strong> </td>
 * 
 * </tr>
 * <tr>
 * <td height="135">9a</td>
 * <td>orientation</td>
 * <td>If column 5 not equal to N: This column specifies the orientation of the component relative to the object in column 1. <br>
 * Values:<br>
 * &nbsp;&nbsp; <strong>+ = plus<br>
 * 
 * &nbsp;&nbsp;&nbsp;</strong><strong>- = minus <br>
 * &nbsp;&nbsp;&nbsp;</strong><strong>0 (zero) = unknown<br>
 * &nbsp;&nbsp;&nbsp;na = irrelevant </strong> <br>
 * By default, components with unknown orientation (0 or na) are treated as if they had + orientation.</td>
 * </tr>
 * <tr>
 * <td height="42">9b</td>
 * 
 * <td>&nbsp;</td>
 * <td> If column 5 equal to N: This column is empty- there is no filler. A tab should be inserted after the 8 th column though so that all lines have 9 columns. </td>
 * </tr>
 * </tbody></table>
 * 
 * 
 * Extended comments:
 * <ul>
 * <li>Columns should be tab delimited. Lines end with a new line (\n). There should be no extra space around the individual tokens.</li>
 * <li>All coordinates given in the file are 1-based inclusive (not 0-based). i.e. the first base of an object is 1 (not 0).</li>
 * 
 * <li>Evidence of linkage. In general, evidence of linkage is provided by end pairs (sometimes referred to as mate pairs). Although, other evidence could be used such as transcript alignments). In
 * some cases, evidence of linkage may be indirect. For example, given the following scaffold:<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;A--B--C--D<br>
 * Where A, B, C and D are components, there could be end pairs linking A and B and end pairs linking A and C. There might be no pairs linking B and C but their linkage is implied.</li>
 * <li>If the object is a contig or scaffold, the object should not start with a gap line. A chromosome will frequently start or end with one or more biological gap types (e.g. telomere or
 * short_arm).</li>
 * <li>A gap of type fragment will usually be flanked by components and not by other gap lines. Typically, successive gap lines are not encouraged, except in the case of gaps implying some
 * biologically defined entity (such as centromere, heterochromatin, etc.).</li>
 * <li>Coordinates of the object are all with respect to the plus strand, no matter the orientation of the component.</li>
 * 
 * <li>object_beg (column 2) should always be less than or equal to object_end (column 3).</li>
 * <li>component_beg (column 7) should always be less than or equal to component_end (column 8).</li>
 * <li>Each object must start with a part_num of 1 (column 4) and an object_beg coordinate of 1 (column 2).</li>
 * <li>Gap lengths must be positive. Negative gaps and gap lines with zero length are not valid.</li>
 * <li>For negative gaps or gaps of unknown size, use 100 as the gaps size, as that is the GenBank/EMBL/DDBJ standard for gaps of unknown size.</li>
 * <li>In the case of an GenBank/EMBL/DDBJ submission, the object identifier should be unique not only within the assembly but also across different versions of the assembly. For example,
 * chrUn01.0001 in the first version of a genome and chrUn02.0001 in the second version.</li>
 * 
 * <li>Any text after a # symbol is assumed to be a comment</li>
 * <li>The use of comment lines at the head of the file is encouraged. Useful information to include in such headers is:</li>
 * <ul>
 * <li>organism name</li>
 * <li>assembly name</li>
 * <li>a description of any non-standard object identifiers</li>
 * 
 * </ul>
 * </ul>
 * 
 * @author leo
 * 
 */
public class Assembly 
{
	String objectNAme;
	int objectBegin;
	int objectEnd;
	int part_number;
	String compoenentType;
	int componentId;
	int gapLength;
	int componentBegin;
	int componentEnd;
	String gapType;
	boolean linkage;
	String orientation;
	
}




