package com.doubles.selfstudy.controller.response;

import com.doubles.selfstudy.dto.question.QuestionBoardCommentDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class QuestionBoardCommentResponse {

    private Long id;
    private String comment; // 댓글
    private UserResponse user; // 유저 id
    private Long questionBoardId; // 게시글 id
    private LocalDateTime createdAt; // 생성 일시
    private String createdBy; // 생성자
    private LocalDateTime modifiedAt; // 수정 일시
    private String modifiedBy; // 수정자

    public static QuestionBoardCommentResponse fromQuestionBoardCommentDto(QuestionBoardCommentDto dto) {
        return new QuestionBoardCommentResponse(
                dto.getId(),
                dto.getComment(),
                UserResponse.fromUserAccountDto(dto.getUserAccountDto()),
                dto.getQuestionBoardId(),
                dto.getCreatedAt(),
                dto.getCreatedBy(),
                dto.getModifiedAt(),
                dto.getModifiedBy()
        );
    }
}
