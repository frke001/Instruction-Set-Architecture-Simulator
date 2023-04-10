package Operations;

import CodeAnalysis.Interpreter;
import Interfaces.*;
import Operands.MemoryLocation;

import java.util.Scanner;


public class Operator {

    private static Scanner skener = new Scanner(System.in);
    private static boolean equals,lessThan;
    public static void add(IStorage arg1, IValue arg2){
        arg1.put(arg1.get() + arg2.get());
    }
    public static void sub(IStorage arg1, IValue arg2){
        arg1.put(arg1.get() - arg2.get());
    }
    public static void mul(IStorage arg1, IValue arg2){
        arg1.put(arg1.get() * arg2.get());
    }
    public static void div(IStorage arg1, IValue arg2){
        if(arg2.get() != 0)
            arg1.put(arg1.get() / arg2.get());
        else
            throw new ArithmeticException("Dividing by zero!");
    }
    public static void and(IStorage arg1, IValue arg2){
        arg1.put(arg1.get() & arg2.get());
    }
    public static void or(IStorage arg1, IValue arg2){
        arg1.put(arg1.get() | arg2.get());
    }
    public static void not(IStorage arg){
        arg.put(~arg.get());
    }
    public static void xor(IStorage arg1, IValue arg2){
        arg1.put(arg1.get() ^ arg2.get());
    }
    public static void mov(IStorage arg1, IValue arg2){
        arg1.put(arg2.get());
    }
    public static void cmp(IStorage arg1, IValue arg2){
        equals = (arg1.get() == arg2.get());
        lessThan = (arg1.get() < arg2.get());
    }
    public static void jmp(IStorage arg){
        if(arg instanceof MemoryLocation){
            MemoryLocation mem = (MemoryLocation) arg;
            Interpreter.rip.put(mem.getMemoryAdress());
        }
    }
    public static void je(IStorage arg){
        if(equals){
            jmp(arg);
        }
    }
    public static void jne(IStorage arg){
        if(!equals){
            jmp(arg);
        }
    }
    public static void jge(IStorage arg){
        if(!lessThan){
            jmp(arg);
        }
    }
    public static void jl(IStorage arg){
        if(lessThan){
            jmp(arg);
        }
    }
    public static void scan(IStorage arg){
         arg.put(skener.nextLong());
    }
    public static void print(IStorage arg){
        System.out.println(arg.get());
    }

    public static void resetEquals(){
        equals=false;
    }
    public static void resetLessThan(){
        lessThan=false;
    }


}
