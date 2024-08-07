package com.doubles.selfstudy.controller;

import com.doubles.selfstudy.controller.request.UserLoginRequest;
import com.doubles.selfstudy.controller.request.UserRegistRequest;
import com.doubles.selfstudy.controller.response.*;
import com.doubles.selfstudy.dto.user.UserAccountDto;
import com.doubles.selfstudy.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class UserController {

    private final UserAccountService userAccountService;

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

    @PostMapping("/user_info")
    public Response<UserInfoResponse> getUserInfo(@RequestBody UserLoginRequest request) {
        UserAccountDto userAccountDto = userAccountService.getUserInfo(request.getUserId());

        return Response.success(UserInfoResponse.fromUserAccountDto(userAccountDto));
    }

    @GetMapping("/profile")
    public void profile() {
        // profile get
    }

    @GetMapping("/alarm")
    public void alarm() {
        // alarm get
    }
}
