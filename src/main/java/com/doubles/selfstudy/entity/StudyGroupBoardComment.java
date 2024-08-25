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

    @Column(name = "comment")
    private String comment;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserAccount userAccount;

    @ManyToOne
    @JoinColumn(name = "study_group_board_id")
    private StudyGroupBoard studyGroupBoard;

    protected StudyGroupBoardComment() {}

    private StudyGroupBoardComment(String comment, UserAccount userAccount, StudyGroupBoard studyGroupBoard) {
        this.comment = comment;
        this.userAccount = userAccount;
        this.studyGroupBoard = studyGroupBoard;
    }

    public StudyGroupBoardComment of(String comment, UserAccount userAccount, StudyGroupBoard studyGroupBoard) {
        return new StudyGroupBoardComment(comment, userAccount, studyGroupBoard);
    }
}
