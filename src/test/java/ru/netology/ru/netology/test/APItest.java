package ru.netology.ru.netology.test;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.val;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.junit.jupiter.api.Test;
import ru.netology.data.CardData;
import ru.netology.data.DataHelper;

import java.sql.DriverManager;
import java.sql.SQLException;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class APItest {

    private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();
    int begBalance1;
    int begBalance2;
    int sum = 5000;

    @Test
    void shouldMakeTransfer() throws SQLException {
        given()
                .spec(requestSpec)
                .body(DataHelper.getAuthInfo())
                .when()
                .post("/api/auth")
                .then()
                .statusCode(200);

        val codeSQL = "SELECT code FROM auth_codes WHERE created = (SELECT max(created) FROM auth_codes);";
        val runner = new QueryRunner();

        try (
                val conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "app", "pass"
                );
        ) {
            val code = runner.query(conn, codeSQL, new ScalarHandler<>());
            System.out.println(code);

            String token =
                    given()
                            .spec(requestSpec)
                            .body(DataHelper.getVerificationInfoFor(DataHelper.getAuthInfo(), (String) code))
                            .when()
                            .post("/api/auth/verification")
                            .then()
                            .statusCode(200)
                            .extract()
                            .path("token");
            System.out.println(token);

            CardData[] cards =
                    given()
                            .spec(requestSpec)
                            .header("Authorization", "Bearer "+ token)
                            .when()
                            .get("/api/cards")
                            .then()
                            .statusCode(200)
                            .extract()
                            .as(CardData[].class);

            System.out.println(cards[0].getBalance());
            System.out.println(cards[1].getBalance());
            begBalance1 = Integer.parseInt(cards[0].getBalance());
            begBalance2 = Integer.parseInt(cards[1].getBalance());

            given()
                    .spec(requestSpec)
                    .header("Authorization", "Bearer "+ token)
                    .body(DataHelper.getTransaction("5559 0000 0000 0002", "5559 0000 0000 0001", sum))
                    .when()
                    .post("/api/transfer")
                    .then()
                    .statusCode(200);

            CardData[] cards2 =
                    given()
                            .spec(requestSpec)
                            .header("Authorization", "Bearer "+ token)
                            .when()
                            .get("/api/cards")
                            .then()
                            .statusCode(200)
                            .extract()
                            .as(CardData[].class);

            System.out.println(cards2[0].getBalance());
            System.out.println(cards2[1].getBalance());
            int endBalance1 = Integer.parseInt(cards2[0].getBalance());
            int endBalance2 = Integer.parseInt(cards2[1].getBalance());

            assertEquals(begBalance1 - sum, endBalance1);
            assertEquals(begBalance2 + sum, endBalance2);
        }
    }
}
