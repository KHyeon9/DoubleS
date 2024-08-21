package com.doubles.selfstudy.dto.studygroup;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StudyGroupPosition {

    Leader("그룹장"),
    Member("그룹원");

    private final String positionName;
}
