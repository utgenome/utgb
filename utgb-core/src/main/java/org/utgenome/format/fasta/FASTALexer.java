// $ANTLR 3.1.1 C:\\Users\\sasaki\\workspace\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\fasta\\FASTA.g 2009-02-26 15:38:48

//--------------------------------------
// UTGB Common Project
//
// FASTALexer.java
// Since: June 6, 2007
//
//--------------------------------------
package org.utgenome.format.fasta;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class FASTALexer extends Lexer {
    public static final int NewLine=8;
    public static final int SEQUENCE=6;
    public static final int Description=13;
    public static final int AminoAcid=11;
    public static final int SingleLineComment=7;
    public static final int FASTA=5;
    public static final int FASTA_LINE=4;
    public static final int AminoAcidChar=10;
    public static final int WhiteSpace=9;
    public static final int EOF=-1;
    public static final int SequenceLine=12;

    private boolean _withinDescription = false;


    // delegates
    // delegators

    public FASTALexer() {;} 
    public FASTALexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public FASTALexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "C:\\Users\\sasaki\\workspace\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\fasta\\FASTA.g"; }

    // $ANTLR start "SingleLineComment"
    public final void mSingleLineComment() throws RecognitionException {
        try {
            int _type = SingleLineComment;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Users\\sasaki\\workspace\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\fasta\\FASTA.g:58:18: ( ';' (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n' )
            // C:\\Users\\sasaki\\workspace\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\fasta\\FASTA.g:58:20: ';' (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n'
            {
            match(';'); 
            // C:\\Users\\sasaki\\workspace\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\fasta\\FASTA.g:58:24: (~ ( '\\n' | '\\r' ) )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0>='\u0000' && LA1_0<='\t')||(LA1_0>='\u000B' && LA1_0<='\f')||(LA1_0>='\u000E' && LA1_0<='\uFFFF')) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // C:\\Users\\sasaki\\workspace\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\fasta\\FASTA.g:58:24: ~ ( '\\n' | '\\r' )
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='\t')||(input.LA(1)>='\u000B' && input.LA(1)<='\f')||(input.LA(1)>='\u000E' && input.LA(1)<='\uFFFF') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);

            // C:\\Users\\sasaki\\workspace\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\fasta\\FASTA.g:58:38: ( '\\r' )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0=='\r') ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // C:\\Users\\sasaki\\workspace\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\fasta\\FASTA.g:58:38: '\\r'
                    {
                    match('\r'); 

                    }
                    break;

            }

            match('\n'); 
            _channel=HIDDEN;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SingleLineComment"

    // $ANTLR start "NewLine"
    public final void mNewLine() throws RecognitionException {
        try {
            int _type = NewLine;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Users\\sasaki\\workspace\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\fasta\\FASTA.g:60:8: ( ( '\\r' )? '\\n' )
            // C:\\Users\\sasaki\\workspace\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\fasta\\FASTA.g:60:10: ( '\\r' )? '\\n'
            {
            // C:\\Users\\sasaki\\workspace\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\fasta\\FASTA.g:60:10: ( '\\r' )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0=='\r') ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // C:\\Users\\sasaki\\workspace\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\fasta\\FASTA.g:60:11: '\\r'
                    {
                    match('\r'); 

                    }
                    break;

            }

            match('\n'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NewLine"

    // $ANTLR start "WhiteSpace"
    public final void mWhiteSpace() throws RecognitionException {
        try {
            int _type = WhiteSpace;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Users\\sasaki\\workspace\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\fasta\\FASTA.g:61:11: ( ( ' ' | '\\r' | '\\t' | '\\u000C' | '\\n' ) )
            // C:\\Users\\sasaki\\workspace\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\fasta\\FASTA.g:61:13: ( ' ' | '\\r' | '\\t' | '\\u000C' | '\\n' )
            {
            if ( (input.LA(1)>='\t' && input.LA(1)<='\n')||(input.LA(1)>='\f' && input.LA(1)<='\r')||input.LA(1)==' ' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            	_channel=HIDDEN;


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WhiteSpace"

    // $ANTLR start "AminoAcidChar"
    public final void mAminoAcidChar() throws RecognitionException {
        try {
            // C:\\Users\\sasaki\\workspace\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\fasta\\FASTA.g:78:1: ( 'A' | 'B' | 'C' | 'D' | 'E' | 'F' | 'G' | 'H' | 'I' | 'K' | 'L' | 'M' | 'N' | 'P' | 'Q' | 'R' | 'S' | 'T' | 'U' | 'V' | 'W' | 'Y' | 'Z' | 'X' | '*' | '-' )
            // C:\\Users\\sasaki\\workspace\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\fasta\\FASTA.g:
            {
            if ( input.LA(1)=='*'||input.LA(1)=='-'||(input.LA(1)>='A' && input.LA(1)<='I')||(input.LA(1)>='K' && input.LA(1)<='N')||(input.LA(1)>='P' && input.LA(1)<='Z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "AminoAcidChar"

    // $ANTLR start "AminoAcid"
    public final void mAminoAcid() throws RecognitionException {
        try {
            // C:\\Users\\sasaki\\workspace\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\fasta\\FASTA.g:85:19: ( ( AminoAcidChar )+ )
            // C:\\Users\\sasaki\\workspace\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\fasta\\FASTA.g:85:21: ( AminoAcidChar )+
            {
            // C:\\Users\\sasaki\\workspace\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\fasta\\FASTA.g:85:21: ( AminoAcidChar )+
            int cnt4=0;
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( (LA4_0=='*'||LA4_0=='-'||(LA4_0>='A' && LA4_0<='I')||(LA4_0>='K' && LA4_0<='N')||(LA4_0>='P' && LA4_0<='Z')) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // C:\\Users\\sasaki\\workspace\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\fasta\\FASTA.g:85:21: AminoAcidChar
            	    {
            	    mAminoAcidChar(); 

            	    }
            	    break;

            	default :
            	    if ( cnt4 >= 1 ) break loop4;
                        EarlyExitException eee =
                            new EarlyExitException(4, input);
                        throw eee;
                }
                cnt4++;
            } while (true);


            }

        }
        finally {
        }
    }
    // $ANTLR end "AminoAcid"

    // $ANTLR start "SequenceLine"
    public final void mSequenceLine() throws RecognitionException {
        try {
            int _type = SequenceLine;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            Token a=null;

            // C:\\Users\\sasaki\\workspace\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\fasta\\FASTA.g:87:1: (a= AminoAcid ( NewLine )? )
            // C:\\Users\\sasaki\\workspace\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\fasta\\FASTA.g:87:3: a= AminoAcid ( NewLine )?
            {
            int aStart233 = getCharIndex();
            mAminoAcid(); 
            a = new CommonToken(input, Token.INVALID_TOKEN_TYPE, Token.DEFAULT_CHANNEL, aStart233, getCharIndex()-1);
            // C:\\Users\\sasaki\\workspace\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\fasta\\FASTA.g:87:15: ( NewLine )?
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0=='\n'||LA5_0=='\r') ) {
                alt5=1;
            }
            switch (alt5) {
                case 1 :
                    // C:\\Users\\sasaki\\workspace\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\fasta\\FASTA.g:87:15: NewLine
                    {
                    mNewLine(); 

                    }
                    break;

            }

             setText(getText().trim()); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SequenceLine"

    // $ANTLR start "Description"
    public final void mDescription() throws RecognitionException {
        try {
            int _type = Description;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Users\\sasaki\\workspace\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\fasta\\FASTA.g:91:12: ( '>' (~ ( '\\n' | '\\r' ) )* NewLine )
            // C:\\Users\\sasaki\\workspace\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\fasta\\FASTA.g:91:14: '>' (~ ( '\\n' | '\\r' ) )* NewLine
            {
            match('>'); 
            // C:\\Users\\sasaki\\workspace\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\fasta\\FASTA.g:91:18: (~ ( '\\n' | '\\r' ) )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( ((LA6_0>='\u0000' && LA6_0<='\t')||(LA6_0>='\u000B' && LA6_0<='\f')||(LA6_0>='\u000E' && LA6_0<='\uFFFF')) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // C:\\Users\\sasaki\\workspace\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\fasta\\FASTA.g:91:18: ~ ( '\\n' | '\\r' )
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='\t')||(input.LA(1)>='\u000B' && input.LA(1)<='\f')||(input.LA(1)>='\u000E' && input.LA(1)<='\uFFFF') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop6;
                }
            } while (true);

            mNewLine(); 
             setText(getText().trim().substring(1)); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "Description"

    public void mTokens() throws RecognitionException {
        // C:\\Users\\sasaki\\workspace\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\fasta\\FASTA.g:1:8: ( SingleLineComment | NewLine | WhiteSpace | SequenceLine | Description )
        int alt7=5;
        switch ( input.LA(1) ) {
        case ';':
            {
            alt7=1;
            }
            break;
        case '\r':
            {
            int LA7_2 = input.LA(2);

            if ( (LA7_2=='\n') ) {
                alt7=2;
            }
            else {
                alt7=3;}
            }
            break;
        case '\n':
            {
            alt7=2;
            }
            break;
        case '\t':
        case '\f':
        case ' ':
            {
            alt7=3;
            }
            break;
        case '*':
        case '-':
        case 'A':
        case 'B':
        case 'C':
        case 'D':
        case 'E':
        case 'F':
        case 'G':
        case 'H':
        case 'I':
        case 'K':
        case 'L':
        case 'M':
        case 'N':
        case 'P':
        case 'Q':
        case 'R':
        case 'S':
        case 'T':
        case 'U':
        case 'V':
        case 'W':
        case 'X':
        case 'Y':
        case 'Z':
            {
            alt7=4;
            }
            break;
        case '>':
            {
            alt7=5;
            }
            break;
        default:
            NoViableAltException nvae =
                new NoViableAltException("", 7, 0, input);

            throw nvae;
        }

        switch (alt7) {
            case 1 :
                // C:\\Users\\sasaki\\workspace\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\fasta\\FASTA.g:1:10: SingleLineComment
                {
                mSingleLineComment(); 

                }
                break;
            case 2 :
                // C:\\Users\\sasaki\\workspace\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\fasta\\FASTA.g:1:28: NewLine
                {
                mNewLine(); 

                }
                break;
            case 3 :
                // C:\\Users\\sasaki\\workspace\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\fasta\\FASTA.g:1:36: WhiteSpace
                {
                mWhiteSpace(); 

                }
                break;
            case 4 :
                // C:\\Users\\sasaki\\workspace\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\fasta\\FASTA.g:1:47: SequenceLine
                {
                mSequenceLine(); 

                }
                break;
            case 5 :
                // C:\\Users\\sasaki\\workspace\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\fasta\\FASTA.g:1:60: Description
                {
                mDescription(); 

                }
                break;

        }

    }


 

}