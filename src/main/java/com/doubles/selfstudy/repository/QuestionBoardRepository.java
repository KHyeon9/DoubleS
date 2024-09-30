package com.doubles.selfstudy.repository;

import com.doubles.selfstudy.dto.question.QuestionBoardTag;
import com.doubles.selfstudy.entity.QuestionBoard;
import com.doubles.selfstudy.entity.UserAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionBoardRepository extends JpaRepository<QuestionBoard, Long> {
    
    // 최신 질문 게시글 5개만 조회
    List<QuestionBoard> findTop5ByOrderByCreatedAtDesc();

    // profile에 사용될 최신 질문 게시글 4개만 조회
    List<QuestionBoard> findTop4ByUserAccountOrderByCreatedAtDesc(UserAccount userAccount);
    
    // 좋아요 갯수와 댓글 갯수를 포함한 리스트 조회
    @Query("SELECT q, " +
            "(SELECT COUNT(qbl) FROM QuestionBoardLike qbl WHERE qbl.questionBoard.id = q.id), " +
            "(SELECT COUNT(qbc) FROM QuestionBoardComment qbc WHERE qbc.questionBoard.id = q.id) " +
            "FROM QuestionBoard q")
    Page<Object[]> findAllQuestionBoardWithCounts(Pageable pageable);

    @Query("SELECT q, " +
            "(SELECT COUNT(qbl) FROM QuestionBoardLike qbl WHERE qbl.questionBoard.id = q.id), " +
            "(SELECT COUNT(qbc) FROM QuestionBoardComment qbc WHERE qbc.questionBoard.id = q.id) " +
            "FROM QuestionBoard q " +
            "WHERE q.tag = :tag")
    Page<Object[]> findAllQuestionBoardWithCountsByTag(QuestionBoardTag tag, Pageable pageable);

    // 좋아요 갯수와 댓글 갯수를 포함한 내 질문 게시글 조회
    @Query("SELECT q, " +
            "(SELECT COUNT(qbl) FROM QuestionBoardLike qbl WHERE qbl.questionBoard.id = q.id), " +
            "(SELECT COUNT(qbc) FROM QuestionBoardComment qbc WHERE qbc.questionBoard.id = q.id) " +
            "FROM QuestionBoard q " +
            "WHERE q.userAccount.userId = :userId")
    Page<Object[]> findAllMyQuestionBoardWithCounts(String userId, Pageable pageable);

    // 좋아요 갯수와 댓글 갯수를 포함한 내 질문 게시글 태그 조회
    @Query("SELECT q, " +
            "(SELECT COUNT(qbl) FROM QuestionBoardLike qbl WHERE qbl.questionBoard.id = q.id), " +
            "(SELECT COUNT(qbc) FROM QuestionBoardComment qbc WHERE qbc.questionBoard.id = q.id) " +
            "FROM QuestionBoard q " +
            "WHERE q.userAccount.userId = :userId AND q.tag = :tag" )
    Page<Object[]> findAllMyQuestionBoardWithCountsByTag(String userId, QuestionBoardTag tag, Pageable pageable);

    @Modifying
    @Query("DELETE FROM QuestionBoard b WHERE b.userAccount = :userAccount")
    void deleteAllByUserAccount(@Param("userAccount") UserAccount userAccount);
}
