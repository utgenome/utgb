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
// XerialJ Project
//
// WIG.g
// Since: Aug 28, 2009
//
//--------------------------------------

grammar WIG;
options
{
  language=Java;
  output=AST;
}
  
tokens
{
Description;
Name;
Value;
Attribute;
}

@lexer::header
{
//--------------------------------------
// UTGB Project
//
// WIGLexer.java
// Since: Aug 28, 2009
//
//--------------------------------------
package org.utgenome.format.wig;
}

@header
{
//--------------------------------------
// UTGB Project
//
// WIGParser.java
// Since: Aug 28, 2009
//
//--------------------------------------
package org.utgenome.format.wig;
}

@rulecatch {

}

// lexer rules
Eq: '=';
Dot: '.';

fragment Digit: '0' .. '9';
fragment HexDigit: ('0' .. '9' | 'A' .. 'F' | 'a' .. 'f');
fragment UnicodeChar: ~('"'| '\\');
fragment StringChar :  UnicodeChar | EscapeSequence;

fragment EscapeSequence
  : '\\' ('\"' | '\\' | '/' | 'b' | 'f' | 'n' | 'r' | 't' | 'u' HexDigit HexDigit HexDigit HexDigit)
  ;

fragment Int: '-'? ('0' | '1'..'9' Digit*);
fragment Frac: Dot Digit+;
fragment Exp: ('e' | 'E') ('+' | '-')? Digit+;

WhiteSpace: (' ' | '\r' | '\t' | '\u000C' | '\n') { $channel=HIDDEN; };

fragment
StringChars: StringChar*;

String: '"' StringChars '"' { setText("\"" + $StringChars.text + "\""); }
    ;
fragment Integer: Int;
fragment Double:  Int (Frac Exp? | Exp);

Number: Integer | Double;

QName: ('A' .. 'Z' | 'a' .. 'z' | Digit | ':' | ',' | '-' | '_' | '.')+;

// parser rules
// sample input:  track name=pairedReads description="Clone Paired Reads" useScore=1

description: descriptionName attribute* -> ^(Description descriptionName attribute*) 
   ;

descriptionName: QName -> Name[$QName.text];
  
attribute: QName Eq attributeValue -> ^(Attribute Name[$QName.text] attributeValue);

attributeValue: (QName | String | Number) -> Value[$attributeValue.text];
