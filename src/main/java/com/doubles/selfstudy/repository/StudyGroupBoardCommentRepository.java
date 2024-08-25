package com.doubles.selfstudy.repository;

import com.doubles.selfstudy.entity.StudyGroupBoard;
import com.doubles.selfstudy.entity.StudyGroupBoardComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyGroupBoardCommentRepository extends JpaRepository<StudyGroupBoardComment, Long> {
    void deleteAllByStudyGroupBoard(StudyGroupBoard studyGroupBoard);
    int countByStudyGroupBoard(StudyGroupBoard studyGroupBoard);
    Page<StudyGroupBoardComment> findAllByStudyGroupBoard(StudyGroupBoard studyGroupBoard, Pageable pageable);
}
