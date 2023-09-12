package com.example.employeeservice.rest.models;

import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Value
public class UpdateEmployeeRequest {
    @NotNull
    String email;
    @NotNull
    String fullName;
    @NotNull
    Date birthday;
    List<String> hobbies = new ArrayList<>();
}
