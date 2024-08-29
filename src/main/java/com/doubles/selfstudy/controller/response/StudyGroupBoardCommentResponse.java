package com.doubles.selfstudy.controller.response;

import com.doubles.selfstudy.dto.studygroup.StudyGroupBoardCommentDto;
import com.doubles.selfstudy.dto.user.UserAccountDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class StudyGroupBoardCommentResponse {

    private Long id;
    private String comment;
    private UserResponse user;
    private Long studyGroupBoardId;
    private LocalDateTime createdAt; // 생성 일시
    private String createdBy; // 생성자
    private LocalDateTime modifiedAt; // 수정 일시
    private String modifiedBy; // 수정자

    public static StudyGroupBoardCommentResponse fromStudyGroupBoardCommentDto(StudyGroupBoardCommentDto dto) {
        return new StudyGroupBoardCommentResponse(
                dto.getId(),
                dto.getComment(),
                UserResponse.fromUserAccountDto(dto.getUser()),
                dto.getStudyGroupBoardId(),
                dto.getCreatedAt(),
                dto.getCreatedBy(),
                dto.getModifiedAt(),
                dto.getModifiedBy()
        );
    }
}
