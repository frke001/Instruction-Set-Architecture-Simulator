package Operations;

public class AssemblyOperation {

    private String opName;
    private byte opByteCode;

    public AssemblyOperation(String opName, byte opByteCode){
        this.opName=opName;
        this.opByteCode=opByteCode;
    }
    public String getOpName(){
        return opName;
    }
    public byte getOpByteCode(){
        return opByteCode;
    }
}
