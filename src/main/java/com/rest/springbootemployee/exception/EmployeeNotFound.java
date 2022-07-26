package com.rest.springbootemployee.exception;

public class EmployeeNotFound extends RuntimeException{
    public EmployeeNotFound() {
        super("employee not found");
    }
}
