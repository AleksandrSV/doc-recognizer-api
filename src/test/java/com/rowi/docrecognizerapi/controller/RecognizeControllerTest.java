package com.rowi.docrecognizerapi.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.rowi.docrecognizerapi.model.Doc;
import com.rowi.docrecognizerapi.model.Passport;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.response.ValidatableResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RequiredArgsConstructor
public class RecognizeControllerTest {

    @LocalServerPort
    private Integer port;

    static Doc doc;
    static Passport passport;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:15-alpine"
    );

    @BeforeAll
    static void beforeAll() {
        postgres.start();
        doc = new Doc();
        passport = new Passport();
        doc.setPassport(passport);
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
    }

    @Test
    void testJson() {
        given()
                .contentType(ContentType.TEXT)
                .when()
                .get("/recognize/test_json")
                .andReturn()
                .then()
                .statusCode(200);
    }


    @Test
    void getRecPass() {
        String token = getToken();

        given().with().auth().oauth2(token)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .get("/recognize/passport/c8ed6b83-7cc3-46c0-91b9-a5969817a139")
                .andReturn()
                .then()
                .statusCode(200);
    }

    @Test
    void postRecPass() {
        String token = getToken();

        String body = "{\n" +
                "  \"fileId\": \"c8ed6b83-7cc3-46c0-91b9-a5969817a159\",\n" +
                "  \"orderId\": 0,\n" +
                "  \"globalCompanyId\": 0,\n" +
                "  \"globalPersonId\": 0\n" +
                "}";

        given().with().auth().oauth2(token)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/recognize/passport")
                .andReturn()
                .then()
                .statusCode(200);
    }

    @Test
    void deleteRecPass() {
        String token = getToken();

        given().with().auth().oauth2(token)
                .contentType(ContentType.JSON)
                .when()
                .delete("/recognize/passport/c8ed6b83-7cc3-46c0-91b9-a5969817a139")
                .andReturn()
                .then()
                .statusCode(200);
    }

    @SneakyThrows
    @Test
    void patchRecPass() {
        String token = getToken();

        ObjectMapper objectMapper = new ObjectMapper();
        String objJackson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(doc);

        given().with().auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(objJackson)
                .when()
                .patch("/recognize/passport/c8ed6b83-7cc3-46c0-91b9-a5969817a139")
                .andReturn()
                .then()
                .statusCode(200);
    }

    @SneakyThrows
    String getToken() {
        RestAssured.baseURI = "https://keycloak.yamakassi.ru";

        Response response = given().with().auth().preemptive()
                .basic("qwert@qwert.qwert", "qwert")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .formParam("grant_type", "password")
                .formParam("client_id", "test")
                .formParam("client_secret", "ZoHLbQVc9gSdd5e8D6akSDprTEFYg2wo")
                .formParam("username", "qwert@qwert.qwert")
                .formParam("password", "qwert")
                .when()
                .post("/auth/realms/test/protocol/openid-connect/token");

        String responseBody = response.getBody().asString();
        setUp();
        return new org.json.JSONObject(responseBody).getString("access_token");
    }

    @Test
    void testToken() {
        System.out.println(getToken());
    }
}
