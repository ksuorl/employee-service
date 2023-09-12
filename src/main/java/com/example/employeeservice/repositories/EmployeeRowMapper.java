package com.example.employeeservice.repositories;

import com.example.employeeservice.models.EmployeeData;
import lombok.SneakyThrows;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EmployeeRowMapper implements RowMapper<EmployeeData> {
    //TODO
    @SneakyThrows
    @Override
    public EmployeeData mapRow(ResultSet rs, int rowNum) {
        DateFormat df = new SimpleDateFormat("YYYY-MM-DD");
        Date date  = df.parse(rs.getString("birthday_date"));
        EmployeeData employee = EmployeeData.builder()
                .employeeUuid(rs.getString("uuid"))
                .fullName(rs.getString("full_name"))
                .email(rs.getString("email"))
                .birthday(date)
                .build();

        return employee;
    }
}
