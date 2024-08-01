package com.doubles.selfstudy.fixture;

import com.doubles.selfstudy.entity.QuestionBoard;
import com.doubles.selfstudy.entity.QuestionBoardComment;
import com.doubles.selfstudy.entity.UserAccount;

public class QuestionBoardCommentFixture {

    public static QuestionBoardComment get(String userId, QuestionBoard questionBoard, String comment) {
        UserAccount userAccount = UserAccount.of(userId, "test", "test", "test", null);

        QuestionBoardComment questionBoardComment = QuestionBoardComment.of(comment, userAccount, questionBoard);

        return questionBoardComment;
    }
}
