package com.doubles.selfstudy.controller.response;

import com.doubles.selfstudy.dto.question.QuestionBoardDto;
import com.doubles.selfstudy.dto.question.QuestionBoardTag;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class QuestionBoardResponse {

    private Long id;
    private UserResponse user; // 유저 정보
    private String title; // 제목
    private String content; // 내용
    private QuestionBoardTagResponse tag; // 태그
    private Long likes; // 좋아요 수
    private Long comments; // 댓글 수
    private Integer viewCounts; // 조회수
    private LocalDateTime createdAt; // 생성 일시
    private String createdBy; // 생성자
    private LocalDateTime modifiedAt; // 수정 일시
    private String modifiedBy; // 수정자

    public static QuestionBoardResponse fromQuestionBoardDto(QuestionBoardDto dto, Long like, Long comments) {
        return new QuestionBoardResponse(
                dto.getId(),
                UserResponse.fromUserAccountDto(dto.getUserAccountDto()),
                dto.getTitle(),
                dto.getContent(),
                QuestionBoardTagResponse.of(dto.getTag().name(), dto.getTag().getTagName()),
                like,
                comments,
                dto.getViewCounts(),
                dto.getCreatedAt(),
                dto.getCreatedBy(),
                dto.getModifiedAt(),
                dto.getModifiedBy()
            );

    }

    public static QuestionBoardResponse fromQuestionBoardDto(QuestionBoardDto dto) {
        return new QuestionBoardResponse(
                dto.getId(),
                UserResponse.fromUserAccountDto(dto.getUserAccountDto()),
                dto.getTitle(),
                dto.getContent(),
                QuestionBoardTagResponse.of(dto.getTag().name(), dto.getTag().getTagName()),
                dto.getLikes(),
                dto.getComments(),
                dto.getViewCounts(),
                dto.getCreatedAt(),
                dto.getCreatedBy(),
                dto.getModifiedAt(),
                dto.getModifiedBy()
        );
    }
}
