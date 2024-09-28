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
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/main/alarm")
@RestController
public class AlarmController {

    private final AlarmService alarmService;

    // 알람 리스트 조회
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

    // 탑 네비 알람 리스트 조회
    @GetMapping("/nav")
    public Response<List<AlarmResponse>> navAlarmList(
            Authentication authentication
    ) {
        return Response.success(
                alarmService.topNavAlarmList(authentication.getName())
                        .stream()
                        .map(AlarmResponse::fromAlarmDto)
                        .toList()
        );
    }

    // 알람 sse 구독
    @GetMapping("/sub")
    public SseEmitter subscribeAlarm(
            Authentication authentication
    ) {
        return alarmService.connectAlarm(authentication.getName());
    }

    // 알람 삭제
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
