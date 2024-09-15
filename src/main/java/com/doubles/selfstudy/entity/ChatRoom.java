package com.doubles.selfstudy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Table(name = "chat_room")
@Entity
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user1_id")
    private UserAccount user1;

    @ManyToOne
    @JoinColumn(name = "user2_id")
    private UserAccount user2;

    @Column(name = "leave_user")
    private String leaveUserId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    protected ChatRoom() {}

    private ChatRoom(UserAccount user1, UserAccount user2, LocalDateTime createdAt) {
        this.user1 = user1;
        this.user2 = user2;
        this.createdAt = createdAt;
    }

    public static ChatRoom of(UserAccount user1, UserAccount user2) {
        return ChatRoom.of(user1, user2, null);
    }

    public static ChatRoom of(UserAccount user1, UserAccount user2, LocalDateTime createdAt) {
        return new ChatRoom(user1, user2, createdAt);
    }

    @PrePersist
    void prePersist() {
        this.leaveUserId = null;
        this.createdAt = LocalDateTime.now();
    }
}
