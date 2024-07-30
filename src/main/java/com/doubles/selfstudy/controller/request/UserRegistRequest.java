package com.doubles.selfstudy.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserRegistRequest {

    private String userId;
    private String nickname;
    private String email;
    private String password;
}
