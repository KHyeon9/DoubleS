package com.doubles.selfstudy.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    DUPLICATED_USER_ID(HttpStatus.CONFLICT, "User ID is duplicated"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "Password is invalid"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "token is invalid"),
    INVALID_PERMISSION(HttpStatus.UNAUTHORIZED, "Permission is invalid"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not founded"),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "Post not founded"),
    ALREADY_LIKED(HttpStatus.CONFLICT, "User already liked the post"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "Permission is invalid"),
    ;

    private HttpStatus status;
    private String message;
}
