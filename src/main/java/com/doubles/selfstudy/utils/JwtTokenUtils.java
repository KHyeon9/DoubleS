package com.doubles.selfstudy.utils;

import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JwtTokenUtils {

    public static String createJwtToken(String userId, String key, long expiredTimeMs) {
        return Jwts.builder()
                .claim("userId", userId)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredTimeMs))
                .signWith(getKey(key))
                .compact();
    }
    
    // 서명 키 생성 메소드
    private static SecretKey getKey(String key) {
        return new SecretKeySpec(
                key.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm() // HS256 사용
        );
    }
}
