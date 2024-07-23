package com.doubles.selfstudy.dto.post;

import com.doubles.selfstudy.entity.QuestionBoardComment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class QuestionBoardCommentDto {

    private Long id;
    private String comment; // 댓글
    private String userId; // 유저 id
    private Long questionBoardId; // 게시글 id
    private LocalDateTime createdAt; // 생성 일시
    private String createdBy; // 생성자
    private LocalDateTime modifiedAt; // 수정 일시
    private String modifiedBy; // 수정자

    public static QuestionBoardCommentDto of(String comment, String userId, Long questionBoardId) {
        return new QuestionBoardCommentDto(null, comment, userId, questionBoardId, null, null, null, null);
    }

    public static QuestionBoardCommentDto of(Long id, String comment, String userId, Long questionBoardId, LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy) {
        return new QuestionBoardCommentDto(id, comment, userId, questionBoardId, createdAt, createdBy, modifiedAt, modifiedBy);
    }

    public static QuestionBoardCommentDto fromEntity(QuestionBoardComment entity) {
        return new QuestionBoardCommentDto(
                entity.getId(),
                entity.getComment(),
                entity.getUserAccount().getUserId(),
                entity.getQuestionBoard().getId(),
                entity.getCreatedAt(),
                entity.getCreatedBy(),
                entity.getModifiedAt(),
                entity.getModifiedBy()
        );
    }
}
