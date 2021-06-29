package com.abank.rest;

import com.abank.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<List<ErrorResponse>> handleMethodValidationExceptions(MethodArgumentNotValidException ex) {

        List<ErrorResponse> errors = ex.getBindingResult().getAllErrors().stream()
                .map(e -> new ErrorResponse(((FieldError) e).getField(), e.getDefaultMessage()))
                .collect(Collectors.toList());
        return ResponseEntity.badRequest().body(errors);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<List<ErrorResponse>> handleConstraintValidationExceptions(ConstraintViolationException ex) {

        List<ErrorResponse> errors = ex.getConstraintViolations().stream()
                .map(e -> new ErrorResponse(e.getPropertyPath().toString(), e.getMessage()))
                .collect(Collectors.toList());
        return ResponseEntity.badRequest().body(errors);
    }
}
