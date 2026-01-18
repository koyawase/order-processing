package waseem.challenge.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ApiError> handleOrderNotFound(OrderNotFoundException ex, HttpServletRequest request) {
        ApiError error = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> handleOrderHistoryNotFoundException(OrderHistoryNotFoundException ex, HttpServletRequest request) {
        ApiError error = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidOrderStatusException.class)
    public ResponseEntity<ApiError> handleInvalidTransitionException(InvalidOrderStatusException ex, HttpServletRequest request) {
        ApiError error = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConflictOrderException.class)
    public ResponseEntity<ApiError> handleConflictOrderException(ConflictOrderException ex, HttpServletRequest request) {
        ApiError error = new ApiError(HttpStatus.CONFLICT, ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

}
