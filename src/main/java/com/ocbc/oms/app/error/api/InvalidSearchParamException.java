package com.ocbc.oms.app.error.api;

public class InvalidSearchParamException extends APIException {
    public InvalidSearchParamException(String message,String code) {
        super(message, code);
    }
}
