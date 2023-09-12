package com.example.employeeservice.rest.models;

import lombok.Value;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Value
public class UpdateEmployeeRequest {
    String email;
    String fullName;
    Date birthday;
    List<String> hobbies;
}
