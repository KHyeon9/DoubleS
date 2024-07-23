package com.doubles.selfstudy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Table(
        name = "question_board_comment",
        indexes = {
            @Index(
                name = "question_board_idx",
                columnList = "question_board_id"
            )
        }
)
@ToString(callSuper = true)
@Entity
public class QuestionBoardComment extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "comment")
    private String comment;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserAccount userAccount;

    @ManyToOne
    @JoinColumn(name = "question_board_id")
    private QuestionBoard questionBoard;

    protected QuestionBoardComment() {}

    private QuestionBoardComment(Long id, String comment, UserAccount userAccount, QuestionBoard questionBoard) {
        this.id = id;
        this.comment = comment;
        this.userAccount = userAccount;
        this.questionBoard = questionBoard;
    }

    public static QuestionBoardComment of(String comment, UserAccount userAccount, QuestionBoard questionBoard) {
        return QuestionBoardComment.of(null, comment, userAccount, questionBoard);
    }

    public static QuestionBoardComment of(Long id, String comment, UserAccount userAccount, QuestionBoard questionBoard) {
        return new QuestionBoardComment(id, comment, userAccount, questionBoard);
    }
}
