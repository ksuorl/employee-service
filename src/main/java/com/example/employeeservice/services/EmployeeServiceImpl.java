package com.example.employeeservice.services;

import com.example.employeeservice.models.EmployeeData;
import com.example.employeeservice.models.EmployeeOperationState;
import com.example.employeeservice.models.OperationNotification;
import com.example.employeeservice.repositories.EmployeeDuplicateException;
import com.example.employeeservice.repositories.EmployeeRepository;
import com.example.employeeservice.rest.models.CreateEmployeeRequest;
import com.example.employeeservice.rest.models.UpdateEmployeeRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private EmployeeRepository employeeRepository;
    private KafkaConnector kafkaConnector;

    @Override
    public EmployeeData createEmployee(CreateEmployeeRequest createEmployeeRequest) throws EmployeeDuplicateException {
        //TODO generator
        Instant operationTime = Instant.now();
        EmployeeData employeeData = EmployeeData.builder()
                .employeeUuid(UUID.randomUUID().toString())
                .email(createEmployeeRequest.getEmail())
                .fullName(createEmployeeRequest.getFullName())
                .birthday(createEmployeeRequest.getBirthday())
                .build();
        //TODO insert hobbies
        EmployeeData result = employeeRepository.createEmployee(employeeData);
        sendNotificationToKafka(result.getEmployeeUuid(), EmployeeOperationState.CREATE, operationTime);
        return result;
    }

    @Override
    public EmployeeData updateEmployee(String employeeUuid, UpdateEmployeeRequest updateEmployeeRequest) throws EmployeeDuplicateException {
        Instant operationTime = Instant.now();
        EmployeeData result = employeeRepository.updateEmployee(EmployeeData.builder()
                .employeeUuid(employeeUuid)
                .email(updateEmployeeRequest.getEmail())
                .fullName(updateEmployeeRequest.getFullName())
                .birthday(updateEmployeeRequest.getBirthday())
                .build());
        sendNotificationToKafka(result.getEmployeeUuid(), EmployeeOperationState.UPDATE, operationTime);
        return result;
    }

    @Override
    public List<EmployeeData> getAllEmployees() {
        return employeeRepository.findAllEmployees();
    }

    @Override
    public void removeEmployee(String employeeUuid) {
        Instant operationTime = Instant.now();
        employeeRepository.removeEmployee(employeeUuid);
        sendNotificationToKafka(employeeUuid, EmployeeOperationState.DELETE, operationTime);
    }

    private void sendNotificationToKafka(String employeeUuid, EmployeeOperationState operationState,
                                         Instant operationTime) {
        try {
            OperationNotification state = OperationNotification.builder()
                    .employeeOperationState(operationState.value)
                    .employeeUuid(employeeUuid)
                    .operationDate(operationTime).build();
            kafkaConnector.sendEmployeeOperationState(state);
        } catch (Exception e) {
            log.error("Couldn't send employeeOperationState to Kafka: " + e.getMessage());
        }
    }
}
