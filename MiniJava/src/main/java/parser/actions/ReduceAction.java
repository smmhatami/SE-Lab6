package parser.actions;

import Log.Log;
import parser.Parser;
import parser.Rule;

public class ReduceAction extends Action {

    public ReduceAction(int number) {
        super(number);
    }

    @Override
    public String toString() {
        return "r" + number;
    }

    @Override
    public boolean performAction(Parser parser) {
        Rule rule = parser.getRule(number);
        for (int i = 0; i < rule.RHS.size(); i++) {
            parser.popAction();
        }

        Log.print(/*"state : " +*/ parser.peekStack() + "\t" + rule.LHS);
    //                        Log.print("LHS : "+rule.LHS);
        parser.pushStackGoto(rule);
        Log.print(/*"new State : " + */parser.peekStack() + "");
    //                        Log.print("");
        try {
            parser.cgSemanticFunction(rule);
        } catch (Exception e) {
            Log.print("Code Genetator Error");
        }
        return false; 
    }
    
}
