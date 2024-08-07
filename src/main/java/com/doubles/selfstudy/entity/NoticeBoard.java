package com.doubles.selfstudy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Table(name = "notice_board")
@ToString(callSuper = true)
@Entity
public class NoticeBoard extends AuditingFields{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id")
    @ManyToOne
    private UserAccount userAccount; // 유저 정보

    @Column(nullable = false, name = "title")
    private String title; // 제목

    @Column(nullable = false, name = "content")
    private String content; // 내용

    protected NoticeBoard() {}

    private NoticeBoard(UserAccount userAccount, String title, String content) {
        this.userAccount = userAccount;
        this.title = title;
        this.content = content;
    }

    public static NoticeBoard of(UserAccount userAccount, String title, String content) {
        return new NoticeBoard(userAccount, title, content);
    }
}
