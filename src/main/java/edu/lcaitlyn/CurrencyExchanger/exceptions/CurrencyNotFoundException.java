package edu.lcaitlyn.CurrencyExchanger.exceptions;

//        {
//            "message": "Валюта не найдена"
//        }
public class CurrencyNotFoundException extends RuntimeException{
    public static final String message = "{\n" +
            "    \"message\": \"Валюта не найдена\"\n" +
            "}";

    public CurrencyNotFoundException() {
        System.err.println("{\n" +
                "    \"message\": \"Валюта не найдена\"\n" +
                "}");
    }
}
