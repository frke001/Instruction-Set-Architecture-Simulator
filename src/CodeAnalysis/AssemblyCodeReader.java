package CodeAnalysis;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class AssemblyCodeReader {

    private String filePath;
    public static List<String> assemblyCode;

    public AssemblyCodeReader(String filePath){
        this.filePath=filePath;
    }
    public void readFile() throws IOException{
        assemblyCode = Files.readAllLines(Paths.get(filePath));
    }

}
