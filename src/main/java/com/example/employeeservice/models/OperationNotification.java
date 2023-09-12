package com.example.employeeservice.models;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder
public class OperationNotification {
    String employeeUuid;
    String employeeOperationState;
    Instant operationDate;
}
