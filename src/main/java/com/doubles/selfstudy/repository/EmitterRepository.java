package com.doubles.selfstudy.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
public class EmitterRepository {
    // 유저 별 emitter 관리를 위한 레포지토리
    private Map<String, SseEmitter> emitterMap = new HashMap<>();

    // 유저 id 별 emitter 저장
    public SseEmitter save(String userId, SseEmitter sseEmitter) {
        final String key = getKey(userId);
        emitterMap.put(key, sseEmitter);

        log.info("Set sseEmitter {}", userId);

        return sseEmitter;
    }

    // 유저 id로 emitter 찾기
    public Optional<SseEmitter> get(String userId) {
        final String key = getKey(userId);
        SseEmitter emitter = emitterMap.get(key);
        if (emitter == null) {
            log.warn("No SseEmitter found for user: {}", userId);
        }
        return Optional.ofNullable(emitter);
    }

    // emitter 삭제
    public void delete(String userId){
        emitterMap.remove(getKey(userId));
    }

    // key 생성
    public String getKey(String userId) {
        return "Emitter:UID:" + userId;
    }
}
