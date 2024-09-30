package com.doubles.selfstudy.controller;

import com.doubles.selfstudy.controller.request.UserLoginRequest;
import com.doubles.selfstudy.controller.request.UserInfoModifyRequest;
import com.doubles.selfstudy.controller.request.UserPasswordModifyRequest;
import com.doubles.selfstudy.controller.request.UserRegistRequest;
import com.doubles.selfstudy.controller.response.*;
import com.doubles.selfstudy.dto.question.QuestionBoardDto;
import com.doubles.selfstudy.dto.user.UserAccountDto;
import com.doubles.selfstudy.service.QuestionBoardService;
import com.doubles.selfstudy.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class UserController {

    private final UserAccountService userAccountService;
    private final QuestionBoardService questionBoardService;

    @PostMapping("/login")
    public Response<UserLoginResponse> login(@RequestBody UserLoginRequest request) {
        // login post
        String token = userAccountService.login(request.getUserId(), request.getPassword());

        return Response.success(new UserLoginResponse(token));
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
