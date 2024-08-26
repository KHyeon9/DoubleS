package com.doubles.selfstudy.controller.response;

import com.doubles.selfstudy.dto.studygroup.StudyGroupPosition;
import com.doubles.selfstudy.dto.studygroup.UserStudyGroupDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class StudyGroupUserResponse {

    private String userId;
    private String nickname;
    private String email;
    private StudyGroupPosition position;
    private LocalDateTime joinedAt;

    public static StudyGroupUserResponse fromUserStudyGroupDto(UserStudyGroupDto dto) {
        return new StudyGroupUserResponse(
                dto.getUser().getUserId(),
                dto.getUser().getNickname(),
                dto.getUser().getEmail(),
                dto.getPosition(),
                dto.getJoinedAt()
        );
    }
}
