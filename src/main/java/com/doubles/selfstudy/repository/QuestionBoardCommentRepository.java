package com.doubles.selfstudy.repository;

import com.doubles.selfstudy.entity.QuestionBoard;
import com.doubles.selfstudy.entity.QuestionBoardComment;
import com.doubles.selfstudy.entity.UserAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionBoardCommentRepository extends JpaRepository<QuestionBoardComment, Long> {

    Page<QuestionBoardComment> findAllByQuestionBoard(QuestionBoard questionBoard, Pageable pageable);

    Long countByQuestionBoard(QuestionBoard questionBoard);

    @Modifying // db변경을 명시
    @Query("DELETE FROM QuestionBoardComment qbc WHERE qbc.questionBoard.id = :questionBoardId")
    void deleteAllByQuestionBoardId(Long questionBoardId);

    // 사용자가 작성한 모든 스터디 그룹 게시글 아래의 댓글 삭제
    @Modifying
    @Query("DELETE FROM QuestionBoardComment c WHERE c.questionBoard.id IN (SELECT b.id FROM QuestionBoard b WHERE b.userAccount = :userAccount)")
    void deleteAllMyBoardCommentByUserAccount(@Param("userAccount") UserAccount userAccount);

    @Modifying
    @Query("DELETE FROM QuestionBoardComment c WHERE c.userAccount = :userAccount")
    void deleteAllByUserAccount(@Param("userAccount") UserAccount userAccount);
}
