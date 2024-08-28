package com.sparta.upgradeschedulemanagement;

import com.sparta.upgradeschedulemanagement.exception.DuplicatedEntityException;
import com.sparta.upgradeschedulemanagement.exception.EntityNotFoundException;
import com.sparta.upgradeschedulemanagement.exception.AuthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandling {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handlerEntityNotFoundException(EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(IllegalAccessError.class)
    public ResponseEntity<String> handlerEntityIllegalAccessError(IllegalAccessError e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }

    @ExceptionHandler(DuplicatedEntityException.class)
    public ResponseEntity<String> handlerDuplicatedEntityException(DuplicatedEntityException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(AuthorizedException.class)
    public ResponseEntity<String> handlerAuthorizedException(AuthorizedException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }
}
