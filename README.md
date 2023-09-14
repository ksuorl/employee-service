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

### Docker Compose support
This project contains a Docker Compose files:
    - `compose.yaml` - includes all services, needed in environment to start application. Will be started automatically, when application started from IDE.
    - `compose-full.yaml`- includes environment services and also application. 
By default, the version of application is 'latest'. Yes can change version by providing specific docker tag for container "ems". Docker images for "ems" should exist in local host.

### Dockerfile
You can find dockerfile in the root of project.
You should build image locally before first usage performing the command from project root dir:

"docker build -t myorg/employee-service:latest . "

or call "gradle bootBuildImage"
### Swagger Spec
http://localhost:8080/api-docs
http://localhost:9000/swagger-ui/index.html


### Integration tests
There is gradle task 'integrationTest' in project.
EmployeeServiceApplication and all related services should run, when you launch task 'integrationTest'.
You can run them easily by running a command:

`docker-compose -f compose-full.yaml up`

Run integration tests by command:
`gradle integrationTest`
