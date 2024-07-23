package com.doubles.selfstudy.repository;

import com.doubles.selfstudy.entity.QuestionBoard;
import com.doubles.selfstudy.entity.QuestionBoardLike;
import com.doubles.selfstudy.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface QuestionBoardLikeRepository extends JpaRepository<QuestionBoardLike, Long> {

    Optional<QuestionBoardLike> findByUserAccountAndQuestionBoard(UserAccount userAccount, QuestionBoard questionBoard);
    Integer countByQuestionBoard(QuestionBoard questionBoard);

    @Transactional
    void deleteAllByQuestionBoard(@Param("question_board") QuestionBoard questionBoard);
}
