package com.doubles.selfstudy.repository;

import com.doubles.selfstudy.entity.QuestionBoard;
import com.doubles.selfstudy.entity.UserAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionBoardRepository extends JpaRepository<QuestionBoard, Long> {

    Page<QuestionBoard> findByUserAccount(UserAccount userAccount, Pageable pageable);

    @Query("SELECT q, " +
            "(SELECT COUNT(qbl) FROM QuestionBoardLike qbl WHERE qbl.questionBoard.id = q.id), " +
            "(SELECT COUNT(qbc) FROM QuestionBoardComment qbc WHERE qbc.questionBoard.id = q.id) " +
            "FROM QuestionBoard q")
    Page<Object[]> findAllByWithLikeCountAndCommentCount(Pageable pageable);

    @Query("SELECT q, " +
            "(SELECT COUNT(qbl) FROM QuestionBoardLike qbl WHERE qbl.questionBoard.id = q.id), " +
            "(SELECT COUNT(qbc) FROM QuestionBoardComment qbc WHERE qbc.questionBoard.id = q.id) " +
            "FROM QuestionBoard q " +
            "WHERE q.userAccount.userId = :userId")
    Page<Object[]> findAllByMyBoardWithLikeCountAndCommentCount(String userId, Pageable pageable);
}
