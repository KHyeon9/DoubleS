package com.doubles.selfstudy.controller.response;

import com.doubles.selfstudy.dto.notification.NotificationBoardDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class NotificationBoardResponse {

    private Long id;
    private UserResponse user; // 유저 정보
    private String title; // 제목
    private String content; // 내용
    private LocalDateTime createdAt; // 생성 일시
    private String createdBy; // 생성자
    private LocalDateTime modifiedAt; // 수정 일시
    private String modifiedBy; // 수정자

    public static NotificationBoardResponse fromNotificationBoardDto(NotificationBoardDto dto) {
        return new NotificationBoardResponse(
                dto.getId(),
                UserResponse.fromUserAccountDto(dto.getUserAccountDto()),
                dto.getTitle(),
                dto.getContent(),
                dto.getCreatedAt(),
                dto.getCreatedBy(),
                dto.getModifiedAt(),
                dto.getModifiedBy()
        );
    }
}
