package com.doubles.selfstudy.controller.response;

import com.doubles.selfstudy.dto.Chat.ChatMessageDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ChatMessageResponse {

    private Long id;
    private ChatRoomResponse chatRoom;
    private UserResponse userAccount;
    private String message;
    private LocalDateTime createdAt;

    public static ChatMessageResponse fromChatMessageDto(ChatMessageDto dto) {
        return new ChatMessageResponse(
                dto.getId(),
                ChatRoomResponse.fromChatRoomDto(dto.getChatRoom()),
                UserResponse.fromUserAccountDto(dto.getUserAccount()),
                dto.getMessage(),
                dto.getCreatedAt()
        );
    }
}
