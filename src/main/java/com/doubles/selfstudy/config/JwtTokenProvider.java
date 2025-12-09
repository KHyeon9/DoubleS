package com.doubles.selfstudy.config;

import com.doubles.selfstudy.dto.user.UserAccountDto;
import com.doubles.selfstudy.exception.DoubleSApplicationException;
import com.doubles.selfstudy.exception.ErrorCode;
import com.doubles.selfstudy.utils.JwtTokenUtils;
import com.doubles.selfstudy.utils.ServiceUtils;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret-key}")
    private String secretKey; // JWT 비밀 키
    // Access Token의 만료 시간
    @Value("${jwt.token.access-expired-time-ms}")
    private Long accessExpiredTimeMs;
    // Refresh Token의 만료 시간
    @Value("${jwt.token.refresh-expired-time-ms}")
    private Long refreshExpiredTimeMs;

    private final ServiceUtils serviceUtils;

    //  Access Token 생성
    public String createAccessToken(String userId) {
        return JwtTokenUtils.createJwtToken(userId, secretKey, accessExpiredTimeMs);
    }

    // Refresh Token 생성
    public String createRefreshToken(String userId) {
        return JwtTokenUtils.createJwtToken(userId, secretKey, refreshExpiredTimeMs);
    }

    // Refresh Token을 사용하여 새로운 Access Token을 생성하는 로직
    public String reissueAccessToken(String refreshToken) {
        // Refresh Token 유효성 검증 및 만료 여부 확인
        if (!validateToken(refreshToken)) {
            // 유효하지 않거나 만료된 경우, 로그아웃 처리 또는 클라이언트에게 재로그인 요청
            throw new DoubleSApplicationException(
                    ErrorCode.INVALID_TOKEN,
                    "Refresh Token이 맞지 않습니다."
            );
        }

        Claims claims = JwtTokenUtils.extractClaims(refreshToken, secretKey);
        String userId = (String) claims.get("userId");

        // 새로운 Access Token 생성
        return createAccessToken(userId);
    }

    // Token 검증
    public boolean validateToken(String token) {
        // return JwtTokenUtils.isExpired(token, secretKey);
        try {
            // isExpired가 만료되면 true를 반환한다고 가정하고,
            // validateToken은 만료되지 않아야 true를 반환하도록 수정
            return !JwtTokenUtils.isExpired(token, secretKey);
        } catch (Exception e) {
            // 서명 오류, 유효하지 않은 토큰 등 예외 처리
            // 서명 오류, 유효하지 않은 토큰 등 예외가 발생하면 유효하지 않으므로 false를 반환합니다.
            // (수정됨: Exception 발생 시 예외를 던지지 않고 false 반환)
            log.error("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    // 토큰에서 Authentication 객체 생성
    public UsernamePasswordAuthenticationToken getAuthentication(String token) {
        Claims claims = JwtTokenUtils.extractClaims(token, secretKey);
        String userId = (String) claims.get("userId");

        UserAccountDto userAccountDto = UserAccountDto.fromEntity(
                serviceUtils.getUserAccountOrException(userId)
        );

        return new UsernamePasswordAuthenticationToken(
                userAccountDto, null, userAccountDto.getAuthorities()
        );
    }

    // 토큰에서 userId 가져오기
    public String getUserId(String token) {
        return JwtTokenUtils.getUserId(token, secretKey);
    }
}
