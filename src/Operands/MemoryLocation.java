package Operands;

import CodeAnalysis.Interpreter;
import Interfaces.IStorage;

public class MemoryLocation implements IStorage {

    private long memoryAdress; //OX
    private byte value;

    public MemoryLocation(Long memoryAdress,Byte value){
        this.memoryAdress=memoryAdress;
        this.value=value;
    }
    public long getMemoryAdress(){
        return memoryAdress;
    }
    public byte getValue(){
        return value;
    }
    @Override
    public long get(){
        return this.value;
    }
    @Override
    public void put(long valueToStore){
        this.value=(byte) valueToStore;
        Interpreter.addressSpace.put(memoryAdress,value);
    }
}
