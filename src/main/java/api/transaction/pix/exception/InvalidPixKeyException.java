package api.transaction.pix.exception;

public class InvalidPixKeyException extends RuntimeException{
    public InvalidPixKeyException(String message){
        super(message);
    }
}
