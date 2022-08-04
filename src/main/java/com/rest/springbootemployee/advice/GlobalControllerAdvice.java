package com.rest.springbootemployee.advice;

import com.rest.springbootemployee.exception.CompanyNotFoundException;
import com.rest.springbootemployee.exception.EmployeeNotFoundException;
import com.rest.springbootemployee.exception.TodoNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({EmployeeNotFoundException.class, CompanyNotFoundException.class, TodoNotFoundException.class})
    public ErrorResponse handleNotFoundException(Exception exception) {
        log.error("路径错误或资源已被删除-------------{}", exception);
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = RuntimeException.class)
    public ErrorResponse handler(RuntimeException exception) {
        log.error("运行时异常：----------------{}", exception);
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    }
}
