import CodeAnalysis.Analysis;
import CodeAnalysis.AssemblyCodeReader;
import CodeAnalysis.ByteCodeGenerator;
import CodeAnalysis.Interpreter;

public class MainTest {
    public static void main(String args[]){
        try {
            AssemblyCodeReader asc = new AssemblyCodeReader(args[0]);
            asc.readFile();
            Interpreter.initializeInterpreter();
            Analysis.assemblyCodeAnalysis();
            ByteCodeGenerator.generateByteCode(Interpreter.startAddress);
            Interpreter.byteCodeExecution();
        }catch (Exception ex){
            System.out.println("\nError: " + ex.getMessage()+"\n");
        }
    }
}
