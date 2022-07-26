package com.rest.springbootemployee.exception;

public class CompanyNotFoundException extends RuntimeException{
    public CompanyNotFoundException() {
        super("company not found");
    }
}
