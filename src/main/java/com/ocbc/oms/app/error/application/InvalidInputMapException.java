package com.ocbc.oms.app.error.application;

public class InvalidInputMapException extends ApplicationException {
    public InvalidInputMapException(String message, String code) {
        super(message,code);
    }
}
