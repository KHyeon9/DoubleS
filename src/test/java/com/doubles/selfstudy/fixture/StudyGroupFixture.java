package com.doubles.selfstudy.fixture;

import com.doubles.selfstudy.entity.StudyGroup;

public class StudyGroupFixture {

    public static StudyGroup get() {
        return StudyGroup.of(1L, "StudyGroupName", "description");
    }

    public static StudyGroup get(String studyGroupName, String description) {
        return StudyGroup.of(1L, studyGroupName, description);
    }
}
