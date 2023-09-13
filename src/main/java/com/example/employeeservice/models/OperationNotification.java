package com.example.employeeservice.models;

import lombok.Builder;
import lombok.Value;

import java.util.Date;

@Value
@Builder
public class OperationNotification {
    String employeeUuid;
    String employeeOperationState;
    Date operationDate;
}
