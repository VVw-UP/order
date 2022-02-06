package com.ocbc.oms.app.error.api;

public class InvalidProductTypeException extends APIException {
    public InvalidProductTypeException(String message, String code) { super(message, code); }
}
