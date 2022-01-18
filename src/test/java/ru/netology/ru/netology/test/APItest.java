package ru.netology.ru.netology.test;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.APIHelper.*;
import static ru.netology.data.DBHelper.*;
import static ru.netology.data.DataHelper.TransactionProperties.fromFirstToSecond;
import static ru.netology.data.DataHelper.getAuthInfo;

public class APItest {
    static final int transactionAmount = 5000;

    @AfterAll
    static void wipeIt() {
        wipeEverything();
    }

    @Test
    void shouldMakeTransfer() {

        logIn(getAuthInfo());
        final String token = verification(getAuthInfo().getLogin(), getCode());
        final int begBalance1 = cardOneBalanceApi(token);
        final int begBalance2 = cardTwoBalanceApi(token);
        assertEquals(begBalance1, cardOneBalanceDb() / 100);
        assertEquals(begBalance2, cardTwoBalanceDb() / 100);
        makeTransaction(token, fromFirstToSecond(transactionAmount), 200);
        assertEquals(begBalance1 - transactionAmount, cardOneBalanceApi(token));
        assertEquals(begBalance2 + transactionAmount, cardTwoBalanceApi(token));
    }
}
