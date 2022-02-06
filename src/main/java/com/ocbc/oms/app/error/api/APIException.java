package com.ocbc.oms.app.error.api;

import lombok.Data;

@Data
public class APIException extends RuntimeException {
    private String message;
    private String code;

    public APIException(String message, String code) {
        this.message = message;
        this.code = code;
    }
}
