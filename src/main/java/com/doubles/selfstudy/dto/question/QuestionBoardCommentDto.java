package com.doubles.selfstudy.dto.question;

import com.doubles.selfstudy.dto.user.UserAccountDto;
import com.doubles.selfstudy.entity.QuestionBoardComment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class QuestionBoardCommentDto {

    private Long id;
    private String comment; // 댓글
    private UserAccountDto userAccountDto; // 유저 id
    private Long questionBoardId; // 게시글 id
    private LocalDateTime createdAt; // 생성 일시
    private String createdBy; // 생성자
    private LocalDateTime modifiedAt; // 수정 일시
    private String modifiedBy; // 수정자

    public static QuestionBoardCommentDto of(String comment, UserAccountDto userAccountDto, Long questionBoardId) {
        return new QuestionBoardCommentDto(null, comment, userAccountDto, questionBoardId, null, null, null, null);
    }

    public static QuestionBoardCommentDto of(Long id, String comment, UserAccountDto userAccountDto, Long questionBoardId, LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy) {
        return new QuestionBoardCommentDto(id, comment, userAccountDto, questionBoardId, createdAt, createdBy, modifiedAt, modifiedBy);
    }

    public static QuestionBoardCommentDto fromEntity(QuestionBoardComment entity) {
        return new QuestionBoardCommentDto(
                entity.getId(),
                entity.getComment(),
                UserAccountDto.fromEntity(entity.getUserAccount()),
                entity.getQuestionBoard().getId(),
                entity.getCreatedAt(),
                entity.getCreatedBy(),
                entity.getModifiedAt(),
                entity.getModifiedBy()
        );
    }
}
