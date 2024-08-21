package com.doubles.selfstudy.dto.studygroup;

import com.doubles.selfstudy.dto.user.UserAccountDto;
import com.doubles.selfstudy.entity.StudyGroup;
import com.doubles.selfstudy.entity.UserAccount;
import com.doubles.selfstudy.entity.UserStudyGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserStudyGroupDto {

    private Long id;
    private UserAccountDto user;
    private StudyGroupPosition position;
    private StudyGroup studyGroup;

    public static UserStudyGroupDto of(UserAccountDto user, StudyGroupPosition position, StudyGroup studyGroup) {
        return UserStudyGroupDto.of(null, user, position, studyGroup);
    }

    public static UserStudyGroupDto of(Long id, UserAccountDto user, StudyGroupPosition position, StudyGroup studyGroup) {
        return new UserStudyGroupDto(id, user, position, studyGroup);
    }

    public static UserStudyGroupDto fromEntity(UserStudyGroup entity) {
        return new UserStudyGroupDto(
                entity.getId(),
                UserAccountDto.fromEntity(entity.getUserAccount()),
                entity.getPosition(),
                entity.getStudyGroup()
        );
    }
}
