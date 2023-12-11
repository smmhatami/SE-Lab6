package codeGenerator;

import Log.Log;
import errorHandler.ErrorHandler;
import scanner.token.Token;
import semantic.symbol.Symbol;
import semantic.symbol.SymbolTable;
import semantic.symbol.SymbolType;

import java.util.Stack;

/**
 * Created by Alireza on 6/27/2015.
 */
public class CodeGenerator {
    private Memory memory;
    private Stack<Address> addressStack;
    private Stack<String> symbolStack;
    private Stack<String> callStack;
    private SymbolTable symbolTable;

    public CodeGenerator() {
        memory = new Memory();
        addressStack = new Stack<>();
        symbolStack = new Stack<>();
        callStack = new Stack<>();
        symbolTable = new SymbolTable(memory);

        //TODO
    }

    public Memory getMemory() {
        return memory;
    }

    public void setMemory(Memory memory) {
        this.memory = memory;
    }

    public void printMemory() {
        getMemory().pintCodeBlock();
    }

    public void semanticFunction(int func, Token next) {
        Log.print("codegenerator : " + func);
        switch (func) {
            case 0:
                return;
            case 1:
                checkID();
                break;
            case 2:
                pid(next);
                break;
            case 3:
                fpid();
                break;
            case 4:
                kpid(next);
                break;
            case 5:
                intpid(next);
                break;
            case 6:
                startCall();
                break;
            case 7:
                call();
                break;
            case 8:
                arg();
                break;
            case 9:
                assign();
                break;
            case 10:
                mathematicalOperation(Operation.ADD);
                break;
            case 11:
                mathematicalOperation(Operation.SUB);
                break;
            case 12:
                mathematicalOperation(Operation.MULT);
                break;
            case 13:
                label();
                break;
            case 14:
                save();
                break;
            case 15:
                whileStatement();
                break;
            case 16:
                jpfSave();
                break;
            case 17:
                jpHere();
                break;
            case 18:
                print();
                break;
            case 19:
                equal();
                break;
            case 20:
                lessThan();
                break;
            case 21:
                and();
                break;
            case 22:
                not();
                break;
            case 23:
                defClass();
                break;
            case 24:
                defMethod();
                break;
            case 25:
                symbolStack.pop();
                break;
            case 26:
                extend();
                break;
            case 27:
                defField();
                break;
            case 28:
                defVar();
                break;
            case 29:
                methodReturn();
                break;
            case 30:
                defParam();
                break;
            case 31:
                lastTypeBool();
                break;
            case 32:
                lastTypeInt();
                break;
            case 33:
                defMain();
                break;
        }
    }

    private void defMain() {
        //ss.pop();
        getMemory().add3AddressCode(addressStack.pop().num, Operation.JP, new Address(memory.getCurrentCodeBlockAddress(), varType.Address), null, null);
        String methodName = "main";
        String className = symbolStack.pop();

        symbolTable.addMethod(className, methodName, getMemory().getCurrentCodeBlockAddress());

        symbolStack.push(className);
        symbolStack.push(methodName);
    }

    //    public void spid(Token next){
//        symbolStack.push(next.value);
//    }
    public void checkID() {
        symbolStack.pop();
        if (addressStack.peek().varType == varType.Non) {
            //TODO : error
        }
    }

    public void pid(Token next) {
        if (symbolStack.size() > 1) {
            String methodName = symbolStack.pop();
            String className = symbolStack.pop();
            try {

                Symbol s = symbolTable.get(className, methodName, next.value);
                addressStack.push(new Address(s.address, FindSymbolType(s.type)));


            } catch (Exception e) {
                addressStack.push(new Address(0, varType.Non));
            }
            symbolStack.push(className);
            symbolStack.push(methodName);
        } else {
            addressStack.push(new Address(0, varType.Non));
        }
        symbolStack.push(next.value);
    }

    public void fpid() {
        addressStack.pop();
        addressStack.pop();

        Symbol s = symbolTable.get(symbolStack.pop(), symbolStack.pop());
        addressStack.push(new Address(s.address, FindSymbolType(s.type)));

    }

    public void kpid(Token next) {
        addressStack.push(symbolTable.get(next.value));
    }

