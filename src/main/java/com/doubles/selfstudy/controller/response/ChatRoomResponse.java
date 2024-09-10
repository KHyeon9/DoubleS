package com.doubles.selfstudy.controller.response;

import com.doubles.selfstudy.dto.Chat.ChatRoomDto;
import com.doubles.selfstudy.dto.user.UserAccountDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ChatRoomResponse {

    private Long id;
    private UserResponse user1;
    private UserResponse user2;
    private String lastMessage;
    private LocalDateTime lastMessageTime;
    private LocalDateTime createdAt;

    public static ChatRoomResponse fromChatRoomDto(ChatRoomDto dto) {
        return new ChatRoomResponse(
                dto.getId(),
                UserResponse.fromUserAccountDto(dto.getUser1()),
                UserResponse.fromUserAccountDto(dto.getUser2()),
                dto.getLastMessage(),
                dto.getLastMessageTime(),
                dto.getCreatedAt()
        );
    }
}
