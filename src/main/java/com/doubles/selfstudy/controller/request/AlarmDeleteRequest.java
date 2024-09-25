package com.doubles.selfstudy.controller.request;

import com.doubles.selfstudy.dto.alarm.AlarmType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AlarmDeleteRequest {

    private Long targetId;
    private AlarmType alarmType;
}
