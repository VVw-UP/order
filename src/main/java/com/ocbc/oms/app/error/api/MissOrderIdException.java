package com.ocbc.oms.app.error.api;

public class MissOrderIdException extends APIException {
    public MissOrderIdException(String message, String code) {
        super(message, code);
    }
}
