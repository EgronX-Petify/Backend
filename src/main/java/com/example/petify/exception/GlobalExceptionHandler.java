package com.example.petify.exception;


import com.example.petify.common.dto.APIErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<APIErrorResponse> handleException(Exception e) {
        log.error("Caught an Exception: " , e);
        APIErrorResponse apiErrorResponse = APIErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("Unexpected error occurred.")
                .build();
        return new ResponseEntity<>(apiErrorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = BadCredentialsException.class)
    public ResponseEntity<APIErrorResponse> handleException(BadCredentialsException e) {
        APIErrorResponse err = APIErrorResponse.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .message("Invalid username or password.")
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

    @ExceptionHandler(value = UsernameAlreadyExists.class)
    public ResponseEntity<APIErrorResponse> handleException(UsernameAlreadyExists e) {
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
}
