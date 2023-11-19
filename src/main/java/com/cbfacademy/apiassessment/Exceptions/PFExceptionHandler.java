package com.cbfacademy.apiassessment.Exceptions;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.util.Date;

@ControllerAdvice
public class PFExceptionHandler {

    @ExceptionHandler(value = {EntityNotFoundException.class})
    public ResponseEntity<Object> handlesEntityNotFoundException(EntityNotFoundException e){
        HttpStatus badRequest = HttpStatus.NOT_FOUND;
        APIException apiException = new APIException(
                e.getMessage(),
                badRequest,
                new Date());
        return new ResponseEntity<>(apiException, badRequest);
    }

    @ExceptionHandler(value = {EntityExistsException.class})
    public ResponseEntity<Object> handlesEntityExistsException(EntityExistsException e){
        HttpStatus badRequest = HttpStatus.CONFLICT;
        APIException apiException = new APIException(
                e.getMessage(),
                badRequest,
                new Date());
        return new ResponseEntity<>(apiException, badRequest);
    }

    @ExceptionHandler(value = {IOException.class})
    public ResponseEntity<Object> handlesIOException(IOException e){
        HttpStatus badRequest = HttpStatus.INTERNAL_SERVER_ERROR;
        APIException apiException = new APIException(
                e.getMessage(),
                badRequest,
                new Date());
        return new ResponseEntity<>(apiException, badRequest);
    }

    @ExceptionHandler(value = {ValidationException.class})
    public ResponseEntity<Object> handlesValidationException(ValidationException e){
        HttpStatus badRequest = HttpStatus.UNPROCESSABLE_ENTITY;
        APIException apiException = new APIException(
                e.getMessage(),
                badRequest,
                new Date());
        return new ResponseEntity<>(apiException, badRequest);
    }

}
