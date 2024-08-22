package com.doubles.selfstudy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "study_group_board")
@Entity
public class StudyGroupBoard extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserAccount userAccount;

    @ManyToOne
    @JoinColumn(name = "study_group_id")
    private StudyGroup studyGroup;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    protected StudyGroupBoard() {}

    private StudyGroupBoard(Long id, UserAccount userAccount, StudyGroup studyGroup, String title, String content) {
        this.id = id;
        this.userAccount = userAccount;
        this.studyGroup = studyGroup;
        this.title = title;
        this.content = content;
    }

    public static StudyGroupBoard of(UserAccount userAccount, StudyGroup studyGroup, String title, String content) {
        return new StudyGroupBoard(null, userAccount, studyGroup, title, content);
    }

    public static StudyGroupBoard of(Long id, UserAccount userAccount, StudyGroup studyGroup, String title, String content) {
        return new StudyGroupBoard(id, userAccount, studyGroup, title, content);
    }
}
