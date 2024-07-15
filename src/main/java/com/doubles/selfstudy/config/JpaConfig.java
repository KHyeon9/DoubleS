package com.doubles.selfstudy.config;

import com.doubles.selfstudy.dto.user.UserAccountDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Slf4j
@Configuration
public class JpaConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> {
            Optional<String> auditor = Optional.ofNullable(SecurityContextHolder.getContext())
                    .map(SecurityContext::getAuthentication) // 인증 정보 가져옴
                    .filter(Authentication::isAuthenticated) // 인증 되었는지 확인
                    .map(Authentication::getPrincipal)
                    .map(UserAccountDto.class::cast)
                    .map(UserAccountDto::getUsername);

            auditor.ifPresent(username -> log.info("Current auditor: {}", username));

            if (auditor.isEmpty()) {
                log.warn("인증 정보를 SecurityContext에서 찾지 못했습니다.");
            }

            return auditor;
        };
    }
}
