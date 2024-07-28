package com.doubles.selfstudy.fixture;

import com.doubles.selfstudy.entity.NoticeBoard;
import com.doubles.selfstudy.entity.UserAccount;

public class NoticeBoardFixture {

    public static NoticeBoard get(UserAccount userAccount) {

        return NoticeBoard.of(userAccount, "title", "content");
    }
}
