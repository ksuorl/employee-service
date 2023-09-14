package com.example.employeeservice.rest.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Schema
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateEmployeeRequest {
    @Schema(description = "Employee's email", example = "anna@email.com")
    @Email(message = "Email should be valid")
    @NotEmpty(message = "The 'Email' is required.")
    private String email;

    @Schema(description = "Employee's first and last name", example = "Anna Ler")
    @NotEmpty(message = "The 'full name' is required.")
    @Size(min = 2, max = 100, message = "The length of full name must be between 2 and 100 characters.")
    private String fullName;

    @Schema(description = "Employee's birthday date(format yyyy-MM-dd)", type = "string", example = "2000-12-21")
    @NotNull(message = "The 'birthday' is required.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd")
    private Date birthday;

    @Schema(description = "List of Employee's hobbies")
    @Builder.Default
    private List<String> hobbies = new ArrayList<>();
}
