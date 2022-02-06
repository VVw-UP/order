package com.ocbc.oms.app.error.api;

public class MissingCurrencyException extends APIException{
    public MissingCurrencyException(String message,String code) {super(message, code);}
}
