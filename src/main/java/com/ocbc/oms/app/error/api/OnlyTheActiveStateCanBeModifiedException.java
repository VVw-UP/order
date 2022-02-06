package com.ocbc.oms.app.error.api;

public class OnlyTheActiveStateCanBeModifiedException extends APIException {
    public OnlyTheActiveStateCanBeModifiedException(String message, String code) {
        super(message, code);
    }
}
