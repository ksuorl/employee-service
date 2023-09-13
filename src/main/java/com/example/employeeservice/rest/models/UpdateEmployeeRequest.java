package com.example.employeeservice.rest.models;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEmployeeRequest {
    @NotNull
    String email;
    @NotNull
    String fullName;
    @NotNull
    Date birthday;
    @Builder.Default
    List<String> hobbies = new ArrayList<>();
}
