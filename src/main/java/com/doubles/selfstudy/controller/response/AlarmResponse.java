package com.doubles.selfstudy.controller.response;

import com.doubles.selfstudy.dto.alarm.AlarmDto;
import com.doubles.selfstudy.dto.alarm.AlarmType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AlarmResponse {

    private AlarmType alarmType;
    private Long targetId;
    private String data;
    private Long alarmCount;

    public static AlarmResponse fromAlarmDto(AlarmDto dto) {
        return new AlarmResponse(
                dto.getAlarmType(),
                dto.getTargetId(),
                dto.getData(),
                dto.getAlarmCount()
        );
    }
}
