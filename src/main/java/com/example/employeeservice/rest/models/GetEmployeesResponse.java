package com.example.employeeservice.rest.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetEmployeesResponse {
    String employeeUuid;
    String email;
    String fullName;
    String birthday;
    List<String> hobbies;
}
