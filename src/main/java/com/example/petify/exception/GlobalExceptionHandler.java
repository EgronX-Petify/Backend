package com.example.petify.exception;


import com.example.petify.common.dto.APIErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<APIErrorResponse> handleException(Exception e) {
        log.error("Caught an Exception: " , e);
        APIErrorResponse err = APIErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("Unexpected error occurred.")
                .build();
        err.addError("type", e.getClass().getName());
        err.addError("error", e.getMessage());
        return new ResponseEntity<>(err, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = BadCredentialsException.class)
    public ResponseEntity<APIErrorResponse> handleException(BadCredentialsException e) {
        APIErrorResponse err = APIErrorResponse.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .message("Invalid username or password.")
                .build();
        return new ResponseEntity<>(err, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = AuthorizationDeniedException.class)
    public ResponseEntity<APIErrorResponse> handleException(AuthorizationDeniedException e) {
        APIErrorResponse err = APIErrorResponse.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .message("Access denied.")
                .build();
        return new ResponseEntity<>(err, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = UsernameNotFoundException.class)
    public ResponseEntity<APIErrorResponse> handleException(UsernameNotFoundException e) {
        APIErrorResponse err = APIErrorResponse.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .message("User not found")
                .build();
        return new ResponseEntity<>(err, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = UsernameAlreadyExistsException.class)
    public ResponseEntity<APIErrorResponse> handleException(UsernameAlreadyExistsException e) {
        APIErrorResponse err = APIErrorResponse.builder()
                .status(HttpStatus.CONFLICT.value())
                .message("User already exists")
                .build();
        err.addError("error", e.getMessage());
        return new ResponseEntity<>(err, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<APIErrorResponse> handleException(IllegalArgumentException e) {
        APIErrorResponse err = APIErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<APIErrorResponse> handleException(ResourceNotFoundException e) {
        APIErrorResponse err = APIErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message("Resource not found.")
                .build();
        err.addError("error", e.getMessage());
        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public  ResponseEntity<APIErrorResponse> handleException(HttpMessageNotReadableException e) {
        APIErrorResponse err = APIErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message("Invalid JSON object.")
                .build();
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = NoHandlerFoundException.class)
    public ResponseEntity<APIErrorResponse> handleException(NoHandlerFoundException e) {
        APIErrorResponse err = APIErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message("Resource not found.")
                .build();
        err.addError("error", e.getMessage());
        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ResponseEntity<APIErrorResponse> handleException(MissingServletRequestParameterException e) {
        APIErrorResponse err = APIErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message("Missing Required request parameters")
                .build();
        err.addError(e.getParameterName(),"Parameter is missing");
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        APIErrorResponse err = APIErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message("Validation failed.")
                .build();
        ex.getBindingResult().getFieldErrors().forEach(fieldError ->
                err.addError(fieldError.getField(), fieldError.getDefaultMessage())
        );
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = InvalidTokenException.class)
    public ResponseEntity<APIErrorResponse> handleException(InvalidTokenException e) {
        APIErrorResponse err = APIErrorResponse.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .message("Invalid token.")
                .build();
        err.addError("error", e.getMessage());
        return new ResponseEntity<>(err, HttpStatus.UNAUTHORIZED);
    }
}
