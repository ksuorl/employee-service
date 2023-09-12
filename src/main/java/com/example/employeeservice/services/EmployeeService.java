package com.example.employeeservice.services;

import com.example.employeeservice.repositories.EmployeeDuplicateException;
import com.example.employeeservice.rest.models.CreateEmployeeRequest;
import com.example.employeeservice.models.EmployeeData;
import com.example.employeeservice.rest.models.UpdateEmployeeRequest;

import java.util.List;

public interface EmployeeService {
    EmployeeData createEmployee(CreateEmployeeRequest createEmployeeRequest) throws EmployeeDuplicateException;
    void updateEmployee(String employeeUuid, UpdateEmployeeRequest updateEmployeeRequest) throws EmployeeDuplicateException;
    List<EmployeeData> getAllEmployees();
    void removeEmployee(String employeeUuid);
}
