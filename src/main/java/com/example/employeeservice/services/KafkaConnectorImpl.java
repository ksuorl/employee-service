package com.example.employeeservice.services;

import com.example.employeeservice.models.OperationNotification;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConnectorImpl implements KafkaConnector {
    @Value("${ems.kafka.topic}")
    private String employeeTaskTopicName;

    private final KafkaTemplate<String, OperationNotification> kafkaTemplate;

    @SneakyThrows
    @Override
    public void sendEmployeeOperationState(OperationNotification message) {
        log.debug("Payload sent: {}", message.toString());
        kafkaTemplate.send(employeeTaskTopicName, message);
    }
}
