package com.doubles.selfstudy.entity;

import com.doubles.selfstudy.dto.question.QuestionBoardTag;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Table(name = "question_board")
@ToString(callSuper = true)
@Entity
public class QuestionBoard extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // UsreAccount와 연결 -> JoinColumn을 통해 user_id와 연결
    @JoinColumn(name = "user_id")
    @ManyToOne
    private UserAccount userAccount; // 유저 정보

    @Column(nullable = false, name = "title")
    private String title; // 제목

    @Column(nullable = false, name = "content")
    private String content; // 내용

    @Enumerated(EnumType.STRING)
    private QuestionBoardTag tag; // 태그

    @Column(name = "view_count")
    private Integer viewCount; // 조회수

    protected QuestionBoard() {}

    private QuestionBoard(UserAccount userAccount, String title, String content, QuestionBoardTag tag, Integer viewCount) {
        this.userAccount = userAccount;
        this.title = title;
        this.content = content;
        this.tag = tag;
        this.viewCount = viewCount;
    }

    public static QuestionBoard of(UserAccount userAccount, String title, String content, QuestionBoardTag tag, Integer viewCount) {
        return new QuestionBoard(userAccount, title, content, tag, viewCount);
    }

    public static QuestionBoard of(UserAccount userAccount, String title, String content, Integer viewCount) {
        return new QuestionBoard(userAccount, title, content, null, viewCount);
    }

    public static QuestionBoard of(UserAccount userAccount, String title, String content, QuestionBoardTag tag) {
        return QuestionBoard.of(userAccount, title, content, tag, 0);
    }

    @PrePersist
    public void prePersist() {
        this.viewCount = 0;
     }

    public void plusViewCount() {
        this.viewCount += 1;
    }
}
