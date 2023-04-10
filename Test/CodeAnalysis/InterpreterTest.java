package CodeAnalysis;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class InterpreterTest {
    private void execute(String path){
        try {
            AssemblyCodeReader asc = new AssemblyCodeReader(path);
            asc.readFile();
            Interpreter.initializeInterpreter();
            Analysis.assemblyCodeAnalysis();
            ByteCodeGenerator.generateByteCode(Interpreter.startAddress);
            Interpreter.byteCodeExecution();
        }catch (Exception ex){
            fail("\nError: " + ex.getMessage()+"\n");
        }
    }
    @Test
    public void arithmeticOperationsAddTest(){
        this.execute("addTest.txt");
        assertEquals(9,Interpreter.getRegister("RAX").getValue());
    }
    @Test
    public void arithmeticOperationsSubTest(){
        this.execute("subTest.txt");
        assertEquals(1,Interpreter.getRegister("RAX").getValue());
    }
    @Test
    public void arithmeticOperationsMulTest(){
        this.execute("mulTest.txt");
        assertEquals(30,Interpreter.getRegister("RAX").getValue());
    }
    @Test
    public void arithmeticOperationsDivTest(){
        this.execute("divTest.txt");
        assertEquals(5,Interpreter.getRegister("RAX").getValue());
    }
    @Test
    public void bitwiseOperationsAndTest(){
        this.execute("andTest.txt");
        assertEquals(1,Interpreter.getRegister("RAX").getValue());
    }
    @Test
    public void bitwiseOperationsOrTest(){
        this.execute("orTest.txt");
        assertEquals(7,Interpreter.getRegister("RAX").getValue());
    }
    @Test
    public void bitwiseOperationsXorTest(){
        this.execute("xorTest.txt");
        assertEquals(6,Interpreter.getRegister("RAX").getValue());
    }
    @Test
    public void bitwiseOperationsNotTest(){
        this.execute("notTest.txt");
        assertEquals(0,Interpreter.getRegister("RAX").getValue());
    }
    @Test
    public void JmpTest(){
        this.execute("jmpTest.txt");
        assertEquals(7,Interpreter.getRegister("RCX").getValue());
        assertEquals(5,Interpreter.getRegister("RAX").getValue());
    }
    @Test
    public void JeJneTest(){
        this.execute("je_jneTest.txt");
        assertEquals(5,Interpreter.getRegister("RBX").getValue());
    }
    @Test
    public void JlJgeTest(){
        this.execute("jl_jgeTest.txt");
        assertEquals(7,Interpreter.getRegister("RBX").getValue());
    }
    @Test
    public void indirectAddressingTest(){
        this.execute("indirectAddressingTest.txt");
        assertEquals(45,Interpreter.getRegister("RCX").getValue());
    }
    @Test
    public void directAddressingTest(){
        this.execute("directAddressingTest.txt");
        assertEquals(16,Interpreter.getRegister("RAX").getValue());
    }
    @Test
    public void scanTest(){
        ByteArrayInputStream input = new ByteArrayInputStream("145".getBytes());
        System.setIn(input);
        this.execute("scanTest.txt");
        assertEquals(145,Interpreter.getRegister("RAX").getValue());
    }
    @Test
    public void selfModifyingTest(){
        this.execute("selfModifyingTest.txt");
        assertEquals(16,Interpreter.getRegister("RBX").getValue());
    }



}