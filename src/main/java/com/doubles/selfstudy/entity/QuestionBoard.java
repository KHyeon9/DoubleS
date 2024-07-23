package com.doubles.selfstudy.entity;

import com.doubles.selfstudy.dto.post.QuestionBoardTag;
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

    @PersistenceContext
    private transient EntityManager entityManager;

    protected QuestionBoard() {}

    private QuestionBoard(UserAccount userAccount, String title, String content, QuestionBoardTag tag) {
        this.userAccount = userAccount;
        this.title = title;
        this.content = content;
        this.tag = tag;
    }

    public static QuestionBoard of(UserAccount userAccount, String title, String content, QuestionBoardTag tag) {
        return new QuestionBoard(userAccount, title, content, tag);
    }

    public static QuestionBoard of(UserAccount userAccount, String title, String content) {
        return QuestionBoard.of(userAccount, title, content, null);
    }

    // delete가 발생시 실행
    @PreRemove
    private void preRemove() {
        // 질문 게시글 삭제시 해당 게시글 좋아요들 삭제
        String deleteLikeJpql = "DELETE FROM QuestionBoardLike qbl WHERE qbl.questionBoard.id = :boardId";
        entityManager.createQuery(deleteLikeJpql)
                .setParameter("boardId", this.id)
                .executeUpdate();
    }

    public void plusViewCount() {
        this.viewCount += 1;
    }
}
