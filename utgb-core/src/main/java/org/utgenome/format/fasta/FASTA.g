//--------------------------------------
// UTGB Common Project
//
// FASTA.g
// Since: June 6, 2007
//
//--------------------------------------

grammar FASTA;
options
{
  language=Java;
  output=AST;
}

tokens
{
FASTA_LINE;
FASTA;
SEQUENCE;
} 
 
@lexer::header
{
//--------------------------------------
// UTGB Common Project
//
// FASTALexer.java
// Since: June 6, 2007
//
//--------------------------------------
package org.utgenome.format.fasta;
}

@lexer::members
{
private boolean _withinDescription = false;
}

@header
{
//--------------------------------------
// UTGB Common Project
//
// FASTAParser.java
// Since: June 6, 2007
//
//--------------------------------------
package org.utgenome.format.fasta;
}

@rulecatch {}

//-------------------
// lexer rules
//-------------------

SingleLineComment: ';' ~('\n'|'\r')* '\r'? '\n' {$channel=HIDDEN;} ;

NewLine: ('\r')? '\n';
WhiteSpace: (' ' | '\r' | '\t' | '\u000C' | '\n') 
{
	$channel=HIDDEN;
} ;
 
/*
AminoAcidChar rule completly covers NucleicAcidChar, and to distinguish them is 
difficult just by seeing FASTA input file.

fragment NucleicAcidChar
: 'A' | 'C' | 'G' | 'T' | 'U' 
| 'R' | 'Y' | 'K' | 'M' | 'S' 
| 'W' | 'B' | 'D' | 'H' | 'V' 
| 'N' | '-'
;
*/
fragment AminoAcidChar
: 'A' | 'B' | 'C' | 'D' | 'E' 
| 'F' | 'G' | 'H' | 'I' | 'K' 
| 'L' | 'M' | 'N' | 'P' | 'Q' 
| 'R' | 'S' | 'T' | 'U' | 'V' 
| 'W' | 'Y' | 'Z' | 'X' | '*' 
| '-'
;
fragment AminoAcid: AminoAcidChar+;
SequenceLine
: a=AminoAcid NewLine? { setText(getText().trim()); }  
;


Description: '>' ~('\n'|'\r')* NewLine
	{ setText(getText().trim().substring(1)); } 	// trim '>' and NewLine
;

//-------------------
// parser rules
//-------------------

fastaLine: Description sequence
	-> ^(FASTA_LINE Description sequence)
;

sequence: SequenceLine+
	-> ^(SEQUENCE SequenceLine+)
	;

fasta: fastaLine+
  -> ^(FASTA fastaLine+)
;



