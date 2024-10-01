package com.doubles.selfstudy.config;

import com.doubles.selfstudy.dto.user.UserAccountDto;
import com.doubles.selfstudy.service.UserAccountService;
import com.doubles.selfstudy.utils.JwtTokenUtils;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret-key}")
    private String secretKey; // JWT 비밀 키
    @Value("${jwt.token.expired-time-ms}")
    private Long expiredTimeMs;

    private final UserAccountService userAccountService;

    // 토큰 생성
    public String createToken(String userId) {
        return JwtTokenUtils.createJwtToken(userId, secretKey, expiredTimeMs);
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        return !JwtTokenUtils.isExpired(token, secretKey);
    }

    // 토큰에서 Authentication 객체 생성
    public UsernamePasswordAuthenticationToken getAuthentication(String token) {
        Claims claims = JwtTokenUtils.extractClaims(token, secretKey);
        String userId = (String) claims.get("userId");

        UserAccountDto userAccountDto = UserAccountDto.fromEntity(userAccountService.loadUserByUserId(userId));

        UserDetails userDetails = new User(
                userAccountDto.getUserId(),
                userAccountDto.getPassword(),
                userAccountDto.getAuthorities()
        );

        return new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
    }
}
