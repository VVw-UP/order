package com.ocbc.oms.app.error.application;

import lombok.Data;

@Data
public class ApplicationException extends RuntimeException{
    private String code;
    private String message;

    public ApplicationException(String message, String code) {
        this.message = message;
        this.code = code;
    }
}
