package com.doubles.selfstudy.repository;

import com.doubles.selfstudy.entity.StudyGroup;
import com.doubles.selfstudy.entity.StudyGroupBoard;
import com.doubles.selfstudy.entity.UserAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudyGroupBoardRepository extends JpaRepository<StudyGroupBoard, Long> {

    @Modifying
    @Query("DELETE FROM StudyGroupBoard b WHERE b.studyGroup = :studyGroup")
    void deleteAllByStudyGroup(@Param("studyGroup") StudyGroup studyGroup);
    Page<StudyGroupBoard> findAllByStudyGroupId(Long studyGroupId, Pageable pageable);

    @Query("SELECT s.id FROM StudyGroupBoard s WHERE s.studyGroup.id = :studyGroupId")
    List<Long> findBoardIdsByStudyGroupId(@Param("studyGroupId") Long studyGroupId);

    void deleteAllByUserAccount(UserAccount userAccount);
}
