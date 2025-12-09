package com.doubles.selfstudy.config;

import com.doubles.selfstudy.exception.DoubleSApplicationException;
import com.doubles.selfstudy.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
@RequiredArgsConstructor
public class JwtChannelInterceptor implements ChannelInterceptor  {

    private final JwtTokenProvider jwtTokenProvider;
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        // 헤더 가져옴
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        // 코멘트 가져오기
        StompCommand command = accessor.getCommand();

        // 지금 상태 확인
        log.debug("STOMP Command: {}", command);

        // CONNECT 명령어 처리 (최초 연결 시 인증 시도)
        if (StompCommand.CONNECT.equals(command)) {
            String fullToken = accessor.getFirstNativeHeader(AUTHORIZATION_HEADER);

            if (fullToken == null || !fullToken.startsWith(BEARER_PREFIX)) {
                log.error("JWT 토큰이 없거나 형식이 잘못되었습니다.");
                // 인증 실패 시, CONNECT 명령을 막기 위해 예외 발생
                throw new DoubleSApplicationException(ErrorCode.INVALID_TOKEN,
                        "인증 정보(JWT 토큰)가 누락되었거나 형식이 잘못되었습니다.");
            }
            // Bearer 접두사 제거
            String token = fullToken.substring(BEARER_PREFIX.length());
            log.debug("Extracted Token: {}", token);

            // 토큰 유효성 검증 및 인증 객체 설정
            // validateToken이 true를 반환해야 (토큰이 유효해야) 인증을 진행합니다.
            if (jwtTokenProvider.validateToken(token)) {
                try {
                    UsernamePasswordAuthenticationToken auth =
                            jwtTokenProvider.getAuthentication(token);
                    // Spring Security Context 설정
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    // STOMP 세션에서도 인증 정보를 사용할 수 있도록 설정
                    accessor.setUser(auth);

                    log.info("STOMP 연결: 유저 인증 성공. User ID: {}", auth.getName());
                } catch (DoubleSApplicationException e) {
                    log.error("유저 인증 과정 중 오류 발생: {}", e.getMessage());
                    throw new DoubleSApplicationException(ErrorCode.INVALID_TOKEN,
                            "유효한 토큰이지만 사용자 정보를 찾을 수 없습니다.");
                }
            } else {
                log.error("JWT 토큰이 유효하지 않거나 만료되었습니다.");
                throw new DoubleSApplicationException(ErrorCode.INVALID_TOKEN,
                        "JWT 토큰이 유효하지 않거나 만료되었습니다. 재로그인이 필요합니다.");
            }
        }

        return message;
    }
}
