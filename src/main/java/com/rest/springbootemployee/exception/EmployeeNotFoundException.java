package com.rest.springbootemployee.exception;

import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class EmployeeNotFoundException extends RuntimeException{
    public EmployeeNotFoundException() {
        super("employee not found");
    }
}
