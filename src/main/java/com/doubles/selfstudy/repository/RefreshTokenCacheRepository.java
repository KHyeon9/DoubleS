package com.doubles.selfstudy.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Repository
public class RefreshTokenCacheRepository {
    // Refresh Token 저장을 위한 RedisTemplate
    private final RedisTemplate<String, String> refreshTokenRedisTemplate;
    // Refresh Token의 유효기간 설정
    // 이는 JWT Refresh Token의 실제 유효기간과 일치해야 합니다.
    private final static Duration REFRESH_TOKEN_TTL = Duration.ofDays(14);

    /**
     * userId를 키로 Refresh Token 저장 (RT 유효 기간과 TTL 동일 설정)
     */
    public void saveRefreshToken(String userId, String refreshToken) {
        String key = getKey(userId);
        log.info("Redis에 Refresh Token을 저장합니다. 키: {}", key);

        refreshTokenRedisTemplate.opsForValue().set(key, refreshToken, REFRESH_TOKEN_TTL);
    }

    /**
     * userId로 저장된 Refresh Token 조회
     */
    public Optional<String> findByUserId(String userId) {
        String key = getKey(userId);
        String token = refreshTokenRedisTemplate.opsForValue().get(key);

        return Optional.ofNullable(token);
    }

    /**
     * userId로 저장된 Refresh Token 삭제 (로그아웃, 탈퇴 시)
     */
    public void deleteByUserId(String userId) {
        String key = getKey(userId);
        log.info("Redis에서 Refresh Token을 삭제합니다. 키: {}", key);

        refreshTokenRedisTemplate.delete(key);
    }

    /**
     * Refresh Token 저장에 사용될 키를 생성합니다. (USER 캐시와 구분)
     */
    private String getKey(String userId) {
        return "REFRESH_TOKEN:" + userId;
    }
}
