package Operands;

import Interfaces.IStorage;

public class Register implements IStorage {

    private String type;
    private long value;
    private byte byteCode;

    public Register(){
        super();
    }
    public Register(String type, long value,byte byteCode){
        this.type=type;
        this.value=value;
        this.byteCode=byteCode;
    }
    public Register(String type, byte byteCode){
        this.type=type;
        this.byteCode=byteCode;
    }
    @Override
    public long get(){
        return this.value;
    }
    @Override
    public void put(long valueToStore){
        this.value=valueToStore;
    }

    public String getType(){
        return this.type;
    }
    public long getValue(){
        return this.value;
    }
    public byte getByteCode(){
        return byteCode;
    }
    public void setType(String type){
        this.type=type;
    }
    public void setValue(long value){
        this.value=value;
    }

}
