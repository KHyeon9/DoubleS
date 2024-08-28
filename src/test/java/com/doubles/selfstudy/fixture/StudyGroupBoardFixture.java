package com.doubles.selfstudy.fixture;

import com.doubles.selfstudy.entity.StudyGroup;
import com.doubles.selfstudy.entity.StudyGroupBoard;
import com.doubles.selfstudy.entity.UserAccount;

public class StudyGroupBoardFixture {

    public static StudyGroupBoard get(String userId, String title, String content) {
        UserAccount userAccount = UserAccount.of(userId, "test", "test", "test", null);
        StudyGroup studyGroup = StudyGroup.of("test", "test");

        return StudyGroupBoard.of(userAccount, studyGroup, title, content);
    }

    public static StudyGroupBoard get(UserAccount userAccount, StudyGroup studyGroup, String title, String content) {
        return StudyGroupBoard.of(userAccount, studyGroup, title, content);
    }
}
