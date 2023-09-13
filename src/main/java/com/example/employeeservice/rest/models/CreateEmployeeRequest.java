package com.example.employeeservice.rest.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class CreateEmployeeRequest {
    @Email(message = "Email should be valid")
    @NotEmpty(message = "The 'Email' is required.")
    private String email;
    @NotEmpty(message = "The 'full name' is required.")
    @Size(min = 2, max = 100, message = "The length of full name must be between 2 and 100 characters.")
    private String fullName;
    @NotNull(message = "The 'birthday' is required.")
    private Date birthday;
    @Builder.Default
    private List<String> hobbies = new ArrayList<>();
}
