package com.doubles.selfstudy.config;

import com.doubles.selfstudy.config.filter.JwtTokenFilter;
import com.doubles.selfstudy.exception.CustomAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

@Configuration
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Value("${jwt.secret-key}")
    private String key;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/ws/init/**").permitAll()  // WebSocket 엔드포인트 허용
                        .requestMatchers("/api/login", "/api/regist").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/main/notice_board").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/main/notice_board/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/main/notice_board/*").hasRole("ADMIN")
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().permitAll()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(new JwtTokenFilter(key, jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(handle ->
                            handle.authenticationEntryPoint(
                                    new CustomAuthenticationEntryPoint()
                            )
                        )
                .build();
    }

    // 패스워드 인코더 추가
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    // 헤더에 인증 데이터를 특정 주소에서만 읽도록 커스텀 (수정할 수도 있음)
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web -> web.ignoring()
                .requestMatchers(RegexRequestMatcher.regexMatcher("^(?!/api/main/).*"))
                .requestMatchers(HttpMethod.POST, "/api/regist", "/api/login")
        );
    }
}
