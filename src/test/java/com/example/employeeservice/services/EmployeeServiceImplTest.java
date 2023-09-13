package com.example.employeeservice.services;

import com.example.employeeservice.models.EmployeeData;
import com.example.employeeservice.models.EmployeeOperationState;
import com.example.employeeservice.models.OperationNotification;
import com.example.employeeservice.repositories.EmployeeDuplicateException;
import com.example.employeeservice.repositories.EmployeeRepository;
import com.example.employeeservice.rest.models.CreateEmployeeRequest;
import com.example.employeeservice.rest.models.UpdateEmployeeRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private KafkaConnector kafkaConnector;
    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Test
    void createEmployeeWhenProcessingWasSuccessful() throws EmployeeDuplicateException {
        Date date = getCurrentDate();
        List<String> hobbies = List.of("pets", "hiking");
        CreateEmployeeRequest request = CreateEmployeeRequest.builder()
                .email("email")
                .birthday(date)
                .fullName("John Wick")
                .hobbies(hobbies)
                .build();
        EmployeeData employeeMockedData = createEmployeeData(date);

        Mockito.when(employeeRepository.createEmployee(any())).thenReturn(employeeMockedData);
        ArgumentCaptor<EmployeeData> employeeDataCaptor = ArgumentCaptor.forClass(EmployeeData.class);
        ArgumentCaptor<OperationNotification> operationNotificationCaptor = ArgumentCaptor.forClass(OperationNotification.class);

        EmployeeData result = employeeService.createEmployee(request);

        Mockito.verify(employeeRepository).createEmployee(employeeDataCaptor.capture());
        Mockito.verify(kafkaConnector).sendEmployeeOperationState(operationNotificationCaptor.capture());
        assertEquals(employeeMockedData, result);
        EmployeeData savedData = employeeDataCaptor.getValue();
        OperationNotification actualNotification = operationNotificationCaptor.getValue();

        assertAll(() -> {
            assertNotNull(savedData.getEmployeeUuid(), "Saved EmployeeUuid was differ from expected");
            assertEquals(request.getEmail(), savedData.getEmail(), "Saved Email was differ from expected");
            assertEquals(request.getFullName(), savedData.getFullName(), "Saved FullName was differ from expected");
            assertEquals(request.getBirthday(), savedData.getBirthday(), "Saved Birthday was differ from expected");
            assertArrayEquals(request.getHobbies().toArray(), savedData.getHobbies().toArray(),
                    "Saved Hobbies value was differ from expected");
        });

        assertAll(() -> {
            assertEquals(employeeMockedData.getEmployeeUuid(), actualNotification.getEmployeeUuid(),
                    "EmployeeUuid in notification message was differ from expected");
            assertNotNull(actualNotification.getOperationDate(), "OperationDate in notification message was differ from expected");
            assertEquals(EmployeeOperationState.CREATE.value, actualNotification.getEmployeeOperationState(),
                    "EmployeeOperationState in notification message was differ from expected");
        });
    }

    @Test
    void updateEmployeeWhenProcessingWasSuccessful() throws EmployeeDuplicateException {
        Date date = getCurrentDate();
        List<String> hobbies = List.of("sport", "hiking");
        String updatedEmployeeUuid = "12121";
        UpdateEmployeeRequest request = UpdateEmployeeRequest.builder()
                .email("email")
                .birthday(date)
                .fullName("John Wick")
                .hobbies(hobbies)
                .build();
        int updatedRowCount = 1;

        Mockito.when(employeeRepository.updateEmployee(any())).thenReturn(updatedRowCount);
        ArgumentCaptor<EmployeeData> employeeDataCaptor = ArgumentCaptor.forClass(EmployeeData.class);
        ArgumentCaptor<OperationNotification> operationNotificationCaptor = ArgumentCaptor.forClass(OperationNotification.class);

        employeeService.updateEmployee(updatedEmployeeUuid, request);

        Mockito.verify(employeeRepository).updateEmployee(employeeDataCaptor.capture());
        Mockito.verify(kafkaConnector).sendEmployeeOperationState(operationNotificationCaptor.capture());
        EmployeeData passedForUpdateData = employeeDataCaptor.getValue();
        OperationNotification actualNotification = operationNotificationCaptor.getValue();

        assertAll(() -> {
            assertNotNull(passedForUpdateData.getEmployeeUuid(), "Saved EmployeeUuid was differ from expected");
            assertEquals(request.getEmail(), passedForUpdateData.getEmail(), "Saved Email was differ from expected");
            assertEquals(request.getFullName(), passedForUpdateData.getFullName(), "Saved FullName was differ from expected");
            assertEquals(request.getBirthday(), passedForUpdateData.getBirthday(), "Saved Birthday was differ from expected");
            assertArrayEquals(request.getHobbies().toArray(), passedForUpdateData.getHobbies().toArray(),
                    "Saved Hobbies value was differ from expected");
        });

        assertAll(() -> {
            assertEquals(updatedEmployeeUuid, actualNotification.getEmployeeUuid(),
                    "EmployeeUuid in notification message was differ from expected");
            assertNotNull(actualNotification.getOperationDate(), "OperationDate in notification message was differ from expected");
            assertEquals(EmployeeOperationState.UPDATE.value, actualNotification.getEmployeeOperationState(),
                    "EmployeeOperationState in notification message was differ from expected");
        });
    }

    @Test
    void getAllEmployees() {
        List<EmployeeData> dataFromRepoMocked = List.of(createEmployeeData(Date.from(Instant.parse("2007-12-03T00:00:00.00Z"))),
                createEmployeeData(Date.from(Instant.parse("2007-12-03T00:00:00.00Z"))));
        Mockito.when(employeeRepository.findAllEmployees()).thenReturn(dataFromRepoMocked);

        List<EmployeeData> result = employeeService.getAllEmployees();

        Mockito.verify(employeeRepository).findAllEmployees();
        Mockito.verifyNoInteractions(kafkaConnector);
        assertEquals(dataFromRepoMocked, result);
    }

    @Test
    void removeEmployeeWhenProcessingWasSuccessful() {
        String employeeUuidForDeletion = "12121";
        ArgumentCaptor<OperationNotification> operationNotificationCaptor = ArgumentCaptor.forClass(OperationNotification.class);
        employeeService.removeEmployee(employeeUuidForDeletion);

        Mockito.verify(kafkaConnector).sendEmployeeOperationState(operationNotificationCaptor.capture());
        OperationNotification actualNotification = operationNotificationCaptor.getValue();

        Mockito.verify(employeeRepository).removeEmployee(employeeUuidForDeletion);
        assertAll(() -> {
            assertEquals(employeeUuidForDeletion, actualNotification.getEmployeeUuid(),
                    "EmployeeUuid in notification message was differ from expected");
            assertNotNull(actualNotification.getOperationDate(), "OperationDate in notification message was differ from expected");
            assertEquals(EmployeeOperationState.DELETE.value, actualNotification.getEmployeeOperationState(),
                    "EmployeeOperationState in notification message was differ from expected");
        });
    }

    @Test
    void createEmployeeWhenSavingFailedShouldNotSendNotification() throws EmployeeDuplicateException {
        Date date = getCurrentDate();
        List<String> hobbies = List.of("sport", "hiking");
        CreateEmployeeRequest request = CreateEmployeeRequest.builder()
                .email("email")
                .birthday(date)
                .fullName("John Wick")
                .hobbies(hobbies)
                .build();

        Mockito.when(employeeRepository.createEmployee(any())).thenThrow(new EmployeeDuplicateException("Duplicate!"));

        assertThrows(EmployeeDuplicateException.class, () -> employeeService.createEmployee(request));

        Mockito.verifyNoInteractions(kafkaConnector);
    }

    @Test
    void createEmployeeWhenNotifyingFailedShouldFinishWithOk() throws EmployeeDuplicateException {
        Date date = getCurrentDate();
        List<String> hobbies = List.of("sport", "hiking");
        CreateEmployeeRequest request = CreateEmployeeRequest.builder()
                .email("email")
                .birthday(date)
                .fullName("John Wick")
                .hobbies(hobbies)
                .build();
        EmployeeData employeeMockedData = createEmployeeData(date);

        Mockito.when(employeeRepository.createEmployee(any())).thenReturn(employeeMockedData);
        Mockito.doThrow(new RuntimeException()).when(kafkaConnector).sendEmployeeOperationState(any());
        EmployeeData result = employeeService.createEmployee(request);

        Mockito.verify(kafkaConnector).sendEmployeeOperationState(any());
        assertEquals(employeeMockedData, result);
    }

    private EmployeeData createEmployeeData(Date birthday) {
        String uuid = UUID.randomUUID().toString();
        List<String> hobbies = List.of("sport", "hiking");
        return EmployeeData.builder()
                .employeeUuid(uuid)
                .email("some email")
                .birthday(birthday)
                .fullName("Name " + uuid)
                .hobbies(hobbies).build();
    }

    private Date getCurrentDate() {
        return Date.from(Instant.now().truncatedTo(ChronoUnit.DAYS));
    }
}