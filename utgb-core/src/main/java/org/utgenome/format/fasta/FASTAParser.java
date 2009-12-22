// $ANTLR 3.1.1 C:\\Users\\sasaki\\workspace\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\fasta\\FASTA.g 2009-02-26 15:38:48

//--------------------------------------
// UTGB Common Project
//
// FASTAParser.java
// Since: June 6, 2007
//
//--------------------------------------
package org.utgenome.format.fasta;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;


import org.antlr.runtime.tree.*;

public class FASTAParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "FASTA_LINE", "FASTA", "SEQUENCE", "SingleLineComment", "NewLine", "WhiteSpace", "AminoAcidChar", "AminoAcid", "SequenceLine", "Description"
    };
    public static final int NewLine=8;
    public static final int SEQUENCE=6;
    public static final int Description=13;
    public static final int AminoAcid=11;
    public static final int SingleLineComment=7;
    public static final int AminoAcidChar=10;
    public static final int FASTA_LINE=4;
    public static final int FASTA=5;
    public static final int WhiteSpace=9;
    public static final int EOF=-1;
    public static final int SequenceLine=12;

    // delegates
    // delegators


        public FASTAParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public FASTAParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        
    protected TreeAdaptor adaptor = new CommonTreeAdaptor();

    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = adaptor;
    }
    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }

    public String[] getTokenNames() { return FASTAParser.tokenNames; }
    public String getGrammarFileName() { return "C:\\Users\\sasaki\\workspace\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\fasta\\FASTA.g"; }


    public static class fastaLine_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "fastaLine"
    // C:\\Users\\sasaki\\workspace\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\fasta\\FASTA.g:99:1: fastaLine : Description sequence -> ^( FASTA_LINE Description sequence ) ;
    public final FASTAParser.fastaLine_return fastaLine() throws RecognitionException {
        FASTAParser.fastaLine_return retval = new FASTAParser.fastaLine_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token Description1=null;
        FASTAParser.sequence_return sequence2 = null;


        Object Description1_tree=null;
        RewriteRuleTokenStream stream_Description=new RewriteRuleTokenStream(adaptor,"token Description");
        RewriteRuleSubtreeStream stream_sequence=new RewriteRuleSubtreeStream(adaptor,"rule sequence");
        try {
            // C:\\Users\\sasaki\\workspace\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\fasta\\FASTA.g:99:10: ( Description sequence -> ^( FASTA_LINE Description sequence ) )
            // C:\\Users\\sasaki\\workspace\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\fasta\\FASTA.g:99:12: Description sequence
            {
            Description1=(Token)match(input,Description,FOLLOW_Description_in_fastaLine321);  
            stream_Description.add(Description1);

            pushFollow(FOLLOW_sequence_in_fastaLine323);
            sequence2=sequence();

            state._fsp--;

            stream_sequence.add(sequence2.getTree());


            // AST REWRITE
            // elements: sequence, Description
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 100:2: -> ^( FASTA_LINE Description sequence )
            {
                // C:\\Users\\sasaki\\workspace\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\fasta\\FASTA.g:100:5: ^( FASTA_LINE Description sequence )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(FASTA_LINE, "FASTA_LINE"), root_1);

                adaptor.addChild(root_1, stream_Description.nextNode());
                adaptor.addChild(root_1, stream_sequence.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "fastaLine"

    public static class sequence_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "sequence"
    // C:\\Users\\sasaki\\workspace\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\fasta\\FASTA.g:103:1: sequence : ( SequenceLine )+ -> ^( SEQUENCE ( SequenceLine )+ ) ;
    public final FASTAParser.sequence_return sequence() throws RecognitionException {
        FASTAParser.sequence_return retval = new FASTAParser.sequence_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token SequenceLine3=null;

        Object SequenceLine3_tree=null;
        RewriteRuleTokenStream stream_SequenceLine=new RewriteRuleTokenStream(adaptor,"token SequenceLine");

        try {
            // C:\\Users\\sasaki\\workspace\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\fasta\\FASTA.g:103:9: ( ( SequenceLine )+ -> ^( SEQUENCE ( SequenceLine )+ ) )
            // C:\\Users\\sasaki\\workspace\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\fasta\\FASTA.g:103:11: ( SequenceLine )+
            {
            // C:\\Users\\sasaki\\workspace\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\fasta\\FASTA.g:103:11: ( SequenceLine )+
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
            	    // C:\\Users\\sasaki\\workspace\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\fasta\\FASTA.g:103:11: SequenceLine
            	    {
            	    SequenceLine3=(Token)match(input,SequenceLine,FOLLOW_SequenceLine_in_sequence342);  
            	    stream_SequenceLine.add(SequenceLine3);


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



            // AST REWRITE
            // elements: SequenceLine
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 104:2: -> ^( SEQUENCE ( SequenceLine )+ )
            {
                // C:\\Users\\sasaki\\workspace\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\fasta\\FASTA.g:104:5: ^( SEQUENCE ( SequenceLine )+ )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(SEQUENCE, "SEQUENCE"), root_1);

                if ( !(stream_SequenceLine.hasNext()) ) {
                    throw new RewriteEarlyExitException();
                }
                while ( stream_SequenceLine.hasNext() ) {
                    adaptor.addChild(root_1, stream_SequenceLine.nextNode());

                }
                stream_SequenceLine.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "sequence"

    public static class fasta_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "fasta"
    // C:\\Users\\sasaki\\workspace\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\fasta\\FASTA.g:107:1: fasta : ( fastaLine )+ -> ^( FASTA ( fastaLine )+ ) ;
    public final FASTAParser.fasta_return fasta() throws RecognitionException {
        FASTAParser.fasta_return retval = new FASTAParser.fasta_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        FASTAParser.fastaLine_return fastaLine4 = null;


        RewriteRuleSubtreeStream stream_fastaLine=new RewriteRuleSubtreeStream(adaptor,"rule fastaLine");
        try {
            // C:\\Users\\sasaki\\workspace\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\fasta\\FASTA.g:107:6: ( ( fastaLine )+ -> ^( FASTA ( fastaLine )+ ) )
            // C:\\Users\\sasaki\\workspace\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\fasta\\FASTA.g:107:8: ( fastaLine )+
            {
            // C:\\Users\\sasaki\\workspace\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\fasta\\FASTA.g:107:8: ( fastaLine )+
            int cnt2=0;
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0==Description) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // C:\\Users\\sasaki\\workspace\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\fasta\\FASTA.g:107:8: fastaLine
            	    {
            	    pushFollow(FOLLOW_fastaLine_in_fasta362);
            	    fastaLine4=fastaLine();

            	    state._fsp--;

            	    stream_fastaLine.add(fastaLine4.getTree());

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



            // AST REWRITE
            // elements: fastaLine
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 108:3: -> ^( FASTA ( fastaLine )+ )
            {
                // C:\\Users\\sasaki\\workspace\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\fasta\\FASTA.g:108:6: ^( FASTA ( fastaLine )+ )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(FASTA, "FASTA"), root_1);

                if ( !(stream_fastaLine.hasNext()) ) {
                    throw new RewriteEarlyExitException();
                }
                while ( stream_fastaLine.hasNext() ) {
                    adaptor.addChild(root_1, stream_fastaLine.nextTree());

                }
                stream_fastaLine.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "fasta"

    // Delegated rules


 

    public static final BitSet FOLLOW_Description_in_fastaLine321 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_sequence_in_fastaLine323 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SequenceLine_in_sequence342 = new BitSet(new long[]{0x0000000000001002L});
    public static final BitSet FOLLOW_fastaLine_in_fasta362 = new BitSet(new long[]{0x0000000000002002L});

}