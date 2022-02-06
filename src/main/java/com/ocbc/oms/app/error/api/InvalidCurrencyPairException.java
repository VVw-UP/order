package com.ocbc.oms.app.error.api;

public class InvalidCurrencyPairException extends APIException{
    public InvalidCurrencyPairException(String message, String code) {super(message, code);}
}
