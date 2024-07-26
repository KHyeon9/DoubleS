package com.doubles.selfstudy.fixture;

import com.doubles.selfstudy.entity.NotificationBoard;
import com.doubles.selfstudy.entity.UserAccount;

public class NotificationBoardFixture {

    public static NotificationBoard get(UserAccount userAccount) {

        NotificationBoard notificationBoard = NotificationBoard.of(userAccount, "title", "content");

        return notificationBoard;
    }
}
