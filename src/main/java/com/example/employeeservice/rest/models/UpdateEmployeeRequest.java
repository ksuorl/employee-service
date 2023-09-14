package com.example.employeeservice.rest.models;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "Employee's email", example = "anna@email.com")
    @NotNull
    String email;

    @Schema(description = "Employee's first and last name", example = "Anna Ler")
    @NotNull
    String fullName;

    @Schema(description = "Employee's birthday date(format YYYY-MM-DD)", example = "2000-12-21")
    @NotNull
    Date birthday;

    @Schema(description = "List of Employee's hobbies")
    @NotNull
    @Builder.Default
    List<String> hobbies = new ArrayList<>();
}
