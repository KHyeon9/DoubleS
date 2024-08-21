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
    private StudyGroup studyGroupId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    protected StudyGroupBoard() {}

    private StudyGroupBoard(Long id, UserAccount studyGroupId, String title, String content) {
        this.id = id;
        this.userAccount = studyGroupId;
        this.title = title;
        this.content = content;
    }

    public static StudyGroupBoard of(UserAccount studyGroupId, String title, String content) {
        return new StudyGroupBoard(null, studyGroupId, title, content);
    }

    public static StudyGroupBoard of(Long id, UserAccount studyGroupId, String title, String content) {
        return new StudyGroupBoard(id, studyGroupId, title, content);
    }
}
