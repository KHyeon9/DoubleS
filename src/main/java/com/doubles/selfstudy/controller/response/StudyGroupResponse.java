package com.doubles.selfstudy.controller.response;

import com.doubles.selfstudy.dto.studygroup.StudyGroupDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StudyGroupResponse {

    private String StudyGroupName;
    private String description;

    public static StudyGroupResponse fromStudyGroupDto(StudyGroupDto dto) {
        return new StudyGroupResponse(
                dto.getStudyGroupName(),
                dto.getDescription()
        );
    }
}