package api.transaction.pix.exception;

public class PixKeyNotFoundException extends RuntimeException{
    public PixKeyNotFoundException(String message){
        super(message);
    }
}
