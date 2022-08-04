package com.rest.springbootemployee.exception;

import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TodoNotFoundException extends RuntimeException {
    public TodoNotFoundException() {
        super("todo not found");
    }
}
