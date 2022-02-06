package com.ocbc.oms.app.error.api;

public class MissingCurrencyPairException extends APIException{
    public MissingCurrencyPairException(String message, String code) {super(message, code);}
}
