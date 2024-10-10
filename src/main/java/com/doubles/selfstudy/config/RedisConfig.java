package com.doubles.selfstudy.config;

import com.doubles.selfstudy.entity.UserAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@RequiredArgsConstructor
@EnableRedisRepositories
@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${spring.data.redis.password}")  // 패스워드 추가
    private String redisPassword;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        // Redis 명령이 2초 안에 완료되지 않으면 타임아웃을 발생시키기 위한 설정
        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                .commandTimeout(Duration.ofSeconds(2))
                .build();

        // RedisStandaloneConfiguration에 호스트, 포트, 패스워드 설정
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
        redisConfig.setHostName(redisHost); // 호스트 서정
        redisConfig.setPort(redisPort); // 포트 설정
        redisConfig.setPassword(redisPassword);  // 패스워드 설정

        return new LettuceConnectionFactory(redisConfig, clientConfig);
    }

    @Bean
    public RedisTemplate<String, UserAccount> userAccountRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        // 템플릿의 레디스의 키 벨류를 설정
        RedisTemplate<String, UserAccount> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory); // 레디스 템플릿과 연결할 펙토리 셋팅
        redisTemplate.setKeySerializer(new StringRedisSerializer()); // 키 직렬화시 키를 문자열로 변환해 저장
        // 직렬화할 타입 명시 -> UserAccount를 JSON형식으로 변환해 저장
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(UserAccount.class));

        return redisTemplate;
    }
}
