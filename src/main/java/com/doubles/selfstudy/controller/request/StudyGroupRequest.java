package com.doubles.selfstudy.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StudyGroupRequest {

    private String studyGroupName;
    private String description;
}
