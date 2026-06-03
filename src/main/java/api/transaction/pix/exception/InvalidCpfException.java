package api.transaction.pix.exception;

public class InvalidCpfException extends RuntimeException{
    public InvalidCpfException(String message){
        super(message);
    }
}
