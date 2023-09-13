FROM amazoncorretto:17-alpine3.18
VOLUME /tmp
COPY build/libs/employee-service-*-SNAPSHOT.jar ems-app.jar
ENTRYPOINT ["java","-jar","/ems-app.jar"]