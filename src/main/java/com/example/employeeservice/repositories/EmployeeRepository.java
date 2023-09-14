package com.example.employeeservice.repositories;

import com.example.employeeservice.models.EmployeeData;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@AllArgsConstructor
public class EmployeeRepository {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private EmployeeRowMapper employeeRowMapper;
    private ObjectMapper objectMapper;
    private final String SQL_FIND_ALL = "select * from employee;";
    private final String SQL_INSERT_EMPLOYEE = "insert into employee(uuid, full_name, email, birthday_date, hobbies)" +
            " values(:uuid, :full_name, :email, :birthday_date, :hobbies);";
    private final String SQL_UPDATE_EMPLOYEE_BY_UUID = "update employee set full_name = :full_name, email = :email," +
            " birthday_date = :birthday_date, hobbies = :hobbies" +
            " where uuid = :uuid;";
    private final String SQL_REMOVE_EMPLOYEE_BY_UUID = "delete from employee where uuid = :uuid;";


    public EmployeeData createEmployee(EmployeeData employeeData) throws EmployeeDuplicateException {
        try {
            namedParameterJdbcTemplate.update(SQL_INSERT_EMPLOYEE, mapToParameters(employeeData));
            return employeeData;
        } catch (DuplicateKeyException duplicateKeyException) {
            String message = "Can't create employee, because of error: " + duplicateKeyException.getCause();
            log.error(message);
            throw new EmployeeDuplicateException(message);
        }
    }

    public List<EmployeeData> findAllEmployees() {
        return namedParameterJdbcTemplate.query(SQL_FIND_ALL, employeeRowMapper);
    }

    public int updateEmployee(EmployeeData employeeData) throws EmployeeDuplicateException {
        try {
            int affectedRowsCount = namedParameterJdbcTemplate.update(SQL_UPDATE_EMPLOYEE_BY_UUID, mapToParameters(employeeData));
            return affectedRowsCount;
        } catch (DuplicateKeyException duplicateKeyException) {
            String message = "Can't create employee, because of error: " + duplicateKeyException.getCause();
            log.error(message);
            throw new EmployeeDuplicateException(message);
        }
    }

    public void removeEmployee(String employeeUuid) {
        Map<String, Object> parametersMap = new HashMap<>();
        parametersMap.put("uuid", employeeUuid);
        namedParameterJdbcTemplate.update(SQL_REMOVE_EMPLOYEE_BY_UUID, parametersMap);
    }

    @SneakyThrows
    private Map<String, Object> mapToParameters(EmployeeData employeeData) {
        Map<String, Object> map = new HashMap<>();
        map.put("uuid", employeeData.getEmployeeUuid());
        map.put("full_name", employeeData.getFullName());
        map.put("email", employeeData.getEmail());
        map.put("birthday_date", employeeData.getBirthday());
        map.put("hobbies", objectMapper.writeValueAsString(employeeData.getHobbies()));
        return map;
    }
}
