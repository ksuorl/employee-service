package com.example.employeeservice.config;

import com.example.employeeservice.repositories.EmployeeRepository;
import com.example.employeeservice.repositories.EmployeeRowMapper;
import com.example.employeeservice.services.EmployeeService;
import com.example.employeeservice.services.EmployeeServiceImpl;
import com.example.employeeservice.services.KafkaConnector;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import javax.sql.DataSource;

@EnableKafka
@Configuration
public class AppConfig {

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(@Qualifier("employeeDataSource") DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    public EmployeeRepository employeeRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                                 EmployeeRowMapper employeeRowMapper,
                                                 ObjectMapper objectMapper) {
        return new EmployeeRepository(namedParameterJdbcTemplate, employeeRowMapper, objectMapper);
    }

    @Bean
    public EmployeeService employeeService(EmployeeRepository employeeRepository,
                                           KafkaConnector kafkaConnector) {
        return new EmployeeServiceImpl(employeeRepository, kafkaConnector);
    }

    @Bean
    public EmployeeRowMapper employeeRowMapper(ObjectMapper objectMapper) {
        return new EmployeeRowMapper(objectMapper);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate(ProducerFactory<String, Object> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
