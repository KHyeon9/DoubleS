package com.doubles.selfstudy.repository;

import com.doubles.selfstudy.entity.Todo;
import com.doubles.selfstudy.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {

    List<Todo> findAllByUserAccount(UserAccount userAccount);

    // 특정 사용자의 완료된 todo리스트의 총 갯수 반환
    @Query("SELECT COUNT(t) FROM Todo t WHERE t.userAccount.userId = :userId AND t.isCompleted = true")
    Integer countCompletedByUserId(@Param("userId") String userId);


    @Modifying
    @Query("DELETE FROM Todo t WHERE t.userAccount = :userAccount")
    void deleteAllByUserAccount(@Param("userAccount") UserAccount userAccount);
}
