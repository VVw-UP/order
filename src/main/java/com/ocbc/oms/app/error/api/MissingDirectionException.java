package com.ocbc.oms.app.error.api;

public class MissingDirectionException extends APIException {
    public MissingDirectionException(String message, String code) {
        super(message, code);
    }
}
