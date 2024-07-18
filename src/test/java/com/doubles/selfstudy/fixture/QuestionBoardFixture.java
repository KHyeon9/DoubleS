package com.doubles.selfstudy.fixture;

import com.doubles.selfstudy.entity.QuestionBoard;
import com.doubles.selfstudy.entity.UserAccount;

public class QuestionBoardFixture {

    public static QuestionBoard get(String userId, Long questionBoardId) {
        UserAccount userAccount = UserAccount.of(userId, "test", "test", "test", null);

        QuestionBoard questionBoard = QuestionBoard.of(userAccount, "title", "content", null);

        return questionBoard;
    }
}
