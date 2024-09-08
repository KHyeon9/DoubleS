package com.doubles.selfstudy.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JwtTokenUtils {

    // userId 반환
    public static String getUserId(String token, String key) {
        return extractClaims(token, key).get("userId", String.class);
    }

    // 토큰 종료 시간
    public static boolean isExpired(String token, String key) {
        Date expiredDate = extractClaims(token, key).getExpiration();

        return expiredDate.before(new Date());
    }
    
    // claim 추출
    public static Claims extractClaims(String token, String key) {
        return Jwts
                .parser() // JWT 파서 객체 생성 (JWT에서 헤더, 페이로드, 서명 추출에 사용하기 위해)
                .verifyWith(getKey(key)) // 서명 검증에 사용할 키 설정
                .build() // 파서 빌드
                .parseSignedClaims(token) // 토큰을 위에 서명 검증할 키를 통해 파싱
                .getPayload(); // 클레임 추출
    }

    // 토큰 생성
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
