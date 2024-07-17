package com.doubles.selfstudy.config;

import com.doubles.selfstudy.config.filter.JwtTokenFilter;
import com.doubles.selfstudy.exception.CustomAuthenticationEntryPoint;
import com.doubles.selfstudy.service.UserAccountService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final UserAccountService userAccountService;

    @Value("${jwt.secret-key}")
    private String key;

    public SecurityConfig(@Lazy UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/login", "/api/regist").permitAll()
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().permitAll()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(new JwtTokenFilter(key, userAccountService), UsernamePasswordAuthenticationFilter.class)
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
}
