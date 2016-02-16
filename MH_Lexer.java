
// File:   MH_Lexer.java
// Date:   October 2015

// Java template file for lexer component of Informatics 2A Assignment 1 (2015).
// Concerns lexical classes and lexer for the language MH (`Micro-Haskell').


import java.io.* ;

class MH_Lexer extends GenLexer implements LEX_TOKEN_STREAM {

static class VarAcceptor extends GenAcceptor implements DFA {
    public String lexClass() {return "VAR";};
    public int numberOfStates() {return 3;};

    int nextState (int state, char c) {
      switch(state) {
        case 0: if (CharTypes.isSmall(c)) return 1 ; else return 2 ;
        case 1: if (CharTypes.isSmall(c) || CharTypes.isLarge(c) || CharTypes.isDigit(c) || c=='\'' ) return 1; else return 2 ;
        case 2: return 2 ;
        default: return 2 ;
      }
    }
    boolean accepting (int state) {return (state == 1) ;}
    boolean dead (int state) {return (state == 2) ;}
}

static class NumAcceptor extends GenAcceptor implements DFA {
    public String lexClass() {return "NUM";};
    public int numberOfStates() {return 3;};

    int nextState (int state, char c){
      switch(state) {
        case 0: if (CharTypes.isDigit(c)) return 1 ; else return 2 ;
        case 1: if (CharTypes.isDigit(c)) return 1 ; else return 2 ;
        default: return 2 ;
      }
    }
    boolean accepting (int state) {return (state == 1) ;}
    boolean dead (int state) {return (state == 2) ;}
}

static class BooleanAcceptor extends GenAcceptor implements DFA {
    public String lexClass() {return "BOOLEAN";};
    public int numberOfStates() {return 9;};

    int nextState (int state, char c){
      switch(state) {
        case 0: if (c=='T') return 1; else if (c=='F') return 5 ; else return 8 ;
        case 1: if (c=='r') return 2; else return 8 ;
        case 2: if (c=='u') return 3; else return 8 ;
        case 3: if (c=='e') return 4; else return 8 ;
        case 4: return 8 ;
        case 5: if (c=='a') return 6; else return 8 ;
        case 6: if (c=='l') return 7; else return 8 ;
        case 7: if (c=='s') return 3; else return 8 ;
        default: return 8 ;
      }
    }
    boolean accepting (int state) {return (state==4) ;}
    boolean dead (int state) {return (state==8) ;}
}

static class SymAcceptor extends GenAcceptor implements DFA {
    public String lexClass() {return "SYM";};
    public int numberOfStates() {return 3;};

    int nextState (int state, char c){
      switch(state) {
        case 0: if(CharTypes.isSymbolic(c)) return 1 ; else return 2 ;
        case 1: if(CharTypes.isSymbolic(c)) return 1 ; else return 2 ;
        default: return 2 ;
      }
    }
    boolean accepting (int state) {return (state==1) ;}
    boolean dead (int state) {return (state==2) ;}
}

static class WhitespaceAcceptor extends GenAcceptor implements DFA {
    public String lexClass() {return "";};
    public int numberOfStates() {return 3;};

    int nextState (int state, char c){
      switch(state) {
        case 0: if(CharTypes.isWhitespace(c)) return 1 ; else return 2 ;
        case 1: if(CharTypes.isWhitespace(c)) return 1 ; else return 2 ;
        default: return 2 ;
      }
    }
    boolean accepting (int state) {return (state==1) ;}
    boolean dead (int state) {return (state==2) ;}
}

static class CommentAcceptor extends GenAcceptor implements DFA {
  public String lexClass() {return "";};
  public int numberOfStates() {return 6;};

  int nextState (int state, char c){
    switch(state) {
      case 0: if(c=='-') return 1 ; else return 5 ;
      case 1: if(c=='-') return 2 ; else return 5 ;
      case 2: if(c=='-') return 2 ; else if (!CharTypes.isNewline(c) && !CharTypes.isSymbolic(c)) return 3; else return 5;
      case 3: if(!CharTypes.isNewline(c)) return 3; else if(CharTypes.isNewline(c)) return 4; else return 5;
      default : return 5 ;
    }
  }
  boolean accepting (int state) {return (state==4) ;}
  boolean dead (int state) {return (state==5) ;}
}

static class TokAcceptor extends GenAcceptor implements DFA {

    String tok ;
    int tokLen ;
    private char[] string;
    private int garbage;

    TokAcceptor (String tok) {
      this.tok = tok ;
      tokLen = tok.length() ;
      this.string = tok.toCharArray() ;
      this.garbage   = tokLen+1 ;
    }

    public String lexClass() {return tok;};
    public int numberOfStates() {return tokLen+2;};

    int nextState (int state, char c){
      if (state < string.length) {
        if (c==string[state]) return state+1;
        else return garbage;
      }
      else return garbage;
    }
    boolean accepting (int state) {return (state==tokLen) ;};
    boolean dead (int state) {return (state==garbage) ;};

}

    static DFA[] MH_acceptors = new DFA[] {
      new TokAcceptor("Integer"),
      new TokAcceptor("Bool"),
      new TokAcceptor("if"),
      new TokAcceptor("then"),
      new TokAcceptor("else"),
      new TokAcceptor("("),
      new TokAcceptor(")"),
      new TokAcceptor(";"),
      new WhitespaceAcceptor(),
      new CommentAcceptor(),
      new BooleanAcceptor(),
      new VarAcceptor(),
      new NumAcceptor(),
      new SymAcceptor()
    };

    MH_Lexer (Reader reader) {
  super(reader,MH_acceptors) ;
    }


}
