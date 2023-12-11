package parser;

import parser.actions.AcceptAction;
import parser.actions.Action;
import parser.actions.ReduceAction;
import parser.actions.ShiftAction;
// import scanner.token.Token;
import parser.scannerFacade.MyToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mohammad hosein on 6/25/2015.
 */

public class ParseTable {
    private ArrayList<Map<MyToken, Action>> actionTable;
    private ArrayList<Map<NonTerminal, Integer>> gotoTable;

    public ParseTable(String jsonTable) throws Exception {
        jsonTable = jsonTable.substring(2, jsonTable.length() - 2);
        String[] Rows = jsonTable.split("\\],\\[");
        Map<Integer, MyToken> terminals = new HashMap<Integer, MyToken>();
        Map<Integer, NonTerminal> nonTerminals = new HashMap<Integer, NonTerminal>();
        Rows[0] = Rows[0].substring(1, Rows[0].length() - 1);
        String[] cols = Rows[0].split("\",\"");
        for (int i = 1; i < cols.length; i++) {
            if (cols[i].startsWith("Goto")) {
                try {
                    nonTerminals.put(i, NonTerminal.valueOf(cols[i].substring(5)));
                } catch (Exception e) {
                }
            } else {
                terminals.put(i, new MyToken(MyToken.getTyepFormString(cols[i]), cols[i]));
            }
        }
        actionTable = new ArrayList<Map<MyToken, Action>>();
        gotoTable = new ArrayList<Map<NonTerminal, Integer>>();
        for (int i = 1; i < Rows.length; i++) {
            if (i == 100) {
                int a = 1;
                a++;
            }
            Rows[i] = Rows[i].substring(1, Rows[i].length() - 1);
            cols = Rows[i].split("\",\"");
            actionTable.add(new HashMap<MyToken, Action>());
            gotoTable.add(new HashMap<>());
            for (int j = 1; j < cols.length; j++) {
                if (!"".equals(cols[j])) {
                    if ("acc".equals(cols[j])) {
                        actionTable.get(actionTable.size() - 1).put(terminals.get(j), new AcceptAction(0));
                    } else if (terminals.containsKey(j)) {
//                        try {
                        MyToken t = terminals.get(j);
                        Integer actionNumber = Integer.parseInt(cols[j].substring(1));
                        Action a = cols[j].charAt(0) == 'r' ? new ReduceAction(actionNumber) : new ShiftAction(actionNumber);
                        actionTable.get(actionTable.size() - 1).put(t, a);
//                        }catch (StringIndexOutOfBoundsException e){
//                            e.printStackTrace();
//                        }
                    } else if (nonTerminals.containsKey(j)) {
                        gotoTable.get(gotoTable.size() - 1).put(nonTerminals.get(j), Integer.parseInt(cols[j]));
                    } else {
                        throw new Exception();
                    }
                }
            }
        }
    }

    public int getGotoTable(int currentState, NonTerminal variable) {
//        try {
        return gotoTable.get(currentState).get(variable);
//        }catch (NullPointerException dd)
//        {
//            dd.printStackTrace();
//        }
//        return 0;
    }

    public Action getActionTable(int currentState, MyToken terminal) {
        return actionTable.get(currentState).get(terminal);
    }
}
