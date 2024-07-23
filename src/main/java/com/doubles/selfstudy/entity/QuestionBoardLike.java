package com.doubles.selfstudy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Table(name = "like")
@Entity
public class QuestionBoardLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "question_board_id")
    private QuestionBoard questionBoard;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserAccount userAccount;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    protected QuestionBoardLike() {}

    private QuestionBoardLike(QuestionBoard questionBoard, UserAccount userAccount, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.questionBoard = questionBoard;
        this.userAccount = userAccount;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public static QuestionBoardLike of(QuestionBoard questionBoard, UserAccount userAccount) {
        return QuestionBoardLike.of(questionBoard, userAccount, null, null);
    }

    public static QuestionBoardLike of(QuestionBoard questionBoard, UserAccount userAccount, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        return new QuestionBoardLike(questionBoard, userAccount, createdAt, modifiedAt);
    }

    @PrePersist
    void createdAt() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    void modifiedAt() {
        this.modifiedAt = LocalDateTime.now();
    }
}
