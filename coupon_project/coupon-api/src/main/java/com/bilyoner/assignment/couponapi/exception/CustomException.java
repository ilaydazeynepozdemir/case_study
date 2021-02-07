package com.bilyoner.assignment.couponapi.exception;

public class CustomException extends Exception {
    private ErrorCodeEnum errorCodeEnum;

    public CustomException(ErrorCodeEnum errorCode) {
        this.errorCodeEnum = errorCode;
    }

}
