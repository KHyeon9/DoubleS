package com.doubles.selfstudy.entity;

import com.doubles.selfstudy.dto.todo.ImportanceType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Table(name = "Todo")
@Entity
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserAccount userAccount; // 작성 유저

    @Column(nullable = false, name = "content")
    private String content; // Todo 내용

    @Column(nullable = false, name = "importance_type")
    private ImportanceType importanceType; // 중요도

    @Column(nullable = false, name = "is_completed")
    private boolean isCompleted; // 완료 여부 (생성시 기본값 -> false)

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    protected Todo() {}

    private Todo(UserAccount userAccount, String content, ImportanceType importanceType, boolean isCompleted, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.userAccount = userAccount;
        this.content = content;
        this.importanceType = importanceType;
        this.isCompleted = isCompleted;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public static Todo of(UserAccount userAccount, String content, ImportanceType importanceType, boolean isCompleted) {
        return Todo.of(userAccount, content, importanceType, isCompleted, null, null);
    }

    public static Todo of(UserAccount userAccount, String content, ImportanceType importanceType) {
        return Todo.of(userAccount, content, importanceType, false, null, null);
    }

    public static Todo of(UserAccount userAccount, String content, ImportanceType importanceType, boolean isCompleted, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        return new Todo(userAccount, content, importanceType, isCompleted, createdAt, modifiedAt);
    }

    @PrePersist
    void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.isCompleted = false;
    }

    @PreUpdate
    void preUpdate() {
        this.modifiedAt = LocalDateTime.now();
    }
}
