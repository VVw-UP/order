package com.ocbc.oms.app.error.api;

public class InvalidUserException extends APIException{
    public InvalidUserException(String message, String code) {super(message, code);}
}
