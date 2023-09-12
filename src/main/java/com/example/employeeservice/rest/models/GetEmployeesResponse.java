package com.example.employeeservice.rest.models;

import lombok.Builder;
import lombok.Value;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Value
@Builder
public class GetEmployeesResponse {
    String employeeUuid;
    String email;
    String fullName;
    String birthday;
    List<String> hobbies;
}
