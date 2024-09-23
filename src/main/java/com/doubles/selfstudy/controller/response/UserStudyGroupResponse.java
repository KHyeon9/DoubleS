package com.doubles.selfstudy.controller.response;

import com.doubles.selfstudy.dto.studygroup.StudyGroupPosition;
import com.doubles.selfstudy.dto.studygroup.UserStudyGroupDto;
import com.doubles.selfstudy.dto.user.UserAccountDto;
import com.doubles.selfstudy.entity.StudyGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserStudyGroupResponse {

    private Long id;
    private UserResponse user;
    private StudyGroupPosition position;
    private StudyGroupResponse studyGroup;
    private LocalDateTime joinedAt;

    public static UserStudyGroupResponse fromUserStudyGroupDto(UserStudyGroupDto dto) {
        return new UserStudyGroupResponse(
                dto.getId(),
                UserResponse.fromUserAccountDto(dto.getUser()),
                dto.getPosition(),
                StudyGroupResponse.fromStudyGroupDto(dto.getStudyGroup()),
                dto.getJoinedAt()
        );
    }
}
