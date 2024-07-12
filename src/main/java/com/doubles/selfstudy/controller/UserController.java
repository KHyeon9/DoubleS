package com.doubles.selfstudy.controller;

import com.doubles.selfstudy.controller.request.UserRegistRequest;
import com.doubles.selfstudy.controller.response.Response;
import com.doubles.selfstudy.controller.response.UserRegistResponse;
import com.doubles.selfstudy.dto.user.UserAccountDto;
import com.doubles.selfstudy.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/user")
@RestController
public class UserController {

    private UserAccountService userAccountService;

    @PostMapping("/login")
    public void login() {
        // login post
    }

    @PostMapping("/regist")
    public Response<UserRegistResponse> regist(@RequestBody UserRegistRequest request) {
        // regist
        UserAccountDto userAccountDto = userAccountService.regist(
                request.getUserId(), request.getPassword(), request.getEmail(), request.getNickname());

        return Response.success(UserRegistResponse.fromDto(userAccountDto));
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
