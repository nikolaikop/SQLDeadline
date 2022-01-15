package ru.netology.ru.netology.test;

import lombok.val;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.data.DBHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DBHelper.wipeEverything;
import static ru.netology.data.DataHelper.getAuthInfo;

public class PageTest {

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    @AfterAll
    static void totalTidyUp() {
        wipeEverything();
    }

    @Test
    @DisplayName("Логин с валидными данными")
    void loginWithValidData() {
        val verificationPage = new LoginPage().validLogin(getAuthInfo());
        DashboardPage dashboardPage = verificationPage.validVerify(DBHelper.getCode());
        assertEquals("Личный кабинет", dashboardPage.getHeading());
    }


}
