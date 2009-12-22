// $ANTLR 3.1.3 Mar 17, 2009 19:23:44 FASTAWalker.g 2009-09-15 18:35:15

package org.utgenome.format.fasta;
import java.util.ArrayList;


import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class FASTAWalker extends TreeParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "FASTA_LINE", "FASTA", "SEQUENCE", "SingleLineComment", "NewLine", "WhiteSpace", "AminoAcidChar", "AminoAcid", "SequenceLine", "Description"
    };
    public static final int AminoAcidChar=10;
    public static final int SequenceLine=12;
    public static final int FASTA=5;
    public static final int SEQUENCE=6;
    public static final int EOF=-1;
    public static final int SingleLineComment=7;
    public static final int AminoAcid=11;
    public static final int Description=13;
    public static final int WhiteSpace=9;
    public static final int NewLine=8;
    public static final int FASTA_LINE=4;

    // delegates
    // delegators


        public FASTAWalker(TreeNodeStream input) {
            this(input, new RecognizerSharedState());
        }
        public FASTAWalker(TreeNodeStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return FASTAWalker.tokenNames; }
    public String getGrammarFileName() { return "FASTAWalker.g"; }



    // $ANTLR start "fastaLine"
    // FASTAWalker.g:22:1: fastaLine returns [FASTASequence seq] : ^( FASTA_LINE d= Description s= sequence ) ;
    public final FASTASequence fastaLine() throws RecognitionException {
        FASTASequence seq = null;

        CommonTree d=null;
        String s = null;




        try {
            // FASTAWalker.g:26:1: ( ^( FASTA_LINE d= Description s= sequence ) )
            // FASTAWalker.g:26:3: ^( FASTA_LINE d= Description s= sequence )
            {
            match(input,FASTA_LINE,FOLLOW_FASTA_LINE_in_fastaLine57); 

            match(input, Token.DOWN, null); 
            d=(CommonTree)match(input,Description,FOLLOW_Description_in_fastaLine61); 
            pushFollow(FOLLOW_sequence_in_fastaLine65);
            s=sequence();

            state._fsp--;


            match(input, Token.UP, null); 

            		return new FASTASequence((d!=null?d.getText():null), s);
            	

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return seq;
    }
    // $ANTLR end "fastaLine"


    // $ANTLR start "sequence"
    // FASTAWalker.g:32:1: sequence returns [String seq] : ^( SEQUENCE (s= SequenceLine )+ ) ;
    public final String sequence() throws RecognitionException {
        String seq = null;

        CommonTree s=null;


          StringBuilder builder = new StringBuilder();

        try {
            // FASTAWalker.g:37:1: ( ^( SEQUENCE (s= SequenceLine )+ ) )
            // FASTAWalker.g:37:3: ^( SEQUENCE (s= SequenceLine )+ )
            {
            match(input,SEQUENCE,FOLLOW_SEQUENCE_in_sequence90); 

            match(input, Token.DOWN, null); 
            // FASTAWalker.g:37:14: (s= SequenceLine )+
            int cnt1=0;
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==SequenceLine) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // FASTAWalker.g:37:15: s= SequenceLine
            	    {
            	    s=(CommonTree)match(input,SequenceLine,FOLLOW_SequenceLine_in_sequence95); 
            	     builder.append((s!=null?s.getText():null)); 

            	    }
            	    break;

            	default :
            	    if ( cnt1 >= 1 ) break loop1;
                        EarlyExitException eee =
                            new EarlyExitException(1, input);
                        throw eee;
                }
                cnt1++;
            } while (true);


            match(input, Token.UP, null); 

                seq = builder.toString();
              

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return seq;
    }
    // $ANTLR end "sequence"


    // $ANTLR start "fasta"
    // FASTAWalker.g:43:1: fasta returns [ArrayList<FASTASequence> result] : ^( FASTA (f= fastaLine )+ ) ;
    public final ArrayList<FASTASequence> fasta() throws RecognitionException {
        ArrayList<FASTASequence> result = null;

        FASTASequence f = null;



        	result = new ArrayList<FASTASequence>();

        try {
            // FASTAWalker.g:48:1: ( ^( FASTA (f= fastaLine )+ ) )
            // FASTAWalker.g:48:3: ^( FASTA (f= fastaLine )+ )
            {
            match(input,FASTA,FOLLOW_FASTA_in_fasta127); 

            match(input, Token.DOWN, null); 
            // FASTAWalker.g:48:11: (f= fastaLine )+
            int cnt2=0;
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0==FASTA_LINE) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // FASTAWalker.g:48:12: f= fastaLine
            	    {
            	    pushFollow(FOLLOW_fastaLine_in_fasta132);
            	    f=fastaLine();

            	    state._fsp--;

            	     result.add(f); 

            	    }
            	    break;

            	default :
            	    if ( cnt2 >= 1 ) break loop2;
                        EarlyExitException eee =
                            new EarlyExitException(2, input);
                        throw eee;
                }
                cnt2++;
            } while (true);


            match(input, Token.UP, null); 
             return result; 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return result;
    }
    // $ANTLR end "fasta"

    // Delegated rules


 

    public static final BitSet FOLLOW_FASTA_LINE_in_fastaLine57 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_Description_in_fastaLine61 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_sequence_in_fastaLine65 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_SEQUENCE_in_sequence90 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_SequenceLine_in_sequence95 = new BitSet(new long[]{0x0000000000001008L});
    public static final BitSet FOLLOW_FASTA_in_fasta127 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_fastaLine_in_fasta132 = new BitSet(new long[]{0x0000000000000018L});

}