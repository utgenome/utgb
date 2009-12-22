// $ANTLR 3.1.1 F:\\workspace\\utgb\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\bed\\BED.g 2009-05-22 10:39:44

//--------------------------------------
// UTGB Project
//
// BEDParser.java
// Since: May 8, 2009
//
//--------------------------------------
package org.utgenome.format.bed;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;


import org.antlr.runtime.tree.*;

public class BEDParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "Description", "Name", "Value", "Attribute", "Eq", "Dot", "Digit", "HexDigit", "UnicodeChar", "EscapeSequence", "StringChar", "Int", "Frac", "Exp", "WhiteSpace", "StringChars", "String", "Integer", "Double", "Number", "QName"
    };
    public static final int Double=22;
    public static final int StringChars=19;
    public static final int Frac=16;
    public static final int QName=24;
    public static final int Eq=8;
    public static final int Exp=17;
    public static final int UnicodeChar=12;
    public static final int Digit=10;
    public static final int Attribute=7;
    public static final int EOF=-1;
    public static final int HexDigit=11;
    public static final int Int=15;
    public static final int Value=6;
    public static final int Description=4;
    public static final int Number=23;
    public static final int Name=5;
    public static final int Dot=9;
    public static final int StringChar=14;
    public static final int WhiteSpace=18;
    public static final int String=20;
    public static final int EscapeSequence=13;
    public static final int Integer=21;

    // delegates
    // delegators


        public BEDParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public BEDParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        
    protected TreeAdaptor adaptor = new CommonTreeAdaptor();

    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = adaptor;
    }
    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }

    public String[] getTokenNames() { return BEDParser.tokenNames; }
    public String getGrammarFileName() { return "F:\\workspace\\utgb\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\bed\\BED.g"; }


    public static class description_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "description"
    // F:\\workspace\\utgb\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\bed\\BED.g:101:1: description : descriptionName ( attribute )* -> ^( Description descriptionName ( attribute )* ) ;
    public final BEDParser.description_return description() throws RecognitionException {
        BEDParser.description_return retval = new BEDParser.description_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        BEDParser.descriptionName_return descriptionName1 = null;

        BEDParser.attribute_return attribute2 = null;


        RewriteRuleSubtreeStream stream_attribute=new RewriteRuleSubtreeStream(adaptor,"rule attribute");
        RewriteRuleSubtreeStream stream_descriptionName=new RewriteRuleSubtreeStream(adaptor,"rule descriptionName");
        try {
            // F:\\workspace\\utgb\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\bed\\BED.g:101:12: ( descriptionName ( attribute )* -> ^( Description descriptionName ( attribute )* ) )
            // F:\\workspace\\utgb\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\bed\\BED.g:101:14: descriptionName ( attribute )*
            {
            pushFollow(FOLLOW_descriptionName_in_description417);
            descriptionName1=descriptionName();

            state._fsp--;

            stream_descriptionName.add(descriptionName1.getTree());
            // F:\\workspace\\utgb\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\bed\\BED.g:101:30: ( attribute )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==QName) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // F:\\workspace\\utgb\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\bed\\BED.g:101:30: attribute
            	    {
            	    pushFollow(FOLLOW_attribute_in_description419);
            	    attribute2=attribute();

            	    state._fsp--;

            	    stream_attribute.add(attribute2.getTree());

            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);



            // AST REWRITE
            // elements: descriptionName, attribute
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 101:41: -> ^( Description descriptionName ( attribute )* )
            {
                // F:\\workspace\\utgb\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\bed\\BED.g:101:44: ^( Description descriptionName ( attribute )* )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(Description, "Description"), root_1);

                adaptor.addChild(root_1, stream_descriptionName.nextTree());
                // F:\\workspace\\utgb\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\bed\\BED.g:101:74: ( attribute )*
                while ( stream_attribute.hasNext() ) {
                    adaptor.addChild(root_1, stream_attribute.nextTree());

                }
                stream_attribute.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }


        finally {
        }
        return retval;
    }
    // $ANTLR end "description"

    public static class descriptionName_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "descriptionName"
    // F:\\workspace\\utgb\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\bed\\BED.g:104:1: descriptionName : QName -> Name[$QName.text] ;
    public final BEDParser.descriptionName_return descriptionName() throws RecognitionException {
        BEDParser.descriptionName_return retval = new BEDParser.descriptionName_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token QName3=null;

        Object QName3_tree=null;
        RewriteRuleTokenStream stream_QName=new RewriteRuleTokenStream(adaptor,"token QName");

        try {
            // F:\\workspace\\utgb\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\bed\\BED.g:104:16: ( QName -> Name[$QName.text] )
            // F:\\workspace\\utgb\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\bed\\BED.g:104:18: QName
            {
            QName3=(Token)match(input,QName,FOLLOW_QName_in_descriptionName443);  
            stream_QName.add(QName3);



            // AST REWRITE
            // elements: 
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 104:24: -> Name[$QName.text]
            {
                adaptor.addChild(root_0, (Object)adaptor.create(Name, (QName3!=null?QName3.getText():null)));

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }


        finally {
        }
        return retval;
    }
    // $ANTLR end "descriptionName"

    public static class attribute_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "attribute"
    // F:\\workspace\\utgb\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\bed\\BED.g:106:1: attribute : QName Eq attributeValue -> ^( Attribute Name[$QName.text] attributeValue ) ;
    public final BEDParser.attribute_return attribute() throws RecognitionException {
        BEDParser.attribute_return retval = new BEDParser.attribute_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token QName4=null;
        Token Eq5=null;
        BEDParser.attributeValue_return attributeValue6 = null;


        Object QName4_tree=null;
        Object Eq5_tree=null;
        RewriteRuleTokenStream stream_Eq=new RewriteRuleTokenStream(adaptor,"token Eq");
        RewriteRuleTokenStream stream_QName=new RewriteRuleTokenStream(adaptor,"token QName");
        RewriteRuleSubtreeStream stream_attributeValue=new RewriteRuleSubtreeStream(adaptor,"rule attributeValue");
        try {
            // F:\\workspace\\utgb\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\bed\\BED.g:106:10: ( QName Eq attributeValue -> ^( Attribute Name[$QName.text] attributeValue ) )
            // F:\\workspace\\utgb\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\bed\\BED.g:106:12: QName Eq attributeValue
            {
            QName4=(Token)match(input,QName,FOLLOW_QName_in_attribute457);  
            stream_QName.add(QName4);

            Eq5=(Token)match(input,Eq,FOLLOW_Eq_in_attribute459);  
            stream_Eq.add(Eq5);

            pushFollow(FOLLOW_attributeValue_in_attribute461);
            attributeValue6=attributeValue();

            state._fsp--;

            stream_attributeValue.add(attributeValue6.getTree());


            // AST REWRITE
            // elements: attributeValue
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 106:36: -> ^( Attribute Name[$QName.text] attributeValue )
            {
                // F:\\workspace\\utgb\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\bed\\BED.g:106:39: ^( Attribute Name[$QName.text] attributeValue )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(Attribute, "Attribute"), root_1);

                adaptor.addChild(root_1, (Object)adaptor.create(Name, (QName4!=null?QName4.getText():null)));
                adaptor.addChild(root_1, stream_attributeValue.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }


        finally {
        }
        return retval;
    }
    // $ANTLR end "attribute"

    public static class attributeValue_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "attributeValue"
    // F:\\workspace\\utgb\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\bed\\BED.g:108:1: attributeValue : ( QName | String | Number ) -> Value[$attributeValue.text] ;
    public final BEDParser.attributeValue_return attributeValue() throws RecognitionException {
        BEDParser.attributeValue_return retval = new BEDParser.attributeValue_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token QName7=null;
        Token String8=null;
        Token Number9=null;

        Object QName7_tree=null;
        Object String8_tree=null;
        Object Number9_tree=null;
        RewriteRuleTokenStream stream_QName=new RewriteRuleTokenStream(adaptor,"token QName");
        RewriteRuleTokenStream stream_String=new RewriteRuleTokenStream(adaptor,"token String");
        RewriteRuleTokenStream stream_Number=new RewriteRuleTokenStream(adaptor,"token Number");

        try {
            // F:\\workspace\\utgb\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\bed\\BED.g:108:15: ( ( QName | String | Number ) -> Value[$attributeValue.text] )
            // F:\\workspace\\utgb\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\bed\\BED.g:108:17: ( QName | String | Number )
            {
            // F:\\workspace\\utgb\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\bed\\BED.g:108:17: ( QName | String | Number )
            int alt2=3;
            switch ( input.LA(1) ) {
            case QName:
                {
                alt2=1;
                }
                break;
            case String:
                {
                alt2=2;
                }
                break;
            case Number:
                {
                alt2=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;
            }

            switch (alt2) {
                case 1 :
                    // F:\\workspace\\utgb\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\bed\\BED.g:108:18: QName
                    {
                    QName7=(Token)match(input,QName,FOLLOW_QName_in_attributeValue480);  
                    stream_QName.add(QName7);


                    }
                    break;
                case 2 :
                    // F:\\workspace\\utgb\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\bed\\BED.g:108:26: String
                    {
                    String8=(Token)match(input,String,FOLLOW_String_in_attributeValue484);  
                    stream_String.add(String8);


                    }
                    break;
                case 3 :
                    // F:\\workspace\\utgb\\utgb-core\\src\\main\\java\\org\\utgenome\\format\\bed\\BED.g:108:35: Number
                    {
                    Number9=(Token)match(input,Number,FOLLOW_Number_in_attributeValue488);  
                    stream_Number.add(Number9);


                    }
                    break;

            }



            // AST REWRITE
            // elements: 
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 108:43: -> Value[$attributeValue.text]
            {
                adaptor.addChild(root_0, (Object)adaptor.create(Value, input.toString(retval.start,input.LT(-1))));

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }


        finally {
        }
        return retval;
    }
    // $ANTLR end "attributeValue"

    // Delegated rules


 

    public static final BitSet FOLLOW_descriptionName_in_description417 = new BitSet(new long[]{0x0000000001000002L});
    public static final BitSet FOLLOW_attribute_in_description419 = new BitSet(new long[]{0x0000000001000002L});
    public static final BitSet FOLLOW_QName_in_descriptionName443 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QName_in_attribute457 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_Eq_in_attribute459 = new BitSet(new long[]{0x0000000001900000L});
    public static final BitSet FOLLOW_attributeValue_in_attribute461 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QName_in_attributeValue480 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_String_in_attributeValue484 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Number_in_attributeValue488 = new BitSet(new long[]{0x0000000000000002L});

}