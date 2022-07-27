package com.rest.springbootemployee.advice;

import com.rest.springbootemployee.exception.CompanyNotFoundException;
import com.rest.springbootemployee.exception.EmployeeNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({EmployeeNotFoundException.class, CompanyNotFoundException.class})
    public ErrorResponse handleNotFoundException(Exception exception) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage());
    }
}
