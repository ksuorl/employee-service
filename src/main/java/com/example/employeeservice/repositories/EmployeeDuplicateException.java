package com.example.employeeservice.repositories;

public class EmployeeDuplicateException extends Exception {
    public EmployeeDuplicateException(String message) {
        super(message);
    }
}
