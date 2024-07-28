package com.doubles.selfstudy.dto.notice;

import com.doubles.selfstudy.dto.user.UserAccountDto;
import com.doubles.selfstudy.entity.NoticeBoard;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NoticeBoardDto {

    private Long id;
    private UserAccountDto userAccountDto; // 유저 정보
    private String title; // 제목
    private String content; // 내용
    private LocalDateTime createdAt; // 생성 일시
    private String createdBy; // 생성자
    private LocalDateTime modifiedAt; // 수정 일시
    private String modifiedBy; // 수정자

    public static NoticeBoardDto of(UserAccountDto userAccountDto, String title, String content) {
        return NoticeBoardDto.of(null, userAccountDto,title, content, null, null, null, null);
    }

    public static NoticeBoardDto of(Long id, UserAccountDto userAccountDto, String title, String content, LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy) {
        return new NoticeBoardDto(id, userAccountDto,title, content, createdAt, createdBy, modifiedAt, modifiedBy);
    }

    public static NoticeBoardDto fromEntity(NoticeBoard entity) {
        return new NoticeBoardDto(
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
