package com.songify.infrastructure.security;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class SecurityErrorHandler {

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    ResponseEntity<SecurityErrorResponse> handleUsernameAlreadyExists(UsernameAlreadyExistsException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new SecurityErrorResponse(HttpStatus.CONFLICT.value(), exception.getMessage()));
    }

    @ExceptionHandler(AuthenticationException.class)
    ResponseEntity<SecurityErrorResponse> handleAuthenticationException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new SecurityErrorResponse(HttpStatus.UNAUTHORIZED.value(), "Invalid username or password"));
    }

    record SecurityErrorResponse(int status, String message) {
    }
}
