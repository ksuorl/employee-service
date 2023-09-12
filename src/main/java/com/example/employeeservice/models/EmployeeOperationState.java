package com.example.employeeservice.models;

public enum EmployeeOperationState {
    CREATE("Create"),
    UPDATE("Update"),
    DELETE("Delete");

    public final String value;

    EmployeeOperationState(String value) {
        this.value = value;
    }
}
