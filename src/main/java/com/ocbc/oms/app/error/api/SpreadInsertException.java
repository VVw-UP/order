package com.ocbc.oms.app.error.api;

public class SpreadInsertException extends RuntimeException {

    public SpreadInsertException(String message){
        super(message);
    }

    public String getCode(){
        return APIErrorConstant.SpreadInsertOrUpdateException;
    }
}
