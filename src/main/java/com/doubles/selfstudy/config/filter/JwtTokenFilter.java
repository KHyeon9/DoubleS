package com.doubles.selfstudy.config.filter;

import com.doubles.selfstudy.dto.user.UserAccountDto;
import com.doubles.selfstudy.service.UserAccountService;
import com.doubles.selfstudy.utils.JwtTokenUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final String key;
    private final UserAccountService userAccountService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // get header
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        // header가 null이거나 토큰 값이 아닌 경우
        if (header == null || !header.startsWith("Bearer ")) {
            log.error("헤더를 얻는 과정에서 에러 발생. 헤더가 null이거나 맞지 않습니다.");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // Bearer 제외 후 문자열로 가져옴
            final String token = header.split(" ")[1].trim();

            // 토큰이 만료되었는지 확인
            if (JwtTokenUtils.isExpired(token, key)) {
                log.error("키가 만료되었습니다.");
                filterChain.doFilter(request, response);
                return;
            }

            String userId = JwtTokenUtils.getUserId(token, key);
            UserAccountDto userAccountDto = userAccountService.loadUserByUserId(userId);

            // 인증 객체 생성 및 값 입력
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userAccountDto,
                    null,
                    userAccountDto.getAuthorities()
            );

            // 추가적인 사용자 세부 정보 추가 (IP 등을 추가 할 수 있음)
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // 사용자 정보를 securityContextHolder에 추가
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (RuntimeException e) {
            log.error("검증 과정에서 에러 발생 : {}", e.toString());
            filterChain.doFilter(request, response);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
