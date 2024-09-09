package com.doubles.selfstudy.controller.response;

import com.doubles.selfstudy.dto.Chat.ChatMessageDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ChatMessageResponse {

    private Long chatRoomId;
    private String userId;
    private String message;
    private LocalDateTime createdAt;

    public static ChatMessageResponse fromChatMessageDto(ChatMessageDto dto) {
        return new ChatMessageResponse(
                dto.getChatRoom().getId(),
                dto.getUserAccount().getUserId(),
                dto.getMessage(),
                dto.getCreatedAt()
        );
    }
}
