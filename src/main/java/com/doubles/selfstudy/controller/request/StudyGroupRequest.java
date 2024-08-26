package com.doubles.selfstudy.controller.request;

import com.doubles.selfstudy.dto.studygroup.StudyGroupDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StudyGroupRequest {

    private String StudyGroupName;
    private String description;
}
