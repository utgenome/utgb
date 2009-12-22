// $ANTLR 3.1.3 Mar 17, 2009 19:23:44 WIG.g 2009-09-02 22:58:09

//--------------------------------------
// UTGB Project
//
// WIGLexer.java
// Since: Aug 28, 2009
//
//--------------------------------------
package org.utgenome.format.wig;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class WIGLexer extends Lexer {
    public static final int Digit=10;
    public static final int Frac=16;
    public static final int HexDigit=11;
    public static final int Eq=8;
    public static final int Exp=17;
    public static final int Int=15;
    public static final int Description=4;
    public static final int UnicodeChar=12;
    public static final int StringChar=14;
    public static final int Name=5;
    public static final int String=20;
    public static final int Attribute=7;
    public static final int Dot=9;
    public static final int StringChars=19;
    public static final int EscapeSequence=13;
    public static final int QName=24;
    public static final int EOF=-1;
    public static final int Value=6;
    public static final int Integer=21;
    public static final int Double=22;
    public static final int WhiteSpace=18;
    public static final int Number=23;

    // delegates
    // delegators

    public WIGLexer() {;} 
    public WIGLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public WIGLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "WIG.g"; }

    // $ANTLR start "Eq"
    public final void mEq() throws RecognitionException {
        try {
            int _type = Eq;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // WIG.g:68:3: ( '=' )
            // WIG.g:68:5: '='
            {
            match('='); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "Eq"

    // $ANTLR start "Dot"
    public final void mDot() throws RecognitionException {
        try {
            int _type = Dot;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // WIG.g:69:4: ( '.' )
            // WIG.g:69:6: '.'
            {
            match('.'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "Dot"

    // $ANTLR start "Digit"
    public final void mDigit() throws RecognitionException {
        try {
            // WIG.g:71:15: ( '0' .. '9' )
            // WIG.g:71:17: '0' .. '9'
            {
            matchRange('0','9'); 

            }

        }
        finally {
        }
    }
    // $ANTLR end "Digit"

    // $ANTLR start "HexDigit"
    public final void mHexDigit() throws RecognitionException {
        try {
            // WIG.g:72:18: ( ( '0' .. '9' | 'A' .. 'F' | 'a' .. 'f' ) )
            // WIG.g:72:20: ( '0' .. '9' | 'A' .. 'F' | 'a' .. 'f' )
            {
            if ( (input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='F')||(input.LA(1)>='a' && input.LA(1)<='f') ) {
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
    // $ANTLR end "HexDigit"

    // $ANTLR start "UnicodeChar"
    public final void mUnicodeChar() throws RecognitionException {
        try {
            // WIG.g:73:21: (~ ( '\"' | '\\\\' ) )
            // WIG.g:73:23: ~ ( '\"' | '\\\\' )
            {
            if ( (input.LA(1)>='\u0000' && input.LA(1)<='!')||(input.LA(1)>='#' && input.LA(1)<='[')||(input.LA(1)>=']' && input.LA(1)<='\uFFFF') ) {
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
    // $ANTLR end "UnicodeChar"

    // $ANTLR start "StringChar"
    public final void mStringChar() throws RecognitionException {
        try {
            // WIG.g:74:21: ( UnicodeChar | EscapeSequence )
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( ((LA1_0>='\u0000' && LA1_0<='!')||(LA1_0>='#' && LA1_0<='[')||(LA1_0>=']' && LA1_0<='\uFFFF')) ) {
                alt1=1;
            }
            else if ( (LA1_0=='\\') ) {
                alt1=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 1, 0, input);

                throw nvae;
            }
            switch (alt1) {
                case 1 :
                    // WIG.g:74:24: UnicodeChar
                    {
                    mUnicodeChar(); 

                    }
                    break;
                case 2 :
                    // WIG.g:74:38: EscapeSequence
                    {
                    mEscapeSequence(); 

                    }
                    break;

            }
        }
        finally {
        }
    }
    // $ANTLR end "StringChar"

    // $ANTLR start "EscapeSequence"
    public final void mEscapeSequence() throws RecognitionException {
        try {
            // WIG.g:77:3: ( '\\\\' ( '\\\"' | '\\\\' | '/' | 'b' | 'f' | 'n' | 'r' | 't' | 'u' HexDigit HexDigit HexDigit HexDigit ) )
            // WIG.g:77:5: '\\\\' ( '\\\"' | '\\\\' | '/' | 'b' | 'f' | 'n' | 'r' | 't' | 'u' HexDigit HexDigit HexDigit HexDigit )
            {
            match('\\'); 
            // WIG.g:77:10: ( '\\\"' | '\\\\' | '/' | 'b' | 'f' | 'n' | 'r' | 't' | 'u' HexDigit HexDigit HexDigit HexDigit )
            int alt2=9;
            switch ( input.LA(1) ) {
            case '\"':
                {
                alt2=1;
                }
                break;
            case '\\':
                {
                alt2=2;
                }
                break;
            case '/':
                {
                alt2=3;
                }
                break;
            case 'b':
                {
                alt2=4;
                }
                break;
            case 'f':
                {
                alt2=5;
                }
                break;
            case 'n':
                {
                alt2=6;
                }
                break;
            case 'r':
                {
                alt2=7;
                }
                break;
            case 't':
                {
                alt2=8;
                }
                break;
            case 'u':
                {
                alt2=9;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;
            }

            switch (alt2) {
                case 1 :
                    // WIG.g:77:11: '\\\"'
                    {
                    match('\"'); 

                    }
                    break;
                case 2 :
                    // WIG.g:77:18: '\\\\'
                    {
                    match('\\'); 

                    }
                    break;
                case 3 :
                    // WIG.g:77:25: '/'
                    {
                    match('/'); 

                    }
                    break;
                case 4 :
                    // WIG.g:77:31: 'b'
                    {
                    match('b'); 

                    }
                    break;
                case 5 :
                    // WIG.g:77:37: 'f'
                    {
                    match('f'); 

                    }
                    break;
                case 6 :
                    // WIG.g:77:43: 'n'
                    {
                    match('n'); 

                    }
                    break;
                case 7 :
                    // WIG.g:77:49: 'r'
                    {
                    match('r'); 

                    }
                    break;
                case 8 :
                    // WIG.g:77:55: 't'
                    {
                    match('t'); 

                    }
                    break;
                case 9 :
                    // WIG.g:77:61: 'u' HexDigit HexDigit HexDigit HexDigit
                    {
                    match('u'); 
                    mHexDigit(); 
                    mHexDigit(); 
                    mHexDigit(); 
                    mHexDigit(); 

                    }
                    break;

            }


            }

        }
        finally {
        }
    }
    // $ANTLR end "EscapeSequence"

    // $ANTLR start "Int"
    public final void mInt() throws RecognitionException {
        try {
            // WIG.g:80:13: ( ( '-' )? ( '0' | '1' .. '9' ( Digit )* ) )
            // WIG.g:80:15: ( '-' )? ( '0' | '1' .. '9' ( Digit )* )
            {
            // WIG.g:80:15: ( '-' )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0=='-') ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // WIG.g:80:15: '-'
                    {
                    match('-'); 

                    }
                    break;

            }

            // WIG.g:80:20: ( '0' | '1' .. '9' ( Digit )* )
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0=='0') ) {
                alt5=1;
            }
            else if ( ((LA5_0>='1' && LA5_0<='9')) ) {
                alt5=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;
            }
            switch (alt5) {
                case 1 :
                    // WIG.g:80:21: '0'
                    {
                    match('0'); 

                    }
                    break;
                case 2 :
                    // WIG.g:80:27: '1' .. '9' ( Digit )*
                    {
                    matchRange('1','9'); 
                    // WIG.g:80:36: ( Digit )*
                    loop4:
                    do {
                        int alt4=2;
                        int LA4_0 = input.LA(1);

                        if ( ((LA4_0>='0' && LA4_0<='9')) ) {
                            alt4=1;
                        }


                        switch (alt4) {
                    	case 1 :
                    	    // WIG.g:80:36: Digit
                    	    {
                    	    mDigit(); 

                    	    }
                    	    break;

                    	default :
                    	    break loop4;
                        }
                    } while (true);


                    }
                    break;

            }


            }

        }
        finally {
        }
    }
    // $ANTLR end "Int"

    // $ANTLR start "Frac"
    public final void mFrac() throws RecognitionException {
        try {
            // WIG.g:81:14: ( Dot ( Digit )+ )
            // WIG.g:81:16: Dot ( Digit )+
            {
            mDot(); 
            // WIG.g:81:20: ( Digit )+
            int cnt6=0;
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( ((LA6_0>='0' && LA6_0<='9')) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // WIG.g:81:20: Digit
            	    {
            	    mDigit(); 

            	    }
            	    break;

            	default :
            	    if ( cnt6 >= 1 ) break loop6;
                        EarlyExitException eee =
                            new EarlyExitException(6, input);
                        throw eee;
                }
                cnt6++;
            } while (true);


            }

        }
        finally {
        }
    }
    // $ANTLR end "Frac"

    // $ANTLR start "Exp"
    public final void mExp() throws RecognitionException {
        try {
            // WIG.g:82:13: ( ( 'e' | 'E' ) ( '+' | '-' )? ( Digit )+ )
            // WIG.g:82:15: ( 'e' | 'E' ) ( '+' | '-' )? ( Digit )+
            {
            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // WIG.g:82:27: ( '+' | '-' )?
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0=='+'||LA7_0=='-') ) {
                alt7=1;
            }
            switch (alt7) {
                case 1 :
                    // WIG.g:
                    {
                    if ( input.LA(1)=='+'||input.LA(1)=='-' ) {
                        input.consume();

                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;}


                    }
                    break;

            }

            // WIG.g:82:40: ( Digit )+
            int cnt8=0;
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( ((LA8_0>='0' && LA8_0<='9')) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // WIG.g:82:40: Digit
            	    {
            	    mDigit(); 

            	    }
            	    break;

            	default :
            	    if ( cnt8 >= 1 ) break loop8;
                        EarlyExitException eee =
                            new EarlyExitException(8, input);
                        throw eee;
                }
                cnt8++;
            } while (true);


            }

        }
        finally {
        }
    }
    // $ANTLR end "Exp"

    // $ANTLR start "WhiteSpace"
    public final void mWhiteSpace() throws RecognitionException {
        try {
            int _type = WhiteSpace;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // WIG.g:84:11: ( ( ' ' | '\\r' | '\\t' | '\\u000C' | '\\n' ) )
            // WIG.g:84:13: ( ' ' | '\\r' | '\\t' | '\\u000C' | '\\n' )
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

    // $ANTLR start "StringChars"
    public final void mStringChars() throws RecognitionException {
        try {
            // WIG.g:87:12: ( ( StringChar )* )
            // WIG.g:87:14: ( StringChar )*
            {
            // WIG.g:87:14: ( StringChar )*
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( ((LA9_0>='\u0000' && LA9_0<='!')||(LA9_0>='#' && LA9_0<='\uFFFF')) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // WIG.g:87:14: StringChar
            	    {
            	    mStringChar(); 

            	    }
            	    break;

            	default :
            	    break loop9;
                }
            } while (true);


            }

        }
        finally {
        }
    }
    // $ANTLR end "StringChars"

    // $ANTLR start "String"
    public final void mString() throws RecognitionException {
        try {
            int _type = String;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            Token StringChars1=null;

            // WIG.g:89:7: ( '\"' StringChars '\"' )
            // WIG.g:89:9: '\"' StringChars '\"'
            {
            match('\"'); 
            int StringChars1Start284 = getCharIndex();
            mStringChars(); 
            StringChars1 = new CommonToken(input, Token.INVALID_TOKEN_TYPE, Token.DEFAULT_CHANNEL, StringChars1Start284, getCharIndex()-1);
            match('\"'); 
             setText("\"" + (StringChars1!=null?StringChars1.getText():null) + "\""); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "String"

    // $ANTLR start "Integer"
    public final void mInteger() throws RecognitionException {
        try {
            // WIG.g:91:17: ( Int )
            // WIG.g:91:19: Int
            {
            mInt(); 

            }

        }
        finally {
        }
    }
    // $ANTLR end "Integer"

    // $ANTLR start "Double"
    public final void mDouble() throws RecognitionException {
        try {
            // WIG.g:92:16: ( Int ( Frac ( Exp )? | Exp ) )
            // WIG.g:92:19: Int ( Frac ( Exp )? | Exp )
            {
            mInt(); 
            // WIG.g:92:23: ( Frac ( Exp )? | Exp )
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( (LA11_0=='.') ) {
                alt11=1;
            }
            else if ( (LA11_0=='E'||LA11_0=='e') ) {
                alt11=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 11, 0, input);

                throw nvae;
            }
            switch (alt11) {
                case 1 :
                    // WIG.g:92:24: Frac ( Exp )?
                    {
                    mFrac(); 
                    // WIG.g:92:29: ( Exp )?
                    int alt10=2;
                    int LA10_0 = input.LA(1);

                    if ( (LA10_0=='E'||LA10_0=='e') ) {
                        alt10=1;
                    }
                    switch (alt10) {
                        case 1 :
                            // WIG.g:92:29: Exp
                            {
                            mExp(); 

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // WIG.g:92:36: Exp
                    {
                    mExp(); 

                    }
                    break;

            }


            }

        }
        finally {
        }
    }
    // $ANTLR end "Double"

    // $ANTLR start "Number"
    public final void mNumber() throws RecognitionException {
        try {
            int _type = Number;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // WIG.g:94:7: ( Integer | Double )
            int alt12=2;
            alt12 = dfa12.predict(input);
            switch (alt12) {
                case 1 :
                    // WIG.g:94:9: Integer
                    {
                    mInteger(); 

                    }
                    break;
                case 2 :
                    // WIG.g:94:19: Double
                    {
                    mDouble(); 

                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "Number"

    // $ANTLR start "QName"
    public final void mQName() throws RecognitionException {
        try {
            int _type = QName;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // WIG.g:96:6: ( ( 'A' .. 'Z' | 'a' .. 'z' | Digit | ':' | ',' | '-' | '_' | '.' )+ )
            // WIG.g:96:8: ( 'A' .. 'Z' | 'a' .. 'z' | Digit | ':' | ',' | '-' | '_' | '.' )+
            {
            // WIG.g:96:8: ( 'A' .. 'Z' | 'a' .. 'z' | Digit | ':' | ',' | '-' | '_' | '.' )+
            int cnt13=0;
            loop13:
            do {
                int alt13=2;
                int LA13_0 = input.LA(1);

                if ( ((LA13_0>=',' && LA13_0<='.')||(LA13_0>='0' && LA13_0<=':')||(LA13_0>='A' && LA13_0<='Z')||LA13_0=='_'||(LA13_0>='a' && LA13_0<='z')) ) {
                    alt13=1;
                }


                switch (alt13) {
            	case 1 :
            	    // WIG.g:
            	    {
            	    if ( (input.LA(1)>=',' && input.LA(1)<='.')||(input.LA(1)>='0' && input.LA(1)<=':')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    if ( cnt13 >= 1 ) break loop13;
                        EarlyExitException eee =
                            new EarlyExitException(13, input);
                        throw eee;
                }
                cnt13++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "QName"

    public void mTokens() throws RecognitionException {
        // WIG.g:1:8: ( Eq | Dot | WhiteSpace | String | Number | QName )
        int alt14=6;
        alt14 = dfa14.predict(input);
        switch (alt14) {
            case 1 :
                // WIG.g:1:10: Eq
                {
                mEq(); 

                }
                break;
            case 2 :
                // WIG.g:1:13: Dot
                {
                mDot(); 

                }
                break;
            case 3 :
                // WIG.g:1:17: WhiteSpace
                {
                mWhiteSpace(); 

                }
                break;
            case 4 :
                // WIG.g:1:28: String
                {
                mString(); 

                }
                break;
            case 5 :
                // WIG.g:1:35: Number
                {
                mNumber(); 

                }
                break;
            case 6 :
                // WIG.g:1:42: QName
                {
                mQName(); 

                }
                break;

        }

    }


    protected DFA12 dfa12 = new DFA12(this);
    protected DFA14 dfa14 = new DFA14(this);
    static final String DFA12_eotS =
        "\2\uffff\2\4\2\uffff\1\4";
    static final String DFA12_eofS =
        "\7\uffff";
    static final String DFA12_minS =
        "\1\55\1\60\2\56\2\uffff\1\56";
    static final String DFA12_maxS =
        "\2\71\2\145\2\uffff\1\145";
    static final String DFA12_acceptS =
        "\4\uffff\1\1\1\2\1\uffff";
    static final String DFA12_specialS =
        "\7\uffff}>";
    static final String[] DFA12_transitionS = {
            "\1\1\2\uffff\1\2\11\3",
            "\1\2\11\3",
            "\1\5\26\uffff\1\5\37\uffff\1\5",
            "\1\5\1\uffff\12\6\13\uffff\1\5\37\uffff\1\5",
            "",
            "",
            "\1\5\1\uffff\12\6\13\uffff\1\5\37\uffff\1\5"
    };

    static final short[] DFA12_eot = DFA.unpackEncodedString(DFA12_eotS);
    static final short[] DFA12_eof = DFA.unpackEncodedString(DFA12_eofS);
    static final char[] DFA12_min = DFA.unpackEncodedStringToUnsignedChars(DFA12_minS);
    static final char[] DFA12_max = DFA.unpackEncodedStringToUnsignedChars(DFA12_maxS);
    static final short[] DFA12_accept = DFA.unpackEncodedString(DFA12_acceptS);
    static final short[] DFA12_special = DFA.unpackEncodedString(DFA12_specialS);
    static final short[][] DFA12_transition;

    static {
        int numStates = DFA12_transitionS.length;
        DFA12_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA12_transition[i] = DFA.unpackEncodedString(DFA12_transitionS[i]);
        }
    }

    class DFA12 extends DFA {

        public DFA12(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 12;
            this.eot = DFA12_eot;
            this.eof = DFA12_eof;
            this.min = DFA12_min;
            this.max = DFA12_max;
            this.accept = DFA12_accept;
            this.special = DFA12_special;
            this.transition = DFA12_transition;
        }
        public String getDescription() {
            return "94:1: Number : ( Integer | Double );";
        }
    }
    static final String DFA14_eotS =
        "\2\uffff\1\11\2\uffff\1\10\2\12\3\uffff\2\10\2\12\1\10\1\12\2\10"+
        "\1\12";
    static final String DFA14_eofS =
        "\24\uffff";
    static final String DFA14_minS =
        "\1\11\1\uffff\1\54\2\uffff\1\60\2\54\3\uffff\1\60\1\53\2\54\1\60"+
        "\1\54\1\53\1\60\1\54";
    static final String DFA14_maxS =
        "\1\172\1\uffff\1\172\2\uffff\1\71\2\172\3\uffff\2\71\2\172\1\71"+
        "\1\172\2\71\1\172";
    static final String DFA14_acceptS =
        "\1\uffff\1\1\1\uffff\1\3\1\4\3\uffff\1\6\1\2\1\5\11\uffff";
    static final String DFA14_specialS =
        "\24\uffff}>";
    static final String[] DFA14_transitionS = {
            "\2\3\1\uffff\2\3\22\uffff\1\3\1\uffff\1\4\11\uffff\1\10\1\5"+
            "\1\2\1\uffff\1\6\11\7\1\10\2\uffff\1\1\3\uffff\32\10\4\uffff"+
            "\1\10\1\uffff\32\10",
            "",
            "\3\10\1\uffff\13\10\6\uffff\32\10\4\uffff\1\10\1\uffff\32"+
            "\10",
            "",
            "",
            "\1\6\11\7",
            "\2\10\1\13\1\uffff\13\10\6\uffff\4\10\1\14\25\10\4\uffff\1"+
            "\10\1\uffff\4\10\1\14\25\10",
            "\2\10\1\13\1\uffff\12\15\1\10\6\uffff\4\10\1\14\25\10\4\uffff"+
            "\1\10\1\uffff\4\10\1\14\25\10",
            "",
            "",
            "",
            "\12\16",
            "\1\12\1\uffff\1\17\2\uffff\12\20",
            "\2\10\1\13\1\uffff\12\15\1\10\6\uffff\4\10\1\14\25\10\4\uffff"+
            "\1\10\1\uffff\4\10\1\14\25\10",
            "\3\10\1\uffff\12\16\1\10\6\uffff\4\10\1\21\25\10\4\uffff\1"+
            "\10\1\uffff\4\10\1\21\25\10",
            "\12\20",
            "\3\10\1\uffff\12\20\1\10\6\uffff\32\10\4\uffff\1\10\1\uffff"+
            "\32\10",
            "\1\12\1\uffff\1\22\2\uffff\12\23",
            "\12\23",
            "\3\10\1\uffff\12\23\1\10\6\uffff\32\10\4\uffff\1\10\1\uffff"+
            "\32\10"
    };

    static final short[] DFA14_eot = DFA.unpackEncodedString(DFA14_eotS);
    static final short[] DFA14_eof = DFA.unpackEncodedString(DFA14_eofS);
    static final char[] DFA14_min = DFA.unpackEncodedStringToUnsignedChars(DFA14_minS);
    static final char[] DFA14_max = DFA.unpackEncodedStringToUnsignedChars(DFA14_maxS);
    static final short[] DFA14_accept = DFA.unpackEncodedString(DFA14_acceptS);
    static final short[] DFA14_special = DFA.unpackEncodedString(DFA14_specialS);
    static final short[][] DFA14_transition;

    static {
        int numStates = DFA14_transitionS.length;
        DFA14_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA14_transition[i] = DFA.unpackEncodedString(DFA14_transitionS[i]);
        }
    }

    class DFA14 extends DFA {

        public DFA14(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 14;
            this.eot = DFA14_eot;
            this.eof = DFA14_eof;
            this.min = DFA14_min;
            this.max = DFA14_max;
            this.accept = DFA14_accept;
            this.special = DFA14_special;
            this.transition = DFA14_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( Eq | Dot | WhiteSpace | String | Number | QName );";
        }
    }
 

}