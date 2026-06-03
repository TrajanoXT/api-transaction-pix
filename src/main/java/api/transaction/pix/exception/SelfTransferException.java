package api.transaction.pix.exception;

public class SelfTransferException extends RuntimeException{
    public SelfTransferException(String message){
        super(message);
    }
}
