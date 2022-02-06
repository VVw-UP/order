package com.ocbc.oms.app.error.api;

public class MissingSearchParamException extends APIException {
    public MissingSearchParamException(String message,String code) {
        super(message, code);
    }
}
