package Exceptions;

public class MemoryErrorException extends Exception{
    public MemoryErrorException(){
        super();
    }
    public MemoryErrorException(String message){
        super(message);
    }
}
