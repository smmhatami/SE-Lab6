package parser.scannerFacade;

import scanner.lexicalAnalyzer;

public class ScannerFacade {

    private lexicalAnalyzer lexicalAnalyzer;
    private MyToken currentToken;

    public ScannerFacade() {
    }

    public void newLexicalAnalyzer(java.util.Scanner sc) {
        lexicalAnalyzer = new lexicalAnalyzer(sc);
    }
    
    public MyToken getNextToken() {
        currentToken = new MyToken(lexicalAnalyzer.getNextToken());
        return currentToken;
    }

    public MyToken getCurrentToken() {
        return currentToken;
    }

}
