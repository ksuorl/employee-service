package com.example.employeeservice.rest.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetEmployeesResponse {
    @Schema(description = "Employee's uuid", example = "c9ea2fd2-fb92-4d1d-b3d0-6fe189a40d52")
    String employeeUuid;

    @Schema(description = "Employee's email", example = "anna@email.com")
    String email;

    @Schema(description = "Employee's first and last name", example = "Anna Ler")
    String fullName;

    @Schema(description = "Employee's birthday date(format yyyy-MM-dd)", type = "string", example = "2000-12-21")
    @NotNull(message = "The 'birthday' is required.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd")
    String birthday;

    @Schema(description = "List of Employee's hobbies")
    List<String> hobbies;
}
