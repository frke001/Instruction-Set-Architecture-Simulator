package Exceptions;

public class SemanticErrorException extends Exception{
    public SemanticErrorException(){
        super();
    }
    public SemanticErrorException(String message){
        super(message);
    }
}
