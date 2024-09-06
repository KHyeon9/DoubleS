package com.doubles.selfstudy.dto.Chat;

import com.doubles.selfstudy.dto.user.UserAccountDto;
import com.doubles.selfstudy.entity.ChatMessage;
import com.doubles.selfstudy.entity.ChatRoom;
import com.doubles.selfstudy.entity.UserAccount;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDto {

    private Long id;
    private ChatRoomDto chatRoom;
    private UserAccountDto userAccount;
    private String message;
    private LocalDateTime createdAt;

    public ChatMessageDto fromEntity(ChatMessage entity) {
        return new ChatMessageDto(
                entity.getId(),
                ChatRoomDto.fromEntity(entity.getChatRoom()),
                UserAccountDto.fromEntity(entity.getUserAccount()),
                entity.getMessage(),
                entity.getCreatedAt()
        );
    }
}
