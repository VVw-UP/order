package com.ocbc.oms.app.error.api;

public class MissingPriceException extends APIException{
    public MissingPriceException(String message, String code){super(message, code);}
}
