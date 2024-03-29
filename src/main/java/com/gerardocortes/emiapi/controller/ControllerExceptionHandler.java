package com.gerardocortes.emiapi.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<FailedResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, ServletWebRequest req) {
        log.debug("Exception on " + req.getContextPath() + " with message: " + ex.getMessage(), ex);

        final String violations = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage() )
                .collect(Collectors.joining("\n", "Violations:\n", ""));

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new FailedResponse(violations));
    }

    @ExceptionHandler(ArithmeticException.class)
    public ResponseEntity<FailedResponse> handleArithmeticException(ArithmeticException ex, ServletWebRequest req) {
        log.error("Exception on " +  req.getContextPath() + " with message: " + ex.getMessage(), ex);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new FailedResponse(ex.getMessage()));
    }

}
