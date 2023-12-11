package parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Stack;

import Log.Log;
import errorHandler.ErrorHandler;
import parser.actions.Action;
import parser.scannerFacade.MyToken;
import parser.scannerFacade.ScannerFacade;

public class Parser {
    private ArrayList<Rule> rules;
    private Stack<Integer> parsStack;
    private ParseTable parseTable;
    // private lexicalAnalyzer lexicalAnalyzer;
    // private CodeGenerator cg;
    private CodeGeneratorFacade cg;
    private ScannerFacade scanner;

    public Parser() {
        parsStack = new Stack<Integer>();
        parsStack.push(0);
        try {
            parseTable = new ParseTable(Files.readAllLines(Paths.get("src/main/resources/parseTable")).get(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
        rules = new ArrayList<Rule>();
        try {
            for (String stringRule : Files.readAllLines(Paths.get("src/main/resources/Rules"))) {
                rules.add(new Rule(stringRule));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        cg = new CodeGeneratorFacade();
        scanner = new ScannerFacade();
    }

    public void startParse(java.util.Scanner sc) {
        scanner.newLexicalAnalyzer(sc);
        MyToken lookAhead = scanner.getNextToken();
        boolean finish = false;
        Action currentAction;
        while (!finish) {
            try {
                Log.print(/*"lookahead : "+*/ lookAhead.toString() + "\t" + parsStack.peek());
//                Log.print("state : "+ parsStack.peek());
                currentAction = parseTable.getActionTable(parsStack.peek(), lookAhead);
                Log.print(currentAction.toString());
                //Log.print("");

                finish = currentAction.performAction(this);
                lookAhead = scanner.getCurrentToken();

                // switch (currentAction.action) {
                    // case shift:
                    //     parsStack.push(currentAction.number);
                    //     lookAhead = scanner.getNextToken();
                    //     break;
//                     case reduce:
//                         Rule rule = rules.get(currentAction.number);
//                         for (int i = 0; i < rule.RHS.size(); i++) {
//                             parsStack.pop();
//                         }

//                         Log.print(/*"state : " +*/ parsStack.peek() + "\t" + rule.LHS);
// //                        Log.print("LHS : "+rule.LHS);
//                         parsStack.push(parseTable.getGotoTable(parsStack.peek(), rule.LHS));
//                         Log.print(/*"new State : " + */parsStack.peek() + "");
// //                        Log.print("");
//                         try {
//                             cg.semanticFunction(rule.semanticAction, lookAhead.getToken());
//                         } catch (Exception e) {
//                             Log.print("Code Genetator Error");
//                         }
//                         break;
                    // case accept:
                    //     finish = true;
                    //     break;
                // }
                Log.print("");
            } catch (Exception ignored) {
                ignored.printStackTrace();
//                boolean find = false;
//                for (NonTerminal t : NonTerminal.values()) {
//                    if (parseTable.getGotoTable(parsStack.peek(), t) != -1) {
//                        find = true;
//                        parsStack.push(parseTable.getGotoTable(parsStack.peek(), t));
//                        StringBuilder tokenFollow = new StringBuilder();
//                        tokenFollow.append(String.format("|(?<%s>%s)", t.name(), t.pattern));
//                        Matcher matcher = Pattern.compile(tokenFollow.substring(1)).matcher(lookAhead.toString());
//                        while (!matcher.find()) {
//                            lookAhead = lexicalAnalyzer.getNextToken();
//                        }
//                    }
//                }
//                if (!find)
//                    parsStack.pop();
            }
        }
        if (!ErrorHandler.hasError) cg.printMemory();
    }

    public Rule getRule(int ruleNum) { 
        return rules.get(ruleNum);
    }

    public Integer popAction() {
        return parsStack.pop();
    } 

    public Integer peekStack() {
        return parsStack.peek();
    }

    public void pushStackGoto(Rule rule) {
        parsStack.push(parseTable.getGotoTable(parsStack.peek(), rule.LHS));
    }

    public void pushIntStack(Integer number) {
        parsStack.push(number);
    }

    public void cgSemanticFunction(Rule rule) {
        cg.semanticFunction(rule.semanticAction, scanner.getCurrentToken().getToken());
    }

    public void getNextToken(){
        scanner.getNextToken();
    }
}
