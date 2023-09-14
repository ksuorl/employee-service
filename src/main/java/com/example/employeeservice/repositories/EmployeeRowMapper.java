package com.example.employeeservice.repositories;

import com.example.employeeservice.models.EmployeeData;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
public class EmployeeRowMapper implements RowMapper<EmployeeData> {

    private ObjectMapper objectMapper;

    @SneakyThrows
    @Override
    public EmployeeData mapRow(ResultSet rs, int rowNum) {
        DateFormat df = new SimpleDateFormat("YYYY-MM-DD");
        Date date = df.parse(rs.getString("birthday_date"));
        String hobbiesJson = rs.getString("hobbies");
        List<String> hobbies = hobbiesJson != null ?
                Arrays.asList(objectMapper.readValue(hobbiesJson, String[].class))
                : new ArrayList<>();
        EmployeeData employee = EmployeeData.builder()
                .employeeUuid(rs.getString("uuid"))
                .fullName(rs.getString("full_name"))
                .email(rs.getString("email"))
                .birthday(date)
                .hobbies(hobbies)
                .build();
        return employee;
    }
}
