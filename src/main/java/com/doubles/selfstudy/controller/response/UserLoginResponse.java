package com.doubles.selfstudy.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserLoginResponse {
    //토큰 담는 response
    private String token;
}
