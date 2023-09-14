package com.example.employeeservice.rest;

import com.example.employeeservice.models.EmployeeData;
import com.example.employeeservice.repositories.EmployeeDuplicateException;
import com.example.employeeservice.rest.exception.InvalidRequestException;
import com.example.employeeservice.rest.models.CreateEmployeeRequest;
import com.example.employeeservice.rest.models.ErrorResponse;
import com.example.employeeservice.rest.models.GetEmployeesResponse;
import com.example.employeeservice.rest.models.UpdateEmployeeRequest;
import com.example.employeeservice.services.DateFormatter;
import com.example.employeeservice.services.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Employee", description = "Employee management APIs")
@RestController
@RequestMapping("/employees")
public class EmployeeController {
    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Operation(
            summary = "Create new Employee",
            description = "Create new Employee object with autogenerated ID in storage, send notification about this in message service." +
                    " The response is Employee object with ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = {@Content(schema = @Schema(implementation = GetEmployeesResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public GetEmployeesResponse createEmployee(@RequestBody @Validated CreateEmployeeRequest createEmployeeRequest) throws EmployeeDuplicateException {
        EmployeeData employee = employeeService.createEmployee(createEmployeeRequest);
        return GetEmployeesResponse.builder()
                .employeeUuid(employee.getEmployeeUuid())
                .birthday(DateFormatter.toFormattedBirthdayDate(employee.getBirthday()))
                .email(employee.getEmail())
                .fullName(employee.getFullName())
                .hobbies(employee.getHobbies())
                .build();
    }

    @Operation(
            summary = "Get all Employee objects")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = GetEmployeesResponse.class)), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "500")})
    @GetMapping
    public List<GetEmployeesResponse> getAllEmployees() {
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

    @Operation(
            summary = "Update Employee with provided uuid value")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "500")})
    @PutMapping
    public void updateEmployee(@RequestParam(name = "uuid") String employeeUuid,
                               @RequestBody @Validated UpdateEmployeeRequest updateEmployeeRequest) throws EmployeeDuplicateException {
        employeeService.updateEmployee(employeeUuid, updateEmployeeRequest);
    }

    @Operation(
            summary = "Delete Employee with provided uuid value")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "500")})
    @DeleteMapping
    public void removeEmployee(@RequestParam(name = "uuid") String employeeUuid) throws InvalidRequestException {
        if (ObjectUtils.isEmpty(employeeUuid)) {
            throw new InvalidRequestException("Employee 'uuid' should be set.");
        }
        employeeService.removeEmployee(employeeUuid);
    }
}
