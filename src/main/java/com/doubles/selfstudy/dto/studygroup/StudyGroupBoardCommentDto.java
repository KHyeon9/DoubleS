package com.doubles.selfstudy.dto.studygroup;

import com.doubles.selfstudy.dto.user.UserAccountDto;
import com.doubles.selfstudy.entity.StudyGroupBoardComment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StudyGroupBoardCommentDto {

    private Long id;
    private String comment;
    private UserAccountDto user;
    private Long studyGroupBoardId;
    private LocalDateTime createdAt; // 생성 일시
    private String createdBy; // 생성자
    private LocalDateTime modifiedAt; // 수정 일시
    private String modifiedBy; // 수정자

    public static StudyGroupBoardCommentDto of(String comment, UserAccountDto user, Long studyGroupBoardId) {
        return new StudyGroupBoardCommentDto(null, comment, user, studyGroupBoardId, null, null, null, null);
    }

    public static StudyGroupBoardCommentDto of(Long id, String comment, UserAccountDto user, Long studyGroupBoardId, LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy) {
        return new StudyGroupBoardCommentDto(id, comment, user, studyGroupBoardId, createdAt, createdBy, modifiedAt, modifiedBy);
    }

    public static StudyGroupBoardCommentDto fromEntity(StudyGroupBoardComment entity) {
        return new StudyGroupBoardCommentDto(
                entity.getId(),
                entity.getComment(),
                UserAccountDto.fromEntity(entity.getUserAccount()),
                entity.getStudyGroupBoard().getId(),
                entity.getCreatedAt(),
                entity.getCreatedBy(),
                entity.getModifiedAt(),
                entity.getModifiedBy()
        );
    }
}
