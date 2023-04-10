package Operations;

import java.util.function.BiConsumer;
import Interfaces.*;
public class AsseblyBinaryOperation extends AssemblyOperation {
    public BiConsumer<IStorage, IValue> binOp;

    public AsseblyBinaryOperation(String opName, byte opByteCode, BiConsumer<IStorage, IValue> binOp){
        super(opName,opByteCode);
        this.binOp=binOp;
    }
}
