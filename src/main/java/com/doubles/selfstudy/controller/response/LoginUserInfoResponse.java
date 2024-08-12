package com.doubles.selfstudy.controller.response;

import com.doubles.selfstudy.dto.user.RoleType;
import com.doubles.selfstudy.dto.user.UserAccountDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginUserInfoResponse {

    private String userId;
    private String nickname;
    private RoleType roleType;

    public static LoginUserInfoResponse fromUserAccountDto(UserAccountDto dto) {
        return new LoginUserInfoResponse(dto.getUserId(), dto.getNickname(), dto.getRoleType());
    }
}
