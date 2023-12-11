package parser.scannerFacade;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        Pattern pattern;
        Matcher matcher;
        for (Type t : Type.values()) {
            if (t.toString().equals(s)) return t;
        }
        for (Type t : Type.values()) {
            pattern = Pattern.compile(t.pattern);
            matcher = pattern.matcher(s);
            if (matcher.matches()) return t;
        }

//        if (s.equals("class")||s.equals("extends")||s.equals("public")||s.equals("static")||s.equals("void")||s.equals("return")||s.equals("main")||
//                s.equals("boolean")||s.equals("int")||s.equals("if")||s.equals("else")||s.equals("while")||s.equals("true")||s.equals("false")||s.equals("System.out.println")) {
//            return KEYWORDS;
//        }else if(s.equals("")){
//
//        }else if(s.equals("")){
//
//        }
        throw new IllegalArgumentException();
    }

    public Token getToken() {
        return token;
    }
    
    
}
