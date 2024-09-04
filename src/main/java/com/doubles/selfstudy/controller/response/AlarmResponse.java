package com.doubles.selfstudy.controller.response;

import com.doubles.selfstudy.dto.alarm.AlarmDto;
import com.doubles.selfstudy.dto.alarm.AlarmType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AlarmResponse {

    private Long id;
    private UserResponse user;
    private AlarmType alarmType;
    private String fromUserId;
    private Long targetId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static AlarmResponse fromAlarmDto(AlarmDto dto) {
        return new AlarmResponse(
                dto.getId(),
                UserResponse.fromUserAccountDto(dto.getUser()),
                dto.getAlarmType(),
                dto.getFromUserId(),
                dto.getTargetId(),
                dto.getContent(),
                dto.getCreatedAt(),
                dto.getModifiedAt()
        );
    }
}
