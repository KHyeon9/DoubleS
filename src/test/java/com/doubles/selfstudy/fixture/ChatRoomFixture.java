package com.doubles.selfstudy.fixture;

import com.doubles.selfstudy.entity.ChatRoom;
import com.doubles.selfstudy.entity.UserAccount;

public class ChatRoomFixture {

    public static ChatRoom get(UserAccount user1, UserAccount user2) {
        return ChatRoom.of(user1, user2);
    }
}
