package com.doubles.selfstudy.repository;

import com.doubles.selfstudy.entity.StudyGroup;
import com.doubles.selfstudy.entity.UserAccount;
import com.doubles.selfstudy.entity.UserStudyGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserStudyGroupRepository extends JpaRepository<UserStudyGroup, Long> {

    Optional<UserStudyGroup> findByUserAccount(UserAccount userAccount);
    void deleteByUserAccount(UserAccount userAccount);
    int countByStudyGroup(StudyGroup studyGroup);

    @Query("SELECT usg.userAccount " +
            "FROM UserStudyGroup usg " +
            "WHERE usg.studyGroup.id = :studyGroupId " +
            "ORDER BY usg.id DESC")
    List<UserAccount> findAllByStudyGroupId(@Param("studyGroupId") Long studyGroupId);
}
