package CodeAnalysis;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.stream.IntStream;

import Exceptions.InvalidOpCodeException;
import Exceptions.MemoryErrorException;
import Interfaces.IStorage;
import Interfaces.IValue;
import Operations.*;
import Operands.*;

public class Interpreter {

    private static Scanner skener = new Scanner(System.in);
    public static final long startAddress = 0x0;
    public static HashMap<Long, Byte> addressSpace = new HashMap<>();
    public static HashSet<String> keyWords = new HashSet<>();
    public static HashSet<String> unaryOperators = new HashSet<>();
    public static HashSet<String> binaryOperators = new HashSet<>();
    public static HashSet<String> registerNames = new HashSet();
    public static HashSet<Register> registers = new HashSet<>();
    public static HashSet<String> jumps = new HashSet<>();
    public static HashMap<String,Byte> keyWordToByteCode = new HashMap<>(); // keywords into byte code
    public static HashMap<Byte,String> byteCodeToKeyWord = new HashMap<>();
    public static HashMap<Byte, AssemblyOperation> byteCodeToOperation = new HashMap<>();
    public static HashMap<Byte, Register> byteCodeToRegister = new HashMap<>();

    public static final String breakpoint = "BREAKPOINT";
    public static final String next = "NEXT";
    public static final String cont = "CONTINUE";
    public static final String register = "REGISTER";
    public static final String constant = "CONSTANT";
    public static final String indirectAdressing = "INDIRECT";
    public static final String label = "LABEL";
    public static final String address = "ADDRESS";

    private static boolean debugging = false;

    public static Register rip;
    private static byte byteCodeGen=1;

