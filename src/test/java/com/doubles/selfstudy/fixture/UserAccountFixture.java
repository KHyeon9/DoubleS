package com.doubles.selfstudy.fixture;

import com.doubles.selfstudy.entity.UserAccount;

public class UserAccountFixture {

    public static UserAccount get(String userId, String password, String email, String nickname) {
        UserAccount userAccount = UserAccount.of(userId, password, email, nickname, "test memo");

        return userAccount;
    }
}
