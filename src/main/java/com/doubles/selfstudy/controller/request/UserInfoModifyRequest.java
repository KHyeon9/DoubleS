package com.doubles.selfstudy.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInfoModifyRequest {

    private String nickname;
    private String email;
    private String memo;
}
