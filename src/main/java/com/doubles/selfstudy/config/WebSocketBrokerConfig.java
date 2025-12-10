package com.doubles.selfstudy.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@RequiredArgsConstructor
@EnableWebSocketMessageBroker
@Configuration
public class WebSocketBrokerConfig implements WebSocketMessageBrokerConfigurer {
    // 인터셉터 적용을 위한 의존성 주입
    private final JwtChannelInterceptor jwtChannelInterceptor;

    // 메시지를 중간에서 라우팅할 때 사용하는 메시지 브로커를 구성
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 메시지를 전송할 브로커 경로
        // 클라이언트에서 1번 채널을 구독하고자 할 때는 /sub/1형식과 같은 규칙을 따라야 한다.
        registry.enableSimpleBroker("/sub");

        // 클라이언트가 메시지를 전송할 경로.
        // 즉, /pub로 시작하는 메시지만 해당 Broker에서 받아서 처리한다.
        registry.setApplicationDestinationPrefixes("/pub");
    }

    // 클라이언트에서 WebSocket에 접속할 수 있는 endpoint를 지정
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 클라이언트가 연결할 엔드포인트
        registry.addEndpoint("/ws/init")  // ex) ws://localhost:8080/ws/init
                .setAllowedOriginPatterns("*");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(jwtChannelInterceptor);
    }
}
