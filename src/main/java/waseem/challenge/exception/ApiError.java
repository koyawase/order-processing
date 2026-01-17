package waseem.challenge.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ApiError(HttpStatus status, String message, String path, LocalDateTime timestamp) {
    public ApiError(HttpStatus status, String message, String path) {
        this(status, message, path, LocalDateTime.now());
    }
}