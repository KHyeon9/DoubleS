package com.doubles.selfstudy.dto.studygroup;

import com.doubles.selfstudy.dto.user.UserAccountDto;
import com.doubles.selfstudy.entity.StudyGroup;
import com.doubles.selfstudy.entity.UserStudyGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserStudyGroupDto {

    private Long id;
    private UserAccountDto user;
    private StudyGroupPosition position;
    private StudyGroupDto studyGroup;
    private LocalDateTime joinedAt;

    public static UserStudyGroupDto of(UserAccountDto user, StudyGroupPosition position, StudyGroupDto studyGroup, LocalDateTime joinedAt) {
        return UserStudyGroupDto.of(null, user, position, studyGroup, joinedAt);
    }

    public static UserStudyGroupDto of(Long id, UserAccountDto user, StudyGroupPosition position, StudyGroupDto studyGroup, LocalDateTime joinedAt) {
        return new UserStudyGroupDto(id, user, position, studyGroup, joinedAt);
    }

    public static UserStudyGroupDto fromEntity(UserStudyGroup entity) {
        return new UserStudyGroupDto(
                entity.getId(),
                UserAccountDto.fromEntity(entity.getUserAccount()),
                entity.getPosition(),
                StudyGroupDto.fromEntity(entity.getStudyGroup()),
                entity.getJoinedAt()
        );
    }
}
