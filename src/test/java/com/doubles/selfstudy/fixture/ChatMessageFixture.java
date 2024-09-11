package com.doubles.selfstudy.fixture;

import com.doubles.selfstudy.entity.ChatMessage;
import com.doubles.selfstudy.entity.ChatRoom;
import com.doubles.selfstudy.entity.UserAccount;

public class ChatMessageFixture {

    public static ChatMessage get(ChatRoom chatRoom, UserAccount user, String message) {
        return ChatMessage.of(chatRoom, user, message);
    }
}
