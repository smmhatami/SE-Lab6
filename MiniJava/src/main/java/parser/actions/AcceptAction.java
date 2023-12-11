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
