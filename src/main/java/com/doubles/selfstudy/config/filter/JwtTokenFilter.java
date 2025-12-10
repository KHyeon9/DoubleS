package com.doubles.selfstudy.config.filter;

import com.doubles.selfstudy.config.JwtTokenProvider;
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
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    private static final List<String> TOKEN_SKIP_URIS = List.of(
            "/api/reissue" // Access Token 재발급 요청은 Access Token 검증을 건너뜁니다.
    );
    // WebSocket 연결 시 STOMP 프로토콜이 아닌 HTTP 요청으로 인증을 시도하는 엔드포인트 목록
    private final static List<String> TOKEN_IN_PARAM_URLS = List.of("/api/main/alarm/sub");
    private static final String BEARER_PREFIX = "Bearer ";
    private static String TOKEN_PREFIX = "token=";


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 토큰 재발급은 jwt 필터 스킵
        if (TOKEN_SKIP_URIS.contains(request.getRequestURI())) {
            log.info("Access Token 검증 스킵: 재발급(Reissue) 경로 접근 허용. URI: {}", request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        final String token;

        // SSE 연결 시 토큰 추출 (쿼리 파라미터 체크)
        if (TOKEN_IN_PARAM_URLS.contains(request.getRequestURI()) && request.getQueryString() != null) {
            String queryString = request.getQueryString();
            String[] params = queryString.split("&");
            String extractedToken = null;
            // 토큰 찾기
            for (String param : params) {
                if (param.startsWith(TOKEN_PREFIX)) {
                    extractedToken = param.substring(TOKEN_PREFIX.length()).trim();
                    break;
                }
            }
            // 토큰이 없는 경우
            if (extractedToken == null || extractedToken.isEmpty()) {
                log.warn("요청 {}에서 쿼리 파라미터에 유효한 토큰을 찾을 수 없습니다.", request.getRequestURI());
                filterChain.doFilter(request, response);
                return;
            }

            token = extractedToken;
            log.info("쿼리 파라미터에서 토큰 추출: {}", request.getRequestURI());
        } else {
            // 일반 HTTP 요청 시 토큰 추출 (Authorization 헤더 체크)
            final String header = request.getHeader(HttpHeaders.AUTHORIZATION);

            // 헤더가 null이거나 Bear 토큰 형식이 아닌 겨우, 인증  없이 다음 필터로 이동
            if (header == null || !header.startsWith(BEARER_PREFIX)) {
                filterChain.doFilter(request, response);
                return;
            }
            // Bearer 접두사 제거 후 문자열로 가져옴
            token = header.substring(BEARER_PREFIX.length()).trim();
        }
        // JWT 유효성 검사 및 인증 처리
        try {
            // validateToken은 토큰이 유효하면 true를 반환해야 합니다.
            // 토큰이 만료되었는지 확인
            if (!jwtTokenProvider.validateToken(token)) {
                log.error("JWT 토큰이 유효하지 않거나 만료되었습니다.");
                filterChain.doFilter(request, response);
                return;
            }

            // 토큰이 유효한 경우, 인증 객체 생성
            UsernamePasswordAuthenticationToken authentication =
                    jwtTokenProvider.getAuthentication(token);

            // 추가적인 사용자 세부 정보 추가 (IP 등을 추가 할 수 있음)
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // 사용자 정보를 securityContextHolder에 추가
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("인증 성공: User ID: {}", authentication.getName());
        } catch (Exception e) {
            // RuntimeException 뿐만 아니라 모든 예외(Exception)를 처리하도록 포괄적으로 변경
            log.error("JWT 인증 과정 중 오류 발생 (토큰 파싱/인증 실패): {}", e.getMessage());
            filterChain.doFilter(request, response);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
