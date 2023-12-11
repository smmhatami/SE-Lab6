package parser.scannerFacade;

import scanner.token.Token;
import scanner.type.Type;

public class MyToken {
    
    private Token token;

    public MyToken(Type type, String value) {
        this.token = new Token(type, value);
    }

    public MyToken(Token token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return String.format("(%s,%s)", token.type.name(), token.value);
    }

    @Override
    public boolean equals(Object o) {
        return this.token.equals(((MyToken) o).getToken());
    }

    @Override
    public int hashCode() {
        return token.hashCode();
    }

    public static Type getTyepFormString(String s) {
        return Token.getTyepFormString(s);
    }

    public Token getToken() {
        return token;
    }
    
    
}
