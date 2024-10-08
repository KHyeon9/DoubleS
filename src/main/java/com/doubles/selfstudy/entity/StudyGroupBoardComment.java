package com.doubles.selfstudy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(
        name = "study_group_board_comment",
        indexes = {
                @Index(
                        name = "study_group_board_idx",
                        columnList = "study_group_board_id"
                )
        }
)
@Entity
public class StudyGroupBoardComment extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserAccount userAccount;

    @ManyToOne
    @JoinColumn(name = "study_group_board_id")
    private StudyGroupBoard studyGroupBoard;

    @Column(name = "comment")
    private String comment;


    protected StudyGroupBoardComment() {}

    private StudyGroupBoardComment(UserAccount userAccount, StudyGroupBoard studyGroupBoard, String comment) {
        this.userAccount = userAccount;
        this.studyGroupBoard = studyGroupBoard;
        this.comment = comment;
    }

    public static StudyGroupBoardComment of(UserAccount userAccount, StudyGroupBoard studyGroupBoard, String comment) {
        return new StudyGroupBoardComment(userAccount, studyGroupBoard, comment);
    }
}
