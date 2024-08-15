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

    @GetMapping("/main/profile")
    public Response<ProfileResponse> getProfileUserInfo(Authentication authentication) {
        // user info get
        UserAccountDto userAccountDto = userAccountService
                .getUserInfo(authentication.getName());

        // profile question board get
        List<QuestionBoardDto> questionBoardDtoList = questionBoardService
                .profileQuestionBoardList(authentication.getName());

        return Response.success(ProfileResponse
                .fromUserAccountDtoAndQuestionBoardListDto(
                        userAccountDto,
                        questionBoardDtoList
                ));
    }
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

    @GetMapping("/alarm")
    public void alarm() {
        // alarm get
    }
}
