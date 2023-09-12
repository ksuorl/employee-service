package com.example.employeeservice.config;

import com.example.employeeservice.repositories.EmployeeRepository;
import com.example.employeeservice.repositories.EmployeeRowMapper;
import com.example.employeeservice.services.EmployeeService;
import com.example.employeeservice.services.EmployeeServiceImpl;
import com.example.employeeservice.services.KafkaConnector;
import com.example.employeeservice.services.KafkaConnectorImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class AppConfig {

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(@Qualifier("employeeDataSource") DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    public EmployeeRepository employeeRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                                 EmployeeRowMapper employeeRowMapper) {
        return new EmployeeRepository(namedParameterJdbcTemplate, employeeRowMapper);
    }

    @Bean
    public EmployeeService employeeService(EmployeeRepository employeeRepository,
                                           KafkaConnector kafkaConnector) {
        return new EmployeeServiceImpl(employeeRepository, kafkaConnector);
    }

    @Bean
    public EmployeeRowMapper employeeRowMapper() {
        return new EmployeeRowMapper();
    }




//    @Bean
//    public SpringLiquibase liquibase(@Qualifier("employeeDataSource") DataSource dataSource,
//                                     LiquibaseProperties liquibaseProperties) {
//
//        // Use liquibase.integration.spring.SpringLiquibase if you don't want Liquibase to start asynchronously
//        SpringLiquibase liquibase = new SpringLiquibase();
//        liquibase.setDataSource(dataSource);
//        liquibase.setChangeLog(liquibaseProperties.getChangeLog());
//        liquibase.setContexts(liquibaseProperties.getContexts());
//        liquibase.setDefaultSchema(liquibaseProperties.getDefaultSchema());
//        liquibase.setDropFirst(liquibaseProperties.isDropFirst());
//        return liquibase;
//    }

//    public DataSource employeeDataSource(String employeeDbUrl, String username,
//                                         String password) {
//        final String driverClassName = "org.h2.Driver";
//        final String jdbcUrl = "jdbc:h2:mem:test";
//        return DataSourceBuilder.create()
//                .driverClassName(driverClassName)
//                .url(jdbcUrl)
//                .username(username)
//                .password(password)
//                .build();
//    }
}
