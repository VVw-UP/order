package com.ocbc.oms.app.error.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.StringJoiner;

import static com.ocbc.oms.app.error.api.APIErrorConstant.METHOD_ARGUMENT_NOT_VALID_EXCEPTION_CODE;

@ControllerAdvice
@Slf4j
public class CustomAPIErrorHandler {

    private ResponseEntity<Object> buildResponseEntity(APIError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }


    /**
     * 处理 @valid 注解参数校验异常
     *
     * @param ex ex
     * @return return
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<Object> handleBindException(BindException ex) {
        StringJoiner stringJoiner = new StringJoiner(" and ");
        for (ObjectError error : ex.getAllErrors()) {
            stringJoiner.add(error.getDefaultMessage());
        }
        return buildResponseEntity(APIError.builder()
            .status(HttpStatus.BAD_REQUEST)
            .message(stringJoiner.toString())
            .code(METHOD_ARGUMENT_NOT_VALID_EXCEPTION_CODE)
            .build());
    }

    @ExceptionHandler(HttpMessageConversionException.class)
    public ResponseEntity<Object> httpMessageConversionException(HttpMessageConversionException ex) {
        return buildResponseEntity(APIError.builder().status(HttpStatus.BAD_REQUEST).message(ex.getMessage()).code(METHOD_ARGUMENT_NOT_VALID_EXCEPTION_CODE).build());
    }

    @ExceptionHandler(APIException.class)
    public ResponseEntity<Object> handleTradeDateValueDateCalculation(APIException ex) {
        return buildResponseEntity(APIError.builder().status(HttpStatus.BAD_REQUEST).message(ex.getMessage()).code(ex.getCode()).build());
    }


}
