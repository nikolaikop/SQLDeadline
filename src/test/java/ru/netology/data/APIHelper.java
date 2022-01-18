package ru.netology.data;

import com.google.common.base.CharMatcher;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import static io.restassured.RestAssured.given;

public class APIHelper {

    private APIHelper() {
    }

    private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    public static void logIn(DataHelper.AuthInfo authInfo) {
        given()
                .spec(requestSpec)
                .body(DataHelper.getAuthInfo())
                .when()
                .post("/api/auth")
                .then()
                .statusCode(200);
    }

    public static String verification(String login, String code) {
        return given()
                .spec(requestSpec)
                .body(DataHelper.getVerificationInfoFor(DataHelper.getAuthInfo(), code))
                .when()
                .post("/api/auth/verification")
                .then()
                .statusCode(200)
                .extract()
                .path("token");
    }

    public static String getCards(String token) {
        String body = given()
                .baseUri("http://localhost:9999/api")
                .header(
                        "Authorization", "Bearer " + token)
                .when()
                .get("/cards")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body(matchesJsonSchemaInClasspath("APIschema.json"))
                .extract()
                .response()
                .getBody()
                .asString();
        return body;
    }

    public static int cardTwoBalanceApi(String token) {
        return Integer.parseInt(CharMatcher.inRange('0', '9').retainFrom(getCards(token).substring(100, 120)));
    }

    public static int cardOneBalanceApi(String token) {
        return Integer.parseInt(CharMatcher.inRange('0', '9').retainFrom(getCards(token).substring(220)));
    }

    public static void makeTransaction(String token, DataHelper.TransactionProperties props, int statusCode) {

        given()
                .baseUri("http://localhost:9999/api")
                .headers(
                        "Authorization", "Bearer " + token,
                        "Content-Type", ContentType.JSON)
                .body(DataHelper.Transaction.makeTransaction(props))
                .when()
                .post("/transfer")
                .then()
                .statusCode(statusCode);
    }

}
