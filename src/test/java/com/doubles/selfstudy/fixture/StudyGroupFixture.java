package com.doubles.selfstudy.fixture;

import com.doubles.selfstudy.entity.StudyGroup;

public class StudyGroupFixture {

    // question board와 다르게 연관관계가 복잡하므로 id를 설정해야함
    public static StudyGroup get() {
        return StudyGroup.of(1L, "StudyGroupName", "description");
    }

    public static StudyGroup get(String studyGroupName, String description) {
        return StudyGroup.of(studyGroupName, description);
    }
}
