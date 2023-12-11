package parser.scannerFacade;

import scanner.LexicalAnalyzer;

public class ScannerFacade {

    private LexicalAnalyzer lexicalAnalyzer;
    private MyToken currentToken;

    public ScannerFacade() {
    }

    public void newLexicalAnalyzer(java.util.Scanner sc) {
        lexicalAnalyzer = new LexicalAnalyzer(sc);
    }
    
    public MyToken getNextToken() {
        currentToken = new MyToken(lexicalAnalyzer.getNextToken());
        return currentToken;
    }

    public MyToken getCurrentToken() {
        return currentToken;
    }

}
