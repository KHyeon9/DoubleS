package com.doubles.selfstudy.dto.Chat;

import com.doubles.selfstudy.dto.user.UserAccountDto;
import com.doubles.selfstudy.entity.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomDto {

    private Long id;
    private UserAccountDto user1;
    private UserAccountDto user2;
    private String lastMessage;
    private LocalDateTime lastMessageTime;
    private LocalDateTime createdAt;

    public static ChatRoomDto fromEntity(ChatRoom entity, String lastMessage, LocalDateTime lastMessageTime) {
        return new ChatRoomDto(
                entity.getId(),
                UserAccountDto.fromEntity(entity.getUser1()),
                UserAccountDto.fromEntity(entity.getUser2()),
                lastMessage,
                lastMessageTime,
                entity.getCreatedAt()
        );
    }

    public static ChatRoomDto fromEntity(ChatRoom entity) {
        return new ChatRoomDto(
                entity.getId(),
                UserAccountDto.fromEntity(entity.getUser1()),
                UserAccountDto.fromEntity(entity.getUser2()),
                null,
                null,
                entity.getCreatedAt()
        );
    }
}
