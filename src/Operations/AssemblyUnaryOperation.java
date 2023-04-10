package Operations;

import Operations.AssemblyOperation;
import java.util.function.Consumer;
import Interfaces.*;

public class AssemblyUnaryOperation extends AssemblyOperation {

    public Consumer<IStorage> unOp;

    public AssemblyUnaryOperation(String opName,Byte opByteCode, Consumer<IStorage> unOp){
        super(opName,opByteCode);
        this.unOp=unOp;
    }
}
