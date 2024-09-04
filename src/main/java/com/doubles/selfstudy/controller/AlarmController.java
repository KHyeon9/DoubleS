package com.doubles.selfstudy.controller;

import com.doubles.selfstudy.controller.response.AlarmResponse;
import com.doubles.selfstudy.controller.response.Response;
import com.doubles.selfstudy.service.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/main/alarm")
@RestController
public class AlarmController {

    private final AlarmService alarmService;

    @GetMapping
    public Response<Page<AlarmResponse>> alarmList(Authentication authentication, Pageable pageable) {
        return Response.success(
                alarmService.alarmList(authentication.getName(), pageable)
                        .map(AlarmResponse::fromAlarmDto)
        );
    }
}
