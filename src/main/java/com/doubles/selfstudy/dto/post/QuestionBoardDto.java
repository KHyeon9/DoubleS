package com.doubles.selfstudy.dto.post;

import com.doubles.selfstudy.dto.user.UserAccountDto;
import com.doubles.selfstudy.entity.QuestionBoard;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionBoardDto {

    private Long id;
    private UserAccountDto userAccountDto; // 유저 정보
    private String title; // 제목
    private String content; // 내용
    private QuestionBoardTag tag; // 태그
    private LocalDateTime createdAt; // 생성 일시
    private String createdBy; // 생성자
    private LocalDateTime modifiedAt; // 수정 일시
    private String modifiedBy; // 수정자

    public static QuestionBoardDto of(UserAccountDto userAccountDto, String title, String content, QuestionBoardTag tag) {
        return new QuestionBoardDto(null, userAccountDto, title, content, tag, null, null, null, null);
    }

    public static QuestionBoardDto of(Long id, UserAccountDto userAccountDto, String title, String content, QuestionBoardTag tag, LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy) {
        return new QuestionBoardDto(id, userAccountDto, title, content, tag, createdAt, createdBy, modifiedAt, modifiedBy);
    }

    public static QuestionBoardDto fromEntity(QuestionBoard entity) {
        return new QuestionBoardDto(
                entity.getId(),
                UserAccountDto.fromEntity(entity.getUserAccount()),
                entity.getTitle(),
                entity.getContent(),
                entity.getTag(),
                entity.getCreatedAt(),
                entity.getCreatedBy(),
                entity.getModifiedAt(),
                entity.getModifiedBy()
        );
    }
}
