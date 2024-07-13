package com.doubles.selfstudy.controller.response;

import com.doubles.selfstudy.dto.user.RoleType;
import com.doubles.selfstudy.dto.user.UserAccountDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserRegistResponse {

    private String userId;
    private String nickname;
    private RoleType roleType;

    public static UserRegistResponse fromDto(UserAccountDto dto) {
        return new UserRegistResponse(
                dto.getUserId(),
                dto.getNickname(),
                dto.getRoleType()
        );
    }
}
