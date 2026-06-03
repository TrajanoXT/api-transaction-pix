package api.transaction.pix.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CpfAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleCpfAlreadyExists(CpfAlreadyExistsException e){
        return build(HttpStatus.CONFLICT,e.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException e){
        return build(HttpStatus.NOT_FOUND,e.getMessage());
    }

    @ExceptionHandler(PixKeyNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePixKeyNotFoundException(PixKeyNotFoundException e){
        return build(HttpStatus.NOT_FOUND,e.getMessage());
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientBalanceException(InsufficientBalanceException e){
        return build(HttpStatus.UNPROCESSABLE_ENTITY,e.getMessage());
    }

    @ExceptionHandler(SelfTransferException.class)
    public ResponseEntity<ErrorResponse> handleSelfTransferException(SelfTransferException e){
        return build(HttpStatus.UNPROCESSABLE_ENTITY,e.getMessage());
    }

    @ExceptionHandler(InvalidAmountException.class)
    public ResponseEntity<ErrorResponse> handleInvalidAmountException(InvalidAmountException e){
        return build(HttpStatus.BAD_REQUEST,e.getMessage());
    }

    @ExceptionHandler(InvalidPixKeyException.class)
    public ResponseEntity<ErrorResponse> handleInvalidPixKeyException(InvalidPixKeyException e){
        return build(HttpStatus.BAD_REQUEST,e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e){
        return build(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
    }

    private ResponseEntity<ErrorResponse> build(HttpStatus status, String message){
        return ResponseEntity.status(status).body(
                new ErrorResponse(message,status.value(),LocalDateTime.now())
        );
    }
}
