package ru.netology.data;

import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBHelper {

    private final static Connection connect = establishConnection();
    private final static QueryRunner runner = new QueryRunner();

    private DBHelper() {
    }

    @SneakyThrows
    private static Connection establishConnection() {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/app", "app", "pass");
    }

    @SneakyThrows
    public static String getCode() {
        return runner.query(connect, "SELECT code FROM auth_codes", new ScalarHandler<>());
    }

    @SneakyThrows
    public static void wipeCodes() {
        runner.execute(connect, "TRUNCATE auth_codes");
    }

    @SneakyThrows
    public static void wipeEverything() {
        runner.execute(connect, "TRUNCATE auth_codes");
        runner.execute(connect, "TRUNCATE cards;");
        runner.execute(connect, "TRUNCATE card_transactions;");
        runner.execute(connect, "DELETE FROM users WHERE status LIKE '%ive';");
    }

}
