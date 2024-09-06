package com.doubles.selfstudy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Table(name = "chat_message")
@Entity
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserAccount userAccount;

    @Column(name = "message")
    private String message;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    protected ChatMessage() {}

    private ChatMessage(ChatRoom chatRoom, UserAccount userAccount, String message, LocalDateTime createdAt) {
        this.chatRoom = chatRoom;
        this.userAccount = userAccount;
        this.message = message;
        this.createdAt = createdAt;
    }

    public static ChatMessage of(ChatRoom chatRoom, UserAccount userAccount, String message) {
        return new ChatMessage(chatRoom, userAccount, message, null);
    }

    public static ChatMessage of(ChatRoom chatRoom, UserAccount userAccount, String message, LocalDateTime createdAt) {
        return new ChatMessage(chatRoom, userAccount, message, createdAt);
    }

    @PrePersist
    void createdAt() {
        this.createdAt = LocalDateTime.now();
    }
}
