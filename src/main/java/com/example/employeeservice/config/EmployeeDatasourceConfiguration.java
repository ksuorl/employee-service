package com.example.employeeservice.config;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
@ConfigurationProperties("datasource.employee")
public class EmployeeDatasourceConfiguration {
    @NotNull
    private String username;
    @NotNull
    private String password;

    @NotNull
    private String url;

    private String driverClassName;


    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Bean
    DataSource employeeDataSource() throws SQLException {
        return DataSourceBuilder
                .create()
                .url(this.url)
                .username(this.username)
                .password(this.password)
                .driverClassName(this.driverClassName)
                .build();
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }
}
