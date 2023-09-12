package com.example.employeeservice.rest;

import com.example.employeeservice.repositories.EmployeeDuplicateException;
import com.example.employeeservice.rest.exception.InvalidRequestException;
import com.example.employeeservice.services.NotificationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class EmployeeExceptionController {
    @ExceptionHandler(value = EmployeeDuplicateException.class)
    public ResponseEntity<Object> exception(EmployeeDuplicateException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = InvalidRequestException.class)
    public ResponseEntity<Object> exception(InvalidRequestException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
