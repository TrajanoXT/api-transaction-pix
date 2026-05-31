package api.transaction.pix.exception;

public class CpfAlreadyExistsException extends RuntimeException{
    public CpfAlreadyExistsException(String message){
        super(message);
    }
}