    public void intpid(Token next) {
        addressStack.push(new Address(Integer.parseInt(next.value), varType.Int, TypeAddress.Imidiate));
    }

    public void startCall() {
        //TODO: method ok
        addressStack.pop();
        addressStack.pop();
        String methodName = symbolStack.pop();
        String className = symbolStack.pop();
        symbolTable.startCall(className, methodName);
        callStack.push(className);
        callStack.push(methodName);

        //symbolStack.push(methodName);
    }

    public void call() {
        //TODO: method ok
        String methodName = callStack.pop();
        String className = callStack.pop();
        try {
            symbolTable.getNextParam(className, methodName);
            ErrorHandler.printError("The few argument pass for method");
        } catch (IndexOutOfBoundsException e) {
        }
        Address temp = new Address(getMemory().getTemp(), FindSymbolType(symbolTable.getMethodReturnType(className, methodName)));
        addressStack.push(temp);
        getMemory().add3AddressCode(Operation.ASSIGN, new Address(temp.num, varType.Address, TypeAddress.Imidiate), new Address(symbolTable.getMethodReturnAddress(className, methodName), varType.Address), null);
        getMemory().add3AddressCode(Operation.ASSIGN, new Address(getMemory().getCurrentCodeBlockAddress() + 2, varType.Address, TypeAddress.Imidiate), new Address(symbolTable.getMethodCallerAddress(className, methodName), varType.Address), null);
        getMemory().add3AddressCode(Operation.JP, new Address(symbolTable.getMethodAddress(className, methodName), varType.Address), null, null);

        //symbolStack.pop();
    }

    public void arg() {
        //TODO: method ok

        String methodName = callStack.pop();
//        String className = symbolStack.pop();
        try {
            Symbol s = symbolTable.getNextParam(callStack.peek(), methodName);
            Address param = addressStack.pop();
            if (param.varType != FindSymbolType(s.type)) {
                ErrorHandler.printError("The argument type isn't match");
            }
            getMemory().add3AddressCode(Operation.ASSIGN, param, new Address(s.address, FindSymbolType(s.type)), null);

//        symbolStack.push(className);

        } catch (IndexOutOfBoundsException e) {
            ErrorHandler.printError("Too many arguments pass for method");
        }
        callStack.push(methodName);

    }

    public void assign() {
        Address s1 = addressStack.pop();
        Address s2 = addressStack.pop();
//        try {
        if (s1.varType != s2.varType) {
            ErrorHandler.printError("The type of operands in assign is different ");
        }
//        }catch (NullPointerException d)
//        {
//            d.printStackTrace();
//        }
        getMemory().add3AddressCode(Operation.ASSIGN, s1, s2, null);
    }

    public void mathematicalOperation(Operation operation) {
        Address temp = new Address(getMemory().getTemp(), varType.Int);
        Address s2 = addressStack.pop();
        Address s1 = addressStack.pop();
        if (s1.varType != varType.Int || s2.varType != varType.Int) {
            ErrorHandler.printError("In mult two operands must be integer");
        }
        getMemory().add3AddressCode(operation, s1, s2, temp);
//        getMemory().saveMemory();
        addressStack.push(temp);
    }



    public void label() {
        addressStack.push(new Address(getMemory().getCurrentCodeBlockAddress(), varType.Address));
    }

    public void save() {
        addressStack.push(new Address(getMemory().saveMemory(), varType.Address));
    }

    public void whileStatement() {
        getMemory().add3AddressCode(addressStack.pop().num, Operation.JPF, addressStack.pop(), new Address(getMemory().getCurrentCodeBlockAddress() + 1, varType.Address), null);
        getMemory().add3AddressCode(Operation.JP, addressStack.pop(), null, null);
    }

    public void jpfSave() {
        Address save = new Address(getMemory().saveMemory(), varType.Address);
        getMemory().add3AddressCode(addressStack.pop().num, Operation.JPF, addressStack.pop(), new Address(getMemory().getCurrentCodeBlockAddress(), varType.Address), null);
        addressStack.push(save);
    }

    public void jpHere() {
        getMemory().add3AddressCode(addressStack.pop().num, Operation.JP, new Address(getMemory().getCurrentCodeBlockAddress(), varType.Address), null, null);
    }

