package com.doubles.selfstudy.repository;

import com.doubles.selfstudy.entity.QuestionBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionBoardRepository extends JpaRepository<QuestionBoard, Long> {
    
    // 최신 질문 게시글 5개만 조회
    List<QuestionBoard> findTop5ByOrderByCreatedAtDesc();
    
    // 좋아요 갯수와 댓글 갯수를 포함한 리스트 조회
    @Query("SELECT q, " +
            "(SELECT COUNT(qbl) FROM QuestionBoardLike qbl WHERE qbl.questionBoard.id = q.id), " +
            "(SELECT COUNT(qbc) FROM QuestionBoardComment qbc WHERE qbc.questionBoard.id = q.id) " +
            "FROM QuestionBoard q")
    Page<Object[]> findAllByWithLikeCountAndCommentCount(Pageable pageable);

    // 좋아요 갯수와 댓글 갯수를 포함한 내 질문 게시글 조회
    @Query("SELECT q, " +
            "(SELECT COUNT(qbl) FROM QuestionBoardLike qbl WHERE qbl.questionBoard.id = q.id), " +
            "(SELECT COUNT(qbc) FROM QuestionBoardComment qbc WHERE qbc.questionBoard.id = q.id) " +
            "FROM QuestionBoard q " +
            "WHERE q.userAccount.userId = :userId")
    Page<Object[]> findAllByMyBoardWithLikeCountAndCommentCount(String userId, Pageable pageable);
}
