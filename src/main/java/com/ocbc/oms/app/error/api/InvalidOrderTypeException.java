package com.ocbc.oms.app.error.api;

public class InvalidOrderTypeException extends APIException {
    public InvalidOrderTypeException(String message,String code) {super(message,code);}
}
