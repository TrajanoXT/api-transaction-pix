package api.transaction.pix.exception;

public class PixKeyAlreadyExistsException extends RuntimeException{
    public PixKeyAlreadyExistsException(String message) {
        super(message);
    }
}
