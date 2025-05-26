package com.doubles.selfstudy.config;

import com.doubles.selfstudy.exception.DoubleSApplicationException;
import com.doubles.selfstudy.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

@RequiredArgsConstructor
public class JwtChannelInterceptor implements ChannelInterceptor  {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        // 메세지 데이터 확인
        System.out.println("Message 데이터: " + message);
        
        // 헤더 가져옴
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // 지금 상태 확인
        System.out.println("지금 상태: " + accessor.getCommand());

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String token = accessor.getFirstNativeHeader("Authorization");
            
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                System.out.println("token: " + token);
                if (!jwtTokenProvider.validateToken(token)) {
                    UsernamePasswordAuthenticationToken auth =
                            jwtTokenProvider.getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    System.out.println("토큰이 일치하고 유저가 인증되었습니다.");
                } else {
                    System.out.println("JWT 토큰이 맞지 않습니다.");
                    throw new DoubleSApplicationException(ErrorCode.INVALID_TOKEN,
                            "JWT 토큰이 없거나 맞지 않습니다.");
                }
            } else {
                System.out.println("JWT 토큰이 없습니다.");
                throw new DoubleSApplicationException(ErrorCode.INVALID_TOKEN,
                        "JWT 토큰이 없습니다.");
            }
        }

        return message;
    }
}
