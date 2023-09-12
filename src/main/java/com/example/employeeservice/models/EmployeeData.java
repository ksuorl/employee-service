package com.example.employeeservice.models;

import lombok.Builder;
import lombok.Value;

import java.util.Date;
import java.util.List;

@Value
@Builder
public class EmployeeData {
    String employeeUuid;
    String email;
    String fullName;
    Date birthday;
    List<String> hobbies;
}
