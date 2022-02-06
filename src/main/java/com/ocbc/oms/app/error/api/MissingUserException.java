package com.ocbc.oms.app.error.api;

public class MissingUserException extends APIException {
    public MissingUserException(String message, String code) {super(message, code);}
}
