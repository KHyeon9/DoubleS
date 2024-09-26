package com.doubles.selfstudy.controller;

import com.doubles.selfstudy.controller.response.AlarmResponse;
import com.doubles.selfstudy.controller.response.Response;
import com.doubles.selfstudy.dto.alarm.AlarmType;
import com.doubles.selfstudy.service.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/main/alarm")
@RestController
public class AlarmController {

    private final AlarmService alarmService;

    @GetMapping
    public Response<Page<AlarmResponse>> alarmList(
            Authentication authentication,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return Response.success(
                alarmService.alarmList(authentication.getName(), pageable)
                        .map(AlarmResponse::fromAlarmDto)
        );
    }

    @DeleteMapping
    public Response<Void> deleteAlarm(
            Authentication authentication,
            @RequestParam Long targetId, // 쿼리 파라미터로 받음
            @RequestParam AlarmType alarmType
    ) {
        alarmService.deleteAlarm(authentication.getName(), targetId,alarmType);

        return Response.success();
    }
}
