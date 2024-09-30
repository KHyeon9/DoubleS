package com.doubles.selfstudy.repository;

import com.doubles.selfstudy.entity.QuestionBoard;
import com.doubles.selfstudy.entity.QuestionBoardLike;
import com.doubles.selfstudy.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestionBoardLikeRepository extends JpaRepository<QuestionBoardLike, Long> {

    Optional<QuestionBoardLike> findByUserAccountAndQuestionBoard(UserAccount userAccount, QuestionBoard questionBoard);
    Long countByQuestionBoard(QuestionBoard questionBoard);
    void deleteByQuestionBoardAndUserAccount(QuestionBoard questionBoard, UserAccount userAccount);

    @Modifying // db 변경을 명시
    @Query("DELETE FROM QuestionBoardLike qbl WHERE qbl.questionBoard.id = :questionBoardId")
    void deleteAllByQuestionBoardId(Long questionBoardId);

    @Modifying
    @Query("DELETE FROM QuestionBoardLike qbl WHERE qbl.userAccount = :userAccount")
    void deleteAllByUserAccount(@Param("userAccount") UserAccount userAccount);

    // 유저가 만든 모든 게시글에 관련된 좋아요 제거
    @Modifying
    @Query("DELETE FROM QuestionBoardLike qbl WHERE qbl.questionBoard IN " +
            "(SELECT qb FROM QuestionBoard qb WHERE qb.userAccount = :userAccount)")
    void deleteAllMyBoardLikesByUserAccount(@Param("userAccount") UserAccount userAccount);
}
