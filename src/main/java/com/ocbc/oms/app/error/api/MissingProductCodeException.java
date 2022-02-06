package com.ocbc.oms.app.error.api;

public class MissingProductCodeException extends APIException {
    public MissingProductCodeException(String message, String code) {super(message, code);}
}
