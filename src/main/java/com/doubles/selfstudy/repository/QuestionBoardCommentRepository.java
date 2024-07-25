package com.doubles.selfstudy.repository;

import com.doubles.selfstudy.entity.QuestionBoard;
import com.doubles.selfstudy.entity.QuestionBoardComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionBoardCommentRepository extends JpaRepository<QuestionBoardComment, Long> {

    Page<QuestionBoardComment> findAllByQuestionBoard(QuestionBoard questionBoard, Pageable pageable);

    Long countByQuestionBoard(QuestionBoard questionBoard);

    @Modifying // db변경을 명시
    @Query("DELETE FROM QuestionBoardComment qbc WHERE qbc.questionBoard.id = :questionBoardId")
    void deleteAllByQuestionBoardId(Long questionBoardId);
}
