package com.doubles.selfstudy.controller;

import com.doubles.selfstudy.controller.request.UserLoginRequest;
import com.doubles.selfstudy.controller.request.UserInfoModifyRequest;
import com.doubles.selfstudy.controller.request.UserPasswordModifyRequest;
import com.doubles.selfstudy.controller.request.UserRegistRequest;
import com.doubles.selfstudy.controller.response.*;
import com.doubles.selfstudy.dto.question.QuestionBoardDto;
import com.doubles.selfstudy.dto.user.TokenDto;
import com.doubles.selfstudy.dto.user.UserAccountDto;
import com.doubles.selfstudy.exception.ErrorCode;
import com.doubles.selfstudy.service.QuestionBoardService;
import com.doubles.selfstudy.service.UserAccountService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class UserController {

    private final UserAccountService userAccountService;
    private final QuestionBoardService questionBoardService;
    private static int REFRESHTOKEN_AGE = 60 * 60 * 24 * 14;
    
    // 로그인
    @PostMapping("/login")
    public Response<UserLoginResponse> login(
            @RequestBody UserLoginRequest request,
            HttpServletResponse httpServletResponse
    ) {
        // Service에서 AT와 RT를 모두 포함하는 TokenResponse를 받습니다.
        TokenDto tokenDto = userAccountService.login(request.getUserId(), request.getPassword());

        System.out.println("tokenDto: " + tokenDto);

        // RT를 HttpOnly 쿠키로 설정합니다.
        String sameSiteHeader = setRefreshTokenCookie(tokenDto.getRefreshToken(), REFRESHTOKEN_AGE, true);
        httpServletResponse.setHeader("Set-Cookie", sameSiteHeader);

        System.out.println("sameSiteHeader: " + sameSiteHeader);

        return Response.success(UserLoginResponse.ofDto(tokenDto));
    }
    
    // 로그아웃
    @PostMapping("/logout")
    public Response<Void> logout(
            Authentication authentication,
            HttpServletResponse httpServletResponse
    ) {
        if (authentication == null) {
            log.warn("인증되지 않은 사용자(/logout) 요청. 토큰이 없거나 이미 만료된 상태일 수 있습니다.");
            // 클라이언트 세션 정리만 하도록 유도하기 위해 성공 응답을 반환할 수도 있습니다.
            // 또는 401 에러를 명시적으로 반환할 수도 있습니다. (정책에 따라 선택)
            return Response.error(ErrorCode.ACCESS_DENIED.name());
        }
        // 서버측 토큰 무효화 (DB/Redis 등에서 삭제)
        userAccountService.logout(authentication.getName());
        // 브라우저 쿠키 삭제를 위해 Max-Age=0인 쿠키 헤더 설정
        String deleteCookieHeader = setRefreshTokenCookie("", 0, true);
        httpServletResponse.setHeader("Set-Cookie", deleteCookieHeader);

        return Response.success();
    }

    // token 재발급
    @PostMapping("/reissue")
    public Response<UserLoginResponse> reissueToken(
            // @CookieValue 어노테이션으로 브라우저가 자동으로 보낸 HttpOnly 쿠키의 값을 추출합니다.
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            HttpServletResponse httpServletResponse
    ) {
        if (refreshToken == null) {
            // Refresh Token이 없는 경우 401 Unauthorized 오류를 반환해야 하지만,
            // 여기서는 Response 객체를 사용하므로 실패 응답을 반환합니다.
            return Response.error(ErrorCode.INVALID_TOKEN.name());
        }
        // RT를 검증하고 새로운 Access/Refresh Token을 발급받습니다.
        TokenDto newTokenDto = userAccountService.reissueToken(refreshToken);

        // RT를 다시 HttpOnly 쿠키로 설정합니다. (토큰 회전)
        String sameSiteHeader = setRefreshTokenCookie(
                newTokenDto.getRefreshToken(), REFRESHTOKEN_AGE,true);
        httpServletResponse.setHeader("Set-Cookie", sameSiteHeader);

        return Response.success(UserLoginResponse.ofDto(newTokenDto));
    }

    private static String setRefreshTokenCookie(String refreshToken, int age, boolean secure) {
        ResponseCookie cookie  = ResponseCookie
                .from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(secure)
                .path("/")
                .maxAge(age) // 14 일 60 * 60 * 24 * 14
                .sameSite("Lax") // SameSite=Lax 속성 적용
                .build();

        return cookie.toString();
    }

    @PostMapping("/regist")
    public Response<UserRegistResponse> regist(@RequestBody UserRegistRequest request) {
        // regist
        UserAccountDto userAccountDto = userAccountService.regist(
                request.getUserId(), request.getPassword(), request.getEmail(), request.getNickname());

        return Response.success(UserRegistResponse.fromDto(userAccountDto));
    }


    // user login시 정보 조회
    @PostMapping("/user_info")
    public Response<LoginUserInfoResponse> getLoginUserInfo(@RequestBody UserLoginRequest request) {
        UserAccountDto userAccountDto = userAccountService.getUserInfo(request.getUserId());

        return Response.success(LoginUserInfoResponse.fromUserAccountDto(userAccountDto));
    }

    // user 프로필에서 정보 조회
    @GetMapping("/main/user_info")
    public Response<ProfileResponse> getLoginUserInfo(Authentication authentication) {

        return Response.success(ProfileResponse
                .fromUserAccountDto(userAccountService.getUserInfo(authentication.getName()))
        );
    }

    // 유저 체크
    @GetMapping("/main/check/{userId}")
    public Response<Boolean> checkUserInfo(@PathVariable String userId) {
        // user info check 찾지 못하면 서비스단에 에러 발생
        // 그러므로 따로 false반환 이유가 없음
        userAccountService.getUserInfo(userId);
        
        return Response.success(true);
    }

    // 유저의 프로필 데이터 가져오기
    @GetMapping("/main/profile/{userId}")
    public Response<ProfileResponse> getProfileUserInfo(@PathVariable String userId) {
        // user info get
        UserAccountDto userAccountDto = userAccountService
                .getUserInfo(userId);

        // profile question board get
        List<QuestionBoardDto> questionBoardDtoList = questionBoardService
                .profileQuestionBoardList(userId);

        return Response.success(ProfileResponse
                .fromUserAccountDtoAndQuestionBoardListDto(
                        userAccountDto,
                        questionBoardDtoList
                ));
    }

    // 유저 정보 수정
    @PutMapping("/main/profile/user_info")
    public Response<ProfileResponse> modifyUserInfo(
            Authentication authentication,
            @RequestBody UserInfoModifyRequest request
            ) {
        return Response.success(
                ProfileResponse.fromUserAccountDto(
                        userAccountService
                                .modifiyUserInfo(
                                        authentication.getName(),
                                        request.getNickname(),
                                        request.getEmail(),
                                        request.getMemo()
                                )
                    )
        );
    }
    
    // 유저 비밀번호 수정
    @PutMapping("/main/profile/user_password")
    public Response<Void> modifyUserPassword(
            Authentication authentication,
            @RequestBody UserPasswordModifyRequest request
    ) {
        userAccountService
                .modifiyUserPassword(
                        authentication.getName(),
                        request.getNowPassword(),
                        request.getChangePassword()
        );

        return Response.success();
    }

    // 유저 삭제
    @DeleteMapping("/main/profile")
    public Response<Void> deleteUserAccount(
            Authentication authentication
    ) {
        userAccountService.deleteUserAccount(authentication.getName());

        return Response.success();
    }
}
