package com.doubles.selfstudy.fixture;

import com.doubles.selfstudy.dto.question.QuestionBoardTag;
import com.doubles.selfstudy.entity.QuestionBoard;
import com.doubles.selfstudy.entity.UserAccount;

public class QuestionBoardFixture {

    public static QuestionBoard get(String userId) {
        UserAccount userAccount = UserAccount.of(userId, "test", "test", "test", null);

        QuestionBoard questionBoard = QuestionBoard.of(userAccount, "title", "content", QuestionBoardTag.Free, 0);

        return questionBoard;
    }

    public static QuestionBoard get(String userId, String title, String content) {
        UserAccount userAccount = UserAccount.of(userId, "test", "test", "test", null);

        QuestionBoard questionBoard = QuestionBoard.of(userAccount, title, content, QuestionBoardTag.Free, 0);

        return questionBoard;
    }
}
