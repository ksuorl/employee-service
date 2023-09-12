package com.example.employeeservice.services;

import com.example.employeeservice.models.OperationNotification;

public interface KafkaConnector {
    void sendEmployeeOperationState(OperationNotification message);
}
