package parser.actions;

import parser.Parser;

public class AcceptAction extends Action {

    public AcceptAction(int number) {
        super(number);
    }

    @Override
    public String toString() {
        return "acc";
    }

    @Override
    public boolean performAction(Parser parser) {
        return true; 
    }
    
}
