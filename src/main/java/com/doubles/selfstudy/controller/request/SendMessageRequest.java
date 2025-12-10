package com.doubles.selfstudy.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SendMessageRequest {

    private Long chatRoomId;
    private String message;
}
