package com.doubles.selfstudy.repository;

import com.doubles.selfstudy.entity.QuestionBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionBoardRepository extends JpaRepository<QuestionBoard, Long> {
}
