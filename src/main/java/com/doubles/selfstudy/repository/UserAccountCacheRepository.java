package com.doubles.selfstudy.repository;

import com.doubles.selfstudy.entity.UserAccount;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Repository
public class UserAccountCacheRepository {
    // redis에 저장, 가져오기 위한 레포지토리
    private final RedisTemplate<String, UserAccount> userAccountRedisTemplate;
    // redis에 저장되어 있는 일수 지정
    private final static Duration USER_CACHE_TTL = Duration.ofDays(3);

    public void setUserAccount(UserAccount userAccount) {
        String key = getKey(userAccount.getUserId());
        log.info("Redis에 UserAccount를 셋팅 {} , {}", userAccount, key);

        userAccountRedisTemplate.opsForValue().set(key, userAccount, USER_CACHE_TTL);
    }

    public Optional<UserAccount> getUserAccount(String userId){
        String key = getKey(userId);
        UserAccount userAccount = userAccountRedisTemplate.opsForValue().get(key);

        return Optional.ofNullable(userAccount);
    }

    public void deleteUserAccount(String userId) {
        String key = getKey(userId);
        log.info("Redis에서 UserAccount를 삭제합니다. 키: {}", key);

        userAccountRedisTemplate.delete(key);
    }

    private String getKey(String userId) {
        return "USER:" + userId;
    }
}
