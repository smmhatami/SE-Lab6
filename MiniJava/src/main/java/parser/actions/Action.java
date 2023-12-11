package parser.actions;

import parser.Parser;

public abstract class Action {
    //if action = shift : number is state
    //if action = reduce : number is number of rule
    public int number;

    public Action(int number) {
        this.number = number;
    }

    // public String toString() {
    //     switch (action) {
    //         case accept:
    //             return "acc";
    //         case shift:
    //             return "s" + number;
    //         case reduce:
    //             return "r" + number;
    //     }
    //     return action.toString() + number;
    // }

    public abstract boolean performAction(Parser parser);
    
}

enum act {
    shift, reduce, accept
}
