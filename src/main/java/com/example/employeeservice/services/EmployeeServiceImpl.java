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
import java.util.Date;
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
        Date operationTime = Date.from(Instant.now());
        EmployeeData employeeData = EmployeeData.builder()
                .employeeUuid(UUID.randomUUID().toString())
                .email(createEmployeeRequest.getEmail())
                .fullName(createEmployeeRequest.getFullName())
                .birthday(createEmployeeRequest.getBirthday())
                .hobbies(createEmployeeRequest.getHobbies())
                .build();
        EmployeeData result = employeeRepository.createEmployee(employeeData);
        trySendNotificationToKafka(result.getEmployeeUuid(), EmployeeOperationState.CREATE, operationTime);
        return result;
    }

    @Override
    public void updateEmployee(String employeeUuid, UpdateEmployeeRequest updateEmployeeRequest) throws EmployeeDuplicateException {
        Date operationTime = Date.from(Instant.now());
        int updatedItemsCount = employeeRepository.updateEmployee(EmployeeData.builder()
                .employeeUuid(employeeUuid)
                .email(updateEmployeeRequest.getEmail())
                .fullName(updateEmployeeRequest.getFullName())
                .birthday(updateEmployeeRequest.getBirthday())
                .hobbies(updateEmployeeRequest.getHobbies())
                .build());
        if (updatedItemsCount > 0) {
            trySendNotificationToKafka(employeeUuid, EmployeeOperationState.UPDATE, operationTime);
        }
    }

    @Override
    public List<EmployeeData> getAllEmployees() {
        return employeeRepository.findAllEmployees();
    }

    @Override
    public void removeEmployee(String employeeUuid) {
        Date operationTime = Date.from(Instant.now());
        employeeRepository.removeEmployee(employeeUuid);
        trySendNotificationToKafka(employeeUuid, EmployeeOperationState.DELETE, operationTime);
    }

    private void trySendNotificationToKafka(String employeeUuid, EmployeeOperationState operationState,
                                            Date operationTime) {
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
