package ru.netology.ru.netology.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.data.DBHelper;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;
import ru.netology.page.VerificationPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PageTest {

    DataHelper user = new DataHelper("vasya", "qwerty1234");

    @Test
    @DisplayName("Логин с валидными данными")
    void loginWithValidData() {
        open("http://localhost:9999");
        LoginPage loginPage = new LoginPage();
        VerificationPage verificationPage = loginPage.validLogin(user);
        DashboardPage dashboardPage = verificationPage.validVerify(DBHelper.getVerificationCode());
        assertEquals("Личный кабинет", dashboardPage.getHeading());
    }
}
