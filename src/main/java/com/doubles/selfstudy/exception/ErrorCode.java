package com.doubles.selfstudy.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    DUPLICATED_USER_ID(HttpStatus.CONFLICT, "User ID is duplicated"),
    DUPLICATED_STUDY_GROUP(HttpStatus.CONFLICT, "Study Group is duplicated"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "Password is invalid"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "token is invalid"),
    INVALID_PERMISSION(HttpStatus.UNAUTHORIZED, "Permission is invalid"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not founded"),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "Post not founded"),
    TODO_NOT_FOUND(HttpStatus.NOT_FOUND, "Todo not founded"),
    CHAT_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "Chat room not founded"),
    USER_STUDY_GROUP_NOT_FOUND(HttpStatus.NOT_FOUND, "User Study Group not founded"),
    STUDY_GROUP_NOT_FOUND(HttpStatus.NOT_FOUND, "Study Group not founded"),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "Comment not founded"),
    ALREADY_LIKED(HttpStatus.CONFLICT, "User already liked the post"),
    ALREADY_STUDY_GROUP_INVITE(HttpStatus.CONFLICT, "User already study Group invite"),
    CHAT_ROOM_IS_EXIST(HttpStatus.CONFLICT, "Chat room is exist"),
    STUDY_GROUP_FULL(HttpStatus.UNPROCESSABLE_ENTITY, "Study Group is full"),
    LEADER_NOT_EXIT(HttpStatus.UNPROCESSABLE_ENTITY, "Leader not exit"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "Permission is invalid"),
    ALARM_CONNECT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Connecting alarm occurs error")
    ;

    private HttpStatus status;
    private String message;
}
