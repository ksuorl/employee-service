package com.example.employeeservice.repositories;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class EmployeeDuplicateException extends Exception {
    public EmployeeDuplicateException(String message) {
        super(message);
    }
}
