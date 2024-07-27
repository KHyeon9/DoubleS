package com.doubles.selfstudy.dto.notification;

import com.doubles.selfstudy.dto.user.UserAccountDto;
import com.doubles.selfstudy.entity.NotificationBoard;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationBoardDto {

    private Long id;
    private UserAccountDto userAccountDto; // 유저 정보
    private String title; // 제목
    private String content; // 내용
    private LocalDateTime createdAt; // 생성 일시
    private String createdBy; // 생성자
    private LocalDateTime modifiedAt; // 수정 일시
    private String modifiedBy; // 수정자

    public static NotificationBoardDto of(UserAccountDto userAccountDto, String title, String content) {
        return NotificationBoardDto.of(null, userAccountDto,title, content, null, null, null, null);
    }

    public static NotificationBoardDto of(Long id, UserAccountDto userAccountDto, String title, String content, LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy) {
        return new NotificationBoardDto(id, userAccountDto,title, content, createdAt, createdBy, modifiedAt, modifiedBy);
    }

    public static NotificationBoardDto fromEntity(NotificationBoard entity) {
        return new NotificationBoardDto(
                entity.getId(),
                UserAccountDto.fromEntity(entity.getUserAccount()),
                entity.getTitle(),
                entity.getContent(),
                entity.getCreatedAt(),
                entity.getCreatedBy(),
                entity.getModifiedAt(),
                entity.getModifiedBy()
        );
    }
}
