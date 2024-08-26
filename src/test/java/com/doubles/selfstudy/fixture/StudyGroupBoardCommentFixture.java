package com.doubles.selfstudy.fixture;

import com.doubles.selfstudy.entity.StudyGroup;
import com.doubles.selfstudy.entity.StudyGroupBoard;
import com.doubles.selfstudy.entity.StudyGroupBoardComment;
import com.doubles.selfstudy.entity.UserAccount;

public class StudyGroupBoardCommentFixture {

    public static StudyGroupBoardComment get(UserAccount userAccount, String comment) {
        StudyGroupBoard studyGroupBoard = StudyGroupBoard.of(
                userAccount,
                StudyGroup.of("studyGroupName", "description"),
                "title",
                "content"
        );

        return StudyGroupBoardComment.of(userAccount, studyGroupBoard, comment);
    }
}
