package CodeAnalysis;

import Exceptions.LexisErrorException;
import Exceptions.SemanticErrorException;
import Exceptions.SyntaxErrorException;

import javax.swing.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;

public class Analysis {



    //public static boolean canExecute = true;
    //public static int numOfErr = 0;
    public static HashMap<String,Integer> labelLine = new LinkedHashMap<>();
    public static HashSet<String> allLabels = new HashSet<>();
    public static void assemblyCodeAnalysis() throws LexisErrorException, SyntaxErrorException, SemanticErrorException {
        int lineNum = 0;
        for (String codeLine : AssemblyCodeReader.assemblyCode) {
            lineNum++;
            codeLine = codeLine.trim().toUpperCase();
            if (!codeLine.contains(" ")) {
                if (codeLine.equals(""))
                    continue;
                if (!codeLine.equals(Interpreter.breakpoint))
                    if (!codeLine.endsWith(":")) {
                        throw new LexisErrorException("Lexis error, line number: " + lineNum);
                    }else{
                        allLabels.add(codeLine.substring(0,codeLine.length()-1));
                    }

            } else {
                codeLine = codeLine.replaceAll("\\s+", " ").replaceAll("\\s*,\\s+", ",");
                String[] codeLineElements = codeLine.split(" ");

                if(codeLineElements.length>2)
                    throw new SyntaxErrorException("Syntax error, line number: " + lineNum);

                if (!Interpreter.keyWords.contains(codeLineElements[0]))
                    throw new LexisErrorException("Lexis error, line number: " + lineNum);

                String operands[] = codeLineElements[1].split(",");
                if(operands.length>2)
                    throw new SyntaxErrorException("Syntax error, line number: " + lineNum);

                if (Interpreter.binaryOperators.contains(codeLineElements[0]) && operands.length != 2)
                    throw new SyntaxErrorException("Syntax error, line number: " + lineNum);
                else if(Interpreter.binaryOperators.contains(codeLineElements[0]) && operands.length == 2){
                    if (ByteCodeGenerator.isNumber(operands[0]))
                        throw new SemanticErrorException("Semantic error, line number: " + lineNum);
                    for (var op : operands) {
                        check(op,lineNum);
                    }
                }
                if (Interpreter.unaryOperators.contains(codeLineElements[0]) && operands.length != 1)
                    throw new SyntaxErrorException("Syntax error, line number: " + lineNum);
                else if (Interpreter.unaryOperators.contains(codeLineElements[0]) && operands.length == 1){
                    if(Interpreter.jumps.contains(codeLineElements[0])){
                        labelLine.put(operands[0],lineNum);
                    }else
                        check(operands[0],lineNum);
                }
            }
        }
        for(var el : labelLine.entrySet()){
            if(!allLabels.contains(el.getKey())){
                throw new SyntaxErrorException("Semantic error, line number: " + el.getValue());
            }
        }

    }
    private static void check(String value,int lineNum) throws SyntaxErrorException{
        if(value.startsWith("[") && value.endsWith("]")) {
            value = value.substring(1, value.length() - 1);
            if (!ByteCodeGenerator.isNumber(value) && !Interpreter.registerNames.contains(value))
                throw new SyntaxErrorException("Syntax error, line number: " + lineNum);
        }else{
            if(!ByteCodeGenerator.isNumber(value) && !Interpreter.registerNames.contains(value))
                throw new SyntaxErrorException("Syntax error, line number: " + lineNum);
        }
    }
}
