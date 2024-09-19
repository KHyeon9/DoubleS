package com.doubles.selfstudy.dto.alarm;

import com.doubles.selfstudy.dto.user.UserAccountDto;
import com.doubles.selfstudy.entity.Alarm;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AlarmDto {

    private AlarmType alarmType;
    private Long targetId;
    private String data;
    private Long alarmCount;

    public static AlarmDto fromEntity(AlarmType alarmType, Long targetId, String data, Long alarmCount) {
        return new AlarmDto(
                alarmType,
                targetId,
                data,
                alarmCount
        );
    }
}
