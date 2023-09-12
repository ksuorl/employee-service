package com.example.employeeservice.rest;

import com.example.employeeservice.repositories.EmployeeDuplicateException;
import com.example.employeeservice.rest.exception.InvalidRequestException;
import com.example.employeeservice.rest.models.CreateEmployeeRequest;
import com.example.employeeservice.rest.models.GetEmployeesResponse;
import com.example.employeeservice.rest.models.UpdateEmployeeRequest;
import com.example.employeeservice.services.DateFormatter;
import com.example.employeeservice.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    private EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public void createEmployee(@RequestBody @Validated CreateEmployeeRequest createEmployeeRequest) throws EmployeeDuplicateException {
        employeeService.createEmployee(createEmployeeRequest);
    }

    @GetMapping
    public List<GetEmployeesResponse> getAllEmployees() {
        //TODO create separate mapper class
        return employeeService.getAllEmployees().stream()
                .map(empl -> GetEmployeesResponse.builder()
                        .employeeUuid(empl.getEmployeeUuid())
                        .birthday(DateFormatter.toFormattedBirthdayDate(empl.getBirthday()))
                        .email(empl.getEmail())
                        .fullName(empl.getFullName())
                        .hobbies(empl.getHobbies())
                        .build())
                .toList();
    }

    @PutMapping
    public void updateEmployee(@RequestParam(name = "uuid") String employeeUuid,
                               @RequestBody @Validated UpdateEmployeeRequest updateEmployeeRequest) throws EmployeeDuplicateException {
        employeeService.updateEmployee(employeeUuid, updateEmployeeRequest);
    }

    @DeleteMapping
    public void removeEmployee(@RequestParam(name = "uuid") String employeeUuid) throws InvalidRequestException {
        if (ObjectUtils.isEmpty(employeeUuid)) {
            throw new InvalidRequestException("Employee 'uuid' should be set.");
        }
        employeeService.removeEmployee(employeeUuid);
    }
}
