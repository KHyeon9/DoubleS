package com.doubles.selfstudy.controller.response;

import com.doubles.selfstudy.dto.user.RoleType;
import com.doubles.selfstudy.dto.user.UserAccountDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProfileResponse {

    private String userId;
    private String nickname;
    private String email;
    private String memo;
    private RoleType roleType;

    public static ProfileResponse fromUserAccountDto(UserAccountDto dto) {
        return new ProfileResponse(dto.getUserId(), dto.getNickname(), dto.getEmail(), dto.getMemo(), dto.getRoleType());
    }
}
