package Exceptions;

public class InvalidOpCodeException extends Exception {
    public InvalidOpCodeException() {
        super();
    }

    public InvalidOpCodeException(String message) {
        super(message);
    }
}
