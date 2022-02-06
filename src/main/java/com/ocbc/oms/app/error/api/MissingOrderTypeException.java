package com.ocbc.oms.app.error.api;

public class MissingOrderTypeException extends APIException {
    public MissingOrderTypeException(String message, String code) {super(message,code);}
}
