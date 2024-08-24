package com.doubles.selfstudy.fixture;

import com.doubles.selfstudy.entity.StudyGroup;
import com.doubles.selfstudy.entity.StudyGroupBoard;
import com.doubles.selfstudy.entity.UserAccount;

public class StudyGroupBoardFixture {

    public static StudyGroupBoard get(UserAccount userAccount, StudyGroup studyGroup, String title, String content) {
        return StudyGroupBoard.of(1L, userAccount, studyGroup, title, content);
    }
}
