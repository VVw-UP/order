package com.ocbc.oms.app.error.api;

public class MissingChannelCodeException extends APIException{
    public MissingChannelCodeException(String message, String code){super(message, code);}
}
