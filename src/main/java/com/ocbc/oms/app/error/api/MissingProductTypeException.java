package com.ocbc.oms.app.error.api;

public class MissingProductTypeException extends APIException {
    public MissingProductTypeException(String message,String code) {
        super(message, code);
    }
}
