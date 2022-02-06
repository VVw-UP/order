package com.ocbc.oms.app.error.api;

public class InvalidCurrencyException extends APIException{
    public InvalidCurrencyException(String message, String code) {super(message, code);}
}
