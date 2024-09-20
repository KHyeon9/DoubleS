package com.doubles.selfstudy.fixture;

import com.doubles.selfstudy.dto.studygroup.StudyGroupPosition;
import com.doubles.selfstudy.entity.StudyGroup;
import com.doubles.selfstudy.entity.UserAccount;
import com.doubles.selfstudy.entity.UserStudyGroup;

public class UserStudyGroupFixture {

    public static UserStudyGroup getLeader(String userId, StudyGroup studyGroup) {
        UserAccount userAccount = UserAccount
                .of(userId, "password", "test@email.com", "test", "test memo");

        return UserStudyGroup.of(userAccount, StudyGroupPosition.Leader, studyGroup);
    }

    public static UserStudyGroup getMember(String userId, StudyGroup studyGroup) {
        UserAccount userAccount = UserAccount
                .of(userId, "password", "test@email.com", "test", "test memo");

        return UserStudyGroup.of(userAccount, StudyGroupPosition.Member, studyGroup);
    }

    public static UserStudyGroup get(UserAccount userAccount) {
        StudyGroup studyGroup = StudyGroup.of("group name", "description");

        return UserStudyGroup.of(userAccount, StudyGroupPosition.Member, studyGroup);
    }
}
