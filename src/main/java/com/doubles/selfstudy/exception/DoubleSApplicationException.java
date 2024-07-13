package com.doubles.selfstudy.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DoubleSApplicationException extends RuntimeException{

    private ErrorCode errorCode;
    private String message;

    // 메세지가 필요없는 경우 메소드
    public DoubleSApplicationException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.message = null;
    }

    @Override
    public String getMessage() {
        if (message == null) {
            return errorCode.getMessage();
        }

        return String.format("%s. %s", errorCode.getMessage(), message);
    }
}
