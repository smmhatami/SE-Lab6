package parser.actions;

import parser.Parser;

public class ShiftAction extends Action {

    public ShiftAction(int number) {
        super(number);
    }

    @Override
    public String toString() {
        return "s" + number;
    }

    @Override
    public boolean performAction(Parser parser) {
        parser.pushIntStack(number);
        parser.getNextToken();
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AcceptAction) {
            return ((AcceptAction) obj).number == this.number;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return number;
    }
    
}
