package com.doubles.selfstudy.controller.response;

import com.doubles.selfstudy.dto.user.UserAccountDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponse {

    private String userId;

    public static UserResponse fromUserAccountDto(UserAccountDto dto) {
        return new UserResponse(
                dto.getUserId()
        );
    }
}
