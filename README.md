# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.1.3/gradle-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.1.3/gradle-plugin/reference/html/#build-image)
* [Docker Compose Support](https://docs.spring.io/spring-boot/docs/3.1.3/reference/htmlsingle/index.html#features.docker-compose)
* [Spring Security](https://docs.spring.io/spring-boot/docs/3.1.3/reference/htmlsingle/index.html#web.security)
* [Liquibase Migration](https://docs.spring.io/spring-boot/docs/3.1.3/reference/htmlsingle/index.html#howto.data-initialization.migration-tool.liquibase)
* [JDBC API](https://docs.spring.io/spring-boot/docs/3.1.3/reference/htmlsingle/index.html#data.sql)

### Application description
The application provides API to:
1. Create an employee with the following properties
   - Uuid (generated automatically)
   - E-mail
   - Full name (first and last name)
   - Birthday (format YYYY-MM-DD)
   - List of hobbies (for example, "soccer", "music", etc)
2. Get a list of all employees (response in JSON Array format)
3. Get a specific employee by uuid (response in JSON Object format)
4. Update an employee
5. Delete an employee
6. Whenever an employee is created, updated or deleted, an event related to this
   action will be pushed in the Kafka message broker.
   The inability to send a notification does not affect the result of the operation.

Notification message includes the following properties:
- employeeUuid
- employeeOperationState (enum value. possible values: ["Create", "Update", "Delete"])
- operationDate

### Swagger Spec
 - http://localhost:8080/api-docs
 - http://localhost:9000/swagger-ui/index.html

### Docker Compose support
This project contains a Docker Compose files:
- `compose.yaml` - includes all services, needed in environment to start the application. Will be started automatically, when the application starts from IDE.
- `compose-full.yaml`- includes environment services and also the application.
By default, the version of the application is 'latest'. Yes can change the version by providing a specific docker tag for container "ems". Docker images for "ems" should exist in the local host.

### Dockerfile
You can find the Dockerfile at the root of the project.
You can build an image locally by performing the command from project root dir:

"docker build -t myorg/employee-service:latest . "

or call "gradle bootBuildImage"

### Integration tests
There is a gradle task 'integrationTest' in the project.
EmployeeServiceApplication and all related services should run, when you launch task 'integrationTest'.
You can run them easily by running a command:

`docker-compose -f compose-full.yaml up`

Run integration tests by command:
`gradle integrationTest`