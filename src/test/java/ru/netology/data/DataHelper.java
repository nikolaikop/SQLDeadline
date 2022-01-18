package ru.netology.data;

import com.google.gson.Gson;
import lombok.Data;
import lombok.Value;

@Data
public class DataHelper {

    private static class CardNumber {
        static final String cardOne = "5559 0000 0000 0001";
        static final String cardTwo = "5559 0000 0000 0002";
    }

    private DataHelper() {
    }

    @Value
    public static class AuthInfo {
        private String login;
        private String password;
    }

    public static AuthInfo getAuthInfo() {
        return new AuthInfo("vasya", "qwerty123");
    }

    @Value
    public static class Verification {
        private String login;
        private String code;
    }

    public static Verification getVerificationInfoFor(AuthInfo authInfo, String code) {
        return new Verification(authInfo.getLogin(), code);
    }

    @Value
    public static class Transaction {
        private String from;
        private String to;
        private String amount;

        public static String makeTransaction(TransactionProperties props) {
            return (new Gson().toJson(new Transaction(props.sourceCard, props.targetCard, props.amount)));
        }
    }

    @Value
    public static class TransactionProperties {
        String sourceCard;
        String targetCard;
        String amount;

        public static TransactionProperties fromFirstToSecond(int amount) {
            return new TransactionProperties(CardNumber.cardOne, CardNumber.cardTwo, Integer.toString(amount));
        }

    }
}
