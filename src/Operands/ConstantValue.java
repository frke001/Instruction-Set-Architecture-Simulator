package Operands;

import Interfaces.IValue;

public class ConstantValue implements IValue {

    private long value;

    public ConstantValue(Long value){
        this.value=value;
    }
    public Long getValue(){
        return value;
    }
    @Override
    public long get(){
        return value;
    }
}
