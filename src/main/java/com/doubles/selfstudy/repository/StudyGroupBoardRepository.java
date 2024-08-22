package com.doubles.selfstudy.repository;

import com.doubles.selfstudy.entity.StudyGroup;
import com.doubles.selfstudy.entity.StudyGroupBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyGroupBoardRepository extends JpaRepository<StudyGroupBoard, Long> {

    void deleteAllByStudyGroup(StudyGroup studyGroup);
    Page<StudyGroupBoard> findAllByStudyGroupId(Long studyGroupId, Pageable pageable);
}
