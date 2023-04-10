package CodeAnalysis;
import Exceptions.LexisErrorException;
import Exceptions.SemanticErrorException;
import Exceptions.SyntaxErrorException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AnalysisTest {

    private void assemblyCodeAnalysis(String path) throws Exception{
        AssemblyCodeReader asc = new AssemblyCodeReader(path);
        asc.readFile();
        Interpreter.initializeInterpreter();
        Analysis.assemblyCodeAnalysis();
    }
    @Test
    public void LexisErrorExceptionTest(){
        assertThrows(LexisErrorException.class,() -> {
            this.assemblyCodeAnalysis("lexisTest.txt");
        });
    }
    @Test
    public void semanticErrorExceptionTest(){
        assertThrows(SemanticErrorException.class,() -> {
            this.assemblyCodeAnalysis("semanticTest.txt");
        });
    }
    @Test
    public void syntaxErrorExceptionTest(){
        assertThrows(SyntaxErrorException.class,() -> {
            this.assemblyCodeAnalysis("syntaxTest.txt");
        });
    }
}