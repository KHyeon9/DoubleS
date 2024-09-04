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

    private Long id;
    private UserAccountDto user;
    private AlarmType alarmType;
    private String fromUserId;
    private Long targetId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static AlarmDto fromEntity(Alarm entity) {
        return new AlarmDto(
                entity.getId(),
                UserAccountDto.fromEntity(entity.getUserAccount()),
                entity.getAlarmType(),
                entity.getFromUserId(),
                entity.getTargetId(),
                entity.getContent(),
                entity.getCreatedAt(),
                entity.getModifiedAt()
        );
    }
}
