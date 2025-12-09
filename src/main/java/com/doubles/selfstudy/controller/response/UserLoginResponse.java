package com.doubles.selfstudy.controller.response;

import com.doubles.selfstudy.dto.user.TokenDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserLoginResponse {
    // 토큰 담는 response
    private String accessToken; // access token
    private String refreshToken; // refresh Token

    public static UserLoginResponse ofDto(TokenDto tokenDto) {
        return new UserLoginResponse(tokenDto.getAccessToken(), tokenDto.getRefreshToken());
    }
}
