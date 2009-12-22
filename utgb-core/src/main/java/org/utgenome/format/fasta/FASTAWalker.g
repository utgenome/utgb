//--------------------------------------
// UTGB Common Project
//
// FASTAWalker.g
// Since: June 6, 2007
//
//--------------------------------------

tree grammar FASTAWalker;
options {
  tokenVocab=FASTA;
  ASTLabelType=CommonTree;
}

@header
{
package org.utgenome.format.fasta;
import java.util.ArrayList;
}
  

fastaLine returns [FASTASequence seq]
@init
{
}
: ^(FASTA_LINE d=Description s=sequence)
	{
		return new FASTASequence($d.text, $s.seq);
	}
	;
	
sequence returns [String seq]
@init
{
  StringBuilder builder = new StringBuilder();
}
: ^(SEQUENCE (s=SequenceLine { builder.append($s.text); } )+)
  {
    $seq = builder.toString();
  }	
  ;

fasta returns [ArrayList<FASTASequence> result]
@init
{
	result = new ArrayList<FASTASequence>();
}
: ^(FASTA (f=fastaLine  { result.add(f); } )+)
{ return result; }
;
