package com.doubles.selfstudy.repository;

import com.doubles.selfstudy.entity.QuestionBoard;
import com.doubles.selfstudy.entity.QuestionBoardLike;
import com.doubles.selfstudy.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
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

    void deleteAllByUserAccount(UserAccount userAccount);
}
