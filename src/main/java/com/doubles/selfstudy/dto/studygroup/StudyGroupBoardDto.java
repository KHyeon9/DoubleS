package com.doubles.selfstudy.dto.studygroup;

import com.doubles.selfstudy.dto.user.UserAccountDto;
import com.doubles.selfstudy.entity.StudyGroupBoard;
import com.doubles.selfstudy.entity.UserAccount;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StudyGroupBoardDto {

    private Long id;
    private UserAccountDto user;
    private String title;
    private String content;
    private int comments;

    public static StudyGroupBoardDto of(UserAccountDto user, String title, String content, int comments) {
        return new StudyGroupBoardDto(null, user, title, content, comments);
    }

    public static StudyGroupBoardDto of(UserAccountDto user, String title, String content) {
        return new StudyGroupBoardDto(null, user, title, content, 0);
    }

    public static StudyGroupBoardDto of(Long id, UserAccountDto user, String title, String content, int comments) {
        return new StudyGroupBoardDto(id, user, title, content, comments);
    }

    public static StudyGroupBoardDto fromEntity(StudyGroupBoard entity, int comments) {
        return new StudyGroupBoardDto(
                entity.getId(),
                UserAccountDto.fromEntity(entity.getUserAccount()),
                entity.getTitle(),
                entity.getContent(),
                comments
        );
    }

    public static StudyGroupBoardDto fromEntity(StudyGroupBoard entity) {
        return new StudyGroupBoardDto(
                entity.getId(),
                UserAccountDto.fromEntity(entity.getUserAccount()),
                entity.getTitle(),
                entity.getContent(),
                0
        );
    }
}
