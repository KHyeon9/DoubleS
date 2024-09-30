package com.doubles.selfstudy.repository;

import com.doubles.selfstudy.entity.StudyGroupBoard;
import com.doubles.selfstudy.entity.StudyGroupBoardComment;
import com.doubles.selfstudy.entity.UserAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface StudyGroupBoardCommentRepository extends JpaRepository<StudyGroupBoardComment, Long> {
    int countByStudyGroupBoard(StudyGroupBoard studyGroupBoard);
    Page<StudyGroupBoardComment> findAllByStudyGroupBoard(StudyGroupBoard studyGroupBoard, Pageable pageable);
    void deleteAllByStudyGroupBoard(StudyGroupBoard studyGroupBoard);

    @Modifying
    @Transactional
    @Query("DELETE FROM StudyGroupBoardComment c WHERE c.studyGroupBoard.id IN :boardIds")
    void deleteAllByStudyGroupBoardIdIn(@Param("boardIds") List<Long> boardIds);

    void deleteAllByUserAccount(UserAccount userAccount);
}
