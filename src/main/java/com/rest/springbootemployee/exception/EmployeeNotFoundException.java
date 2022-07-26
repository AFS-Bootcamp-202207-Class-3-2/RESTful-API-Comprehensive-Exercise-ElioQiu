package com.rest.springbootemployee.exception;

public class EmployeeNotFoundException extends RuntimeException{
    public EmployeeNotFoundException() {
        super("employee not found");
    }
}
