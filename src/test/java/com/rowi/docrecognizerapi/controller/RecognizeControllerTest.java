package com.rowi.docrecognizerapi.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.rowi.docrecognizerapi.model.Doc;
import com.rowi.docrecognizerapi.model.Passport;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${services.tests-env.username}")
    private String username;
    @Value("${services.tests-env.password}")
    private String password;
    @Value("${services.tests-env.client_id}")
    private String client_id;
    @Value("${services.tests-env.client_secret}")
    private String client_secret;

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
        doc.setFileId(UUID.fromString("c35544bb-243f-4681-9787-4fa6e3c3866c"));
        doc.setOrderId(0L);
        doc.setGlobalPersonId(0L);
        doc.setGlobalCompanyId(0L);
        doc.setDeleted(false);
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
        String token = getToken();

        given().with().auth().oauth2(token)
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
                .get("/recognize/passport/?orderId=123&page=0&size=3")
                .andReturn()
                .then()
                .statusCode(200);
    }

    @Test
    void postRecPass() {
        String token = getToken();

        String body = "{\n" +
                "  \"fileId\": \"c8ed6b83-7cc3-46c0-91b9-a5969817a159\",\n" +
                "  \"orderId\": 123,\n" +
                "  \"globalCompanyId\": 456,\n" +
                "  \"globalPersonId\": 12\n" +
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
                .delete("/recognize/passport/c35544bb-243f-4681-9787-4fa6e3c3866c")
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
                .patch("/recognize/passport/c35544bb-243f-4681-9787-4fa6e3c3866c")
                .andReturn()
                .then()
                .statusCode(200);
    }

    @SneakyThrows
    String getToken() {
        RestAssured.baseURI = "https://keycloak.yamakassi.ru";

        Response response = given().with().auth().preemptive()
                .basic(username, password)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .formParam("grant_type", "password")
                .formParam("client_id", client_id)
                .formParam("client_secret", client_secret)
                .formParam("username", username)
                .formParam("password", password)
                .when()
                .post("/realms/test/protocol/openid-connect/token");

        String responseBody = response.getBody().asString();
        setUp();
        return new org.json.JSONObject(responseBody).getString("access_token");
    }

    @Test
    void testToken() {
        System.out.println(getToken());
    }
}
