package com.example.employeeservice.rest;

import ch.qos.logback.core.testUtil.RandomUtil;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItems;

class EmployeeControllerIntegrationTest {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://127.0.0.1";
        RestAssured.port = 9000;
    }

    @Test
    void createEmployeeWithEmptyBody() {
        given().log().ifValidationFails()
                .contentType(ContentType.JSON.toString())
                .when()
                .post("/employees")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("error", Matchers.equalTo("Bad Request"));
    }

    @Test
    void createEmployee() {
        String uniqueEmail = getUniqueEmail();
        given().log().ifValidationFails()
                .contentType(ContentType.JSON.toString())
                .body("""
                        {
                                "fullName": "RR",
                                "email": "%s",
                                "birthday": "2000-10-30",
                                "hobbies": ["e", "s"]
                        }
                        """.formatted(uniqueEmail))
                .when()
                .post("/employees")
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .body("fullName", Matchers.equalTo("RR"))
                .body("employeeUuid", Matchers.notNullValue())
                .body("birthday", Matchers.equalTo("2000-10-30"));
    }

    @Test
    void getAllEmployees() {
        //preparation
        given().log().ifValidationFails()
                .contentType(ContentType.JSON.toString())
                .body("""
                        {
                                "fullName": "name 1",
                                "email": "%s",
                                "birthday": "2000-10-30",
                                "hobbies": ["e", "s"]
                        }
                        """.formatted(getUniqueEmail()))
                .when()
                .post("/employees")
                .then()
                .statusCode(HttpStatus.SC_CREATED);

        RestAssured.given().log().ifValidationFails()
                .contentType(ContentType.JSON.toString())
                .body("""
                        {
                                "fullName": "name 2",
                                "email": "%s",
                                "birthday": "2000-10-30",
                                "hobbies": ["e", "s"]
                        }
                        """.formatted(getUniqueEmail()))
                .when()
                .post("/employees")
                .then()
                .statusCode(HttpStatus.SC_CREATED);

        //test
        RestAssured.given().log().ifValidationFails()
                .contentType(ContentType.JSON.toString())
                .when()
                .get("/employees")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("fullName", hasItems("name 1", "name 2"));
    }

    //TODO
//
//    @Test
//    void updateEmployee() {
//    }
//
//    @Test
//    void removeEmployee() {
//    }

    private String getUniqueEmail() {
        return RandomUtil.getPositiveInt() + "@example.com";
    }
}