    public static void initializeInterpreter(){
        setRegisters();
        setOperations();
        setOperandTypeFlags();
    }
    private static byte generateByteCode(){
        return byteCodeGen++;
    }
    private static void addRegister(Register reg){
        byteCodeToRegister.put(reg.getByteCode(),reg);
        registerNames.add(reg.getType());
        registers.add(reg);
    }
    private static void addOperation(AssemblyOperation op){
        byteCodeToOperation.put(op.getOpByteCode(),op);
    }
    private static void setRegisters(){
        addRegister(new Register("RAX",generateByteCode()));
        addRegister(new Register("RBX",generateByteCode()));
        addRegister(new Register("RCX",generateByteCode()));
        addRegister(new Register("RDX",generateByteCode()));
        byteCodeToRegister.values().stream().forEach((reg) -> {
            keyWords.add(reg.getType());
            keyWordToByteCode.put(reg.getType(),reg.getByteCode());
            byteCodeToKeyWord.put(reg.getByteCode(),reg.getType());
        });

        rip = new Register("RIP",generateByteCode());
        rip.setValue(startAddress);
        registers.add(rip);
    }
    private static void setOperations(){
        addOperation(new AsseblyBinaryOperation("ADD",generateByteCode(), Operator::add));
        addOperation(new AsseblyBinaryOperation("SUB",generateByteCode(), Operator::sub));
        addOperation(new AsseblyBinaryOperation("MUL",generateByteCode(), Operator::mul));
        addOperation(new AsseblyBinaryOperation("DIV",generateByteCode(), Operator::div));
        addOperation(new AsseblyBinaryOperation("AND",generateByteCode(), Operator::and));
        addOperation(new AsseblyBinaryOperation("OR",generateByteCode(), Operator::or));
        addOperation(new AsseblyBinaryOperation("XOR",generateByteCode(), Operator::xor));
        addOperation(new AssemblyUnaryOperation("NOT",generateByteCode(), Operator::not));
        addOperation(new AsseblyBinaryOperation("CMP",generateByteCode(), Operator::cmp));
        addOperation(new AsseblyBinaryOperation("MOV",generateByteCode(), Operator::mov));
        addOperation(new AssemblyUnaryOperation("JE",generateByteCode(), Operator::je));
        jumps.add("JE");
        addOperation(new AssemblyUnaryOperation("JGE",generateByteCode(), Operator::jge));
        jumps.add("JGE");
        addOperation(new AssemblyUnaryOperation("JNE",generateByteCode(), Operator::jne));
        jumps.add("JNE");
        addOperation(new AssemblyUnaryOperation("JL",generateByteCode(), Operator::jl));
        jumps.add("JL");
        addOperation(new AssemblyUnaryOperation("JMP",generateByteCode(), Operator::jmp));
        jumps.add("JMP");
        addOperation(new AssemblyUnaryOperation("SCAN",generateByteCode(), Operator::scan));
        addOperation(new AssemblyUnaryOperation("PRINT",generateByteCode(), Operator::print));

        byteCodeToOperation.values().stream().forEach((op) ->{
            keyWords.add(op.getOpName());
            keyWordToByteCode.put(op.getOpName(),op.getOpByteCode());
            byteCodeToKeyWord.put(op.getOpByteCode(),op.getOpName());
            if(op instanceof AsseblyBinaryOperation && !op.getOpName().equals("MUL") && !op.getOpName().equals("DIV"))
                binaryOperators.add(op.getOpName());
            else
                unaryOperators.add(op.getOpName());
        });
        keyWords.add(breakpoint);
        keyWordToByteCode.put(breakpoint,generateByteCode());
        byteCodeToKeyWord.put(keyWordToByteCode.get(breakpoint),breakpoint);
    }
    private static void setOperandTypeFlags(){
        keyWordToByteCode.put(register,generateByteCode());
        byteCodeToKeyWord.put(keyWordToByteCode.get(register),register);
        keyWordToByteCode.put(constant,generateByteCode());
        byteCodeToKeyWord.put(keyWordToByteCode.get(constant),constant);
        keyWordToByteCode.put(address,generateByteCode()); //[adress]
        byteCodeToKeyWord.put(keyWordToByteCode.get(address),address);
        keyWordToByteCode.put(indirectAdressing,generateByteCode()); //[reg]
        byteCodeToKeyWord.put(keyWordToByteCode.get(indirectAdressing),indirectAdressing);
        keyWordToByteCode.put(label,generateByteCode());
        byteCodeToKeyWord.put(keyWordToByteCode.get(label),label);
    }
    private static void ripInc(){ // PC++
        rip.setValue(rip.getValue()+1);
    }
    public static void byteCodeExecution() throws MemoryErrorException, InvalidOpCodeException {

        while(addressSpace.get(rip.getValue())!=0) {

            // Fetch
            byte byteCode = addressSpace.get(rip.getValue()); // we get the byte code of the instruction
            ripInc();
            if(byteCodeToKeyWord.get(byteCode).equals(breakpoint)){
                debugging=true;
                debug();
                continue;
            }else {
                //Decode
                AssemblyOperation instruction = byteCodeToOperation.get(byteCode); // specifying instructions by byte code
                if (instruction == null) {
                    throw new InvalidOpCodeException("Invalid operation code!");
                }
                //Fetch operands
                if (instruction instanceof AsseblyBinaryOperation) {

                    AsseblyBinaryOperation binaryOp = (AsseblyBinaryOperation) instruction;
                    var arg1 = fetchArgs();
                    var arg2 = fetchArgs();
                    // Execute
                    binaryOp.binOp.accept((IStorage) arg1, arg2);
                } else {
                    AssemblyUnaryOperation unaryOp = (AssemblyUnaryOperation) instruction;
                    var arg = fetchArgs();
                    // Execute
                    unaryOp.unOp.accept((IStorage) arg);
                }
            }
            if(debugging)
                debug();

        }
        Operator.resetEquals();
        Operator.resetEquals();
        rip.setValue(startAddress);
        addressSpace.clear();
    }
    private static IValue fetchArgs() throws MemoryErrorException{

        byte byteCode = addressSpace.get(rip.getValue());
        ripInc();
        String flagType = byteCodeToKeyWord.get(byteCode);

        if(register.equals(flagType)){ // if it is register
            Register reg = byteCodeToRegister.get(addressSpace.get(rip.getValue())); // determining the registry type
            ripInc();
            return reg;

        }else if(constant.equals(flagType)){
            long constant = readLong();
            return new ConstantValue(constant);

        }else if(address.equals(flagType)){
            long address = readLong();
            if(!addressSpace.containsKey(address)){
                addressSpace.put(address,(byte)0);
            }
            return new MemoryLocation(address,addressSpace.get(address));

        }else if(indirectAdressing.equals(flagType)){ // [reg] the memory location as value is placed in reg
            byteCode = addressSpace.get(rip.getValue());
            ripInc();
            Register reg = byteCodeToRegister.get(byteCode);
            return new MemoryLocation(reg.getValue(),addressSpace.get(reg.getValue()));

        }else if(label.equals(flagType)){
            long address = readLong(); // we read 8B from memory
            return new MemoryLocation(address,addressSpace.get(address));

        }else
            throw new MemoryErrorException("Memory error!");
    }

    private static void printRegisters(){
        System.out.println();
        registers.stream().forEach((reg)->{
            System.out.println(reg.getType() + ": " + reg.get());
        });
        System.out.println();
    }
    public static void debug(){
        printRegisters();
        String input="";
        do{

            input = skener.nextLine();
            input=input.toUpperCase();
            if(input.startsWith("&")){
                input = input.substring(1,input.length());

                if(input.startsWith("0x") || input.startsWith("0X")) {
                    input = input.substring(2,input.length());
                    long address = Long.parseLong(input, 16);
                    System.out.println(input + ": " + (addressSpace.containsKey(address) ? addressSpace.get(address) : 0));

                }else{
                    long address = Long.parseLong(input);
                    System.out.println(input + ": " + (addressSpace.containsKey(address) ? addressSpace.get(address) : 0));
                }
            }else if(next.equals(input)){ // executes the next instruction and is still in debugging mode
                debugging = true;
                return;

            }else if(cont.equals(input)){
                debugging = false; // executes until the next breakpoint
                return;
            }

        }while(true);
    }
    private static long readLong(){
        byte[] longBytes = new byte[8];
        for(int i=0;i<8;i++){
            longBytes[i] = addressSpace.get(rip.getValue());
            ripInc(); // we place eight bytes from the address pointed to by rip
        }
        return ByteBuffer.wrap(longBytes).getLong();
    }
    public static Register getRegister(String name){
        for(var reg : registers){
            if(reg.getType().equals(name))
                return reg;
        }
        return null;
    }
}