    public void print() {
        getMemory().add3AddressCode(Operation.PRINT, addressStack.pop(), null, null);
    }

    public void equal() {
        Address temp = new Address(getMemory().getTemp(), varType.Bool);
        Address s2 = addressStack.pop();
        Address s1 = addressStack.pop();
        if (s1.varType != s2.varType) {
            ErrorHandler.printError("The type of operands in equal operator is different");
        }
        getMemory().add3AddressCode(Operation.EQ, s1, s2, temp);
        addressStack.push(temp);
    }

    public void lessThan() {
        Address temp = new Address(getMemory().getTemp(), varType.Bool);
        Address s2 = addressStack.pop();
        Address s1 = addressStack.pop();
        if (s1.varType != varType.Int || s2.varType != varType.Int) {
            ErrorHandler.printError("The type of operands in less than operator is different");
        }
        getMemory().add3AddressCode(Operation.LT, s1, s2, temp);
        addressStack.push(temp);
    }

    public void and() {
        Address temp = new Address(getMemory().getTemp(), varType.Bool);
        Address s2 = addressStack.pop();
        Address s1 = addressStack.pop();
        if (s1.varType != varType.Bool || s2.varType != varType.Bool) {
            ErrorHandler.printError("In and operator the operands must be boolean");
        }
        getMemory().add3AddressCode(Operation.AND, s1, s2, temp);
        addressStack.push(temp);
    }

    public void not() {
        Address temp = new Address(getMemory().getTemp(), varType.Bool);
        Address s2 = addressStack.pop();
        Address s1 = addressStack.pop();
        if (s1.varType != varType.Bool) {
            ErrorHandler.printError("In not operator the operand must be boolean");
        }
        getMemory().add3AddressCode(Operation.NOT, s1, s2, temp);
        addressStack.push(temp);
    }

    public void defClass() {
        addressStack.pop();
        symbolTable.addClass(symbolStack.peek());
    }

    public void defMethod() {
        addressStack.pop();
        String methodName = symbolStack.pop();
        String className = symbolStack.pop();

        symbolTable.addMethod(className, methodName, getMemory().getCurrentCodeBlockAddress());

        symbolStack.push(className);
        symbolStack.push(methodName);
    }


    public void extend() {
        addressStack.pop();
        symbolTable.setSuperClass(symbolStack.pop(), symbolStack.peek());
    }

    public void defField() {
        addressStack.pop();
        symbolTable.addField(symbolStack.pop(), symbolStack.peek());
    }

    public void defVar() {
        addressStack.pop();

        String var = symbolStack.pop();
        String methodName = symbolStack.pop();
        String className = symbolStack.pop();

        symbolTable.addMethodLocalVariable(className, methodName, var);

        symbolStack.push(className);
        symbolStack.push(methodName);
    }

    public void methodReturn() {
        //TODO : call ok

        String methodName = symbolStack.pop();
        Address s = addressStack.pop();
        if (s.varType != FindSymbolType(symbolTable.getMethodReturnType(symbolStack.peek(), methodName))) {
            ErrorHandler.printError("The type of method and return address was not match");
        }
        getMemory().add3AddressCode(Operation.ASSIGN, s, new Address(symbolTable.getMethodReturnAddress(symbolStack.peek(), methodName), varType.Address, TypeAddress.Indirect), null);
        getMemory().add3AddressCode(Operation.JP, new Address(symbolTable.getMethodCallerAddress(symbolStack.peek(), methodName), varType.Address), null, null);

        //symbolStack.pop();
    }

    public void defParam() {
        //TODO : call Ok
        addressStack.pop();
        String param = symbolStack.pop();
        String methodName = symbolStack.pop();
        String className = symbolStack.pop();

        symbolTable.addMethodParameter(className, methodName, param);

        symbolStack.push(className);
        symbolStack.push(methodName);
    }

    public void lastTypeBool() {
        symbolTable.setLastType(SymbolType.Bool);
    }

    public void lastTypeInt() {
        symbolTable.setLastType(SymbolType.Int);
    }

    private varType FindSymbolType(SymbolType symbolType){

        if (symbolType == SymbolType.Bool) {
            return varType.Bool;
        }
        return varType.Int;
    }

    public void main() {

    }
}
