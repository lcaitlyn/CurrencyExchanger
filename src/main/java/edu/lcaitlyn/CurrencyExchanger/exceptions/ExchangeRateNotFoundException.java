package edu.lcaitlyn.CurrencyExchanger.exceptions;

public class ExchangeRateNotFoundException extends RuntimeException {
    public static final String message = "{\n" +
            "    \"message\": \"Валютный курс не найден\"\n" +
            "}";

    public ExchangeRateNotFoundException() {
    }

    @Override
    public String getMessage() {
        return message;
    }
}
