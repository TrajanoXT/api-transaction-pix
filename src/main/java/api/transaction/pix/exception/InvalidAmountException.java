package api.transaction.pix.exception;

public class InvalidAmountException extends RuntimeException{
    public InvalidAmountException(String message){
        super(message);
    }
}
