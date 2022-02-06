package com.ocbc.oms.app.error.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public class APIError {
    private HttpStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private String message;
    private String code;

    @Builder
    public APIError(HttpStatus status, String message, String code) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.message = message;
        this.code = code;
    }
}
